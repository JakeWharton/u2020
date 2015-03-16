package com.jakewharton.u2020.util;

import android.content.SharedPreferences;

public final class EnumPreferences {
  private EnumPreferences() {
  }

  public static <T extends Enum> T getEnumValue(SharedPreferences preferences, Class<T> type,
      String key, T defaultValue) {
    String name = preferences.getString(key, null);
    if (name != null) {
      try {
        return type.cast(Enum.valueOf(type, name));
      } catch (IllegalArgumentException ignored) {
      }
    }

    return defaultValue;
  }

  public static void saveEnumValue(SharedPreferences preferences, String key, Enum value) {
    preferences.edit().putString(key, value.name()).apply();
  }
}
