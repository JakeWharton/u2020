package com.jakewharton.u2020.data;

import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;
import java.io.IOException;
import org.junit.Test;
import org.threeten.bp.Instant;
import static com.google.common.truth.Truth.assertThat;

public final class InstantAdapterTest {

    private static final Instant EPOCH = Instant.ofEpochSecond(0);

    private static final Instant RECENT = Instant.ofEpochSecond(1438337819);

    private final Moshi moshi = new Moshi.Builder().add(new InstantAdapter()).build();

    private final JsonAdapter<Instant> adapter = moshi.adapter(Instant.class).lenient();

    @Test
    public void serialization() throws IOException {
        assertThat(adapter.toJson(EPOCH)).isEqualTo("\"1970-01-01T00:00:00Z\"");
        assertThat(adapter.toJson(RECENT)).isEqualTo("\"2015-07-31T10:16:59Z\"");
    }

    @Test
    public void deserialization() throws IOException {
        assertThat(adapter.fromJson("\"1970-01-01T00:00:00Z\"")).isEqualTo(EPOCH);
        assertThat(adapter.fromJson("\"2015-07-31T10:16:59Z\"")).isEqualTo(RECENT);
    }
}
