package com.jakewharton.u2020.data.api;

public interface MockResponseSupplier {
  <T extends Enum<T>> T get(Class<T> cls);
  void set(Enum<?> value);
}
