package com.jakewharton.u2020.data.api;

public enum Sort {
  VIRAL("viral"),
  TIME("time");

  private final String value;

  Sort(String value) {
    this.value = value;
  }

  @Override public String toString() {
    return value;
  }
}
