package com.jakewharton.u2020.ui;

import dagger.Module;
import dagger.Provides;
import javax.inject.Singleton;

@Module(
    overrides = true,
    library = true,
    complete = false
)
public final class InternalReleaseUiModule {
  @Provides @Singleton AppContainer provideAppContainer(
      TelescopeAppContainer telescopeAppContainer) {
    return telescopeAppContainer;
  }
}
