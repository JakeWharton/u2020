package com.jakewharton.u2020.data.api.model;

import android.app.Application;
import android.content.res.AssetManager;
import android.graphics.BitmapFactory;
import java.io.InputStream;
import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public final class MockImageLoader {
  private final AssetManager assetManager;

  @Inject MockImageLoader(Application application) {
    assetManager = application.getAssets();
  }

  /** A filename like {@code abc123.jpg} inside the {@code mock/images} asset folder. */
  public ImageBuilder newImage(String filename) {
    String path = "mock/images/" + filename;

    int width;
    int height;
    try {
      InputStream open = assetManager.open(path);

      BitmapFactory.Options options = new BitmapFactory.Options();
      options.inJustDecodeBounds = true;

      BitmapFactory.decodeStream(open, null, options);

      width = options.outWidth;
      height = options.outHeight;
    } catch (Exception e) {
      throw new RuntimeException("Unable to load " + filename, e);
    }

    String id = filename.substring(0, filename.lastIndexOf('.'));
    String link = "mock:///" + path;
    return new ImageBuilder(id, link, id /* default title == id */, width, height);
  }

  public static class ImageBuilder {
    private final String id;
    private final String link;
    private final int width;
    private final int height;
    private String title;
    private long datetime = System.currentTimeMillis();
    private int views;
    private boolean isAlbum;

    private ImageBuilder(String id, String link, String title, int width, int height) {
      this.id = id;
      this.link = link;
      this.title = title;
      this.width = width;
      this.height = height;
    }

    public ImageBuilder title(String title) {
      this.title = title;
      return this;
    }

    public ImageBuilder datetime(int datetime) {
      this.datetime = datetime;
      return this;
    }

    public ImageBuilder views(int views) {
      this.views = views;
      return this;
    }

    public ImageBuilder isAlbum(boolean isAlbum) {
      this.isAlbum = isAlbum;
      return this;
    }

    public Image build() {
      return new Image(id, link, title, width, height, datetime, views, isAlbum);
    }
  }
}
