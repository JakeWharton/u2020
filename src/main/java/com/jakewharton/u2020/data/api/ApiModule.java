package com.jakewharton.u2020.data.api;

import com.google.gson.Gson;
import com.jakewharton.u2020.data.api.oauth.OauthInterceptor;
import com.jakewharton.u2020.data.api.oauth.OauthService;
import com.squareup.okhttp.OkHttpClient;
import dagger.Module;
import dagger.Provides;
import javax.inject.Named;
import javax.inject.Singleton;
import retrofit.Endpoint;
import retrofit.Endpoints;
import retrofit.RestAdapter;
import retrofit.client.OkClient;
import retrofit.converter.GsonConverter;

@Module(
    complete = false,
    library = true,
    injects = {
        OauthService.class
    }
)
public final class ApiModule {
  public static final String PRODUCTION_API_URL = "https://api.github.com";

  @Provides @Singleton Endpoint provideEndpoint() {
    return Endpoints.newFixedEndpoint(PRODUCTION_API_URL);
  }

  @Provides @Singleton @Named("Api") OkHttpClient provideApiClient(OkHttpClient client,
      OauthInterceptor oauthInterceptor) {
    return createApiClient(client, oauthInterceptor);
  }

  @Provides @Singleton RestAdapter provideRestAdapter(Endpoint endpoint,
      @Named("Api") OkHttpClient client, Gson gson) {
    return new RestAdapter.Builder() //
        .setClient(new OkClient(client)) //
        .setEndpoint(endpoint) //
        .setConverter(new GsonConverter(gson)) //
        .build();
  }

  @Provides @Singleton GithubService provideGithubService(RestAdapter restAdapter) {
    return restAdapter.create(GithubService.class);
  }

  static OkHttpClient createApiClient(OkHttpClient client, OauthInterceptor oauthInterceptor) {
    client = client.clone();
    client.interceptors().add(oauthInterceptor);
    return client;
  }
}
