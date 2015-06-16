package com.jakewharton.u2020.ui.trending;

import android.content.Context;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import butterknife.ButterKnife;
import butterknife.InjectView;
import com.jakewharton.u2020.R;
import com.jakewharton.u2020.data.api.model.Repository;
import com.jakewharton.u2020.ui.misc.Truss;
import com.jakewharton.u2020.ui.transform.CircleStrokeTransformation;
import com.squareup.picasso.Picasso;

public final class TrendingItemView extends RelativeLayout {
  @InjectView(R.id.trending_repository_avatar) ImageView avatarView;
  @InjectView(R.id.trending_repository_name) TextView nameView;
  @InjectView(R.id.trending_repository_description) TextView descriptionView;
  @InjectView(R.id.trending_repository_stars) TextView starsView;
  @InjectView(R.id.trending_repository_forks) TextView forksView;

  private final CircleStrokeTransformation avatarTransformation;
  private final int descriptionColor;

  public TrendingItemView(Context context, AttributeSet attrs) {
    super(context, attrs);

    TypedValue outValue = new TypedValue();
    context.getTheme().resolveAttribute(android.R.attr.textColorSecondary, outValue, true);
    descriptionColor = outValue.data;

    // TODO: Make this a singleton.
    avatarTransformation =
        new CircleStrokeTransformation(context, getResources().getColor(R.color.avatar_stroke), 1);
  }

  @Override protected void onFinishInflate() {
    super.onFinishInflate();
    ButterKnife.inject(this);
  }

  public void bindTo(Repository repository, Picasso picasso) {
    picasso.load(repository.owner.avatarUrl)
        .placeholder(R.drawable.avatar)
        .fit()
        .transform(avatarTransformation)
        .into(avatarView);
    nameView.setText(repository.name);
    starsView.setText(String.valueOf(repository.stars));
    forksView.setText(String.valueOf(repository.forks));

    Truss description = new Truss();
    description.append(repository.owner.login);

    if (!TextUtils.isEmpty(repository.description)) {
      description.pushSpan(new ForegroundColorSpan(descriptionColor));
      description.append(" — ");
      description.append(repository.description);
      description.popSpan();
    }

    descriptionView.setText(description.build());
  }
}
