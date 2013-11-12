package com.jakewharton.u2020.ui.debug;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.jakewharton.u2020.ui.misc.BindableAdapter;

class NetworkVarianceAdapter extends BindableAdapter<Integer> {
  private static final int[] VALUES = {
      20, 40, 60
  };

  public static int getPositionForValue(int value) {
    for (int i = 0; i < VALUES.length; i++) {
      if (VALUES[i] == value) {
        return i;
      }
    }
    return 1; // Default to 40% if something changes.
  }

  NetworkVarianceAdapter(Context context) {
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
    TextView tv = (TextView) view.findViewById(android.R.id.text1);
    tv.setText("±" + item + "%");
  }

  @Override
  public View newDropDownView(LayoutInflater inflater, int position, ViewGroup container) {
    return inflater.inflate(android.R.layout.simple_spinner_dropdown_item, container, false);
  }
}
