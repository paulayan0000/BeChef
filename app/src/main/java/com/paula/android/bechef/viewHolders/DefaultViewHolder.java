package com.paula.android.bechef.viewHolders;

import android.content.Context;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.paula.android.bechef.R;
import com.paula.android.bechef.data.entity.BaseItem;
import com.paula.android.bechef.data.entity.DiscoverItem;
import com.squareup.picasso.Picasso;

public class DefaultViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    private final TextView mTvItemTitle, mTvTags, mTvTimeRating;
    private final ImageView mIvImage;
    protected ImageButton mIbtnChoose, mIbtnEdit;

    public DefaultViewHolder(@NonNull View itemView) {
        super(itemView);
        mTvItemTitle = itemView.findViewById(R.id.textview_title);
        mTvTags = itemView.findViewById(R.id.textview_tags);
        mTvTimeRating = itemView.findViewById(R.id.textview_time_and_rating);
        mIvImage = itemView.findViewById(R.id.imageview_thumbnail);
        itemView.setOnClickListener(this);
    }

    public void bindView(Context context, BaseItem baseItem) {
        if (baseItem instanceof DiscoverItem) {
            mTvTimeRating.setText(baseItem.getCreatedTime());
        } else {
            mTvTimeRating.setText(String.format(context.getString(R.string.time_and_rating),
                    baseItem.getCreatedTime(),
                    baseItem.getRating()));
        }

        mTvItemTitle.setText(baseItem.getTitle());
        if ("".equals(baseItem.getTags())) {
            mTvTags.setVisibility(View.GONE);
        } else {
            mTvTags.setVisibility(View.VISIBLE);
            mTvTags.setText(baseItem.getTags());
        }

        String imageUrl = baseItem.getImageUrl();
        Picasso.with(itemView.getContext())
                .load(imageUrl.isEmpty() ? null : imageUrl)
                .error(R.drawable.all_picture_placeholder)
                .placeholder(R.drawable.all_picture_placeholder)
                .into(mIvImage);
    }

    @Override
    public void onClick(View v) {
        int position = getAdapterPosition();
        if (position < 0) return;
    }
}
