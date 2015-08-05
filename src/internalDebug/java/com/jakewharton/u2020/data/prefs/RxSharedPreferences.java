package com.jakewharton.u2020.data.prefs;

import android.content.SharedPreferences;
import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.functions.Action0;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.subscriptions.Subscriptions;

import static android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import static com.jakewharton.u2020.util.Preconditions.checkNotNull;

public final class RxSharedPreferences {
  public static RxSharedPreferences create(SharedPreferences sharedPreferences) {
    return new RxSharedPreferences(sharedPreferences);
  }

  private final SharedPreferences sharedPreferences;
  private final Observable<String> changedKeys;

  private RxSharedPreferences(final SharedPreferences sharedPreferences) {
    this.sharedPreferences = checkNotNull(sharedPreferences, "sharedPreferences == null");
    this.changedKeys = Observable.create(new Observable.OnSubscribe<String>() {
      @Override public void call(final Subscriber<? super String> subscriber) {
        final OnSharedPreferenceChangeListener listener = new OnSharedPreferenceChangeListener() {
          @Override public void onSharedPreferenceChanged(SharedPreferences sharedPreferences,
              String key) {
            subscriber.onNext(key);
          }
        };

        Subscription subscription = Subscriptions.create(new Action0() {
          @Override public void call() {
            sharedPreferences.unregisterOnSharedPreferenceChangeListener(listener);
          }
        });
        subscriber.add(subscription);

        sharedPreferences.registerOnSharedPreferenceChangeListener(listener);
      }
    }).share();
  }

  public Observable<String> getString(String key) {
    return getString(key, null);
  }

  public Observable<String> getString(String key, final String defaultValue) {
    return changedKeys.filter(matchesKey(key))
        .startWith(key)
        .map(new Func1<String, String>() {
          @Override public String call(String changedKey) {
            return sharedPreferences.getString(changedKey, defaultValue);
          }
        });
  }

  public Action1<String> setString(final String key) {
    return new Action1<String>() {
      @Override public void call(String value) {
        sharedPreferences.edit().putString(key, value).apply();
      }
    };
  }

  public Observable<Boolean> getBoolean(String key) {
    return getBoolean(key, null);
  }

  public Observable<Boolean> getBoolean(String key, final Boolean defaultValue) {
    return changedKeys.filter(matchesKey(key))
        .startWith(key)
        .map(new Func1<String, Boolean>() {
          @Override public Boolean call(String changedKey) {
            return sharedPreferences.getBoolean(changedKey, defaultValue);
          }
        });
  }

  public Action1<Boolean> setBoolean(final String key) {
    return new Action1<Boolean>() {
      @Override public void call(Boolean value) {
        sharedPreferences.edit().putBoolean(key, value).apply();
      }
    };
  }

  public Observable<Integer> getInt(String key) {
    return getInt(key, null);
  }

  public Observable<Integer> getInt(String key, final Integer defaultValue) {
    return changedKeys.filter(matchesKey(key))
        .startWith(key)
        .map(new Func1<String, Integer>() {
          @Override public Integer call(String changedKey) {
            return sharedPreferences.getInt(changedKey, defaultValue);
          }
        });
  }

  private static Func1<String, Boolean> matchesKey(final String key) {
    return new Func1<String, Boolean>() {
      @Override public Boolean call(String value) {
        return key.equals(value);
      }
    };
  }
}
