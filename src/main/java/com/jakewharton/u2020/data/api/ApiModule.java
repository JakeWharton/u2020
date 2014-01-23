package com.jakewharton.u2020.data.api;

import com.squareup.okhttp.OkHttpClient;
import dagger.Module;
import dagger.Provides;
import javax.inject.Singleton;
import retrofit.RestAdapter;
import retrofit.Server;
import retrofit.client.Client;
import retrofit.client.OkClient;

@Module(
    complete = false,
    library = true
)
public final class ApiModule {
  public static final String PRODUCTION_API_URL = "https://api.imgur.com/3/";
  private static final String CLIENT_ID = "3436c108ccc17d3";

  @Provides @Singleton @ClientId String provideClientId() {
    return CLIENT_ID;
  }

  @Provides @Singleton Server provideServer() {
    return new Server(PRODUCTION_API_URL);
  }

  @Provides @Singleton Client provideClient(OkHttpClient client) {
    return new OkClient(client);
  }

  @Provides @Singleton
  RestAdapter provideRestAdapter(Server server, Client client, ApiHeaders headers) {
    return new RestAdapter.Builder() //
        .setClient(client) //
        .setServer(server) //
        .setRequestInterceptor(headers) //
        .build();
  }

  @Provides @Singleton GalleryService provideGalleryService(RestAdapter restAdapter) {
    return restAdapter.create(GalleryService.class);
  }
}
