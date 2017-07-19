package com.jakewharton.u2020.data.api;

import org.threeten.bp.LocalDate;

import static com.jakewharton.u2020.util.Preconditions.checkNotNull;
import static org.threeten.bp.format.DateTimeFormatter.ISO_LOCAL_DATE;

public final class SearchQuery {
  private final LocalDate createdSince;

  private SearchQuery(Builder builder) {
    this.createdSince = checkNotNull(builder.createdSince, "createdSince == null");
  }

  @Override public String toString() {
    // Returning null here is not ideal, but it lets retrofit drop the query param altogether.
    return createdSince == null ? null : "created:>=" + ISO_LOCAL_DATE.format(createdSince);
  }

  public static final class Builder {
    private LocalDate createdSince;

    public Builder createdSince(LocalDate createdSince) {
      this.createdSince = createdSince;
      return this;
    }

    public SearchQuery build() {
      return new SearchQuery(this);
    }
  }
}
