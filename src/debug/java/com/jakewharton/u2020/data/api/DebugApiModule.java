package com.jakewharton.u2020.data.api;

import android.content.SharedPreferences;
import com.jakewharton.u2020.data.DebugDataModule;
import com.jakewharton.u2020.data.IsMockMode;
import dagger.Module;
import dagger.Provides;
import javax.inject.Singleton;
import retrofit.MockRestAdapter;
import retrofit.RestAdapter;
import retrofit.android.AndroidMockValuePersistence;

@Module(
    addsTo = DebugDataModule.class,
    overrides = true
)
public class DebugApiModule {
  @Provides @Singleton
  MockRestAdapter provideMockRestAdapter(RestAdapter restAdapter, SharedPreferences preferences) {
    MockRestAdapter mockRestAdapter = MockRestAdapter.from(restAdapter);
    AndroidMockValuePersistence.install(mockRestAdapter, preferences);
    return mockRestAdapter;
  }

  @Provides @Singleton
  GalleryService provideGalleryService(RestAdapter restAdapter, MockRestAdapter mockRestAdapter,
      @IsMockMode boolean isMockMode, MockGalleryService mockService) {
    if (isMockMode) {
      return mockRestAdapter.create(GalleryService.class, mockService);
    }
    return restAdapter.create(GalleryService.class);
  }
}
