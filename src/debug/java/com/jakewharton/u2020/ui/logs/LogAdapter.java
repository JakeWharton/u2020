package com.jakewharton.u2020.ui.logs;

import android.content.Context;
import android.support.annotation.ColorRes;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import butterknife.ButterKnife;
import butterknife.InjectView;
import com.jakewharton.u2020.R;
import com.jakewharton.u2020.ui.misc.BindableAdapter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import rx.functions.Action1;

import static com.jakewharton.u2020.data.LumberYard.Entry;

final class LogAdapter extends BindableAdapter<Entry> implements Action1<Entry> {
  private List<Entry> logs;

  public LogAdapter(Context context) {
    super(context);
    logs = Collections.emptyList();
  }

  public void setLogs(List<Entry> logs) {
    this.logs = new ArrayList<>(logs);
  }

  @Override public void call(Entry entry) {
    logs.add(entry);
    notifyDataSetChanged();
  }

  @Override public int getCount() {
    return logs.size();
  }

  @Override public Entry getItem(int i) {
    return logs.get(i);
  }

  @Override public long getItemId(int i) {
    return i;
  }

  @Override public View newView(LayoutInflater inflater, int position, ViewGroup container) {
    View view = inflater.inflate(R.layout.debug_logs_list_item, container, false);
    LogItemViewHolder viewHolder = new LogItemViewHolder(view);
    view.setTag(viewHolder);
    return view;
  }

  @Override public void bindView(Entry item, int position, View view) {
    LogItemViewHolder viewHolder = (LogItemViewHolder) view.getTag();
    viewHolder.setEntry(item);
  }

  static final class LogItemViewHolder {
    private final View rootView;
    @InjectView(R.id.debug_log_level) TextView levelView;
    @InjectView(R.id.debug_log_tag) TextView tagView;
    @InjectView(R.id.debug_log_message) TextView messageView;

    public LogItemViewHolder(View rootView) {
      this.rootView = rootView;
      ButterKnife.inject(this, rootView);
    }

    public void setEntry(Entry entry) {
      LogLevelStyle levelStyle = LogLevelStyle.fromLogLevel(entry.level);
      rootView.setBackgroundResource(levelStyle.backgroundColorResId);
      levelView.setText(levelStyle.letter);
      tagView.setText(entry.tag);
      messageView.setText(entry.message);
    }
  }

  public enum LogLevelStyle {
    VERBOSE("V", R.color.debug_log_accent_debug),
    DEBUG("D", R.color.debug_log_accent_debug),
    INFO("I", R.color.debug_log_accent_info),
    WARN("W", R.color.debug_log_accent_warn),
    ERROR("E", R.color.debug_log_accent_error),
    ASSERT("A", R.color.debug_log_accent_error),
    UNKNOWN("?", R.color.debug_log_accent_unknown);

    public final String letter;
    public final int backgroundColorResId;

    LogLevelStyle(String letter, @ColorRes int backgroundColorResId) {
      this.letter = letter;
      this.backgroundColorResId = backgroundColorResId;
    }

    public static LogLevelStyle fromLogLevel(int level) {
      switch (level) {
        case Log.VERBOSE:
          return VERBOSE;
        case Log.DEBUG:
          return DEBUG;
        case Log.INFO:
          return INFO;
        case Log.WARN:
          return WARN;
        case Log.ERROR:
          return ERROR;
        case Log.ASSERT:
          return ASSERT;
        default:
          return UNKNOWN;
      }
    }
  }
}
