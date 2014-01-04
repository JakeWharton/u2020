package com.jakewharton.u2020.ui.gallery;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.ButterKnife;
import butterknife.InjectView;
import com.jakewharton.u2020.R;
import com.jakewharton.u2020.data.api.model.Image;
import com.jakewharton.u2020.ui.misc.BindableAdapter;
import com.squareup.picasso.Picasso;
import java.util.Collections;
import java.util.List;

public class GalleryAdapter extends BindableAdapter<Image> {
  private List<Image> images = Collections.emptyList();

  private final Picasso picasso;

  public GalleryAdapter(Context context, Picasso picasso) {
    super(context);
    this.picasso = picasso;
  }

  public void replaceWith(List<Image> images) {
    this.images = images;
    notifyDataSetChanged();
  }

  @Override public int getCount() {
    return images.size();
  }

  @Override public Image getItem(int position) {
    return images.get(position);
  }

  @Override public long getItemId(int position) {
    return position;
  }

  @Override public View newView(LayoutInflater inflater, int position, ViewGroup container) {
    View view = inflater.inflate(R.layout.gallery_view_image, container, false);
    view.setTag(new Holder(view));
    return view;
  }

  @Override public void bindView(Image item, int position, View view) {
    Holder holder = (Holder) view.getTag();

    holder.title.setText(item.title);

    picasso.load(item.link)
        .fit()
        .centerCrop()
        .into(holder.image);
  }

  static class Holder {
    @InjectView(R.id.gallery_image_image) ImageView image;
    @InjectView(R.id.gallery_image_title) TextView title;

    public Holder(View view) {
      ButterKnife.inject(this, view);
    }
  }
}
