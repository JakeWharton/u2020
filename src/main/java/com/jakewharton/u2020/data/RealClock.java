package com.jakewharton.u2020.data;

public final class RealClock implements Clock {
  @Override public long millis() {
    return System.currentTimeMillis();
  }

  @Override public long nanos() {
    return System.nanoTime();
  }
}
