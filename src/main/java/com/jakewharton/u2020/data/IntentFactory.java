package com.jakewharton.u2020.data;

import android.content.Intent;
import android.net.Uri;
import javax.inject.Inject;
import javax.inject.Singleton;

/** Creates {@link Intent}s for launching into external applications. */
@Singleton
public class IntentFactory {
  @Inject public IntentFactory() {
  }

  public Intent createUrlIntent(String url) {
    Intent intent = new Intent(Intent.ACTION_VIEW);
    intent.setData(Uri.parse(url));
    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

    return intent;
  }
}
