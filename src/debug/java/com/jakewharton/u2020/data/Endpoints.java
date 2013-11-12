package com.jakewharton.u2020.data;

import com.jakewharton.u2020.data.api.ApiModule;

public enum Endpoints {
  PRODUCTION("Production", ApiModule.PRODUCTION_API_URL),
  STAGING("Staging", "https://api.staging.imgur.com/3/"),
  MOCK_MODE("Mock Mode", "mock://mode/"),
  CUSTOM("Custom", null);

  public final String name;
  public final String url;

  Endpoints(String name, String url) {
    this.name = name;
    this.url = url;
  }

  public static Endpoints from(String endpoint) {
    for (Endpoints value : values()) {
      if (value.url != null && value.url.equals(endpoint)) {
        return value;
      }
    }
    return CUSTOM;
  }

  public static boolean isMockMode(String endpoint) {
    return from(endpoint) == MOCK_MODE;
  }
}
