package com.jakewharton.u2020.data;

import android.app.Application;
import android.content.SharedPreferences;
import com.jakewharton.u2020.DebugU2020Module;
import com.jakewharton.u2020.data.api.DebugApiModule;
import com.jakewharton.u2020.data.prefs.BooleanPreference;
import com.jakewharton.u2020.data.prefs.IntPreference;
import com.jakewharton.u2020.data.prefs.StringPreference;
import com.squareup.okhttp.OkHttpClient;
import dagger.Module;
import dagger.Provides;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import javax.inject.Singleton;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

@Module(
    addsTo = DebugU2020Module.class,
    includes = DebugApiModule.class,
    overrides = true
)
public class DebugDataModule {
  private static final int DEFAULT_ANIMATION_SPEED = 1; // 1x (normal) speed.
  private static final boolean DEFAULT_PICASSO_DEBUGGING = false; // Debug indicators displayed

  @Provides @Singleton OkHttpClient provideOkHttpClient(Application app) {
    OkHttpClient client = DataModule.createOkHttpClient(app);
    client.setSslSocketFactory(createBadSslSocketFactory());
    return client;
  }

  @Provides @Singleton @Endpoint
  StringPreference provideEndpointPreference(SharedPreferences preferences) {
    return new StringPreference(preferences, "debug_endpoint", Endpoints.MOCK_MODE.url);
  }

  @Provides @Singleton @IsMockMode boolean provideIsMockMode(@Endpoint StringPreference endpoint) {
    return Endpoints.isMockMode(endpoint.get());
  }

  @Provides @Singleton @NetworkProxy
  StringPreference provideNetworkProxy(SharedPreferences preferences) {
    return new StringPreference(preferences, "debug_network_proxy");
  }

  @Provides @Singleton @AnimationSpeed
  IntPreference provideAnimationSpeed(SharedPreferences preferences) {
    return new IntPreference(preferences, "debug_animation_speed", DEFAULT_ANIMATION_SPEED);
  }

  @Provides @Singleton @PicassoDebugging
  BooleanPreference providePicassoDebugging(SharedPreferences preferences) {
    return new BooleanPreference(preferences, "debug_picasso_debugging", DEFAULT_PICASSO_DEBUGGING);
  }

  private static SSLSocketFactory createBadSslSocketFactory() {
    try {
      // Construct SSLSocketFactory that accepts any cert.
      SSLContext context = SSLContext.getInstance("TLS");
      TrustManager permissive = new X509TrustManager() {
        @Override public void checkClientTrusted(X509Certificate[] chain, String authType)
            throws CertificateException {
        }

        @Override public void checkServerTrusted(X509Certificate[] chain, String authType)
            throws CertificateException {
        }

        @Override public X509Certificate[] getAcceptedIssuers() {
          return null;
        }
      };
      context.init(null, new TrustManager[] { permissive }, null);
      return context.getSocketFactory();
    } catch (Exception e) {
      throw new AssertionError(e);
    }
  }
}
