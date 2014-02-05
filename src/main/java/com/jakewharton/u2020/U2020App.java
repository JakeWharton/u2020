package com.jakewharton.u2020;

import android.app.Application;
import android.content.Context;
import com.jakewharton.u2020.ui.ActivityHierarchyServer;
import dagger.ObjectGraph;
import java.util.concurrent.TimeUnit;
import javax.inject.Inject;
import mortar.Mortar;
import mortar.MortarContext;
import mortar.MortarScope;
import timber.log.Timber;

import static timber.log.Timber.DebugTree;

public class U2020App extends Application implements MortarContext {
  private MortarScope applicationScope;

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

    applicationScope =
        Mortar.createRootScope(BuildConfig.DEBUG, ObjectGraph.create(Modules.list(this)));
    Mortar.inject(this, this);
    applicationScope.getObjectGraph().inject(this);

    long diff = TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - start);
    Timber.i("Global object graph creation took %sms", diff);
  }

  public void inject(Object o) {
    Mortar.inject(this, o);
  }

  public static U2020App get(Context context) {
    return (U2020App) context.getApplicationContext();
  }

  @Override public MortarScope getMortarScope() {
    return applicationScope;
  }
}
