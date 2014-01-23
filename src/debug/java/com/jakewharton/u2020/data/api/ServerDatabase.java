package com.jakewharton.u2020.data.api;

import com.jakewharton.u2020.data.api.model.Image;
import com.jakewharton.u2020.data.api.model.MockImageLoader;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;
import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public final class ServerDatabase {
  private static final AtomicLong NEXT_ID = new AtomicLong();

  public static long nextId() {
    return NEXT_ID.getAndIncrement();
  }

  public static String nextStringId() {
    return Long.toHexString(nextId());
  }

  // TODO maybe id->image map and section->id multimap so we can re-use images?
  private final Map<Section, List<Image>> imagesBySection = new LinkedHashMap<>();

  @Inject public ServerDatabase(MockImageLoader mockImageLoader) {
    List<Image> hotImages = new ArrayList<>();
    imagesBySection.put(Section.HOT, hotImages);

    hotImages.add(mockImageLoader.newImage("0y3uACw.jpg") //
        .title("Much Dagger") //
        .views(4000) //
        .build()); //
    hotImages.add(mockImageLoader.newImage("9PcLf86.jpg") //
        .title("Nice Picasso") //
        .views(854) //
        .build());
    hotImages.add(mockImageLoader.newImage("DgKWqio.jpg") //
        .title("Omg Scalpel") //
        .build());
    hotImages.add(mockImageLoader.newImage("e3LxhEC.jpg") //
        .title("Open Source Amaze") //
        .build());
    hotImages.add(mockImageLoader.newImage("p3jUQjI.jpg") //
        .title("So RxJava") //
        .views(2000) //
        .build());
    hotImages.add(mockImageLoader.newImage("P8hx3pg.jpg") //
        .title("Madge Amaze") //
        .build());
    hotImages.add(mockImageLoader.newImage("vSxLdXJ.jpg") //
        .title("Very ButterKnife") //
        .views(3040) //
        .build());
    hotImages.add(mockImageLoader.newImage("DOGE-6.jpg") //
        .title("Good AOSP") //
        .build());
    hotImages.add(mockImageLoader.newImage("DOGE-10.jpg") //
        .title("Many OkHttp") //
        .views(1500) //
        .build());
    hotImages.add(mockImageLoader.newImage("DOGE-16.jpg") //
        .title("Wow Retrofit") //
        .views(3000) //
        .build());
  }

  public List<Image> getImagesForSection(Section section) {
    return imagesBySection.get(section);
  }
}
