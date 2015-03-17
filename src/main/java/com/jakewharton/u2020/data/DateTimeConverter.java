package com.jakewharton.u2020.data;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import java.lang.reflect.Type;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

public final class DateTimeConverter
    implements JsonSerializer<DateTime>, JsonDeserializer<DateTime> {
  private static final DateTimeFormatter DATE_TIME_FORMAT =
      DateTimeFormat.forPattern("yyyy-MM-dd'T'HH:mm:ss'Z'");

  @Override public JsonElement serialize(DateTime src, Type typeOfSrc,
      JsonSerializationContext context) {
    return new JsonPrimitive(DATE_TIME_FORMAT.print(src));
  }

  @Override public DateTime deserialize(JsonElement json, Type typeOfT,
      JsonDeserializationContext context) throws JsonParseException {
    return DATE_TIME_FORMAT.parseDateTime(json.getAsString());
  }
}
