package com.jakewharton.u2020.data;

import android.content.Intent;
import com.jakewharton.u2020.data.prefs.BooleanPreference;
import com.jakewharton.u2020.ui.ExternalIntentActivity;
import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * An {@link IntentFactory} implementation that wraps all {@code Intent}s with a debug action, which
 * launches an activity that allows you to inspect the content.
 */
@Singleton
public class DebugIntentFactory extends IntentFactory {
  private final boolean isMockMode;
  private final BooleanPreference captureIntents;

  @Inject public DebugIntentFactory(@IsMockMode boolean isMockMode,
      @CaptureIntents BooleanPreference captureIntents) {
    this.isMockMode = isMockMode;
    this.captureIntents = captureIntents;
  }

  @Override public Intent createUrlIntent(String url) {
    Intent baseIntent = super.createUrlIntent(url);
    if (!isMockMode || !captureIntents.get()) {
      return baseIntent;
    } else {
      return ExternalIntentActivity.createIntent(baseIntent);
    }
  }
}
