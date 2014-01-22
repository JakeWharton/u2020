package com.jakewharton.u2020.data.api.model;

public class Image {
  public final String id;

  public final String link;
  public final String title;

  public final int width;
  public final int height;
  public final long datetime;
  public final int views;
  public final boolean is_album;

  public Image(String id, String link, String title, int width, int height, long datetime,
      int views, boolean isAlbum) {
    this.id = id;
    this.link = link;
    this.title = title;
    this.width = width;
    this.height = height;
    this.datetime = datetime;
    this.views = views;
    this.is_album = isAlbum;
  }
}
