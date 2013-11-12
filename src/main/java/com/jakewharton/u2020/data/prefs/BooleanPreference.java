package com.jakewharton.u2020.data.prefs;

import android.content.SharedPreferences;

public class BooleanPreference {
  private final SharedPreferences preferences;
  private final String key;
  private final boolean defaultValue;

  public BooleanPreference(SharedPreferences preferences, String key) {
    this(preferences, key, false);
  }

  public BooleanPreference(SharedPreferences preferences, String key, boolean defaultValue) {
    this.preferences = preferences;
    this.key = key;
    this.defaultValue = defaultValue;
  }

  public boolean get() {
    return preferences.getBoolean(key, defaultValue);
  }

  public boolean isSet() {
    return preferences.contains(key);
  }

  public void set(boolean value) {
    preferences.edit().putBoolean(key, value).apply();
  }

  public void delete() {
    preferences.edit().remove(key).apply();
  }
}
