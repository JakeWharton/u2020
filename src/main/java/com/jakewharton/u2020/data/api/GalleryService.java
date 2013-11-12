package com.jakewharton.u2020.data.api;

import retrofit.client.Response;
import retrofit.http.GET;
import retrofit.http.Path;
import rx.Observable;

public interface GalleryService {
  @GET("/gallery/{section}/{sort}/{page}") //
  Observable<Response> listGallery( //
      @Path("section") Section section, //
      @Path("sort") Sort sort, //
      @Path("page") int page);
}
