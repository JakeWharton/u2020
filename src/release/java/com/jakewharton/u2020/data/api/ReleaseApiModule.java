package com.jakewharton.u2020.data.api;

import com.squareup.okhttp.OkHttpClient;
import dagger.Module;
import dagger.Provides;
import javax.inject.Singleton;
import retrofit.Endpoint;
import retrofit.Endpoints;
import retrofit.RestAdapter;
import retrofit.client.Client;
import retrofit.client.OkClient;

@Module(includes = ApiModule.class)
public final class ReleaseApiModule {

  @Provides @Singleton Endpoint provideEndpoint() {
    return Endpoints.newFixedEndpoint(ApiModule.PRODUCTION_API_URL);
  }

  @Provides @Singleton GalleryService provideGalleryService(RestAdapter restAdapter) {
    return restAdapter.create(GalleryService.class);
  }
}
