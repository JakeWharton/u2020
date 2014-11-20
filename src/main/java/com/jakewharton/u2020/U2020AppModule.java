package com.jakewharton.u2020;

import android.app.Application;
import dagger.Module;
import dagger.Provides;
import javax.inject.Singleton;

@Module
public final class U2020AppModule {
  private final U2020App app;

  public U2020AppModule(U2020App app) {
    this.app = app;
  }

  @Provides @Singleton Application provideApplication() {
    return app;
  }
}
