package com.jakewharton.u2020.ui;

import com.jakewharton.u2020.DebugU2020Module;
import com.jakewharton.u2020.ui.debug.DebugAppContainer;
import com.jakewharton.u2020.ui.debug.SocketActivityHierarchyServer;
import dagger.Module;
import dagger.Provides;
import javax.inject.Singleton;

@Module(
    addsTo = DebugU2020Module.class,
    overrides = true
)
public class DebugUiModule {
  @Provides @Singleton AppContainer provideAppContainer(DebugAppContainer debugAppContainer) {
    return debugAppContainer;
  }

  @Provides @Singleton ActivityHierarchyServer provideActivityHierarchyServer() {
    return new SocketActivityHierarchyServer();
  }
}
