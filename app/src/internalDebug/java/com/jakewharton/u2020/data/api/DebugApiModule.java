package com.jakewharton.u2020.data.api;

import android.content.SharedPreferences;
import com.f2prateek.rx.preferences.Preference;
import com.jakewharton.u2020.data.ApiEndpoint;
import com.jakewharton.u2020.data.IsMockMode;
import com.jakewharton.u2020.data.NetworkDelay;
import com.jakewharton.u2020.data.NetworkErrorCode;
import com.jakewharton.u2020.data.NetworkErrorPercent;
import com.jakewharton.u2020.data.NetworkFailurePercent;
import com.jakewharton.u2020.data.NetworkVariancePercent;
import com.jakewharton.u2020.data.api.oauth.OauthInterceptor;
import dagger.Module;
import dagger.Provides;
import javax.inject.Named;
import javax.inject.Singleton;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.mock.MockRetrofit;
import retrofit2.mock.NetworkBehavior;
import timber.log.Timber;

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

  @Provides @Singleton HttpLoggingInterceptor provideLoggingInterceptor() {
    HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor(message -> Timber.tag("OkHttp").v(message));
    loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.HEADERS);
    return loggingInterceptor;
  }

  @Provides @Singleton @Named("Api") OkHttpClient provideApiClient(OkHttpClient client,
      OauthInterceptor oauthInterceptor, HttpLoggingInterceptor loggingInterceptor) {
    return ApiModule.createApiClient(client, oauthInterceptor)
        .addInterceptor(loggingInterceptor)
        .build();
  }

  @Provides @Singleton NetworkBehavior provideBehavior(@NetworkDelay Preference<Long> networkDelay,
      @NetworkVariancePercent Preference<Integer> networkVariancePercent,
      @NetworkFailurePercent Preference<Integer> networkFailurePercent,
      @NetworkErrorPercent Preference<Integer> networkErrorPercent,
      Preference<NetworkErrorCode> networkErrorCode) {
    NetworkBehavior behavior = NetworkBehavior.create();
    behavior.setDelay(networkDelay.get(), MILLISECONDS);
    behavior.setVariancePercent(networkVariancePercent.get());
    behavior.setFailurePercent(networkFailurePercent.get());
    behavior.setErrorPercent(networkErrorPercent.get());
    behavior.setErrorFactory(
        () -> Response.error(networkErrorCode.get().code, ResponseBody.create(null, new byte[0])));
    return behavior;
  }

  @Provides @Singleton MockRetrofit provideMockRetrofit(Retrofit retrofit,
      NetworkBehavior behavior) {
    return new MockRetrofit.Builder(retrofit)
        .networkBehavior(behavior)
        .build();
  }

  @Provides @Singleton GithubService provideGithubService(Retrofit retrofit,
      @IsMockMode boolean isMockMode, MockGithubService mockService) {
    return isMockMode ? mockService : retrofit.create(GithubService.class);
  }

  @Provides @Singleton MockResponseSupplier provideResponseSupplier(SharedPreferences preferences) {
    return new SharedPreferencesMockResponseSupplier(preferences);
  }

  @Provides @Singleton MockGithubService provideMockGitHubService(MockRetrofit mockRetrofit,
      MockResponseSupplier responseSupplier) {
    return new MockGithubService(mockRetrofit, responseSupplier);
  }
}
