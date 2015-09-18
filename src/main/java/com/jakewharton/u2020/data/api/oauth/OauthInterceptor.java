package com.jakewharton.u2020.data.api.oauth;

import com.f2prateek.rx.preferences.Preference;
import com.squareup.okhttp.Interceptor;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import java.io.IOException;
import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton public final class OauthInterceptor implements Interceptor {
  private final Preference<String> accessToken;

  @Inject public OauthInterceptor(@AccessToken Preference<String> accessToken) {
    this.accessToken = accessToken;
  }

  @Override public Response intercept(Chain chain) throws IOException {
    Request.Builder builder = chain.request().newBuilder();

    if (accessToken.isSet()) {
      builder.header("Authorization", "token " + accessToken.get());
    }

    return chain.proceed(builder.build());
  }
}
