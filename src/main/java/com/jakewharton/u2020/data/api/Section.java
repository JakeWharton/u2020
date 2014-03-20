package com.jakewharton.u2020.data.api;

import com.google.gson.annotations.SerializedName;

public enum Section {
  @SerializedName("hot") HOT,
  @SerializedName("top") TOP,
  @SerializedName("user") USER;
}
