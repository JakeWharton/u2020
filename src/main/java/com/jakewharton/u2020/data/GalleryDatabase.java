package com.jakewharton.u2020.data;

import com.jakewharton.u2020.data.api.GalleryService;
import com.jakewharton.u2020.data.api.Section;
import com.jakewharton.u2020.data.api.Sort;
import com.jakewharton.u2020.data.api.model.Image;
import com.jakewharton.u2020.data.api.transforms.GalleryToImageList;
import com.jakewharton.u2020.data.rx.EndObserver;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import javax.inject.Inject;
import javax.inject.Singleton;
import rx.Observer;
import rx.Subscription;
import rx.android.concurrency.AndroidSchedulers;
import rx.concurrency.Schedulers;
import rx.subjects.PublishSubject;

/** Poor-man's in-memory cache of responses. Must be accessed on the main thread. */
@Singleton
public class GalleryDatabase {
  private final GalleryService galleryService;

  private final Map<Section, List<Image>> galleryCache = new LinkedHashMap<>();
  private final Map<Section, PublishSubject<List<Image>>> galleryRequests = new LinkedHashMap<>();

  @Inject public GalleryDatabase(GalleryService galleryService) {
    this.galleryService = galleryService;
  }

  public Subscription loadGallery(final Section section, Observer<List<Image>> observer) {
    List<Image> images = galleryCache.get(section);
    if (images != null) {
      // We have a cached value. Emit it immediately.
      observer.onNext(images);
    }

    PublishSubject<List<Image>> galleryRequest = galleryRequests.get(section);
    if (galleryRequest != null) {
      // There's an in-flight network request for this section already. Join it.
      return galleryRequest.subscribe(observer);
    }

    galleryRequest = PublishSubject.create();
    galleryRequests.put(section, galleryRequest);

    Subscription subscription = galleryRequest.subscribe(observer);

    galleryRequest.subscribe(new EndObserver<List<Image>>() {
      @Override public void onEnd() {
        galleryRequests.remove(section);
      }

      @Override public void onNext(List<Image> images) {
        galleryCache.put(section, images);
      }
    });

    galleryService.listGallery(section, Sort.VIRAL, 1)
        .map(new GalleryToImageList())
        .subscribeOn(Schedulers.threadPoolForIO())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(galleryRequest);

    return subscription;
  }
}
