package com.jakewharton.u2020;

import com.jakewharton.u2020.data.DebugDataModule;
import com.jakewharton.u2020.ui.DebugUiModule;
import dagger.Module;

@Module(
    addsTo = U2020Module.class,
    includes = {
        DebugUiModule.class,
        DebugDataModule.class
    },
    overrides = true
)
public class DebugU2020Module {
}
