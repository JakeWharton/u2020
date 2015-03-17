package com.jakewharton.u2020.ui;

import android.app.Activity;
import android.os.Bundle;
import android.view.ViewGroup;
import com.jakewharton.u2020.R;
import com.jakewharton.u2020.U2020App;
import javax.inject.Inject;

public class MainActivity extends Activity {
  @Inject AppContainer appContainer;

  private ViewGroup container;

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    U2020App app = U2020App.get(this);
    app.inject(this);

    container = appContainer.get(this);

    getLayoutInflater().inflate(R.layout.trending_view, container);
  }
}
