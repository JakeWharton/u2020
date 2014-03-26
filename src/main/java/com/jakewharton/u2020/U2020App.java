package com.jakewharton.u2020;

import android.app.Application;
import android.content.Context;
import com.jakewharton.u2020.ui.ActivityHierarchyServer;
import dagger.ObjectGraph;
import hugo.weaving.DebugLog;
import javax.inject.Inject;
import timber.log.Timber;

import static timber.log.Timber.DebugTree;

public class U2020App extends Application {
  private ObjectGraph objectGraph;

  @Inject ActivityHierarchyServer activityHierarchyServer;

  @Override public void onCreate() {
    super.onCreate();

    if (BuildConfig.DEBUG) {
      Timber.plant(new DebugTree());
    } else {
      // TODO Crashlytics.start(this);
      // TODO Timber.plant(new CrashlyticsTree());
    }

    buildObjectGraphAndInject();

    registerActivityLifecycleCallbacks(activityHierarchyServer);
  }

  @DebugLog
  public void buildObjectGraphAndInject() {
    objectGraph = ObjectGraph.create(Modules.list(this));
    objectGraph.inject(this);
  }

  public void inject(Object o) {
    objectGraph.inject(o);
  }

  public static U2020App get(Context context) {
    return (U2020App) context.getApplicationContext();
  }
}
