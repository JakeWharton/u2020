package com.jakewharton.u2020;

import android.app.Application;
import com.jakewharton.u2020.data.DataModule;
import com.jakewharton.u2020.ui.UiModule;
import dagger.Module;
import dagger.Provides;
import javax.inject.Singleton;

@Module(includes = { UiModule.class, DataModule.class }, injects = { U2020App.class })
public final class U2020Module {

    private final U2020App app;

    public U2020Module(U2020App app) {
        this.app = app;
    }

    @Provides
    @Singleton
    Application provideApplication() {
        return app;
    }
}
