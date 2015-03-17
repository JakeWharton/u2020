package com.jakewharton.u2020.data.api;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import static com.jakewharton.u2020.util.Preconditions.checkNotNull;

public final class SearchQuery {
  private static final DateTimeFormatter ISO_8601_DATE = DateTimeFormat.forPattern("yyyy-MM-dd");

  private final DateTime createdSince;

  private SearchQuery(Builder builder) {
    this.createdSince = checkNotNull(builder.createdSince, "createdSince == null");
  }

  @Override public String toString() {
    // Returning null here is not ideal, but it lets retrofit drop the query param altogether.
    return createdSince == null ? null : "created:>=" + ISO_8601_DATE.print(createdSince);
  }

  public static final class Builder {
    private DateTime createdSince;

    public Builder createdSince(DateTime createdSince) {
      this.createdSince = createdSince;
      return this;
    }

    public SearchQuery build() {
      return new SearchQuery(this);
    }
  }
}
