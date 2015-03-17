package com.jakewharton.u2020.ui;

import com.jakewharton.u2020.ui.trending.TrendingView;
import com.jakewharton.u2020.util.ViewSubscriptions;
import dagger.Module;
import dagger.Provides;
import javax.inject.Named;
import javax.inject.Singleton;
import rx.Scheduler;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

@Module(
    injects = {
        MainActivity.class,
        TrendingView.class,
    },
    complete = false,
    library = true
)
public class UiModule {
  @Provides @Singleton AppContainer provideAppContainer() {
    return AppContainer.DEFAULT;
  }

  @Provides @Singleton ActivityHierarchyServer provideActivityHierarchyServer() {
    return ActivityHierarchyServer.NONE;
  }

  @Provides @Singleton @Named("subscribeOn") Scheduler provideSubscribeOnScheduler() {
    return Schedulers.io();
  }

  @Provides @Singleton @Named("observeOn") Scheduler provideObserveOnScheduler() {
    return AndroidSchedulers.mainThread();
  }

  @Provides ViewSubscriptions provideViewSubscriptions(
      @Named("subscribeOn") Scheduler subscribeOnScheduler,
      @Named("observeOn") Scheduler observeOnScheduler) {
    return new ViewSubscriptions(subscribeOnScheduler, observeOnScheduler);
  }
}
