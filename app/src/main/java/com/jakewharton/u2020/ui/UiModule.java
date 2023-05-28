package com.jakewharton.u2020.ui;

import dagger.Module;
import dagger.Provides;
import javax.inject.Singleton;

@Module(injects = { MainActivity.class }, complete = false, library = true)
public final class UiModule {

    @Provides
    @Singleton
    ViewContainer provideViewContainer() {
        return ViewContainer.DEFAULT;
    }
}
