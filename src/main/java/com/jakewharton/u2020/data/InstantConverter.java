package com.jakewharton.u2020.data;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import java.io.IOException;
import org.threeten.bp.Instant;

public final class InstantConverter extends TypeAdapter<Instant> {
  @Override public void write(JsonWriter out, Instant value) throws IOException {
    out.value(value.toString());
  }

  @Override public Instant read(JsonReader in) throws IOException {
    return Instant.parse(in.nextString());
  }
}
