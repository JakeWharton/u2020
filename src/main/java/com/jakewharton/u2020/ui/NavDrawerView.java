package com.jakewharton.u2020.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;

public final class NavDrawerView extends LinearLayout {
  public NavDrawerView(Context context, AttributeSet attrs) {
    super(context, attrs);
    setOrientation(VERTICAL);
  }
}
