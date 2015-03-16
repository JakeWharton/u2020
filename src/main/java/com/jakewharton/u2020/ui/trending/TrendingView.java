package com.jakewharton.u2020.ui.trending;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.widget.LinearLayout;
import butterknife.ButterKnife;
import butterknife.InjectView;
import com.jakewharton.u2020.R;
import com.jakewharton.u2020.U2020App;
import com.jakewharton.u2020.data.api.GithubService;
import com.jakewharton.u2020.data.api.Order;
import com.jakewharton.u2020.data.api.SearchQuery;
import com.jakewharton.u2020.data.api.Sort;
import com.jakewharton.u2020.data.api.transforms.SearchResultToRepositoryList;
import com.jakewharton.u2020.ui.misc.BetterViewAnimator;
import com.jakewharton.u2020.ui.misc.DividerItemDecoration;
import com.jakewharton.u2020.util.ViewSubscriptions;
import com.squareup.picasso.Picasso;
import javax.inject.Inject;
import org.joda.time.DateTime;

import static com.jakewharton.u2020.ui.misc.DividerItemDecoration.VERTICAL_LIST;

public final class TrendingView extends LinearLayout {
  @InjectView(R.id.trending_animator) BetterViewAnimator animatorView;
  @InjectView(R.id.trending_list) RecyclerView trendingView;

  @Inject GithubService githubService;
  @Inject Picasso picasso;
  @Inject ViewSubscriptions subscriptions;

  private final float dividerPaddingStart;

  private final TrendingAdapter adapter;

  public TrendingView(Context context, AttributeSet attrs) {
    super(context, attrs);
    U2020App.get(context).inject(this);

    dividerPaddingStart =
        getResources().getDimensionPixelSize(R.dimen.trending_divider_padding_start);

    adapter = new TrendingAdapter(picasso);
  }

  @Override protected void onFinishInflate() {
    super.onFinishInflate();
    ButterKnife.inject(this);

    adapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
      @Override public void onChanged() {
        animatorView.setDisplayedChildId(R.id.trending_list);
      }
    });

    trendingView.setLayoutManager(new LinearLayoutManager(getContext()));
    trendingView.addItemDecoration(
        new DividerItemDecoration(getContext(), VERTICAL_LIST, dividerPaddingStart, safeIsRtl()));
    trendingView.setAdapter(adapter);
  }

  @Override protected void onAttachedToWindow() {
    super.onAttachedToWindow();

    DateTime lastWeek = DateTime.now().minusDays(7);
    SearchQuery trendingQuery = new SearchQuery.Builder().createdSince(lastWeek).build();

    subscriptions.add(githubService.repositories(trendingQuery, Sort.STARS, Order.DESC)
        .map(new SearchResultToRepositoryList()), adapter);
  }

  @Override protected void onDetachedFromWindow() {
    super.onDetachedFromWindow();
    subscriptions.unsubscribe();
  }

  private boolean safeIsRtl() {
    return Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1 && isRtl();
  }

  @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1) private boolean isRtl() {
    return getLayoutDirection() == LAYOUT_DIRECTION_RTL;
  }
}
