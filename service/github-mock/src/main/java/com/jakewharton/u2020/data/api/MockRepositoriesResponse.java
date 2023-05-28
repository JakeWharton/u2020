package com.jakewharton.u2020.data.api;

import com.jakewharton.u2020.data.api.model.RepositoriesResponse;
import java.util.Arrays;
import java.util.Collections;
import static com.jakewharton.u2020.data.api.MockRepositories.BUTTERKNIFE;
import static com.jakewharton.u2020.data.api.MockRepositories.DAGGER;
import static com.jakewharton.u2020.data.api.MockRepositories.JAVAPOET;
import static com.jakewharton.u2020.data.api.MockRepositories.MOSHI;
import static com.jakewharton.u2020.data.api.MockRepositories.OKHTTP;
import static com.jakewharton.u2020.data.api.MockRepositories.OKIO;
import static com.jakewharton.u2020.data.api.MockRepositories.PICASSO;
import static com.jakewharton.u2020.data.api.MockRepositories.RETROFIT;
import static com.jakewharton.u2020.data.api.MockRepositories.SQLBRITE;
import static com.jakewharton.u2020.data.api.MockRepositories.TELESCOPE;
import static com.jakewharton.u2020.data.api.MockRepositories.U2020;
import static com.jakewharton.u2020.data.api.MockRepositories.WIRE;

public enum MockRepositoriesResponse {

    SUCCESS("Success", new RepositoriesResponse(//
    Arrays.//
    asList(//
    BUTTERKNIFE, //
    DAGGER, //
    JAVAPOET, //
    OKHTTP, //
    OKIO, //
    PICASSO, //
    RETROFIT, //
    SQLBRITE, //
    TELESCOPE, //
    U2020, //
    WIRE, MOSHI))), ONE("One", new RepositoriesResponse(Collections.singletonList(DAGGER))), EMPTY("Empty", new RepositoriesResponse(null));

    public final String name;

    // TODO make this @Immutable.
    @SuppressWarnings("ImmutableEnumChecker")
    public final RepositoriesResponse response;

    MockRepositoriesResponse(String name, RepositoriesResponse response) {
        this.name = name;
        this.response = response;
    }

    @Override
    public String toString() {
        return name;
    }
}
