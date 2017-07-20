package com.jakewharton.u2020.ui.misc;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class EnumAdapter<T extends Enum<T>> extends BindableAdapter<T> {
  private final T[] enumConstants;
  private final boolean showNull;
  private final int nullOffset;

  public EnumAdapter(Context context, Class<T> enumType) {
    this(context, enumType, false);
  }

  public EnumAdapter(Context context, Class<T> enumType, boolean showNull) {
    super(context);
    this.enumConstants = enumType.getEnumConstants();
    this.showNull = showNull;
    this.nullOffset = showNull ? 1 : 0;
  }

  @Override public final int getCount() {
    return enumConstants.length + nullOffset;
  }

  @Override public final T getItem(int position) {
    if (showNull && position == 0) {
      return null;
    }

    return enumConstants[position - nullOffset];
  }

  @Override public final long getItemId(int position) {
    return position;
  }

  @Override public View newView(LayoutInflater inflater, int position, ViewGroup container) {
    return inflater.inflate(android.R.layout.simple_spinner_item, container, false);
  }

  @Override public final void bindView(T item, int position, View view) {
    TextView tv = view.findViewById(android.R.id.text1);
    tv.setText(getName(item));
  }

  @Override
  public final View newDropDownView(LayoutInflater inflater, int position, ViewGroup container) {
    return inflater.inflate(android.R.layout.simple_spinner_dropdown_item, container, false);
  }

  protected String getName(T item) {
    return String.valueOf(item);
  }
}
