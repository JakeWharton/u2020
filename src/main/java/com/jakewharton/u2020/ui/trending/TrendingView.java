package com.jakewharton.u2020.ui.trending;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.os.Build;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.AttributeSet;
import android.view.ContextThemeWrapper;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import butterknife.Bind;
import butterknife.BindDimen;
import butterknife.ButterKnife;
import butterknife.OnItemSelected;
import com.jakewharton.u2020.R;
import com.jakewharton.u2020.data.Funcs;
import com.jakewharton.u2020.data.Injector;
import com.jakewharton.u2020.data.IntentFactory;
import com.jakewharton.u2020.data.api.GithubService;
import com.jakewharton.u2020.data.api.Order;
import com.jakewharton.u2020.data.api.Results;
import com.jakewharton.u2020.data.api.SearchQuery;
import com.jakewharton.u2020.data.api.Sort;
import com.jakewharton.u2020.data.api.model.RepositoriesResponse;
import com.jakewharton.u2020.data.api.model.Repository;
import com.jakewharton.u2020.data.api.transforms.SearchResultToRepositoryList;
import com.jakewharton.u2020.ui.misc.BetterViewAnimator;
import com.jakewharton.u2020.ui.misc.DividerItemDecoration;
import com.jakewharton.u2020.ui.misc.EnumAdapter;
import com.jakewharton.u2020.util.Intents;
import com.squareup.picasso.Picasso;
import javax.inject.Inject;
import retrofit.Response;
import retrofit.Result;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;
import rx.subjects.PublishSubject;
import rx.subscriptions.CompositeSubscription;
import timber.log.Timber;

import static com.jakewharton.u2020.ui.misc.DividerItemDecoration.VERTICAL_LIST;

public final class TrendingView extends LinearLayout
    implements SwipeRefreshLayout.OnRefreshListener, TrendingAdapter.RepositoryClickListener {
  @Bind(R.id.trending_toolbar) Toolbar toolbarView;
  @Bind(R.id.trending_timespan) Spinner timespanView;
  @Bind(R.id.trending_animator) BetterViewAnimator animatorView;
  @Bind(R.id.trending_swipe_refresh) SwipeRefreshLayout swipeRefreshView;
  @Bind(R.id.trending_list) RecyclerView trendingView;
  @Bind(R.id.trending_loading_message) TextView loadingMessageView;

  @BindDimen(R.dimen.trending_divider_padding_start) float dividerPaddingStart;

  @Inject GithubService githubService;
  @Inject Picasso picasso;
  @Inject IntentFactory intentFactory;
  @Inject DrawerLayout drawerLayout;

  private final PublishSubject<TrendingTimespan> timespanSubject;
  private final EnumAdapter<TrendingTimespan> timespanAdapter;
  private final TrendingAdapter trendingAdapter;
  private final CompositeSubscription subscriptions = new CompositeSubscription();

  public TrendingView(Context context, AttributeSet attrs) {
    super(context, attrs);
    if (!isInEditMode()) {
      Injector.obtain(context).inject(this);
    }

    timespanSubject = PublishSubject.create();
    timespanAdapter = new TrendingTimespanAdapter(
        new ContextThemeWrapper(getContext(), R.style.Theme_U2020_TrendingTimespan));
    trendingAdapter = new TrendingAdapter(picasso, this);
  }

  @Override protected void onFinishInflate() {
    super.onFinishInflate();
    ButterKnife.bind(this);

    AnimationDrawable ellipsis =
        (AnimationDrawable) getResources().getDrawable(R.drawable.dancing_ellipsis);
    loadingMessageView.setCompoundDrawablesWithIntrinsicBounds(null, null, ellipsis, null);
    ellipsis.start();

    toolbarView.setNavigationIcon(R.drawable.menu_icon);
    toolbarView.setNavigationOnClickListener(v -> drawerLayout.openDrawer(GravityCompat.START));

    timespanView.setAdapter(timespanAdapter);
    timespanView.setSelection(TrendingTimespan.WEEK.ordinal());

    swipeRefreshView.setColorSchemeResources(R.color.accent);
    swipeRefreshView.setOnRefreshListener(this);

    trendingAdapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
      @Override public void onChanged() {
        animatorView.setDisplayedChildId(trendingAdapter.getItemCount() == 0 //
            ? R.id.trending_empty //
            : R.id.trending_swipe_refresh);
        swipeRefreshView.setRefreshing(false);
      }
    });

    trendingView.setLayoutManager(new LinearLayoutManager(getContext()));
    trendingView.addItemDecoration(
        new DividerItemDecoration(getContext(), VERTICAL_LIST, dividerPaddingStart, safeIsRtl()));
    trendingView.setAdapter(trendingAdapter);
  }

  @Override protected void onAttachedToWindow() {
    super.onAttachedToWindow();

    Observable<Result<RepositoriesResponse>> result = timespanSubject //
        .flatMap(trendingSearch) //
        .observeOn(AndroidSchedulers.mainThread()) //
        .share();
    subscriptions.add(result //
        .filter(Results.isSuccess()) //
        .map(SearchResultToRepositoryList.instance()) //
        .subscribe(trendingAdapter));
    subscriptions.add(result //
        .filter(Funcs.not(Results.isSuccess())) //
        .subscribe(trendingError));

    // Load the default selection.
    onRefresh();
  }

  private final Func1<TrendingTimespan, Observable<Result<RepositoriesResponse>>> trendingSearch =
      new Func1<TrendingTimespan, Observable<Result<RepositoriesResponse>>>() {
        @Override
        public Observable<Result<RepositoriesResponse>> call(TrendingTimespan trendingTimespan) {
          SearchQuery trendingQuery = new SearchQuery.Builder() //
              .createdSince(trendingTimespan.createdSince()) //
              .build();
          return githubService.repositories(trendingQuery, Sort.STARS, Order.DESC)
              .subscribeOn(Schedulers.io());
        }
      };

  private final Action1<Result<RepositoriesResponse>> trendingError = new Action1<Result<RepositoriesResponse>>() {
    @Override public void call(Result<RepositoriesResponse> result) {
      if (result.isError()) {
        Timber.e(result.error(), "Failed to get trending repositories");
      } else {
        Response<RepositoriesResponse> response = result.response();
        Timber.e("Failed to get trending repositories. Server returned " + response.code());
      }
      swipeRefreshView.setRefreshing(false);
      animatorView.setDisplayedChildId(R.id.trending_error);
    }
  };

  @Override protected void onDetachedFromWindow() {
    super.onDetachedFromWindow();
    subscriptions.unsubscribe();
  }

  @OnItemSelected(R.id.trending_timespan) void timespanSelected(final int position) {
    if (animatorView.getDisplayedChildId() != R.id.trending_swipe_refresh) {
      animatorView.setDisplayedChildId(R.id.trending_loading);
    }

    // For whatever reason, the SRL's spinner does not draw itself when we call setRefreshing(true)
    // unless it is posted.
    post(() -> {
      swipeRefreshView.setRefreshing(true);
      timespanSubject.onNext(timespanAdapter.getItem(position));
    });
  }

  @Override public void onRefresh() {
    timespanSelected(timespanView.getSelectedItemPosition());
  }

  @Override public void onRepositoryClick(Repository repository) {
    Intents.maybeStartActivity(getContext(), intentFactory.createUrlIntent(repository.html_url));
  }

  private boolean safeIsRtl() {
    return Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1 && isRtl();
  }

  @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1) private boolean isRtl() {
    return getLayoutDirection() == LAYOUT_DIRECTION_RTL;
  }
}
