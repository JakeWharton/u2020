package com.jakewharton.u2020.data.api.transforms;

import com.jakewharton.u2020.data.api.model.RepositoriesResponse;
import com.jakewharton.u2020.data.api.model.Repository;
import java.util.Collections;
import java.util.List;
import retrofit.Result;
import rx.functions.Func1;

public final class SearchResultToRepositoryList implements Func1<Result<RepositoriesResponse>, List<Repository>> {
  private static volatile SearchResultToRepositoryList instance;

  public static SearchResultToRepositoryList instance() {
    if (instance == null) {
      instance = new SearchResultToRepositoryList();
    }
    return instance;
  }

  @Override public List<Repository> call(Result<RepositoriesResponse> result) {
    RepositoriesResponse repositoriesResponse = result.response().body();
    return repositoriesResponse.items == null //
        ? Collections.<Repository>emptyList() //
        : repositoriesResponse.items;
  }
}
