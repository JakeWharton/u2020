package com.jakewharton.u2020.data.api;

import com.jakewharton.u2020.data.api.model.Repository;
import com.jakewharton.u2020.data.api.model.SearchResult;
import com.jakewharton.u2020.data.api.model.User;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.inject.Inject;
import javax.inject.Singleton;
import org.joda.time.DateTime;
import retrofit.http.Query;
import rx.Observable;

@Singleton
final class MockGithubService implements GithubService {
  @Inject MockGithubService() {
  }

  @Override public Observable<SearchResult> repositories(@Query("q") SearchQuery query,
      @Query("sort") Sort sort, @Query("order") Order order) {
    List<Repository> repositories = new ArrayList<>(REPOSITORIES);
    SortUtil.sort(repositories, sort, order);

    return Observable.just(new SearchResult(repositories));
  }

  private static final List<Repository> REPOSITORIES = Arrays.asList( //
      new Repository.Builder() //
          .name("u2020") //
          .owner(new User("JakeWharton")) //
          .description("A sample Android app which showcases advanced usage of Dagger among other"
              + " open source libraries.") //
          .forks(260) //
          .stars(1487) //
          .watchers(140) //
          .htmlUrl("https://github.com/JakeWharton/u2020") //
          .updatedAt(DateTime.parse("2015-03-14")) //
          .build());
}
