package com.jakewharton.u2020.data.api;

import retrofit.Result;
import rx.functions.Func1;

public final class Results {
  private static final Func1<Result<?>, Boolean> SUCCESS =
      new Func1<Result<?>, Boolean>() {
        @Override public Boolean call(Result<?> result) {
          return !result.isError() && result.response().isSuccess();
        }
      };

  public static Func1<Result<?>, Boolean> isSuccess() {
    return SUCCESS;
  }

  private Results() {
    throw new AssertionError("No instances.");
  }
}
