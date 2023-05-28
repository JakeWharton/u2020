package com.jakewharton.u2020.intentfactory;

import android.content.Intent;

/**
 * An {@link IntentFactory} implementation that wraps all {@code Intent}s with a debug action, which
 * launches an activity that allows you to inspect the content.
 */
public final class CapturingIntentFactory implements IntentFactory {

    private final IntentFactory realIntentFactory;

    private final BooleanSupplier captureIntents;

    public CapturingIntentFactory(IntentFactory realIntentFactory, BooleanSupplier captureIntents) {
        this.realIntentFactory = realIntentFactory;
        this.captureIntents = captureIntents;
    }

    @Override
    public Intent createUrlIntent(String url) {
        Intent baseIntent = realIntentFactory.createUrlIntent(url);
        if (captureIntents.getAsBoolean()) {
            return CapturedIntentActivity.createIntent(baseIntent);
        }
        return baseIntent;
    }

    // TODO replace with j.u.f.BooleanSupplier when minSdk 24.
    public interface BooleanSupplier {

        boolean getAsBoolean();
    }
}
