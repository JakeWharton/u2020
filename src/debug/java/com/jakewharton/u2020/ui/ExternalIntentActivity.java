package com.jakewharton.u2020.ui;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.style.StyleSpan;
import android.view.MenuItem;
import android.widget.TextView;
import butterknife.ButterKnife;
import butterknife.InjectView;
import com.jakewharton.u2020.R;
import com.jakewharton.u2020.ui.misc.Truss;
import com.jakewharton.u2020.util.Intents;
import java.util.Arrays;

public final class ExternalIntentActivity extends Activity implements Toolbar.OnMenuItemClickListener {
  public static final String ACTION = "com.jakewharton.u2020.intent.EXTERNAL_INTENT";
  public static final String EXTRA_BASE_INTENT = "debug_base_intent";

  public static Intent createIntent(Intent baseIntent) {
    Intent intent = new Intent();
    intent.setAction(ACTION);
    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
    intent.putExtra(EXTRA_BASE_INTENT, baseIntent);
    return intent;
  }

  @InjectView(R.id.toolbar) Toolbar toolbarView;
  @InjectView(R.id.action) TextView actionView;
  @InjectView(R.id.data) TextView dataView;
  @InjectView(R.id.extras) TextView extrasView;

  private Intent baseIntent;

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.debug_external_intent_activity);
    ButterKnife.inject(this);

    toolbarView.inflateMenu(R.menu.debug_external_intent);
    toolbarView.setOnMenuItemClickListener(this);

    baseIntent = getIntent().getParcelableExtra(EXTRA_BASE_INTENT);
    fillAction();
    fillData();
    fillExtras();
  }

  @Override public boolean onMenuItemClick(MenuItem menuItem) {
    switch (menuItem.getItemId()) {
      case R.id.debug_launch:
        if (Intents.maybeStartActivity(this, baseIntent)) {
          finish();
        }
        return true;
      default:
        return false;
    }
  }

  private void fillAction() {
    String action = baseIntent.getAction();
    actionView.setText(action == null ? "None!" : action);
  }

  private void fillData() {
    Uri data = baseIntent.getData();
    dataView.setText(data == null ? "None!" : data.toString());
  }

  private void fillExtras() {
    Bundle extras = baseIntent.getExtras();
    if (extras == null) {
      extrasView.setText("None!");
    } else {
      Truss truss = new Truss();
      for (String key : extras.keySet()) {
        Object value = extras.get(key);

        String valueString;
        if (value.getClass().isArray()) {
          valueString = Arrays.toString((Object[]) value);
        } else {
          valueString = value.toString();
        }

        truss.pushSpan(new StyleSpan(Typeface.BOLD));
        truss.append(key).append(":\n");
        truss.popSpan();
        truss.append(valueString).append("\n\n");
      }

      extrasView.setText(truss.build());
    }
  }
}
