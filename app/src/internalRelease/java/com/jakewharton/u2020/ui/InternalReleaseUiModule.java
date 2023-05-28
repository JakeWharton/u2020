package com.jakewharton.u2020.ui;

import dagger.Module;
import dagger.Provides;
import javax.inject.Singleton;

@Module(overrides = true, library = true, complete = false)
public final class InternalReleaseUiModule {

    @Provides
    @Singleton
    ViewContainer provideViewContainer(TelescopeViewContainer telescopeViewContainer) {
        return telescopeViewContainer;
    }
}
