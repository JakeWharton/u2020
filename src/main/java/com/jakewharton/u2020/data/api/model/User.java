package com.jakewharton.u2020.data.api.model;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import com.google.gson.annotations.SerializedName;

import static com.jakewharton.u2020.util.Preconditions.checkNotNull;

public final class User {
  @NonNull public final String login;
  @SerializedName("avatar_url") @Nullable public final String avatarUrl;

  public User(String login, @Nullable String avatarUrl) {
    this.login = checkNotNull(login, "login == null");
    this.avatarUrl = avatarUrl;
  }

  @Override public String toString() {
    return "User{" +
        "login='" + login + '\'' +
        ", avatarUrl='" + avatarUrl + '\'' +
        '}';
  }
}
