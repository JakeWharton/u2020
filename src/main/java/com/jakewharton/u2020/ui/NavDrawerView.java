package com.jakewharton.u2020.ui;

import android.content.Context;
import android.support.annotation.DrawableRes;
import android.support.annotation.StringRes;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.jakewharton.u2020.R;

public final class NavDrawerView extends LinearLayout {
  public NavDrawerView(Context context, AttributeSet attrs) {
    super(context, attrs);
    setOrientation(VERTICAL);

    // Do not let clicks trickle through to the content view.
    setClickable(true);
  }

  public void addItem(@DrawableRes int icon, @StringRes int text, OnClickListener listener) {
    LayoutInflater layoutInflater = LayoutInflater.from(getContext());
    Item item = (Item) layoutInflater.inflate(R.layout.main_drawer_item, this, false);

    item.setText(text);
    item.setCompoundDrawablesWithIntrinsicBounds(icon, 0, 0, 0);
    item.setOnClickListener(listener);

    addView(item);
  }

  public static final class Item extends TextView {
    public Item(Context context, AttributeSet attrs) {
      super(context, attrs);
    }
  }
}
