package com.jakewharton.u2020.ui.logs;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.widget.ListView;
import android.widget.Toast;
import com.jakewharton.u2020.data.LumberYard;
import com.jakewharton.u2020.util.Intents;
import java.io.File;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

public final class LogsDialog extends AlertDialog {
  private final LumberYard lumberYard;
  private final LogAdapter adapter;

  private CompositeSubscription subscriptions;

  public LogsDialog(Context context, LumberYard lumberYard) {
    super(context);
    this.lumberYard = lumberYard;

    adapter = new LogAdapter(context);

    ListView listView = new ListView(context);
    listView.setTranscriptMode(ListView.TRANSCRIPT_MODE_NORMAL);
    listView.setAdapter(adapter);

    setTitle("Logs");
    setView(listView);
    setButton(BUTTON_NEGATIVE, "Close", new OnClickListener() {
      @Override public void onClick(DialogInterface dialog, int which) {
        // NO-OP.
      }
    });
    setButton(BUTTON_POSITIVE, "Share", new OnClickListener() {
      @Override public void onClick(DialogInterface dialog, int which) {
        share();
      }
    });
  }

  @Override protected void onStart() {
    super.onStart();

    adapter.setLogs(lumberYard.bufferedLogs());

    subscriptions = new CompositeSubscription();
    subscriptions.add(lumberYard.logs() //
        .observeOn(AndroidSchedulers.mainThread()) //
        .subscribe(adapter));
  }

  @Override protected void onStop() {
    super.onStop();
    subscriptions.unsubscribe();
  }

  private void share() {
    lumberYard.save() //
        .subscribeOn(Schedulers.io()) //
        .observeOn(AndroidSchedulers.mainThread()) //
        .subscribe(new Subscriber<File>() {
          @Override public void onCompleted() {
            // NO-OP.
          }

          @Override public void onError(Throwable e) {
            Toast.makeText(getContext(), "Couldn't save the logs for sharing.", Toast.LENGTH_SHORT)
                .show();
          }

          @Override public void onNext(File file) {
            Intent sendIntent = new Intent(Intent.ACTION_SEND);
            sendIntent.setType("text/plain");
            sendIntent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(file));
            Intents.maybeStartActivity(getContext(), sendIntent);
          }
        });
  }
}
