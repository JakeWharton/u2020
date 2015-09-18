package com.jakewharton.u2020.ui.debug;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.os.PowerManager;
import android.support.v4.view.GravityCompat;
import android.view.ContextThemeWrapper;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import butterknife.Bind;
import butterknife.ButterKnife;
import com.f2prateek.rx.preferences.Preference;
import com.jakewharton.madge.MadgeFrameLayout;
import com.jakewharton.scalpel.ScalpelFrameLayout;
import com.jakewharton.u2020.R;
import com.jakewharton.u2020.data.LumberYard;
import com.jakewharton.u2020.data.PixelGridEnabled;
import com.jakewharton.u2020.data.PixelRatioEnabled;
import com.jakewharton.u2020.data.ScalpelEnabled;
import com.jakewharton.u2020.data.ScalpelWireframeEnabled;
import com.jakewharton.u2020.data.SeenDebugDrawer;
import com.jakewharton.u2020.ui.AppContainer;
import com.jakewharton.u2020.ui.bugreport.BugReportLens;
import com.jakewharton.u2020.util.EmptyActivityLifecycleCallbacks;
import com.mattprecious.telescope.TelescopeLayout;
import javax.inject.Inject;
import javax.inject.Singleton;
import rx.Observable;
import rx.functions.Action1;
import rx.subscriptions.CompositeSubscription;

import static android.content.Context.POWER_SERVICE;
import static android.os.PowerManager.ACQUIRE_CAUSES_WAKEUP;
import static android.os.PowerManager.FULL_WAKE_LOCK;
import static android.os.PowerManager.ON_AFTER_RELEASE;
import static android.view.WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED;

/**
 * An {@link AppContainer} for debug builds which wrap the content view with a sliding drawer on
 * the right that holds all of the debug information and settings.
 */
@Singleton
public final class DebugAppContainer implements AppContainer {
  private final LumberYard lumberYard;
  private final Preference<Boolean> seenDebugDrawer;
  private final Preference<Boolean> pixelGridEnabled;
  private final Preference<Boolean> pixelRatioEnabled;
  private final Preference<Boolean> scalpelEnabled;
  private final Preference<Boolean> scalpelWireframeEnabled;

  static class ViewHolder {
    @Bind(R.id.debug_drawer_layout) DebugDrawerLayout drawerLayout;
    @Bind(R.id.debug_drawer) ViewGroup debugDrawer;
    @Bind(R.id.telescope_container) TelescopeLayout telescopeLayout;
    @Bind(R.id.madge_container) MadgeFrameLayout madgeFrameLayout;
    @Bind(R.id.debug_content) ScalpelFrameLayout content;
  }

  @Inject public DebugAppContainer(LumberYard lumberYard,
      @SeenDebugDrawer Preference<Boolean> seenDebugDrawer,
      @PixelGridEnabled Preference<Boolean> pixelGridEnabled,
      @PixelRatioEnabled Preference<Boolean> pixelRatioEnabled,
      @ScalpelEnabled Preference<Boolean> scalpelEnabled,
      @ScalpelWireframeEnabled Preference<Boolean> scalpelWireframeEnabled) {
    this.lumberYard = lumberYard;
    this.seenDebugDrawer = seenDebugDrawer;
    this.pixelGridEnabled = pixelGridEnabled;
    this.pixelRatioEnabled = pixelRatioEnabled;
    this.scalpelEnabled = scalpelEnabled;
    this.scalpelWireframeEnabled = scalpelWireframeEnabled;
  }

  @Override public ViewGroup bind(final Activity activity) {
    activity.setContentView(R.layout.debug_activity_frame);

    final ViewHolder viewHolder = new ViewHolder();
    ButterKnife.bind(viewHolder, activity);

    final Context drawerContext = new ContextThemeWrapper(activity, R.style.Theme_U2020_Debug);
    final DebugView debugView = new DebugView(drawerContext);
    viewHolder.debugDrawer.addView(debugView);

    // Set up the contextual actions to watch views coming in and out of the content area.
    ContextualDebugActions contextualActions = debugView.getContextualDebugActions();
    contextualActions.setActionClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        viewHolder.drawerLayout.closeDrawers();
      }
    });
    viewHolder.content.setOnHierarchyChangeListener(
        HierarchyTreeChangeListener.wrap(contextualActions));

    viewHolder.drawerLayout.setDrawerShadow(R.drawable.debug_drawer_shadow, GravityCompat.END);
    viewHolder.drawerLayout.setDrawerListener(new DebugDrawerLayout.SimpleDrawerListener() {
      @Override public void onDrawerOpened(View drawerView) {
        debugView.onDrawerOpened();
      }
    });

    TelescopeLayout.cleanUp(activity); // Clean up any old screenshots.
    viewHolder.telescopeLayout.setLens(new BugReportLens(activity, lumberYard));

    // If you have not seen the debug drawer before, show it with a message
    if (!seenDebugDrawer.get()) {
      viewHolder.drawerLayout.postDelayed(new Runnable() {
        @Override public void run() {
          viewHolder.drawerLayout.openDrawer(GravityCompat.END);
          Toast.makeText(drawerContext, R.string.debug_drawer_welcome, Toast.LENGTH_LONG).show();
        }
      }, 1000);
      seenDebugDrawer.set(true);
    }

    final CompositeSubscription subscriptions = new CompositeSubscription();
    setupMadge(viewHolder, subscriptions);
    setupScalpel(viewHolder, subscriptions);

    final Application app = activity.getApplication();
    app.registerActivityLifecycleCallbacks(new EmptyActivityLifecycleCallbacks() {
      @Override public void onActivityDestroyed(Activity lifecycleActivity) {
        if (lifecycleActivity == activity) {
          subscriptions.unsubscribe();
          app.unregisterActivityLifecycleCallbacks(this);
        }
      }
    });

    riseAndShine(activity);
    return viewHolder.content;
  }

  private void setupMadge(final ViewHolder viewHolder, CompositeSubscription subscriptions) {
    subscriptions.add(pixelGridEnabled.asObservable().subscribe(new Action1<Boolean>() {
      @Override public void call(Boolean enabled) {
        viewHolder.madgeFrameLayout.setOverlayEnabled(enabled);
      }
    }));
    subscriptions.add(pixelRatioEnabled.asObservable().subscribe(new Action1<Boolean>() {
      @Override public void call(Boolean enabled) {
        viewHolder.madgeFrameLayout.setOverlayRatioEnabled(enabled);
      }
    }));
  }

  private void setupScalpel(final ViewHolder viewHolder, CompositeSubscription subscriptions) {
    subscriptions.add(scalpelEnabled.asObservable().subscribe(new Action1<Boolean>() {
      @Override public void call(Boolean enabled) {
        viewHolder.content.setLayerInteractionEnabled(enabled);
      }
    }));
    subscriptions.add(scalpelWireframeEnabled.asObservable().subscribe(new Action1<Boolean>() {
      @Override public void call(Boolean enabled) {
        viewHolder.content.setDrawViews(!enabled);
      }
    }));
  }

  /**
   * Show the activity over the lock-screen and wake up the device. If you launched the app manually
   * both of these conditions are already true. If you deployed from the IDE, however, this will
   * save you from hundreds of power button presses and pattern swiping per day!
   */
  public static void riseAndShine(Activity activity) {
    activity.getWindow().addFlags(FLAG_SHOW_WHEN_LOCKED);

    PowerManager power = (PowerManager) activity.getSystemService(POWER_SERVICE);
    PowerManager.WakeLock lock =
        power.newWakeLock(FULL_WAKE_LOCK | ACQUIRE_CAUSES_WAKEUP | ON_AFTER_RELEASE, "wakeup!");
    lock.acquire();
    lock.release();
  }
}
