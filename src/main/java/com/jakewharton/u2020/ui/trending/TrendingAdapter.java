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

final class TrendingAdapter extends RecyclerView.Adapter<TrendingAdapter.ViewHolder>
    implements Action1<List<Repository>> {
  public interface RepositoryClickListener {
    void onRepositoryClick(Repository repository);
  }

  private final Picasso picasso;
  private final RepositoryClickListener repositoryClickListener;

  private List<Repository> repositories = Collections.emptyList();

  public TrendingAdapter(Picasso picasso, RepositoryClickListener repositoryClickListener) {
    this.picasso = picasso;
    this.repositoryClickListener = repositoryClickListener;
    setHasStableIds(true);
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
    viewHolder.bindTo(repositories.get(i));
  }

  @Override public long getItemId(int position) {
    return position;
  }

  @Override public int getItemCount() {
    return repositories.size();
  }

  public final class ViewHolder extends RecyclerView.ViewHolder {
    public final TrendingItemView itemView;

    public ViewHolder(TrendingItemView itemView) {
      super(itemView);
      this.itemView = itemView;
      this.itemView.setOnClickListener(v -> {
        Repository repository = repositories.get(getAdapterPosition());
        repositoryClickListener.onRepositoryClick(repository);
      });
    }

    public void bindTo(Repository repository) {
      itemView.bindTo(repository, picasso);
    }
  }
}
