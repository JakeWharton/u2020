package com.jakewharton.u2020.data.api;

import com.jakewharton.u2020.data.api.model.RepositoriesResponse;
import retrofit2.Result;
import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

public interface GithubService {
  @GET("search/repositories") //
  Observable<Result<RepositoriesResponse>> repositories( //
      @Query("q") SearchQuery query, //
      @Query("sort") Sort sort, //
      @Query("order") Order order);
}
