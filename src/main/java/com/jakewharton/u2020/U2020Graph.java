package com.jakewharton.u2020;

import com.jakewharton.u2020.ui.MainActivity;
import com.jakewharton.u2020.ui.gallery.GalleryView;
import dagger.Component;
import javax.inject.Singleton;

/**
 * A common interface implemented by both the Release and Debug flavored components.
 */
public interface U2020Graph {
  void inject(U2020App app);
  void inject(MainActivity activity);
  void inject(GalleryView view);
}
