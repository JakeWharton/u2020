package com.jakewharton.u2020.ui;

import android.app.Activity;
import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.TextView;
import com.jakewharton.u2020.U2020App;
import javax.inject.Inject;

public class MainActivity extends Activity {
  @Inject AppContainer appContainer;

  private ViewGroup container;

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    U2020App app = (U2020App) getApplication();
    app.getObjectGraph().inject(this);

    container = appContainer.get(this, app);

    TextView tv = new TextView(this);
    tv.setText("FOO");
    container.addView(tv);
  }
}
