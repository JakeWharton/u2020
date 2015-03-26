package com.jakewharton.u2020.ui.logs;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.widget.ListView;
import com.jakewharton.u2020.data.LumberYard;
import rx.android.schedulers.AndroidSchedulers;
import rx.subscriptions.CompositeSubscription;

import static com.jakewharton.u2020.data.LumberYard.Entry;
import static com.jakewharton.u2020.ui.logs.LogAdapter.LogLevelStyle;

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
    Intent sendIntent = new Intent();
    sendIntent.setAction(Intent.ACTION_SEND);
    sendIntent.putExtra(Intent.EXTRA_TEXT, createShareBody()); // TODO: File instead of gross text.
    sendIntent.setType("text/plain");
    getContext().startActivity(sendIntent);
  }

  private String createShareBody() {
    StringBuilder builder = new StringBuilder("---------- Logs ----------\n");

    for (int i = 0; i < adapter.getCount(); i++) {
      Entry entry = adapter.getItem(i);
      builder.append(String.format("%22s", entry.tag))
          .append(' ')
          .append(LogLevelStyle.fromLogLevel(entry.level).letter)
          .append(' ')
          .append(entry.message)
          .append('\n');
    }

    return builder.toString();
  }
}
