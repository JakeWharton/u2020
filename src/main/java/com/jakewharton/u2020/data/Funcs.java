package com.jakewharton.u2020.data;

import rx.functions.Func1;

public final class Funcs {
  public static <T> Func1<T, Boolean> not(final Func1<T, Boolean> func) {
    return new Func1<T, Boolean>() {
      @Override public Boolean call(T value) {
        return !func.call(value);
      }
    };
  }

  private Funcs() {
    throw new AssertionError("No instances.");
  }
}
