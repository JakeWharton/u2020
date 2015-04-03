package com.jakewharton.u2020.ui;

import android.annotation.TargetApi;
import android.app.Activity;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.widget.DrawerLayout;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Toast;
import butterknife.ButterKnife;
import butterknife.InjectView;
import com.jakewharton.u2020.R;
import com.jakewharton.u2020.data.Injector;
import dagger.ObjectGraph;
import javax.inject.Inject;

import static android.widget.Toast.LENGTH_SHORT;

public final class MainActivity extends Activity {
  @InjectView(R.id.main_drawer_layout) DrawerLayout drawerLayout;
  @InjectView(R.id.main_drawer) NavDrawerView drawer;
  @InjectView(R.id.main_content) ViewGroup content;

  @Inject AppContainer appContainer;

  private ObjectGraph activityGraph;

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    LayoutInflater inflater = getLayoutInflater();

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
      // Remove the status bar color. The DrawerLayout is responsible for drawing it from now on.
      setStatusBarColor(getWindow());
    }

    // Explicitly reference the application object since we don't want to match our own injector.
    ObjectGraph appGraph = Injector.obtain(getApplication());
    appGraph.inject(this);
    activityGraph = appGraph.plus(new MainActivityModule(this));

    ViewGroup container = appContainer.bind(this);

    inflater.inflate(R.layout.main_activity, container);
    ButterKnife.inject(this, container);

    drawerLayout.setStatusBarBackgroundColor(getResources().getColor(R.color.status_bar));
    drawerLayout.setDrawerShadow(R.drawable.drawer_shadow, Gravity.START);

    drawer.addItem(R.drawable.nav_trending, R.string.nav_trending, new View.OnClickListener() {
      @Override public void onClick(View v) {
        drawerLayout.closeDrawers();
        Toast.makeText(MainActivity.this, "Trending!", LENGTH_SHORT).show();
      }
    });
    drawer.addItem(R.drawable.nav_search, R.string.nav_search, new View.OnClickListener() {
      @Override public void onClick(View v) {
        drawerLayout.closeDrawers();
        Toast.makeText(MainActivity.this, "Search!", LENGTH_SHORT).show();
      }
    });

    inflater.inflate(R.layout.trending_view, content);
  }

  @Override public Object getSystemService(@NonNull String name) {
    if (Injector.matchesService(name)) {
      return activityGraph;
    }
    return super.getSystemService(name);
  }

  @TargetApi(Build.VERSION_CODES.LOLLIPOP)
  private static void setStatusBarColor(Window window) {
    window.setStatusBarColor(Color.TRANSPARENT);
  }
}
