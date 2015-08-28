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
  public static abstract class DebugAction<T extends View> {
    private final Class<T> viewClass;

    protected DebugAction(Class<T> viewClass) {
      this.viewClass = viewClass;
    }

    /** Invoked when the action has been added as available to run. */
    public void added() {}

    /** Invoked when the action has been removed as available to run. */
    public void removed() {}

    /** Return true if action is applicable. Called each time the target view is added. */
    public boolean enabled() {
      return true;
    }

    /** Human-readable action name. Displayed in debug drawer. */
    public abstract String name();

    /** Perform this action using the specified view. */
    public abstract void run(T view);
  }

  private final Map<DebugAction<? extends View>, View> buttonMap;
  private final Map<Class<? extends View>, List<DebugAction<? extends View>>> actionMap;

  private final Context drawerContext;
  private final View contextualTitleView;
  private final LinearLayout contextualListView;

  private View.OnClickListener clickListener;

  public ContextualDebugActions(DebugView debugView, Set<DebugAction> debugActions) {
    buttonMap = new LinkedHashMap<>();
    actionMap = new LinkedHashMap<>();

    drawerContext = debugView.getContext();
    contextualTitleView = debugView.contextualTitleView;
    contextualListView = debugView.contextualListView;

    for (DebugAction<?> debugAction : debugActions) {
      Class cls = debugAction.viewClass;
      Timber.d("Adding %s action for %s.", debugAction.getClass().getSimpleName(), cls.getSimpleName());

      List<DebugAction<? extends View>> actionList = actionMap.get(cls);
      if (actionList == null) {
        actionList = new ArrayList<>(2);
        actionMap.put(cls, actionList);
      }
      actionList.add(debugAction);
    }
  }

  public void setActionClickListener(View.OnClickListener clickListener) {
    this.clickListener = clickListener;
  }

  @Override public void onChildViewAdded(View parent, View child) {
    List<DebugAction<? extends View>> actions = actionMap.get(child.getClass());
    if (actions != null) {
      for (DebugAction<? extends View> action : actions) {
        if (!action.enabled()) {
          continue;
        }
        Timber.d("Adding debug action \"%s\" for %s.", action.name(),
            child.getClass().getSimpleName());

        View button = createButton(action, child);
        buttonMap.put(action, button);
        contextualListView.addView(button);
        action.added();
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
        View buttonView = buttonMap.remove(action);
        if (buttonView != null) {
          contextualListView.removeView(buttonView);
          action.removed();
        }
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
        if (clickListener != null) {
          clickListener.onClick(view);
        }
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
