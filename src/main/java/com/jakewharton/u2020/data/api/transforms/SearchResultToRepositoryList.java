package com.jakewharton.u2020.data.api.transforms;

import com.jakewharton.u2020.data.api.model.RepositoriesResponse;
import com.jakewharton.u2020.data.api.model.Repository;
import java.util.List;
import rx.functions.Func1;

public final class SearchResultToRepositoryList implements Func1<RepositoriesResponse, List<Repository>> {
  @Override public List<Repository> call(RepositoriesResponse repositoriesResponse) {
    return repositoriesResponse.items;
  }
}
