package com.jakewharton.u2020.data;

import android.content.Context;
import dagger.ObjectGraph;

public final class Injector {
  private static final String INJECTOR_SERVICE = "com.jakewharton.u2020.injector";

  @SuppressWarnings("ResourceType") // Explicitly doing a custom service.
  public static ObjectGraph obtain(Context context) {
    return (ObjectGraph) context.getSystemService(INJECTOR_SERVICE);
  }

  public static boolean matchesService(String name) {
    return INJECTOR_SERVICE.equals(name);
  }

  private Injector() {
    throw new AssertionError("No instances.");
  }
}
