package com.jakewharton.u2020.data.api;

import com.jakewharton.u2020.data.api.model.SearchResult;
import retrofit.http.GET;
import retrofit.http.Query;
import rx.Observable;

public interface GithubService {
  @GET("/search/repositories") //
  Observable<SearchResult> repositories( //
      @Query("q") SearchQuery query, //
      @Query("sort") Sort sort, //
      @Query("order") Order order);
}
