package com.jakewharton.u2020.intentfactory;

import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.RestrictTo;
import android.support.v7.app.AppCompatActivity;
import android.text.SpannableStringBuilder;
import android.text.style.StyleSpan;
import android.util.Log;
import android.widget.TextView;
import java.lang.reflect.Field;
import java.util.Arrays;
import static android.support.annotation.RestrictTo.Scope.LIBRARY;

@RestrictTo(LIBRARY)
public final class CapturedIntentActivity extends AppCompatActivity {

    public static final String ACTION = "com.jakewharton.u2020.intent.EXTERNAL_INTENT";

    public static final String EXTRA_BASE_INTENT = "debug_base_intent";

    static Intent createIntent(Intent baseIntent) {
        Intent intent = new Intent();
        intent.setAction(ACTION);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra(EXTRA_BASE_INTENT, baseIntent);
        return intent;
    }

    private TextView actionView;

    private TextView dataView;

    private TextView extrasView;

    private TextView flagsView;

    private Intent baseIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        baseIntent = getIntent().getParcelableExtra(EXTRA_BASE_INTENT);
        setContentView(R.layout.captured_intent);
        actionView = findViewById(R.id.action);
        dataView = findViewById(R.id.data);
        extrasView = findViewById(R.id.extras);
        flagsView = findViewById(R.id.flags);
        findViewById(R.id.launch).setOnClickListener(view -> {
            startActivity(baseIntent);
            finish();
        });
        fillAction();
        fillData();
        fillExtras();
        fillFlags();
    }

    private void fillAction() {
        String action = baseIntent.getAction();
        actionView.setText(action == null ? "None!" : action);
    }

    private void fillData() {
        Uri data = baseIntent.getData();
        dataView.setText(data == null ? "None!" : data.toString());
    }

    private void fillExtras() {
        Bundle extras = baseIntent.getExtras();
        if (extras == null) {
            extrasView.setText("None!");
        } else {
            SpannableStringBuilder text = new SpannableStringBuilder();
            for (String key : extras.keySet()) {
                Object value = extras.get(key);
                String valueString;
                if (value.getClass().isArray()) {
                    valueString = Arrays.toString((Object[]) value);
                } else {
                    valueString = value.toString();
                }
                int start = text.length();
                text.append(key).append(":\n");
                text.setSpan(new StyleSpan(Typeface.BOLD), start, text.length(), 0);
                text.append(valueString).append("\n\n");
            }
            extrasView.setText(text);
        }
    }

    private void fillFlags() {
        int flags = baseIntent.getFlags();
        StringBuilder builder = new StringBuilder();
        for (Field field : Intent.class.getDeclaredFields()) {
            try {
                if (field.getName().startsWith("FLAG_") && field.getType() == Integer.TYPE && (flags & field.getInt(null)) != 0) {
                    builder.append(field.getName()).append('\n');
                }
            } catch (IllegalAccessException e) {
                Log.e("CapturedIntentActivity", "Couldn't read value for: " + field.getName(), e);
            }
        }
        flagsView.setText(builder.length() == 0 ? "None!" : builder.toString());
    }
}
