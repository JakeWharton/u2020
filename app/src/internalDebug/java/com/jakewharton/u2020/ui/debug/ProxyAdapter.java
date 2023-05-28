package com.jakewharton.u2020.ui.debug;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.f2prateek.rx.preferences.Preference;
import com.jakewharton.u2020.ui.misc.BindableAdapter;
import java.net.InetSocketAddress;

class ProxyAdapter extends BindableAdapter<String> {

    public static final int NONE = 0;

    public static final int PROXY = 1;

    private final Preference<InetSocketAddress> proxyAddress;

    ProxyAdapter(Context context, Preference<InetSocketAddress> proxyAddress) {
        super(context);
        if (proxyAddress == null) {
            throw new IllegalStateException("proxy == null");
        }
        this.proxyAddress = proxyAddress;
    }

    @Override
    public int getCount() {
        return 2 + /* "None" and "Set" */
        (proxyAddress.isSet() ? 1 : 0);
    }

    @Override
    public String getItem(int position) {
        if (position == 0) {
            return "None";
        }
        if (position == getCount() - 1) {
            return "Set…";
        }
        return proxyAddress.get().toString();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View newView(LayoutInflater inflater, int position, ViewGroup container) {
        return inflater.inflate(android.R.layout.simple_spinner_item, container, false);
    }

    @Override
    public void bindView(String item, int position, View view) {
        TextView tv = view.findViewById(android.R.id.text1);
        tv.setText(item);
    }

    @Override
    public View newDropDownView(LayoutInflater inflater, int position, ViewGroup container) {
        return inflater.inflate(android.R.layout.simple_spinner_dropdown_item, container, false);
    }
}
