package com.jakewharton.u2020.ui.misc;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.v7.widget.AppCompatImageView;
import android.util.AttributeSet;
import android.widget.ImageView;
import com.jakewharton.u2020.R;

import static android.view.View.MeasureSpec.EXACTLY;

public final class AspectRatioImageView extends AppCompatImageView {
  private final int widthRatio;
  private final int heightRatio;

  public AspectRatioImageView(Context context, AttributeSet attrs) {
    super(context, attrs);

    TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.AspectRatioImageView);
    widthRatio = a.getInteger(R.styleable.AspectRatioImageView_widthRatio, 1);
    heightRatio = a.getInteger(R.styleable.AspectRatioImageView_heightRatio, 1);
    a.recycle();
  }

  @Override protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
    int widthMode = MeasureSpec.getMode(widthMeasureSpec);
    int widthSize = MeasureSpec.getSize(widthMeasureSpec);
    int heightMode = MeasureSpec.getMode(heightMeasureSpec);
    int heightSize = MeasureSpec.getSize(heightMeasureSpec);

    if (widthMode == EXACTLY) {
      if (heightMode != EXACTLY) {
        heightSize = (int) (widthSize * 1f / widthRatio * heightRatio);
      }
    } else if (heightMode == EXACTLY) {
      widthSize = (int) (heightSize * 1f / heightRatio * widthRatio);
    } else {
      throw new IllegalStateException("Either width or height must be EXACTLY.");
    }

    widthMeasureSpec = MeasureSpec.makeMeasureSpec(widthSize, EXACTLY);
    heightMeasureSpec = MeasureSpec.makeMeasureSpec(heightSize, EXACTLY);

    super.onMeasure(widthMeasureSpec, heightMeasureSpec);
  }
}
