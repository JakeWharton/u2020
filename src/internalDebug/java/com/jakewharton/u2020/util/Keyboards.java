package com.jakewharton.u2020.util;

import android.content.Context;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

public final class Keyboards {
  public static void showKeyboard(View view) {
    getInputManager(view.getContext()).showSoftInput(view, 0);
  }

  private static InputMethodManager getInputManager(Context context) {
    return (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
  }

  private Keyboards() {
    throw new AssertionError("No instances.");
  }
}
