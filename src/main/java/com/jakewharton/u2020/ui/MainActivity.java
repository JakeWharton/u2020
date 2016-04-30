package com.jakewharton.u2020.ui;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Toast;
import butterknife.BindView;
import butterknife.BindColor;
import butterknife.ButterKnife;
import com.jakewharton.u2020.R;
import com.jakewharton.u2020.data.Injector;
import com.jakewharton.u2020.data.api.oauth.OauthService;
import dagger.ObjectGraph;
import javax.inject.Inject;

import static android.widget.Toast.LENGTH_SHORT;

public final class MainActivity extends Activity {
  @BindView(R.id.main_drawer_layout) DrawerLayout drawerLayout;
  @BindView(R.id.main_navigation) NavigationView drawer;
  @BindView(R.id.main_content) ViewGroup content;

  @BindColor(R.color.status_bar) int statusBarColor;

  @Inject ViewContainer viewContainer;

  private ObjectGraph activityGraph;

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    LayoutInflater inflater = getLayoutInflater();

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
      // Remove the status bar color. The DrawerLayout is responsible for drawing it from now on.
      setStatusBarColor(getWindow());
    }

    // Explicitly reference the application object since we don't want to match our own injector.
    ObjectGraph appGraph = Injector.obtain(getApplication());
    appGraph.inject(this);
    activityGraph = appGraph.plus(new MainActivityModule(this));

    ViewGroup container = viewContainer.forActivity(this);

    inflater.inflate(R.layout.main_activity, container);
    ButterKnife.bind(this, container);

    drawerLayout.setStatusBarBackgroundColor(statusBarColor);
    drawerLayout.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);

    drawer.setNavigationItemSelectedListener(item -> {
      switch (item.getItemId()) {
        case R.id.nav_search:
          Toast.makeText(MainActivity.this, "Search!", LENGTH_SHORT).show();
          break;
        case R.id.nav_trending:
          Toast.makeText(MainActivity.this, "Trending!", LENGTH_SHORT).show();
          break;
        default:
          throw new IllegalStateException("Unknown navigation item: " + item.getTitle());
      }

      drawerLayout.closeDrawers();
      // If we supported actual navigation, we would change what was checked and navigate there.
      //item.setChecked(true);

      return true;
    });

    inflater.inflate(R.layout.trending_view, content);
  }

  @Override public Object getSystemService(@NonNull String name) {
    if (Injector.matchesService(name)) {
      return activityGraph;
    }
    return super.getSystemService(name);
  }

  @Override protected void onDestroy() {
    activityGraph = null;
    super.onDestroy();
  }

  @Override protected void onNewIntent(Intent intent) {
    super.onNewIntent(intent);

    Uri data = intent.getData();
    if (data == null) return;

    if ("u2020".equals(data.getScheme())) {
      Intent serviceIntent = new Intent(this, OauthService.class);
      serviceIntent.setData(data);
      startService(serviceIntent);
    }
  }

  @TargetApi(Build.VERSION_CODES.LOLLIPOP)
  private static void setStatusBarColor(Window window) {
    window.setStatusBarColor(Color.TRANSPARENT);
  }
}
