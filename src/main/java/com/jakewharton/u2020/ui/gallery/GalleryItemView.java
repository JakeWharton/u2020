package com.jakewharton.u2020.ui.gallery;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.ButterKnife;
import butterknife.InjectView;
import com.jakewharton.u2020.R;
import com.jakewharton.u2020.data.api.model.Image;
import com.squareup.picasso.Picasso;

public class GalleryItemView extends FrameLayout {
  @InjectView(R.id.gallery_image_image) ImageView image;
  @InjectView(R.id.gallery_image_title) TextView title;

  private float aspectRatio = 1;

  public GalleryItemView(Context context, AttributeSet attrs) {
    super(context, attrs);
  }

  @Override protected void onFinishInflate() {
    super.onFinishInflate();
    ButterKnife.inject(this);
  }

  public void bindTo(Image item, Picasso picasso) {
    aspectRatio = 1f * item.width / item.height;
    requestLayout();

    title.setText(item.title);

    // TODO this sucks. should be .fit().centerCrop() but re-use and re-layout kills.
    picasso.load(item.link).into(image);
  }

  @Override protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
    int mode = MeasureSpec.getMode(widthMeasureSpec);
    if (mode != MeasureSpec.EXACTLY) {
      throw new IllegalStateException("layout_width must be match_parent");
    }

    int width = MeasureSpec.getSize(widthMeasureSpec);
    int height = (int) (width / aspectRatio);
    heightMeasureSpec = MeasureSpec.makeMeasureSpec(height, MeasureSpec.EXACTLY);
    super.onMeasure(widthMeasureSpec, heightMeasureSpec);
  }
}
