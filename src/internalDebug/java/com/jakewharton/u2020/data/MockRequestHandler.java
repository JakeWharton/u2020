package com.jakewharton.u2020.data;

import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.SystemClock;
import android.util.LruCache;
import com.squareup.picasso.Downloader;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Request;
import com.squareup.picasso.RequestHandler;
import java.io.IOException;
import retrofit.mock.NetworkBehavior;

import static java.util.concurrent.TimeUnit.MILLISECONDS;

/**
 * A Picasso {@link Downloader} which loads images from assets but attempts to emulate the
 * subtleties of a real HTTP client and its disk cache.
 * <p>
 * Images <em>must</em> be in the form {@code mock:///path/to/asset.png}.
 */
public final class MockRequestHandler extends RequestHandler {
  private final NetworkBehavior behavior;
  private final AssetManager assetManager;

  /** Emulate the disk cache by storing the URLs in an LRU using its size as the value. */
  private final LruCache<String, Long> emulatedDiskCache =
      new LruCache<String, Long>(DataModule.DISK_CACHE_SIZE) {
        @Override protected int sizeOf(String key, Long value) {
          return (int) Math.min(value.longValue(), Integer.MAX_VALUE);
        }
      };

  public MockRequestHandler(NetworkBehavior behavior, AssetManager assetManager) {
    this.behavior = behavior;
    this.assetManager = assetManager;
  }

  @Override public boolean canHandleRequest(Request data) {
    return "mock".equals(data.uri.getScheme());
  }

  @Override public Result load(Request request, int networkPolicy) throws IOException {
    String imagePath = request.uri.getPath().substring(1); // Grab only the path sans leading slash.

    // Check the disk cache for the image. A non-null return value indicates a hit.
    boolean cacheHit = emulatedDiskCache.get(imagePath) != null;

    // If there's a hit, grab the image stream and return it.
    if (cacheHit) {
      return new Result(loadBitmap(imagePath), Picasso.LoadedFrom.DISK);
    }

    // If we are not allowed to hit the network and the cache missed return a big fat nothing.
    if (NetworkPolicy.isOfflineOnly(networkPolicy)) {
      return null;
    }

    // If we got this far there was a cache miss and hitting the network is required. See if we need
    // to fake an network error.
    if (behavior.calculateIsFailure()) {
      SystemClock.sleep(behavior.calculateDelay(MILLISECONDS));
      throw new IOException("Fake network error!");
    }

    // We aren't throwing a network error so fake a round trip delay.
    SystemClock.sleep(behavior.calculateDelay(MILLISECONDS));

    // Since we cache missed put it in the LRU.
    AssetFileDescriptor fileDescriptor = assetManager.openFd(imagePath);
    long size = fileDescriptor.getLength();
    fileDescriptor.close();

    emulatedDiskCache.put(imagePath, size);

    // Grab the image stream and return it.
    return new Result(loadBitmap(imagePath), Picasso.LoadedFrom.NETWORK);
  }

  Bitmap loadBitmap(String imagePath) throws IOException {
    return BitmapFactory.decodeStream(assetManager.open(imagePath));
  }
}
