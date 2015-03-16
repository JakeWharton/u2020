package com.jakewharton.u2020.ui.trending;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import com.jakewharton.u2020.R;
import com.jakewharton.u2020.data.api.model.Repository;
import com.squareup.picasso.Picasso;
import java.util.Collections;
import java.util.List;
import rx.functions.Action1;

public final class TrendingAdapter extends RecyclerView.Adapter<TrendingAdapter.ViewHolder>
    implements Action1<List<Repository>> {
  private final Picasso picasso;

  private List<Repository> repositories = Collections.emptyList();

  public TrendingAdapter(Picasso picasso) {
    this.picasso = picasso;
  }

  @Override public void call(List<Repository> repositories) {
    this.repositories = repositories;
    notifyDataSetChanged();
  }

  @Override public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
    TrendingItemView view = (TrendingItemView) LayoutInflater.from(viewGroup.getContext())
        .inflate(R.layout.trending_view_repository, viewGroup, false);
    return new ViewHolder(view);
  }

  @Override public void onBindViewHolder(ViewHolder viewHolder, int i) {
    viewHolder.bindTo(repositories.get(i), picasso);
  }

  @Override public long getItemId(int position) {
    return position;
  }

  @Override public int getItemCount() {
    return repositories.size();
  }

  public static final class ViewHolder extends RecyclerView.ViewHolder {
    public final TrendingItemView itemView;
    public Repository repository;

    public ViewHolder(TrendingItemView itemView) {
      super(itemView);
      this.itemView = itemView;
    }

    public void bindTo(Repository repository, Picasso picasso) {
      this.repository = repository;
      itemView.bindTo(repository, picasso);
    }
  }
}
