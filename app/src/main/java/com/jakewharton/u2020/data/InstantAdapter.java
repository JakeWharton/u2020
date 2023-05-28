package com.jakewharton.u2020.data;

import com.squareup.moshi.FromJson;
import com.squareup.moshi.ToJson;
import org.threeten.bp.Instant;

// Accessed via reflection by Moshi.
@SuppressWarnings("unused")
public final class InstantAdapter {

    @ToJson
    public String toJson(Instant instant) {
        return instant.toString();
    }

    @FromJson
    public Instant fromJson(String value) {
        return Instant.parse(value);
    }
}
