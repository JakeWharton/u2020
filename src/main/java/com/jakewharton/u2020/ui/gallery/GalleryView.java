package com.jakewharton.u2020.ui.gallery;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.AbsListView;
import butterknife.ButterKnife;
import butterknife.InjectView;
import com.jakewharton.u2020.R;
import com.jakewharton.u2020.data.api.model.Image;
import com.jakewharton.u2020.ui.misc.BetterViewAnimator;
import com.squareup.picasso.Picasso;
import java.util.List;
import javax.inject.Inject;
import mortar.Mortar;

public class GalleryView extends BetterViewAnimator {
  @InjectView(R.id.gallery_grid) AbsListView galleryView;

  @Inject GalleryBlueprint.Presenter presenter;
  @Inject Picasso picasso;

  private final GalleryAdapter adapter;

  public GalleryView(Context context, AttributeSet attrs) {
    super(context, attrs);
    Mortar.inject(context, this);

    adapter = new GalleryAdapter(context, picasso);
  }

  @Override protected void onFinishInflate() {
    super.onFinishInflate();
    ButterKnife.inject(this);

    galleryView.setAdapter(adapter);

    presenter.takeView(this);
  }

  @Override protected void onDetachedFromWindow() {
    presenter.dropView(this);
    super.onDetachedFromWindow();
  }

  public void showImages(List<Image> images) {
    adapter.replaceWith(images);
    setDisplayedChildId(R.id.gallery_grid);
  }
}
