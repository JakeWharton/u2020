package com.jakewharton.u2020.ui.debug;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.jakewharton.u2020.ui.misc.BindableAdapter;

class NetworkDelayAdapter extends BindableAdapter<Long> {
  private static final long[] VALUES = {
      250, 500, 1000, 2000, 3000
  };

  public static int getPositionForValue(long value) {
    for (int i = 0; i < VALUES.length; i++) {
      if (VALUES[i] == value) {
        return i;
      }
    }
    return 3; // Default to 2000 if something changes.
  }

  NetworkDelayAdapter(Context context) {
    super(context);
  }

  @Override public int getCount() {
    return VALUES.length;
  }

  @Override public Long getItem(int position) {
    return VALUES[position];
  }

  @Override public long getItemId(int position) {
    return position;
  }

  @Override public View newView(LayoutInflater inflater, int position, ViewGroup container) {
    return inflater.inflate(android.R.layout.simple_spinner_item, container, false);
  }

  @Override public void bindView(Long item, int position, View view) {
    TextView tv = (TextView) view.findViewById(android.R.id.text1);
    tv.setText(item + "ms");
  }

  @Override
  public View newDropDownView(LayoutInflater inflater, int position, ViewGroup container) {
    return inflater.inflate(android.R.layout.simple_spinner_dropdown_item, container, false);
  }
}
