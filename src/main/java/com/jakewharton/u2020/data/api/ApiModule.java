package com.jakewharton.u2020.data.api;

import com.jakewharton.u2020.data.api.oauth.OauthInterceptor;
import com.jakewharton.u2020.data.api.oauth.OauthService;
import com.squareup.moshi.Moshi;
import dagger.Module;
import dagger.Provides;
import javax.inject.Named;
import javax.inject.Singleton;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import retrofit2.MoshiConverterFactory;
import retrofit2.Retrofit;
import retrofit2.RxJavaCallAdapterFactory;

@Module(
    complete = false,
    library = true,
    injects = {
        OauthService.class
    }
)
public final class ApiModule {
  public static final HttpUrl PRODUCTION_API_URL = HttpUrl.parse("https://api.github.com/");

  @Provides @Singleton HttpUrl provideBaseUrl() {
    return PRODUCTION_API_URL;
  }

  @Provides @Singleton @Named("Api") OkHttpClient provideApiClient(OkHttpClient client,
      OauthInterceptor oauthInterceptor) {
    return createApiClient(client, oauthInterceptor).build();
  }

  @Provides @Singleton Retrofit provideRetrofit(HttpUrl baseUrl, @Named("Api") OkHttpClient client,
      Moshi moshi) {
    return new Retrofit.Builder() //
        .client(client) //
        .baseUrl(baseUrl) //
        .addConverterFactory(MoshiConverterFactory.create(moshi)) //
        .addCallAdapterFactory(RxJavaCallAdapterFactory.create()) //
        .build();
  }

  @Provides @Singleton GithubService provideGithubService(Retrofit retrofit) {
    return retrofit.create(GithubService.class);
  }

  static OkHttpClient.Builder createApiClient(OkHttpClient client, OauthInterceptor oauthInterceptor) {
    return client.newBuilder()
        .addInterceptor(oauthInterceptor);
  }
}
