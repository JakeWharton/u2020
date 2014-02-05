package com.jakewharton.u2020.ui.gallery;

import android.os.Bundle;
import com.jakewharton.u2020.U2020Module;
import com.jakewharton.u2020.data.GalleryDatabase;
import com.jakewharton.u2020.data.api.Section;
import com.jakewharton.u2020.data.api.model.Image;
import com.jakewharton.u2020.data.rx.EndlessObserver;
import com.jakewharton.u2020.ui.MainView;
import java.util.List;
import javax.inject.Inject;
import javax.inject.Singleton;
import mortar.Blueprint;
import mortar.ViewPresenter;
import rx.Subscription;

public class GalleryBlueprint implements Blueprint {
  @Override public String getMortarScopeName() {
    return getClass().getName();
  }

  @Override public Object getDaggerModule() {
    return new Module();
  }

  @dagger.Module(
      injects = MainView.class,
      addsTo = U2020Module.class,
      library = true)
  static class Module {
  }

  @Singleton public static class Presenter extends ViewPresenter<GalleryView> {

    @Inject GalleryDatabase galleryDatabase;

    private Section section = Section.HOT;
    private Subscription request;

    private List<Image> images;

    @Override public void onLoad(Bundle savedInstanceState) {
      super.onLoad(savedInstanceState);
      GalleryView view = getView();
      if (view == null) return;

      if (images != null) {
        view.showImages(images);
      } else if (request == null) {
        request = galleryDatabase.loadGallery(section, new EndlessObserver<List<Image>>() {
          @Override public void onNext(List<Image> images) {
            Presenter.this.images = images;
            GalleryView view = getView();
            if (view == null) {
              return;
            }
            view.showImages(images);
          }
        });
      }
    }

    @Override protected void onDestroy() {
      super.onDestroy();
      if (request != null) {
        request.unsubscribe();
      }
    }
  }
}
