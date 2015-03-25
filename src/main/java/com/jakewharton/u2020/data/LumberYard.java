package com.jakewharton.u2020.data;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;
import javax.inject.Inject;
import javax.inject.Singleton;
import rx.Observable;
import rx.subjects.PublishSubject;
import timber.log.Timber;

@Singleton
public class LumberYard {
  private static final int BUFFER_SIZE = 200;

  private final Deque<Entry> entries = new ArrayDeque<>(BUFFER_SIZE + 1);
  private final PublishSubject<Entry> entrySubject = PublishSubject.create();

  @Inject public LumberYard() {
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

  public static final class Entry {
    public final int level;
    public final String tag;
    public final String message;

    public Entry(int level, String tag, String message) {
      this.level = level;
      this.tag = tag;
      this.message = message;
    }
  }
}
