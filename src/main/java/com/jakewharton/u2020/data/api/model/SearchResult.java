package com.jakewharton.u2020.data.api.model;

import java.util.List;

public final class SearchResult {
  public final List<Repository> items;

  public SearchResult(List<Repository> items) {
    this.items = items;
  }
}
