package com.jakewharton.u2020.data;

import android.app.Application;
import android.content.SharedPreferences;
import com.f2prateek.rx.preferences.Preference;
import com.f2prateek.rx.preferences.RxSharedPreferences;
import com.jakewharton.picasso.OkHttp3Downloader;
import com.jakewharton.u2020.data.api.ApiModule;
import com.jakewharton.u2020.data.api.oauth.AccessToken;
import com.jakewharton.u2020.intentfactory.IntentFactory;
import com.squareup.moshi.Moshi;
import com.squareup.picasso.Picasso;
import dagger.Module;
import dagger.Provides;
import java.io.File;
import javax.inject.Singleton;
import okhttp3.Cache;
import okhttp3.OkHttpClient;
import org.threeten.bp.Clock;
import timber.log.Timber;

import static android.content.Context.MODE_PRIVATE;
import static com.jakewharton.byteunits.DecimalByteUnit.MEGABYTES;

@Module(
    includes = ApiModule.class,
    complete = false,
    library = true
)
public final class DataModule {
  static final int DISK_CACHE_SIZE = (int) MEGABYTES.toBytes(50);

  @Provides @Singleton SharedPreferences provideSharedPreferences(Application app) {
    return app.getSharedPreferences("u2020", MODE_PRIVATE);
  }

  @Provides @Singleton RxSharedPreferences provideRxSharedPreferences(SharedPreferences prefs) {
    return RxSharedPreferences.create(prefs);
  }

  @Provides @Singleton @AccessToken
  Preference<String> provideAccessToken(RxSharedPreferences prefs) {
    return prefs.getString("access-token");
  }

  @Provides @Singleton Moshi provideMoshi() {
    return new Moshi.Builder()
        .add(new InstantAdapter())
        .build();
  }

  @Provides @Singleton Clock provideClock() {
    return Clock.systemDefaultZone();
  }

  @Provides @Singleton IntentFactory provideIntentFactory() {
    return IntentFactory.REAL;
  }

  @Provides @Singleton OkHttpClient provideOkHttpClient(Application app) {
    return createOkHttpClient(app).build();
  }

  @Provides @Singleton Picasso providePicasso(Application app, OkHttpClient client) {
    return new Picasso.Builder(app)
        .downloader(new OkHttp3Downloader(client))
        .listener((picasso, uri, e) -> Timber.e(e, "Failed to load image: %s", uri))
        .build();
  }

  static OkHttpClient.Builder createOkHttpClient(Application app) {
    // Install an HTTP cache in the application cache directory.
    File cacheDir = new File(app.getCacheDir(), "http");
    Cache cache = new Cache(cacheDir, DISK_CACHE_SIZE);

    return new OkHttpClient.Builder()
        .cache(cache);
  }
}
