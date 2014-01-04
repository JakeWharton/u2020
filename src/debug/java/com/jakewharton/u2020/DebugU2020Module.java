package com.jakewharton.u2020;

import com.jakewharton.u2020.data.DebugDataModule;
import com.jakewharton.u2020.ui.DebugUiModule;
import dagger.Module;

@Module(
    includes = {
        DebugUiModule.class,
        DebugDataModule.class
    },
    complete = false,
    library = true
)
public class DebugU2020Module {
}
