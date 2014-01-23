package com.jakewharton.u2020.ui.debug;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.jakewharton.u2020.ui.misc.BindableAdapter;

import static butterknife.ButterKnife.findById;

class AnimationSpeedAdapter extends BindableAdapter<Integer> {
  private static final int[] VALUES = {
      1, 2, 3, 5, 10
  };

  public static int getPositionForValue(int value) {
    for (int i = 0; i < VALUES.length; i++) {
      if (VALUES[i] == value) {
        return i;
      }
    }
    return 0; // Default to 1x if something changes.
  }

  AnimationSpeedAdapter(Context context) {
    super(context);
  }

  @Override public int getCount() {
    return VALUES.length;
  }

  @Override public Integer getItem(int position) {
    return VALUES[position];
  }

  @Override public long getItemId(int position) {
    return position;
  }

  @Override public View newView(LayoutInflater inflater, int position, ViewGroup container) {
    return inflater.inflate(android.R.layout.simple_spinner_item, container, false);
  }

  @Override public void bindView(Integer item, int position, View view) {
    TextView tv = findById(view, android.R.id.text1);
    if (item == 1) {
      tv.setText("Normal");
    } else {
      tv.setText(item + "x slower");
    }
  }

  @Override
  public View newDropDownView(LayoutInflater inflater, int position, ViewGroup container) {
    return inflater.inflate(android.R.layout.simple_spinner_dropdown_item, container, false);
  }
}
