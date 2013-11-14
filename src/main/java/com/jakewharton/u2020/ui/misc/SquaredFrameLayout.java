package com.jakewharton.u2020.ui.misc;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.FrameLayout;

public class SquaredFrameLayout extends FrameLayout {
  public SquaredFrameLayout(Context context, AttributeSet attrs) {
    super(context, attrs);
  }

  @Override protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
    int mode = MeasureSpec.getMode(widthMeasureSpec);
    if (mode != MeasureSpec.EXACTLY) {
      throw new IllegalStateException("layout_width must be match_parent");
    }

    super.onMeasure(widthMeasureSpec, widthMeasureSpec);
    int size = getMeasuredWidth();
    setMeasuredDimension(size, size);
  }
}
