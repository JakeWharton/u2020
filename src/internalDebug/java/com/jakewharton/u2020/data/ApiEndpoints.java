package com.jakewharton.u2020.data;

import com.jakewharton.u2020.data.api.ApiModule;

public enum ApiEndpoints {
  PRODUCTION("Production", ApiModule.PRODUCTION_API_URL.toString()),
  // STAGING("Staging", "https://api.staging.github.com/"),
  MOCK_MODE("Mock Mode", "http://localhost/mock"),
  CUSTOM("Custom", null);

  public final String name;
  public final String url;

  ApiEndpoints(String name, String url) {
    this.name = name;
    this.url = url;
  }

  @Override public String toString() {
    return name;
  }

  public static ApiEndpoints from(String endpoint) {
    for (ApiEndpoints value : values()) {
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
