package com.jakewharton.u2020.ui.debug;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.jakewharton.u2020.data.Endpoints;
import com.jakewharton.u2020.ui.misc.BindableAdapter;

import static butterknife.ButterKnife.findById;

class ServerEndpointAdapter extends BindableAdapter<Endpoints> {
  ServerEndpointAdapter(Context context) {
    super(context);
  }

  @Override public int getCount() {
    return Endpoints.values().length;
  }

  @Override public Endpoints getItem(int position) {
    return Endpoints.values()[position];
  }

  @Override public long getItemId(int position) {
    return position;
  }

  @Override public View newView(LayoutInflater inflater, int position, ViewGroup container) {
    return inflater.inflate(android.R.layout.simple_spinner_item, container, false);
  }

  @Override public void bindView(Endpoints item, int position, View view) {
    TextView tv = findById(view, android.R.id.text1);
    tv.setText(item.name);
  }

  @Override
  public View newDropDownView(LayoutInflater inflater, int position, ViewGroup container) {
    return inflater.inflate(android.R.layout.simple_spinner_dropdown_item, container, false);
  }
}
