package com.jakewharton.u2020.data;

import android.app.Application;
import android.content.SharedPreferences;
import com.f2prateek.rx.preferences.Preference;
import com.f2prateek.rx.preferences.RxSharedPreferences;
import com.jakewharton.picasso.OkHttp3Downloader;
import com.jakewharton.u2020.IsInstrumentationTest;
import com.jakewharton.u2020.data.api.DebugApiModule;
import com.jakewharton.u2020.data.api.oauth.AccessToken;
import com.jakewharton.u2020.data.prefs.InetSocketAddressPreferenceAdapter;
import com.jakewharton.u2020.intentfactory.CapturingIntentFactory;
import com.jakewharton.u2020.intentfactory.IntentFactory;
import com.squareup.picasso.Picasso;
import dagger.Module;
import dagger.Provides;
import java.net.InetSocketAddress;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import javax.inject.Singleton;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import okhttp3.OkHttpClient;
import retrofit2.mock.NetworkBehavior;
import timber.log.Timber;

@Module(includes = DebugApiModule.class, complete = false, library = true, overrides = true)
public final class DebugDataModule {

    // 1x (normal) speed.
    private static final int DEFAULT_ANIMATION_SPEED = 1;

    // Debug indicators displayed
    private static final boolean DEFAULT_PICASSO_DEBUGGING = false;

    // No pixel grid overlay.
    private static final boolean DEFAULT_PIXEL_GRID_ENABLED = false;

    // No pixel ratio overlay.
    private static final boolean DEFAULT_PIXEL_RATIO_ENABLED = false;

    // No crazy 3D view tree.
    private static final boolean DEFAULT_SCALPEL_ENABLED = false;

    // Draw views by default.
    private static final boolean DEFAULT_SCALPEL_WIREFRAME_ENABLED = false;

    // Show debug drawer first time.
    private static final boolean DEFAULT_SEEN_DEBUG_DRAWER = false;

    // Capture external intents.
    private static final boolean DEFAULT_CAPTURE_INTENTS = true;

    @Provides
    @Singleton
    RxSharedPreferences provideRxSharedPreferences(SharedPreferences prefs) {
        return RxSharedPreferences.create(prefs);
    }

    @Provides
    @Singleton
    IntentFactory provideIntentFactory(@IsMockMode boolean isMockMode, @CaptureIntents Preference<Boolean> captureIntents) {
        return new CapturingIntentFactory(IntentFactory.REAL, () -> isMockMode && captureIntents.get());
    }

    @Provides
    @Singleton
    OkHttpClient provideOkHttpClient(Application app, Preference<InetSocketAddress> networkProxyAddress) {
        return DataModule.createOkHttpClient(app).sslSocketFactory(createBadSslSocketFactory()).proxy(InetSocketAddressPreferenceAdapter.createProxy(networkProxyAddress.get())).build();
    }

    @Provides
    @Singleton
    @AccessToken
    Preference<String> provideAccessToken(RxSharedPreferences prefs, @ApiEndpoint Preference<String> endpoint) {
        // Return an endpoint-specific preference.
        return prefs.getString("access-token-" + endpoint.get());
    }

    @Provides
    @Singleton
    @ApiEndpoint
    Preference<String> provideEndpointPreference(RxSharedPreferences preferences) {
        return preferences.getString("debug_endpoint", ApiEndpoints.MOCK_MODE.url);
    }

    @Provides
    @Singleton
    @IsMockMode
    boolean provideIsMockMode(@ApiEndpoint Preference<String> endpoint, @IsInstrumentationTest boolean isInstrumentationTest) {
        // Running in an instrumentation forces mock mode.
        return isInstrumentationTest || ApiEndpoints.isMockMode(endpoint.get());
    }

    @Provides
    @Singleton
    @NetworkDelay
    Preference<Long> provideNetworkDelay(RxSharedPreferences preferences) {
        return preferences.getLong("debug_network_delay", 2000l);
    }

    @Provides
    @Singleton
    @NetworkFailurePercent
    Preference<Integer> provideNetworkFailurePercent(RxSharedPreferences preferences) {
        return preferences.getInteger("debug_network_failure_percent", 3);
    }

    @Provides
    @Singleton
    @NetworkErrorPercent
    Preference<Integer> provideNetworkErrorPercent(RxSharedPreferences preferences) {
        return preferences.getInteger("debug_network_error_percent", 0);
    }

    @Provides
    @Singleton
    Preference<NetworkErrorCode> provideNetworkErrorCode(RxSharedPreferences preferences) {
        return preferences.getEnum("debug_network_error_code", NetworkErrorCode.HTTP_500, NetworkErrorCode.class);
    }

    @Provides
    @Singleton
    @NetworkVariancePercent
    Preference<Integer> provideNetworkVariancePercent(RxSharedPreferences preferences) {
        return preferences.getInteger("debug_network_variance_percent", 40);
    }

    @Provides
    @Singleton
    Preference<InetSocketAddress> provideNetworkProxyAddress(RxSharedPreferences preferences) {
        return preferences.getObject("debug_network_proxy", InetSocketAddressPreferenceAdapter.INSTANCE);
    }

    @Provides
    @Singleton
    @CaptureIntents
    Preference<Boolean> provideCaptureIntentsPreference(RxSharedPreferences preferences) {
        return preferences.getBoolean("debug_capture_intents", DEFAULT_CAPTURE_INTENTS);
    }

    @Provides
    @Singleton
    @AnimationSpeed
    Preference<Integer> provideAnimationSpeed(RxSharedPreferences preferences) {
        return preferences.getInteger("debug_animation_speed", DEFAULT_ANIMATION_SPEED);
    }

    @Provides
    @Singleton
    @PicassoDebugging
    Preference<Boolean> providePicassoDebugging(RxSharedPreferences preferences) {
        return preferences.getBoolean("debug_picasso_debugging", DEFAULT_PICASSO_DEBUGGING);
    }

    @Provides
    @Singleton
    @PixelGridEnabled
    Preference<Boolean> providePixelGridEnabled(RxSharedPreferences preferences) {
        return preferences.getBoolean("debug_pixel_grid_enabled", DEFAULT_PIXEL_GRID_ENABLED);
    }

    @Provides
    @Singleton
    @PixelRatioEnabled
    Preference<Boolean> providePixelRatioEnabled(RxSharedPreferences preferences) {
        return preferences.getBoolean("debug_pixel_ratio_enabled", DEFAULT_PIXEL_RATIO_ENABLED);
    }

    @Provides
    @Singleton
    @SeenDebugDrawer
    Preference<Boolean> provideSeenDebugDrawer(RxSharedPreferences preferences) {
        return preferences.getBoolean("debug_seen_debug_drawer", DEFAULT_SEEN_DEBUG_DRAWER);
    }

    @Provides
    @Singleton
    @ScalpelEnabled
    Preference<Boolean> provideScalpelEnabled(RxSharedPreferences preferences) {
        return preferences.getBoolean("debug_scalpel_enabled", DEFAULT_SCALPEL_ENABLED);
    }

    @Provides
    @Singleton
    @ScalpelWireframeEnabled
    Preference<Boolean> provideScalpelWireframeEnabled(RxSharedPreferences preferences) {
        return preferences.getBoolean("debug_scalpel_wireframe_drawer", DEFAULT_SCALPEL_WIREFRAME_ENABLED);
    }

    @Provides
    @Singleton
    Picasso providePicasso(OkHttpClient client, NetworkBehavior behavior, @IsMockMode boolean isMockMode, Application app) {
        Picasso.Builder builder = new Picasso.Builder(app).downloader(new OkHttp3Downloader(client));
        if (isMockMode) {
            builder.addRequestHandler(new MockRequestHandler(behavior, app.getAssets()));
        }
        builder.listener((picasso, uri, exception) -> {
            Timber.e(exception, "Error while loading image %s", uri);
        });
        return builder.build();
    }

    private static SSLSocketFactory createBadSslSocketFactory() {
        try {
            // Construct SSLSocketFactory that accepts any cert.
            SSLContext context = SSLContext.getInstance("TLS");
            TrustManager permissive = new X509TrustManager() {

                @Override
                public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
                }

                @Override
                public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
                }

                @Override
                public X509Certificate[] getAcceptedIssuers() {
                    return new X509Certificate[0];
                }
            };
            context.init(null, new TrustManager[] { permissive }, null);
            return context.getSocketFactory();
        } catch (Exception e) {
            throw new AssertionError(e);
        }
    }
}
