package com.jakewharton.u2020.ui;

import android.app.Activity;
import android.os.Bundle;
import com.jakewharton.u2020.R;

public final class DebugActivity extends Activity {
  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.debug_activity);
  }
}
