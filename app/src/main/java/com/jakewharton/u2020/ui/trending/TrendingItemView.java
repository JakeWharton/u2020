package com.jakewharton.u2020.ui.trending;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.jakewharton.u2020.R;
import com.jakewharton.u2020.data.api.model.Repository;
import com.jakewharton.u2020.ui.misc.Truss;
import com.jakewharton.u2020.ui.transform.CircleStrokeTransformation;
import com.squareup.picasso.Picasso;

public final class TrendingItemView extends RelativeLayout {

    @BindView(R.id.trending_repository_avatar)
    ImageView avatarView;

    @BindView(R.id.trending_repository_name)
    TextView nameView;

    @BindView(R.id.trending_repository_description)
    TextView descriptionView;

    @BindView(R.id.trending_repository_stars)
    TextView starsView;

    @BindView(R.id.trending_repository_forks)
    TextView forksView;

    private final CircleStrokeTransformation avatarTransformation;

    private final int descriptionColor;

    public TrendingItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedValue outValue = new TypedValue();
        context.getTheme().resolveAttribute(android.R.attr.textColorSecondary, outValue, true);
        descriptionColor = outValue.data;
        // TODO: Make this a singleton.
        avatarTransformation = new CircleStrokeTransformation(context, ContextCompat.getColor(context, R.color.avatar_stroke), 1);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        ButterKnife.bind(this);
    }

    public void bindTo(Repository repository, Picasso picasso) {
        picasso.load(repository.owner.avatar_url).placeholder(R.drawable.avatar).fit().transform(avatarTransformation).into(avatarView);
        nameView.setText(repository.name);
        starsView.setText(String.valueOf(repository.watchers));
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
