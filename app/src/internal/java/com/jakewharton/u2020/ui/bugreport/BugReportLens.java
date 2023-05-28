package com.jakewharton.u2020.ui.bugreport;

import android.app.Activity;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.ShareCompat;
import android.util.DisplayMetrics;
import android.widget.Toast;
import com.jakewharton.u2020.BuildConfig;
import com.jakewharton.u2020.data.LumberYard;
import com.jakewharton.u2020.util.Intents;
import com.jakewharton.u2020.util.Strings;
import com.mattprecious.telescope.Lens;
import java.io.File;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import static com.jakewharton.u2020.ui.bugreport.BugReportDialog.ReportListener;
import static com.jakewharton.u2020.ui.bugreport.BugReportView.Report;

/**
 * Pops a dialog asking for more information about the bug report and then creates an email with a
 * JIRA-formatted body.
 */
public final class BugReportLens extends Lens implements ReportListener {

    private final Activity context;

    private final LumberYard lumberYard;

    private File screenshot;

    public BugReportLens(Activity context, LumberYard lumberYard) {
        this.context = context;
        this.lumberYard = lumberYard;
    }

    @Override
    public void onCapture(File screenshot) {
        this.screenshot = screenshot;
        BugReportDialog dialog = new BugReportDialog(context);
        dialog.setReportListener(this);
        dialog.show();
    }

    @Override
    public void onBugReportSubmit(final Report report) {
        if (report.includeLogs) {
            lumberYard.save().subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Subscriber<File>() {

                @Override
                public void onCompleted() {
                    // NO-OP.
                }

                @Override
                public void onError(Throwable e) {
                    Toast.makeText(context, "Couldn't attach the logs.", Toast.LENGTH_SHORT).show();
                    submitReport(report, null);
                }

                @Override
                public void onNext(File logs) {
                    submitReport(report, logs);
                }
            });
        } else {
            submitReport(report, null);
        }
    }

    private void submitReport(Report report, File logs) {
        DisplayMetrics dm = context.getResources().getDisplayMetrics();
        String densityBucket = getDensityString(dm);
        ShareCompat.IntentBuilder intent = ShareCompat.IntentBuilder.from(context).setType("message/rfc822").// TODO: .addEmailTo("u2020-bugs@blackhole.io")
        setSubject(report.title);
        StringBuilder body = new StringBuilder();
        if (!Strings.isBlank(report.description)) {
            body.append("{panel:title=Description}\n").append(report.description).append("\n{panel}\n\n");
        }
        body.append("{panel:title=App}\n");
        body.append("Version: ").append(BuildConfig.VERSION_NAME).append('\n');
        body.append("Version code: ").append(BuildConfig.VERSION_CODE).append('\n');
        body.append("{panel}\n\n");
        body.append("{panel:title=Device}\n");
        body.append("Make: ").append(Build.MANUFACTURER).append('\n');
        body.append("Model: ").append(Build.MODEL).append('\n');
        body.append("Resolution: ").append(dm.heightPixels).append("x").append(dm.widthPixels).append('\n');
        body.append("Density: ").append(dm.densityDpi).append("dpi (").append(densityBucket).append(")\n");
        body.append("Release: ").append(Build.VERSION.RELEASE).append('\n');
        body.append("API: ").append(Build.VERSION.SDK_INT).append('\n');
        body.append("{panel}");
        intent.setText(body.toString());
        if (screenshot != null && report.includeScreenshot) {
            intent.addStream(Uri.fromFile(screenshot));
        }
        if (logs != null) {
            intent.addStream(Uri.fromFile(logs));
        }
        Intents.maybeStartActivity(context, intent.getIntent());
    }

    private static String getDensityString(DisplayMetrics displayMetrics) {
        switch(displayMetrics.densityDpi) {
            case DisplayMetrics.DENSITY_LOW:
                return "ldpi";
            case DisplayMetrics.DENSITY_MEDIUM:
                return "mdpi";
            case DisplayMetrics.DENSITY_HIGH:
                return "hdpi";
            case DisplayMetrics.DENSITY_XHIGH:
                return "xhdpi";
            case DisplayMetrics.DENSITY_XXHIGH:
                return "xxhdpi";
            case DisplayMetrics.DENSITY_XXXHIGH:
                return "xxxhdpi";
            case DisplayMetrics.DENSITY_TV:
                return "tvdpi";
            default:
                return String.valueOf(displayMetrics.densityDpi);
        }
    }
}
