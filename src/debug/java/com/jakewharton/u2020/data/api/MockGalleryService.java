package com.jakewharton.u2020.data.api;

import com.jakewharton.u2020.data.api.model.Gallery;
import javax.inject.Inject;
import javax.inject.Singleton;
import rx.Observable;

@Singleton
final class MockGalleryService implements GalleryService {
  private final ServerDatabase serverDatabase;

  @Inject
  MockGalleryService(ServerDatabase serverDatabase) {
    this.serverDatabase = serverDatabase;
  }

  @Override public Observable<Gallery> listGallery(Section section, Sort sort, int page) {
    return Observable.empty(); // TODO
  }
}
