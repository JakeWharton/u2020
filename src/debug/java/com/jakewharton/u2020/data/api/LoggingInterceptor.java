package com.jakewharton.u2020.data.api;

import com.jakewharton.u2020.data.Clock;
import com.squareup.okhttp.Headers;
import com.squareup.okhttp.Interceptor;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import java.io.IOException;
import java.util.concurrent.TimeUnit;
import javax.inject.Inject;
import javax.inject.Singleton;
import timber.log.Timber;

/** Verbose logging of network calls, which includes path, headers, and times. */
@Singleton
public final class LoggingInterceptor implements Interceptor {
  private final Clock clock;

  @Inject public LoggingInterceptor(Clock clock) {
    this.clock = clock;
  }

  @Override public Response intercept(Chain chain) throws IOException {
    Request request = chain.request();

    long t1 = clock.nanos();
    Timber.v("Sending request %s%s", request.url(), prettyHeaders(request.headers()));

    Response response = chain.proceed(request);

    long t2 = clock.nanos();
    Timber.v("Received response (%s) for %s in %sms%s", response.code(), response.request().url(),
        TimeUnit.NANOSECONDS.toMillis(t2 - t1), prettyHeaders(response.headers()));

    return response;
  }

  private String prettyHeaders(Headers headers) {
    if (headers.size() == 0) return "";

    StringBuilder builder = new StringBuilder();
    builder.append("\n  Headers:");

    for (int i = 0; i < headers.size(); i++) {
      builder.append("\n    ").append(headers.name(i)).append(": ").append(headers.value(i));
    }

    return builder.toString();
  }
}
