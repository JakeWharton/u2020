package com.jakewharton.u2020.ui.trending;

import com.jakewharton.u2020.ui.debug.ContextualDebugActions.DebugAction;
import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public final class ScrollBottomTrendingDebugAction extends DebugAction<TrendingView> {

    @Inject
    public ScrollBottomTrendingDebugAction() {
        super(TrendingView.class);
    }

    @Override
    public String name() {
        return "Scroll to bottom";
    }

    @Override
    public void run(TrendingView view) {
        view.trendingView.smoothScrollToPosition(view.trendingView.getAdapter().getItemCount() - 1);
    }
}
