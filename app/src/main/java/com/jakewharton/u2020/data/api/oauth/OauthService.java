package com.jakewharton.u2020.data.api.oauth;

import android.app.IntentService;
import android.content.Intent;
import com.jakewharton.u2020.data.Injector;
import dagger.ObjectGraph;
import javax.inject.Inject;

public final class OauthService extends IntentService {
  @Inject OauthManager oauthManager;

  public OauthService() {
    super(OauthService.class.getSimpleName());
  }

  @Override public void onCreate() {
    super.onCreate();
    ObjectGraph appGraph = Injector.obtain(getApplication());
    appGraph.inject(this);
  }

  @Override protected void onHandleIntent(Intent intent) {
    oauthManager.handleResult(intent.getData());
  }
}
