package com.jakewharton.u2020.data.api;

import com.jakewharton.u2020.data.api.model.RepositoriesResponse;
import com.jakewharton.u2020.data.api.model.Repository;
import java.util.ArrayList;
import retrofit2.adapter.rxjava.Result;
import retrofit2.mock.BehaviorDelegate;
import retrofit2.mock.Calls;
import retrofit2.mock.MockRetrofit;
import rx.Observable;

public final class MockGithubService implements GithubService {
  private final BehaviorDelegate<GithubService> delegate;
  private final MockResponseSupplier responses;

  public MockGithubService(MockRetrofit mockRetrofit, MockResponseSupplier responses) {
    this.delegate = mockRetrofit.create(GithubService.class);
    this.responses = responses;
  }

  @Override public Observable<Result<RepositoriesResponse>> repositories(SearchQuery query,
      Sort sort, Order order) {
    RepositoriesResponse response = responses.get(MockRepositoriesResponse.class).response;

    if (response.items != null) {
      // Don't modify the original list when sorting.
      ArrayList<Repository> items = new ArrayList<>(response.items);
      SortUtil.sort(items, sort, order);
      response = new RepositoriesResponse(items);
    }

    return delegate.returning(Calls.response(response)).repositories(query, sort, order);
  }
}
