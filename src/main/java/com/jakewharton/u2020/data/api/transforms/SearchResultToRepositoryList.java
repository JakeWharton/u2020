package com.jakewharton.u2020.data.api.transforms;

import com.jakewharton.u2020.data.api.model.Repository;
import com.jakewharton.u2020.data.api.model.SearchResult;
import java.util.List;
import rx.functions.Func1;

public final class SearchResultToRepositoryList implements Func1<SearchResult, List<Repository>> {
  @Override public List<Repository> call(SearchResult searchResult) {
    return searchResult.items;
  }
}
