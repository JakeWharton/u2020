package com.jakewharton.u2020.ui;

import android.app.Activity;
import android.view.ViewGroup;
import butterknife.ButterKnife;
import butterknife.InjectView;
import com.jakewharton.u2020.R;
import com.jakewharton.u2020.data.LumberYard;
import com.jakewharton.u2020.ui.bugreport.BugReportLens;
import com.mattprecious.telescope.TelescopeLayout;
import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public final class TelescopeAppContainer implements AppContainer {
  private final LumberYard lumberYard;

  @Inject public TelescopeAppContainer(LumberYard lumberYard) {
    this.lumberYard = lumberYard;
  }

  @InjectView(R.id.telescope_container) TelescopeLayout telescopeLayout;

  @Override public ViewGroup bind(Activity activity) {
    activity.setContentView(R.layout.internal_activity_frame);
    ButterKnife.inject(this, activity);

    TelescopeLayout.cleanUp(activity); // Clean up any old screenshots.
    telescopeLayout.setLens(new BugReportLens(activity, lumberYard));

    return telescopeLayout;
  }
}
