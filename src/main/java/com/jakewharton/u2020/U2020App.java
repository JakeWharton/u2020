package com.jakewharton.u2020;

import android.app.Application;
import android.support.annotation.NonNull;
import com.jakewharton.u2020.data.Injector;
import com.jakewharton.u2020.data.LumberYard;
import com.jakewharton.u2020.ui.ActivityHierarchyServer;
import com.squareup.leakcanary.LeakCanary;
import dagger.ObjectGraph;
import javax.inject.Inject;
import net.danlew.android.joda.JodaTimeAndroid;
import timber.log.Timber;

import static timber.log.Timber.DebugTree;

public final class U2020App extends Application {
  private ObjectGraph objectGraph;

  @Inject ActivityHierarchyServer activityHierarchyServer;
  @Inject LumberYard lumberYard;

  @Override public void onCreate() {
    super.onCreate();
    JodaTimeAndroid.init(this);
    LeakCanary.install(this);

    if (BuildConfig.DEBUG) {
      Timber.plant(new DebugTree());
    } else {
      // TODO Crashlytics.start(this);
      // TODO Timber.plant(new CrashlyticsTree());
    }

    objectGraph = ObjectGraph.create(Modules.list(this));
    objectGraph.inject(this);

    lumberYard.cleanUp();
    Timber.plant(lumberYard.tree());

    registerActivityLifecycleCallbacks(activityHierarchyServer);
  }

  @Override public Object getSystemService(@NonNull String name) {
    if (Injector.matchesService(name)) {
      return objectGraph;
    }
    return super.getSystemService(name);
  }
}
