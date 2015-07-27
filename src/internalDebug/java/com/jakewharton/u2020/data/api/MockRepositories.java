package com.jakewharton.u2020.data.api;

import com.jakewharton.u2020.data.api.model.Repository;
import org.threeten.bp.LocalDate;

import static com.jakewharton.u2020.data.api.MockUsers.JAKE_WHARTON;
import static com.jakewharton.u2020.data.api.MockUsers.MATT_PRECIOUS;
import static com.jakewharton.u2020.data.api.MockUsers.SQUARE;
import static org.threeten.bp.ZoneOffset.UTC;

final class MockRepositories {
  static final Repository BUTTERKNIFE = new Repository.Builder() //
      .name("butterknife") //
      .owner(JAKE_WHARTON) //
      .description("View \"injection\" library for Android.") //
      .forks(626) //
      .stars(3136) //
      .htmlUrl("https://github.com/JakeWharton/butterknife") //
      .updatedAt(LocalDate.of(2015, 3, 15).atStartOfDay(UTC).toInstant()) //
      .build();
  static final Repository DAGGER = new Repository.Builder() //
      .name("dagger") //
      .owner(SQUARE) //
      .description("A fast dependency injector for Android and Java.") //
      .forks(574) //
      .stars(3085) //
      .htmlUrl("https://github.com/square/dagger") //
      .updatedAt(LocalDate.of(2015, 3, 5).atStartOfDay(UTC).toInstant()) //
      .build();
  static final Repository JAVAPOET = new Repository.Builder() //
      .name("javapoet") //
      .owner(SQUARE) //
      .description("A Java API for generating .java source files.") //
      .forks(137) //
      .stars(809) //
      .htmlUrl("https://github.com/square/javapoet") //
      .updatedAt(LocalDate.of(2015, 3, 6).atStartOfDay(UTC).toInstant()) //
      .build();
  static final Repository OKHTTP = new Repository.Builder() //
      .name("okhttp") //
      .owner(SQUARE) //
      .description("An HTTP+SPDY client for Android and Java applications.") //
      .forks(828) //
      .stars(3663) //
      .htmlUrl("https://github.com/square/okhttp") //
      .updatedAt(LocalDate.of(2015, 3, 15).atStartOfDay(UTC).toInstant()) //
      .build();
  static final Repository OKIO = new Repository.Builder() //
      .name("okio") //
      .owner(SQUARE) //
      .description("A modern I/O API for Java") //
      .forks(126) //
      .stars(954) //
      .htmlUrl("https://github.com/square/okio") //
      .updatedAt(LocalDate.of(2015, 3, 11).atStartOfDay(UTC).toInstant()) //
      .build();
  static final Repository PICASSO = new Repository.Builder() //
      .name("picasso") //
      .owner(SQUARE) //
      .description("A powerful image downloading and caching library for Android") //
      .forks(1513) //
      .stars(5279) //
      .htmlUrl("https://github.com/square/picasso") //
      .updatedAt(LocalDate.of(2015, 3, 14).atStartOfDay(UTC).toInstant()) //
      .build();
  static final Repository RETROFIT = new Repository.Builder() //
      .name("retrofit") //
      .owner(SQUARE) //
      .description("Type-safe REST client for Android and Java by Square, Inc.") //
      .forks(775) //
      .stars(4583) //
      .htmlUrl("https://github.com/square/retrofit") //
      .updatedAt(LocalDate.of(2015, 2, 2).atStartOfDay(UTC).toInstant()) //
      .build();
  static final Repository SQLBRITE = new Repository.Builder() //
      .name("sqlbrite") //
      .owner(SQUARE) //
      .description("A lightweight wrapper around SQLiteOpenHelper which introduces reactive stream"
          + " semantics to SQL operations.") //
      .forks(63) //
      .stars(987) //
      .htmlUrl("https://github.com/square/sqlbrite") //
      .updatedAt(LocalDate.of(2015, 3, 6).atStartOfDay(UTC).toInstant()) //
      .build();
  static final Repository TELESCOPE = new Repository.Builder() //
      .name("telescope") //
      .owner(MATT_PRECIOUS) //
      .description("A simple tool to allow easy bug report capturing within your app.") //
      .forks(31) //
      .stars(399) //
      .htmlUrl("https://github.com/mattprecious/telescope") //
      .updatedAt(LocalDate.of(2015, 2, 6).atStartOfDay(UTC).toInstant()) //
      .build();
  static final Repository U2020 = new Repository.Builder() //
      .name("u2020") //
      .owner(JAKE_WHARTON) //
      .description("A sample Android app which showcases advanced usage of Dagger among other"
          + " open source libraries.") //
      .forks(260) //
      .stars(1487) //
      .htmlUrl("https://github.com/JakeWharton/u2020") //
      .updatedAt(LocalDate.of(2015, 3, 14).atStartOfDay(UTC).toInstant()) //
      .build();
  static final Repository WIRE = new Repository.Builder() //
      .name("wire") //
      .owner(SQUARE) //
      .description("Clean, lightweight protocol buffers for Android and Java.") //
      .forks(100) //
      .stars(616) //
      .htmlUrl("https://github.com/square/wire") //
      .updatedAt(LocalDate.of(2015, 3, 6).atStartOfDay(UTC).toInstant()) //
      .build();
  static final Repository MOSHI = new Repository.Builder() //
      .name("moshi") //
      .owner(SQUARE) //
      .description("") // Intentionally empty description.
      .forks(19) //
      .stars(465) //
      .htmlUrl("https://github.com/square/moshi") //
      .updatedAt(LocalDate.of(2015, 6, 16).atStartOfDay(UTC).toInstant()) //
      .build();

  private MockRepositories() {
    throw new AssertionError("No instances.");
  }
}
