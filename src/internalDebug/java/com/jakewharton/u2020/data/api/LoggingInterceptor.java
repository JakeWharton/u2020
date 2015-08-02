package com.jakewharton.u2020.data.api;

import com.squareup.okhttp.Headers;
import com.squareup.okhttp.Interceptor;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import java.io.IOException;
import javax.inject.Inject;
import javax.inject.Singleton;
import org.threeten.bp.Clock;
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

    long startMs = clock.millis();
    Timber.v("Sending request %s%s", request.url(), prettyHeaders(request.headers()));

    Response response = chain.proceed(request);

    long tookMs = clock.millis() - startMs;
    Timber.v("Received response (%s) for %s in %sms%s", response.code(), response.request().url(),
        tookMs, prettyHeaders(response.headers()));

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
