package com.jakewharton.u2020.data.api.model;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import static com.jakewharton.u2020.util.Preconditions.checkNotNull;

public final class User {
  @NonNull public final String login;
  @Nullable public final String avatar_url;

  public User(String login, @Nullable String avatar_url) {
    this.login = checkNotNull(login, "login == null");
    this.avatar_url = avatar_url;
  }

  @Override public String toString() {
    return "User{" +
        "login='" + login + '\'' +
        ", avatar_url='" + avatar_url + '\'' +
        '}';
  }
}
