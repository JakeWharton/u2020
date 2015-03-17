package com.jakewharton.u2020.data.api;

public enum Sort {
  STARS("stars"),
  FORKS("forks"),
  UPDATED("updated");

  private final String value;

  Sort(String value) {
    this.value = value;
  }

  @Override public String toString() {
    return value;
  }
}
