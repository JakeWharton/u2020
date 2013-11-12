package com.jakewharton.u2020.data.api;

import com.jakewharton.u2020.data.DataModule;
import com.squareup.okhttp.OkHttpClient;
import dagger.Module;
import dagger.Provides;
import javax.inject.Singleton;
import retrofit.RestAdapter;
import retrofit.Server;
import retrofit.client.Client;
import retrofit.client.OkClient;

@Module(
    addsTo = DataModule.class
)
public class ApiModule {
  public static final String PRODUCTION_API_URL = "https://api.imgur.com/3/";

  @Provides @Singleton Server provideServer() {
    return new Server(PRODUCTION_API_URL);
  }

  @Provides @Singleton Client provideClient(OkHttpClient client) {
    return new OkClient(client);
  }

  @Provides @Singleton RestAdapter provideRestAdapter(Server server, Client client) {
    return new RestAdapter.Builder()
        .setClient(client)
        .setServer(server)
        .build();
  }

  @Provides @Singleton GalleryService provideGalleryService(RestAdapter restAdapter) {
    return restAdapter.create(GalleryService.class);
  }
}
