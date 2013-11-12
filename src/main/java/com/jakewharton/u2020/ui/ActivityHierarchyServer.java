package com.jakewharton.u2020.ui;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;

/** A "view server" adaptation which automatically hooks itself up to all activities. */
public interface ActivityHierarchyServer extends Application.ActivityLifecycleCallbacks {
  /** An {@link ActivityHierarchyServer} which does nothing. */
  ActivityHierarchyServer NONE = new ActivityHierarchyServer() {
    @Override public void onActivityCreated(Activity activity, Bundle bundle) {}
    @Override public void onActivityStarted(Activity activity) {}
    @Override public void onActivityResumed(Activity activity) {}
    @Override public void onActivityPaused(Activity activity) {}
    @Override public void onActivityStopped(Activity activity) {}
    @Override public void onActivitySaveInstanceState(Activity activity, Bundle bundle) {}
    @Override public void onActivityDestroyed(Activity activity) {}
  };
}
