package com.jakewharton.u2020.data.api.transforms;

import com.jakewharton.u2020.data.api.model.RepositoriesResponse;
import com.jakewharton.u2020.data.api.model.Repository;
import java.util.Collections;
import java.util.List;
import rx.functions.Func1;

public final class SearchResultToRepositoryList implements Func1<RepositoriesResponse, List<Repository>> {
  private static SearchResultToRepositoryList instance;

  public static SearchResultToRepositoryList instance() {
    if (instance == null) {
      instance = new SearchResultToRepositoryList();
    }
    return instance;
  }

  @Override public List<Repository> call(RepositoriesResponse repositoriesResponse) {
    return repositoriesResponse.items == null //
        ? Collections.<Repository>emptyList() //
        : repositoriesResponse.items;
  }
}
