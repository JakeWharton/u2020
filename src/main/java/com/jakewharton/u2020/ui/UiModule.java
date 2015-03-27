package com.jakewharton.u2020.ui;

import com.jakewharton.u2020.ui.trending.TrendingView;
import dagger.Module;
import dagger.Provides;
import javax.inject.Singleton;

@Module(
    injects = {
        MainActivity.class,
        TrendingView.class,
    },
    complete = false,
    library = true
)
public final class UiModule {
  @Provides @Singleton AppContainer provideAppContainer() {
    return AppContainer.DEFAULT;
  }

  @Provides @Singleton ActivityHierarchyServer provideActivityHierarchyServer() {
    return ActivityHierarchyServer.NONE;
  }
}
