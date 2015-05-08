package com.jakewharton.u2020.data;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import java.io.IOException;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

public final class DateTimeConverter extends TypeAdapter<DateTime> {
  private static final DateTimeFormatter DATE_TIME_FORMAT =
      DateTimeFormat.forPattern("yyyy-MM-dd'T'HH:mm:ss'Z'");

  @Override public void write(JsonWriter out, DateTime value)
      throws IOException {
    out.value(DATE_TIME_FORMAT.print(value));
  }

  @Override public DateTime read(JsonReader in) throws IOException {
    return DATE_TIME_FORMAT.parseDateTime(in.nextString());
  }
}
