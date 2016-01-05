package com.jakewharton.u2020.data.api;

import android.content.SharedPreferences;
import com.jakewharton.u2020.data.api.model.RepositoriesResponse;
import com.jakewharton.u2020.data.api.model.Repository;
import com.jakewharton.u2020.util.EnumPreferences;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;
import javax.inject.Inject;
import javax.inject.Singleton;
import retrofit2.Result;
import retrofit2.mock.BehaviorDelegate;
import retrofit2.mock.Calls;
import retrofit2.mock.MockRetrofit;
import rx.Observable;

@Singleton
public final class MockGithubService implements GithubService {
  private final BehaviorDelegate<GithubService> delegate;
  private final SharedPreferences preferences;
  private final Map<Class<? extends Enum<?>>, Enum<?>> responses = new LinkedHashMap<>();

  @Inject MockGithubService(MockRetrofit mockRetrofit, SharedPreferences preferences) {
    this.delegate = mockRetrofit.create(GithubService.class);
    this.preferences = preferences;

    // Initialize mock responses.
    loadResponse(MockRepositoriesResponse.class, MockRepositoriesResponse.SUCCESS);
  }

  /**
   * Initializes the current response for {@code responseClass} from {@code SharedPreferences}, or
   * uses {@code defaultValue} if a response was not found.
   */
  private <T extends Enum<T>> void loadResponse(Class<T> responseClass, T defaultValue) {
    responses.put(responseClass, EnumPreferences.getEnumValue(preferences, responseClass, //
        responseClass.getCanonicalName(), defaultValue));
  }

  public <T extends Enum<T>> T getResponse(Class<T> responseClass) {
    return responseClass.cast(responses.get(responseClass));
  }

  public <T extends Enum<T>> void setResponse(Class<T> responseClass, T value) {
    responses.put(responseClass, value);
    EnumPreferences.saveEnumValue(preferences, responseClass.getCanonicalName(), value);
  }

  @Override public Observable<Result<RepositoriesResponse>> repositories(SearchQuery query,
      Sort sort, Order order) {
    RepositoriesResponse response = getResponse(MockRepositoriesResponse.class).response;

    if (response.items != null) {
      // Don't modify the original list when sorting.
      ArrayList<Repository> items = new ArrayList<>(response.items);
      SortUtil.sort(items, sort, order);
      response = new RepositoriesResponse(items);
    }

    return delegate.returning(Calls.response(response)).repositories(query, sort, order);
  }
}
