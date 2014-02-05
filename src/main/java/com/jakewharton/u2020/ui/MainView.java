package com.jakewharton.u2020.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import javax.inject.Inject;
import mortar.Blueprint;
import mortar.Mortar;
import mortar.MortarScope;

public class MainView extends FrameLayout {
  @Inject MainBlueprint.Presenter presenter;

  public MainView(Context context, AttributeSet attrs) {
    super(context, attrs);
    Mortar.inject(context, this);
  }

  @Override protected void onFinishInflate() {
    super.onFinishInflate();
    presenter.takeView(this);
  }

  public void showScreen(Blueprint firstScreen, int layoutId) {
    MortarScope myScope = Mortar.getScope(getContext());
    MortarScope newChildScope = myScope.requireChild(firstScreen);

    View oldChild = getChildAt(0);
    View newChild;

    if (oldChild != null) {
      MortarScope oldChildScope = Mortar.getScope(oldChild.getContext());
      if (oldChildScope.getName().equals(firstScreen.getMortarScopeName())) {
        // If it's already showing, short circuit.
        return;
      }

      oldChildScope.destroy();
    }

    // Create the new child.
    Context childContext = newChildScope.createContext(getContext());
    newChild = LayoutInflater.from(childContext).inflate(layoutId, null);

    // Out with the old, in with the new.
    if (oldChild != null) removeView(oldChild);
    addView(newChild);
  }
}
