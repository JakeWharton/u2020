package com.jakewharton.u2020.util;

import android.os.Looper;
import rx.Observable;
import rx.Observer;
import rx.Scheduler;
import rx.functions.Action1;
import rx.subscriptions.CompositeSubscription;

/**
 * Subscribes to an observable using the appropriate {@link Observable#observeOn(Scheduler)
 * observeOn} and {@link Observable#subscribeOn(Scheduler) subscribeOn} schedulers. Multiple
 * observables can be registered and unsubscribed all at once.
 */
public final class ViewSubscriptions {
  private final Scheduler subscribeOnScheduler;
  private final Scheduler observeOnScheduler;

  private CompositeSubscription subscriptions = new CompositeSubscription();

  public ViewSubscriptions(Scheduler subscribeOnScheduler, Scheduler observeOnScheduler) {
    this.subscribeOnScheduler = subscribeOnScheduler;
    this.observeOnScheduler = observeOnScheduler;
  }

  public <T> void add(Observable<T> observable, Action1<? super T> action) {
    if (Looper.myLooper() != Looper.getMainLooper()) {
      throw new AssertionError("Must be on main thread");
    }
    subscriptions.add(observable.subscribeOn(subscribeOnScheduler)
        .observeOn(observeOnScheduler)
        .subscribe(action));
  }

  public <T> void add(Observable<T> observable, Observer<? super T> observer) {
    if (Looper.myLooper() != Looper.getMainLooper()) {
      throw new AssertionError("Must be on main thread");
    }
    subscriptions.add(observable.subscribeOn(subscribeOnScheduler)
        .observeOn(observeOnScheduler)
        .unsubscribeOn(observeOnScheduler)
        .subscribe(observer));
  }

  public void unsubscribe() {
    subscriptions.unsubscribe();
    subscriptions = new CompositeSubscription();
  }
}
