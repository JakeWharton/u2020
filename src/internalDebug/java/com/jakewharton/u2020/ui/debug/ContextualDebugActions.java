package com.jakewharton.u2020.ui.debug;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import com.jakewharton.u2020.R;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import timber.log.Timber;

public class ContextualDebugActions implements ViewGroup.OnHierarchyChangeListener {
  public interface DebugAction<T extends View> {
    String name();
    Class<T> viewClass();
    void run(T view);
  }

  private final Map<DebugAction<? extends View>, View> buttonMap;
  private final Map<Class<? extends View>, List<DebugAction<? extends View>>> actionMap;

  private final DebugDrawerLayout drawerLayout;
  private final Context drawerContext;
  private final View contextualTitleView;
  private final LinearLayout contextualListView;

  public ContextualDebugActions(DebugAppContainer container, Set<DebugAction<?>> debugActions) {
    buttonMap = new LinkedHashMap<>();
    actionMap = new LinkedHashMap<>();

    drawerLayout = container.drawerLayout;
    drawerContext = container.drawerContext;
    contextualTitleView = container.contextualTitleView;
    contextualListView = container.contextualListView;

    for (DebugAction<?> debugAction : debugActions) {
      putAction(debugAction.viewClass(), debugAction);
    }
  }

  private void putAction(Class<? extends View> view, DebugAction<? extends View> action) {
    Timber.d("Adding %s action for %s.", action.getClass().getSimpleName(), view.getSimpleName());

    List<DebugAction<? extends View>> actions = actionMap.get(view);
    if (actions == null) {
      actions = new ArrayList<>(2);
      actionMap.put(view, actions);
    }
    actions.add(action);
  }

  @Override public void onChildViewAdded(View parent, View child) {
    List<DebugAction<? extends View>> actions = actionMap.get(child.getClass());
    if (actions != null) {
      for (DebugAction<? extends View> action : actions) {
        Timber.d("Adding debug action \"%s\" for %s.", action.name(),
            child.getClass().getSimpleName());

        View button = createButton(action, child);
        buttonMap.put(action, button);
        contextualListView.addView(button);
      }
      updateContextualVisibility();
    }
  }

  @Override public void onChildViewRemoved(View parent, View child) {
    List<DebugAction<? extends View>> actions = actionMap.get(child.getClass());
    if (actions != null) {
      for (DebugAction<? extends View> action : actions) {
        Timber.d("Removing debug action \"%s\" for %s.", action.name(),
            child.getClass().getSimpleName());
        contextualListView.removeView(buttonMap.remove(action));
      }
      updateContextualVisibility();
    }
  }

  private Button createButton(final DebugAction action, final View child) {
    Button button = (Button) LayoutInflater.from(drawerContext)
        .inflate(R.layout.debug_drawer_contextual_action, contextualListView, false);
    button.setText(action.name());
    button.setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View view) {
        drawerLayout.closeDrawers();
        action.run(child);
      }
    });
    return button;
  }

  private void updateContextualVisibility() {
    int visibility = contextualListView.getChildCount() > 0 ? View.VISIBLE : View.GONE;
    contextualTitleView.setVisibility(visibility);
    contextualListView.setVisibility(visibility);
  }
}
