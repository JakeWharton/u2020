package com.jakewharton.u2020;

import com.jakewharton.u2020.ui.InternalReleaseUiModule;
import dagger.Module;

@Module(
    addsTo = U2020Module.class,
    includes = InternalReleaseUiModule.class,
    overrides = true
)
public final class InternalReleaseU2020Module {
}
