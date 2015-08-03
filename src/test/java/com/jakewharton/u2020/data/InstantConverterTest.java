package com.jakewharton.u2020.data;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.junit.Test;
import org.threeten.bp.Instant;

import static com.google.common.truth.Truth.assertThat;

public final class InstantConverterTest {
  private static final Instant EPOCH = Instant.ofEpochSecond(0);
  private static final Instant RECENT = Instant.ofEpochSecond(1438337819);

  private final Gson gson = new GsonBuilder()
      .registerTypeAdapter(Instant.class, new InstantConverter())
      .create();

  @Test public void serialization() {
    assertThat(gson.toJson(EPOCH)).isEqualTo("\"1970-01-01T00:00:00Z\"");
    assertThat(gson.toJson(RECENT)).isEqualTo("\"2015-07-31T10:16:59Z\"");
  }

  @Test public void deserialization() {
    assertThat(gson.fromJson("\"1970-01-01T00:00:00Z\"", Instant.class)).isEqualTo(EPOCH);
    assertThat(gson.fromJson("\"2015-07-31T10:16:59Z\"", Instant.class)).isEqualTo(RECENT);
  }
}
