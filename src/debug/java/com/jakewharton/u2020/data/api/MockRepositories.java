package com.jakewharton.u2020.data.api;

import com.jakewharton.u2020.data.api.model.Repository;
import com.jakewharton.u2020.data.api.model.User;
import org.joda.time.DateTime;

final class MockRepositories {
  static final Repository BUTTERKNIFE = new Repository.Builder() //
      .name("butterknife") //
      .owner(new User("JakeWharton")) //
      .description("View \"injection\" library for Android.") //
      .forks(626) //
      .stars(3136) //
      .watchers(266) //
      .htmlUrl("https://github.com/JakeWharton/butterknife") //
      .updatedAt(DateTime.parse("2015-03-15")) //
      .build();
  static final Repository DAGGER = new Repository.Builder() //
      .name("dagger") //
      .owner(new User("square")) //
      .description("A fast dependency injector for Android and Java.") //
      .forks(574) //
      .stars(3085) //
      .watchers(332) //
      .htmlUrl("https://github.com/square/dagger") //
      .updatedAt(DateTime.parse("2015-03-05")) //
      .build();
  static final Repository JAVAPOET = new Repository.Builder() //
      .name("javapoet") //
      .owner(new User("square")) //
      .description("A Java API for generating .java source files.") //
      .forks(137) //
      .stars(809) //
      .watchers(80) //
      .htmlUrl("https://github.com/square/javapoet") //
      .updatedAt(DateTime.parse("2015-03-06")) //
      .build();
  static final Repository OKHTTP = new Repository.Builder() //
      .name("okhttp") //
      .owner(new User("square")) //
      .description("An HTTP+SPDY client for Android and Java applications.") //
      .forks(828) //
      .stars(3663) //
      .watchers(359) //
      .htmlUrl("https://github.com/square/okhttp") //
      .updatedAt(DateTime.parse("2015-03-15")) //
      .build();
  static final Repository OKIO = new Repository.Builder() //
      .name("okio") //
      .owner(new User("square")) //
      .description("A modern I/O API for Java") //
      .forks(126) //
      .stars(954) //
      .watchers(88) //
      .htmlUrl("https://github.com/square/okio") //
      .updatedAt(DateTime.parse("2015-03-11")) //
      .build();
  static final Repository PICASSO = new Repository.Builder() //
      .name("picasso") //
      .owner(new User("square")) //
      .description("A powerful image downloading and caching library for Android") //
      .forks(1513) //
      .stars(5279) //
      .watchers(537) //
      .htmlUrl("https://github.com/square/picasso") //
      .updatedAt(DateTime.parse("2015-03-14")) //
      .build();
  static final Repository RETROFIT = new Repository.Builder() //
      .name("retrofit") //
      .owner(new User("square")) //
      .description("Type-safe REST client for Android and Java by Square, Inc.") //
      .forks(775) //
      .stars(4583) //
      .watchers(398) //
      .htmlUrl("https://github.com/square/retrofit") //
      .updatedAt(DateTime.parse("2015-02-02")) //
      .build();
  static final Repository SQLBRITE = new Repository.Builder() //
      .name("sqlbrite") //
      .owner(new User("square")) //
      .description("A lightweight wrapper around SQLiteOpenHelper which introduces reactive stream"
          + " semantics to SQL operations.") //
      .forks(63) //
      .stars(987) //
      .watchers(97) //
      .htmlUrl("https://github.com/square/sqlbrite") //
      .updatedAt(DateTime.parse("2015-03-06")) //
      .build();
  static final Repository TELESCOPE = new Repository.Builder() //
      .name("telescope") //
      .owner(new User("mattprecious")) //
      .description("A simple tool to allow easy bug report capturing within your app.") //
      .forks(31) //
      .stars(399) //
      .watchers(18) //
      .htmlUrl("https://github.com/mattprecious/telescope") //
      .updatedAt(DateTime.parse("2015-02-06")) //
      .build();
  static final Repository U2020 = new Repository.Builder() //
      .name("u2020") //
      .owner(new User("JakeWharton")) //
      .description("A sample Android app which showcases advanced usage of Dagger among other"
          + " open source libraries.") //
      .forks(260) //
      .stars(1487) //
      .watchers(140) //
      .htmlUrl("https://github.com/JakeWharton/u2020") //
      .updatedAt(DateTime.parse("2015-03-14")) //
      .build();
  static final Repository WIRE = new Repository.Builder() //
      .name("wire") //
      .owner(new User("square")) //
      .description("Clean, lightweight protocol buffers for Android and Java.") //
      .forks(100) //
      .stars(616) //
      .watchers(83) //
      .htmlUrl("https://github.com/square/wire") //
      .updatedAt(DateTime.parse("2015-03-06")) //
      .build();

  private MockRepositories() {
    throw new AssertionError("No instances.");
  }
}
