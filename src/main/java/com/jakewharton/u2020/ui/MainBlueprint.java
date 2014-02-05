package com.jakewharton.u2020.ui;

import android.os.Bundle;
import com.jakewharton.u2020.R;
import com.jakewharton.u2020.U2020Module;
import com.jakewharton.u2020.ui.gallery.GalleryBlueprint;
import javax.inject.Inject;
import javax.inject.Singleton;
import mortar.Blueprint;
import mortar.ViewPresenter;

public class MainBlueprint implements Blueprint {

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
  public static class Module {
  }

  @Singleton public static class Presenter extends ViewPresenter<MainView> {

    @Inject
    public Presenter() {

    }

    @Override public void onLoad(Bundle savedInstanceState) {
      super.onLoad(savedInstanceState);

      MainView view = getView();
      if (view == null) return;

      view.showScreen(new GalleryBlueprint(), R.layout.gallery_view);
    }
  }
}
