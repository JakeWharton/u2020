package com.jakewharton.u2020.data.prefs;

import android.content.SharedPreferences;
import com.google.gson.Gson;
import java.lang.reflect.Type;

public class ObjectPreference<T> {
  private final SharedPreferences preferences;
  private final String key;
  private final Gson gson;
  private final Type type;
  private final T defaultValue;
  private T value;

  public ObjectPreference(SharedPreferences preferences, Gson gson, Class<T> type, String key) {
    this(preferences, gson, type, key, null);
  }

  public ObjectPreference(SharedPreferences preferences, Gson gson, Class<T> type, String key,
      T defaultValue) {
    this.preferences = preferences;
    this.key = key;
    this.gson = gson;
    this.type = type;
    this.defaultValue = defaultValue;
  }

  public T get() {
    if (value == null) {
      String stringValue = preferences.getString(key, null);
      if (stringValue == null) {
        return defaultValue;
      }
      value = gson.fromJson(stringValue, type);
    }
    return value;
  }

  public boolean isSet() {
    return preferences.contains(key);
  }

  public void set(T value) {
    String stringValue = gson.toJson(value, type);
    preferences.edit().putString(key, stringValue).apply();
    this.value = value;
  }

  public void delete() {
    value = null;
    preferences.edit().remove(key).apply();
  }
}
