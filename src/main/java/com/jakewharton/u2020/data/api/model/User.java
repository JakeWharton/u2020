package com.jakewharton.u2020.data.api.model;

public final class User {
  public final String login;

  public User(String login) {
    this.login = login;
  }

  @Override public String toString() {
    return "User{" +
        "login='" + login + '\'' +
        '}';
  }
}
