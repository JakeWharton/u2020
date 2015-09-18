package com.jakewharton.u2020.data.api;

import com.f2prateek.rx.preferences.Preference;
import com.jakewharton.u2020.data.ApiEndpoint;
import com.jakewharton.u2020.data.IsMockMode;
import com.jakewharton.u2020.data.NetworkDelay;
import com.jakewharton.u2020.data.NetworkFailurePercent;
import com.jakewharton.u2020.data.NetworkVariancePercent;
import com.jakewharton.u2020.data.api.oauth.OauthInterceptor;
import com.squareup.okhttp.HttpUrl;
import com.squareup.okhttp.OkHttpClient;
import dagger.Module;
import dagger.Provides;
import javax.inject.Named;
import javax.inject.Singleton;
import retrofit.Retrofit;
import retrofit.mock.MockRetrofit;
import retrofit.mock.NetworkBehavior;
import retrofit.mock.RxJavaBehaviorAdapter;

import static java.util.concurrent.TimeUnit.MILLISECONDS;

@Module(
    complete = false,
    library = true,
    overrides = true
)
public final class DebugApiModule {
  @Provides @Singleton HttpUrl provideHttpUrl(@ApiEndpoint Preference<String> apiEndpoint) {
    return HttpUrl.parse(apiEndpoint.get());
  }

  @Provides @Singleton @Named("Api") OkHttpClient provideApiClient(OkHttpClient client,
      OauthInterceptor oauthInterceptor, LoggingInterceptor loggingInterceptor) {
    client = ApiModule.createApiClient(client, oauthInterceptor);
    client.interceptors().add(loggingInterceptor);
    return client;
  }

  @Provides @Singleton NetworkBehavior provideBehavior(@NetworkDelay Preference<Long> networkDelay,
      @NetworkFailurePercent Preference<Integer> networkFailurePercent,
      @NetworkVariancePercent Preference<Integer> networkVariancePercent) {
    NetworkBehavior behavior = NetworkBehavior.create();
    behavior.setDelay(networkDelay.get(), MILLISECONDS);
    behavior.setFailurePercent(networkFailurePercent.get());
    behavior.setVariancePercent(networkVariancePercent.get());
    return behavior;
  }

  @Provides @Singleton MockRetrofit provideMockRetrofit(NetworkBehavior behavior) {
    return new MockRetrofit(behavior, RxJavaBehaviorAdapter.create());
  }

  @Provides @Singleton GithubService provideGithubService(Retrofit retrofit,
      MockRetrofit mockRetrofit, @IsMockMode boolean isMockMode, MockGithubService mockService) {
    if (isMockMode) {
      return mockRetrofit.create(GithubService.class, mockService);
    }
    return retrofit.create(GithubService.class);
  }
}
