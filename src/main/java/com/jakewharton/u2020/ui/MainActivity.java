package com.jakewharton.u2020.ui;

import android.app.Activity;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import butterknife.ButterKnife;
import butterknife.InjectView;
import com.jakewharton.u2020.R;
import com.jakewharton.u2020.U2020App;
import javax.inject.Inject;

public final class MainActivity extends Activity {
  @Inject AppContainer appContainer;

  @InjectView(R.id.main_drawer_layout) DrawerLayout drawerLayout;
  @InjectView(R.id.main_drawer_content) ViewGroup drawer;
  @InjectView(R.id.main_content) ViewGroup content;

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    LayoutInflater inflater = getLayoutInflater();

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
      // Remove the status bar color. The DrawerLayout is responsible for drawing it from now on.
      getWindow().setStatusBarColor(Color.TRANSPARENT);
    }

    U2020App app = U2020App.get(this);
    app.inject(this);

    ViewGroup container = appContainer.get(this);

    inflater.inflate(R.layout.main_activity, container);
    ButterKnife.inject(this, container);

    drawerLayout.setStatusBarBackgroundColor(getResources().getColor(R.color.status_bar));
    drawerLayout.setDrawerShadow(R.drawable.drawer_shadow, Gravity.START);

    inflater.inflate(R.layout.main_drawer, drawer);
    inflater.inflate(R.layout.trending_view, content);
  }
}
