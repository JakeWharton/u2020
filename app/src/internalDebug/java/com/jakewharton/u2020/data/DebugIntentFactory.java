package com.jakewharton.u2020.data;

import android.content.Intent;
import com.f2prateek.rx.preferences.Preference;
import com.jakewharton.u2020.ui.ExternalIntentActivity;

/**
 * An {@link IntentFactory} implementation that wraps all {@code Intent}s with a debug action, which
 * launches an activity that allows you to inspect the content.
 */
public final class DebugIntentFactory implements IntentFactory {
  private final IntentFactory realIntentFactory;
  private final boolean isMockMode;
  private final Preference<Boolean> captureIntents;

  public DebugIntentFactory(IntentFactory realIntentFactory, boolean isMockMode,
      Preference<Boolean> captureIntents) {
    this.realIntentFactory = realIntentFactory;
    this.isMockMode = isMockMode;
    this.captureIntents = captureIntents;
  }

  @Override public Intent createUrlIntent(String url) {
    Intent baseIntent = realIntentFactory.createUrlIntent(url);
    if (!isMockMode || !captureIntents.get()) {
      return baseIntent;
    } else {
      return ExternalIntentActivity.createIntent(baseIntent);
    }
  }
}
