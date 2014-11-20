package com.jakewharton.u2020;

import com.jakewharton.u2020.data.DebugDataModule;
import com.jakewharton.u2020.ui.DebugUiModule;

import dagger.Component;
import javax.inject.Singleton;

/**
 * The core debug component for u2020 applications
 */
@Singleton
@Component(modules = { U2020AppModule.class, DebugUiModule.class, DebugDataModule.class })
public interface U2020Component extends U2020Graph {
  /**
   * An initializer that creates the graph from an application. 
   */
  final static class Initializer {
    static U2020Graph init(U2020App app) {
      return Dagger_U2020Component.builder()
          .u2020AppModule(new U2020AppModule(app))
          .build();
    }
    private Initializer() {} // No instances.
  }
}
