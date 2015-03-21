package com.jakewharton.u2020.ui.trending;

import com.jakewharton.u2020.data.api.SearchQuery;
import org.joda.time.DateTime;
import org.joda.time.DurationFieldType;

public enum TrendingTimespan {
  DAY("today", 1, DurationFieldType.days()),
  WEEK("last week", 1, DurationFieldType.weeks()),
  MONTH("last month", 1, DurationFieldType.months());

  private final String name;
  private final int duration;
  private final DurationFieldType durationType;

  TrendingTimespan(String name, int duration, DurationFieldType durationType) {
    this.name = name;
    this.duration = duration;
    this.durationType = durationType;
  }

  /** Returns a {@code DateTime} to use with {@link SearchQuery.Builder#createdSince(DateTime)}. */
  public DateTime createdSince() {
    return DateTime.now().withFieldAdded(durationType, -duration);
  }

  @Override public String toString() {
    return name;
  }
}
