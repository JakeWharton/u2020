package com.jakewharton.u2020.ui;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.NonNull;
import com.jakewharton.u2020.R;
import com.jakewharton.u2020.data.Injector;
import dagger.ObjectGraph;

public final class DebugActivity extends Activity {

    private ObjectGraph appGraph;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        appGraph = Injector.obtain(getApplication());
        setContentView(R.layout.debug_activity);
    }

    @Override
    public Object getSystemService(@NonNull String name) {
        if (Injector.matchesService(name)) {
            return appGraph;
        }
        return super.getSystemService(name);
    }
}
