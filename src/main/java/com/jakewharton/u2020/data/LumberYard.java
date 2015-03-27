package com.jakewharton.u2020.data;

import android.app.Application;
import android.os.AsyncTask;
import android.util.Log;
import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Date;
import java.util.Deque;
import java.util.List;
import java.util.Locale;
import javax.inject.Inject;
import javax.inject.Singleton;
import okio.BufferedSink;
import okio.Okio;
import rx.Observable;
import rx.Subscriber;
import rx.subjects.PublishSubject;
import timber.log.Timber;

@Singleton
public final class LumberYard {
  private static final int BUFFER_SIZE = 200;
  private static final ThreadLocal<DateFormat> LOG_FILE_FORMAT = new ThreadLocal<DateFormat>() {
    @Override protected DateFormat initialValue() {
      return new SimpleDateFormat("yyyy-MM-dd-HHmmss.'log'", Locale.US);
    }
  };

  private final Application app;

  private final Deque<Entry> entries = new ArrayDeque<>(BUFFER_SIZE + 1);
  private final PublishSubject<Entry> entrySubject = PublishSubject.create();

  @Inject public LumberYard(Application app) {
    this.app = app;
  }

  public Timber.Tree tree() {
    return new Timber.DebugTree() {
      @Override protected void logMessage(int priority, String tag, String message) {
        addEntry(new Entry(priority, tag, message));
      }
    };
  }

  private synchronized void addEntry(Entry entry) {
    entries.addLast(entry);
    if (entries.size() > BUFFER_SIZE) {
      entries.removeFirst();
    }

    entrySubject.onNext(entry);
  }

  public List<Entry> bufferedLogs() {
    return new ArrayList<>(entries);
  }

  public Observable<Entry> logs() {
    return entrySubject;
  }

  /**  Save the current logs to disk. */
  public Observable<File> save() {
    return Observable.create(new Observable.OnSubscribe<File>() {
      @Override public void call(Subscriber<? super File> subscriber) {
        File folder = app.getExternalFilesDir(null);
        if (folder == null) {
          subscriber.onError(new IOException("External storage is not mounted."));
          return;
        }

        String fileName = LOG_FILE_FORMAT.get().format(new Date());
        File output = new File(folder, fileName);

        BufferedSink sink = null;
        try {
          sink = Okio.buffer(Okio.sink(output));
          List<Entry> entries = bufferedLogs();
          for (Entry entry : entries) {
            sink.writeUtf8(entry.prettyPrint()).writeByte('\n');
          }

          subscriber.onNext(output);
          subscriber.onCompleted();
        } catch (IOException e) {
          subscriber.onError(e);
        } finally {
          if (sink != null) {
            try {
              sink.close();
            } catch (IOException e) {
              subscriber.onError(e);
            }
          }
        }
      }
    });
  }

  /**
   * Delete all of the log files saved to disk. Be careful not to call this before any intents have
   * finished using the file reference.
   */
  public void cleanUp() {
    new AsyncTask<Void, Void, Void>() {
      @Override protected Void doInBackground(Void... folders) {
        File folder = app.getExternalFilesDir(null);
        if (folder != null) {
          for (File file : folder.listFiles()) {
            if (file.getName().endsWith(".log")) {
              file.delete();
            }
          }
        }

        return null;
      }
    }.execute();
  }

  public static final class Entry {
    public final int level;
    public final String tag;
    public final String message;

    public Entry(int level, String tag, String message) {
      this.level = level;
      this.tag = tag;
      this.message = message;
    }

    public String prettyPrint() {
      return String.format("%22s %s %s", tag, displayLevel(),
          // Indent newlines to match the original indentation.
          message.replaceAll("\\n{1}", "\n                         "));
    }

    public String displayLevel() {
      switch (level) {
        case Log.VERBOSE:
          return "V";
        case Log.DEBUG:
          return "D";
        case Log.INFO:
          return "I";
        case Log.WARN:
          return "W";
        case Log.ERROR:
          return "E";
        case Log.ASSERT:
          return "A";
        default:
          return "?";
      }
    }
  }
}
