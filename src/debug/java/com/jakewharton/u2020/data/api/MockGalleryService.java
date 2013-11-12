package com.jakewharton.u2020.data.api;

import retrofit.client.Response;
import rx.Observable;

final class MockGalleryService implements GalleryService {
  @Override public Observable<Response> listGallery(Section section, Sort sort, int page) {
    return Observable.empty(); // TODO
  }
}
