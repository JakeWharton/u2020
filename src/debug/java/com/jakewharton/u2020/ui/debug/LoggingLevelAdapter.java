package com.jakewharton.u2020.ui.debug;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.jakewharton.u2020.ui.misc.BindableAdapter;
import retrofit.RestAdapter;

class LoggingLevelAdapter extends BindableAdapter<RestAdapter.LogLevel> {
  LoggingLevelAdapter(Context context) {
    super(context);
  }

  @Override public int getCount() {
    return RestAdapter.LogLevel.values().length;
  }

  @Override public RestAdapter.LogLevel getItem(int position) {
    return RestAdapter.LogLevel.values()[position];
  }

  @Override public long getItemId(int position) {
    return position;
  }

  @Override public View newView(LayoutInflater inflater, int position, ViewGroup container) {
    return inflater.inflate(android.R.layout.simple_spinner_item, container, false);
  }

  @Override public void bindView(RestAdapter.LogLevel item, int position, View view) {
    TextView tv = (TextView) view.findViewById(android.R.id.text1);
    tv.setText(item.toString());
  }

  @Override
  public View newDropDownView(LayoutInflater inflater, int position, ViewGroup container) {
    return inflater.inflate(android.R.layout.simple_spinner_dropdown_item, container, false);
  }
}
