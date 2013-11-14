package com.jakewharton.u2020.data.api.model;

public class Image {
  public final String id;

  public final String link;
  public final String title;
  public final String description;

  public final int score;
  public final int downs;
  public final int ups;

  public final String type;
  public final int width;
  public final int height;
  public final int datetime;
  public final int size;
  public final int views;
  public final long bandwidth;
  public final boolean is_album;
  public final boolean animated;

  public final boolean favorite;
  public final boolean nsfw;

  public final String account_url;

  public Image(String id, String link, String title, String description, int score, int downs,
      int ups, String type, int width, int height, int datetime, int size, int views,
      long bandwidth, boolean is_album, boolean animated, boolean favorite, boolean nsfw,
      String account_url) {
    this.id = id;
    this.link = link;
    this.title = title;
    this.description = description;
    this.score = score;
    this.downs = downs;
    this.ups = ups;
    this.type = type;
    this.width = width;
    this.height = height;
    this.datetime = datetime;
    this.size = size;
    this.views = views;
    this.bandwidth = bandwidth;
    this.is_album = is_album;
    this.animated = animated;
    this.favorite = favorite;
    this.nsfw = nsfw;
    this.account_url = account_url;
  }
}
