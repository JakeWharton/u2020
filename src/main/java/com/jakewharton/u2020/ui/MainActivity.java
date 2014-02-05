package com.jakewharton.u2020.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.ViewGroup;
import com.jakewharton.u2020.R;
import com.jakewharton.u2020.U2020App;
import javax.inject.Inject;
import mortar.Mortar;
import mortar.MortarActivityScope;
import mortar.MortarContext;
import mortar.MortarScope;

import static android.content.Intent.ACTION_MAIN;
import static android.content.Intent.CATEGORY_LAUNCHER;

public class MainActivity extends Activity implements MortarContext {
  private MortarActivityScope activityScope;
  @Inject AppContainer appContainer;

  private ViewGroup container;

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    if (isWrongInstance()) {
      finish();
      return;
    }

    U2020App app = U2020App.get(this);

    activityScope = Mortar.requireActivityScope(app.getMortarScope(), new MainBlueprint());
    Mortar.inject(this, this);

    activityScope.onCreate(savedInstanceState);

    container = appContainer.get(this, app);

    getLayoutInflater().inflate(R.layout.main, container);
  }

  @Override protected void onSaveInstanceState(Bundle outState) {
    super.onSaveInstanceState(outState);
    activityScope.onSaveInstanceState(outState);
  }

  @Override protected void onDestroy() {
    super.onDestroy();

    if (isFinishing() && activityScope != null) {
      activityScope.destroy();
      activityScope = null;
    }
  }

  /**
   * Dev tools and the play store (and others?) launch with a different intent, and so
   * lead to a redundant instance of this activity being spawned. <a
   * href="http://stackoverflow.com/questions/17702202/find-out-whether-the-current-activity-will-be-task-root-eventually-after-pendin"
   * >Details</a>.
   */
  private boolean isWrongInstance() {
    if (!isTaskRoot()) {
      Intent intent = getIntent();
      boolean isMainAction = intent.getAction() != null && intent.getAction().equals(ACTION_MAIN);
      return intent.hasCategory(CATEGORY_LAUNCHER) && isMainAction;
    }
    return false;
  }

  private MainView getMainView() {
    ViewGroup root = (ViewGroup) findViewById(android.R.id.content);
    return (MainView) root.getChildAt(0);
  }

  @Override public MortarScope getMortarScope() {
    return activityScope;
  }
}
