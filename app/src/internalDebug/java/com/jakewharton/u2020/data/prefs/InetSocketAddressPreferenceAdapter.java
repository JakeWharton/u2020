package com.jakewharton.u2020.data.prefs;

import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import com.f2prateek.rx.preferences.Preference;
import com.jakewharton.u2020.util.Strings;
import java.net.InetSocketAddress;
import java.net.Proxy;
import static java.net.Proxy.Type.HTTP;

public class InetSocketAddressPreferenceAdapter implements Preference.Adapter<InetSocketAddress> {

    public static final InetSocketAddressPreferenceAdapter INSTANCE = new InetSocketAddressPreferenceAdapter();

    InetSocketAddressPreferenceAdapter() {
    }

    @Override
    public void set(@NonNull String key, @NonNull InetSocketAddress address, @NonNull SharedPreferences.Editor editor) {
        String host = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
            host = address.getHostString();
        } else {
            host = address.getHostName();
        }
        int port = address.getPort();
        editor.putString(key, host + ":" + port);
    }

    @Override
    public InetSocketAddress get(@NonNull String key, @NonNull SharedPreferences preferences) {
        String value = preferences.getString(key, null);
        // Not called unless value is present.
        assert value != null;
        String[] parts = value.split(":", 2);
        String host = parts[0];
        int port = parts.length > 1 ? Integer.parseInt(parts[1]) : 80;
        return InetSocketAddress.createUnresolved(host, port);
    }

    @Nullable
    public static InetSocketAddress parse(@Nullable String value) {
        if (Strings.isBlank(value)) {
            return null;
        }
        String[] parts = value.split(":", 2);
        if (parts.length == 0) {
            return null;
        }
        String host = parts[0];
        int port = parts.length > 1 ? Integer.parseInt(parts[1]) : 80;
        return InetSocketAddress.createUnresolved(host, port);
    }

    @Nullable
    public static Proxy createProxy(@Nullable InetSocketAddress address) {
        if (address == null) {
            return null;
        }
        return new Proxy(HTTP, address);
    }
}
