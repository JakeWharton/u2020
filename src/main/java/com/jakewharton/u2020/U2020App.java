package com.jakewharton.u2020;

import android.app.Application;
import com.jakewharton.u2020.ui.ActivityHierarchyServer;
import dagger.ObjectGraph;
import java.util.concurrent.TimeUnit;
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

  public void buildObjectGraphAndInject() {
    long start = System.nanoTime();

    objectGraph = ObjectGraph.create(Modules.list(this));
    objectGraph.inject(this);

    long diff = TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - start);
    Timber.i("Global object graph creation took %sms", diff);
  }

  public ObjectGraph getObjectGraph() {
    return objectGraph;
  }
}
