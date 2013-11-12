package com.jakewharton.u2020.ui;

import android.app.Activity;
import android.view.ViewGroup;
import com.jakewharton.u2020.U2020App;

/** An indirection which allows controlling the root container used for each activity. */
public interface AppContainer {
  /** The root {@link android.view.ViewGroup} into which the activity should place its contents. */
  ViewGroup get(Activity activity, U2020App app);

  /** An {@link AppContainer} which returns the normal activity content view. */
  AppContainer DEFAULT = new AppContainer() {
    @Override public ViewGroup get(Activity activity, U2020App app) {
      return (ViewGroup) activity.findViewById(android.R.id.content);
    }
  };
}
