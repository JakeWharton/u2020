package com.jakewharton.u2020.ui;

import android.app.Activity;
import android.support.v7.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.view.ContextThemeWrapper;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import butterknife.ButterKnife;
import butterknife.InjectView;
import com.jakewharton.u2020.R;
import com.jakewharton.u2020.data.LumberYard;
import com.jakewharton.u2020.data.prefs.BooleanPreference;
import com.jakewharton.u2020.ui.bugreport.BugReportLens;
import com.mattprecious.telescope.Lens;
import com.mattprecious.telescope.TelescopeLayout;
import java.io.File;
import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public final class TelescopeAppContainer implements AppContainer {
  private final LumberYard lumberYard;
  private final BooleanPreference seenTelescopeDialog;

  @Inject public TelescopeAppContainer(LumberYard lumberYard, SharedPreferences preferences) {
    this.lumberYard = lumberYard;
    this.seenTelescopeDialog = new BooleanPreference(preferences, "internal-seen-telescope-dialog");
  }

  @InjectView(R.id.telescope_container) TelescopeLayout telescopeLayout;

  @Override public ViewGroup bind(final Activity activity) {
    activity.setContentView(R.layout.internal_activity_frame);
    ButterKnife.inject(this, activity);

    TelescopeLayout.cleanUp(activity); // Clean up any old screenshots.
    telescopeLayout.setLens(new BugReportLens(activity, lumberYard));

    // If you have not seen the telescope dialog before, show it.
    if (!seenTelescopeDialog.get()) {
      telescopeLayout.postDelayed(new Runnable() {
        @Override public void run() {
          if (activity.isFinishing()) {
            return;
          }

          seenTelescopeDialog.set(true);
          showTelescopeDialog(activity);
        }
      }, 1000);
    }

    return telescopeLayout;
  }

  public void showTelescopeDialog(final Activity activity) {
    LayoutInflater inflater = LayoutInflater.from(activity);
    TelescopeLayout content =
        (TelescopeLayout) inflater.inflate(R.layout.telescope_tutorial_dialog, null);
    final AlertDialog dialog =
        new AlertDialog.Builder(activity).setView(content).setCancelable(false).create();

    content.setLens(new Lens() {
      @Override public void onCapture(File file) {
        dialog.dismiss();

        Context toastContext = new ContextThemeWrapper(activity, android.R.style.Theme_DeviceDefault_Dialog);
        LayoutInflater toastInflater = LayoutInflater.from(toastContext);
        Toast toast = Toast.makeText(toastContext, "", Toast.LENGTH_SHORT);
        View toastView = toastInflater.inflate(R.layout.telescope_tutorial_toast, null);
        toast.setView(toastView);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
      }
    });

    dialog.show();
  }
}
