package com.jakewharton.u2020;

import android.app.Application;
import android.os.Handler;
import android.os.Looper;
import android.os.StrictMode;
import android.os.StrictMode.ThreadPolicy;
import android.os.StrictMode.VmPolicy;
import android.support.annotation.NonNull;
import com.jakewharton.threetenabp.AndroidThreeTen;
import com.jakewharton.u2020.data.Injector;
import com.jakewharton.u2020.data.LumberYard;
import com.jakewharton.u2020.ui.ActivityHierarchyServer;
import com.squareup.leakcanary.LeakCanary;
import dagger.ObjectGraph;
import javax.inject.Inject;
import timber.log.Timber;

import static timber.log.Timber.DebugTree;

public final class U2020App extends Application {
  private ObjectGraph objectGraph;

  @Inject ActivityHierarchyServer activityHierarchyServer;
  @Inject LumberYard lumberYard;

  @Override public void onCreate() {
    super.onCreate();
    if (LeakCanary.isInAnalyzerProcess(this)) {
      return;
    }
    AndroidThreeTen.init(this);
    LeakCanary.install(this);

    if (BuildConfig.DEBUG) {
      Timber.plant(new DebugTree());

      // ActivityThread does not allow setting StrictMode during Application#onCreate.
      // Post it to run as soon as possible after this method.
      new Handler(Looper.getMainLooper()).postAtFrontOfQueue(() -> {
        StrictMode.setVmPolicy(new VmPolicy.Builder()
            .detectAll()
            .penaltyLog()
            .penaltyDeath()
            .build());
        StrictMode.setThreadPolicy(new ThreadPolicy.Builder()
            .detectAll()
            .penaltyLog()
            .penaltyDeath()
            .build());
      });
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
