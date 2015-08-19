package com.jakewharton.u2020.data.api.oauth;

import android.content.Intent;
import android.net.Uri;
import com.google.gson.Gson;
import com.jakewharton.u2020.data.IntentFactory;
import com.jakewharton.u2020.data.prefs.StringPreference;
import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.HttpUrl;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import java.io.IOException;
import javax.inject.Inject;
import javax.inject.Singleton;
import timber.log.Timber;

@Singleton public final class OauthManager {
  private static final String CLIENT_ID = "5793abe5bcb6d90f0240";
  private static final String CLIENT_SECRET = "81a35659c60fc376629432a51fd81e5c66a8dace";

  private final IntentFactory intentFactory;
  private final OkHttpClient client;
  private final Gson gson;
  private final StringPreference accessToken;

  @Inject public OauthManager(IntentFactory intentFactory, OkHttpClient client, Gson gson,
      @AccessToken StringPreference accessToken) {
    this.intentFactory = intentFactory;
    this.client = client;
    this.gson = gson;
    this.accessToken = accessToken;
  }

  public Intent createLoginIntent() {
    HttpUrl authorizeUrl = HttpUrl.parse("https://github.com/login/oauth/authorize") //
        .newBuilder() //
        .addQueryParameter("client_id", CLIENT_ID) //
        .build();

    return intentFactory.createUrlIntent(authorizeUrl.toString());
  }

  public void handleResult(Uri data) {
    if (data == null) return;

    String code = data.getQueryParameter("code");
    if (code == null) return;

    try {
      // Trade our code for an access token.
      Request request = new Request.Builder() //
          .url("https://github.com/login/oauth/access_token") //
          .header("Accept", "application/json") //
          .post(new FormEncodingBuilder() //
              .add("client_id", CLIENT_ID) //
              .add("client_secret", CLIENT_SECRET) //
              .add("code", code) //
              .build()) //
          .build();

      Response response = client.newCall(request).execute();
      if (response.isSuccessful()) {
        AccessTokenResponse accessTokenResponse =
            gson.getAdapter(AccessTokenResponse.class).fromJson(response.body().string());
        if (accessTokenResponse != null && accessTokenResponse.access_token != null) {
          accessToken.set(accessTokenResponse.access_token);
        }
      }
    } catch (IOException e) {
      Timber.w(e, "Failed to get access token.");
    }
  }

  private static final class AccessTokenResponse {
    public final String access_token;

    private AccessTokenResponse(String access_token, String scope) {
      this.access_token = access_token;
    }
  }
}
