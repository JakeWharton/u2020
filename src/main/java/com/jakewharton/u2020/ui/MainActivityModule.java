package com.jakewharton.u2020.ui;

import android.support.v4.widget.DrawerLayout;
import com.jakewharton.u2020.U2020Module;
import com.jakewharton.u2020.ui.trending.TrendingView;
import dagger.Module;
import dagger.Provides;
import javax.inject.Singleton;

@Module(
    addsTo = U2020Module.class,
    injects = TrendingView.class
)
public final class MainActivityModule {
  private final MainActivity mainActivity;

  MainActivityModule(MainActivity mainActivity) {
    this.mainActivity = mainActivity;
  }

  @Provides @Singleton DrawerLayout provideDrawerLayout() {
    return mainActivity.drawerLayout;
  }
}
