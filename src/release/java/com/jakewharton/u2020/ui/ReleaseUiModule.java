package com.jakewharton.u2020.ui;

import com.jakewharton.u2020.ui.gallery.GalleryView;
import dagger.Module;
import dagger.Provides;
import javax.inject.Singleton;

@Module
public class ReleaseUiModule {
  @Provides @Singleton AppContainer provideAppContainer() {
    return AppContainer.DEFAULT;
  }

  @Provides @Singleton ActivityHierarchyServer provideActivityHierarchyServer() {
    return ActivityHierarchyServer.NONE;
  }
}
