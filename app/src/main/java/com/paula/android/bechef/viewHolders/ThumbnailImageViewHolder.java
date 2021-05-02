package com.paula.android.bechef.viewHolders;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.paula.android.bechef.R;
import com.paula.android.bechef.utils.Constants;
import com.paula.android.bechef.utils.EditCallback;
import com.squareup.picasso.Picasso;

public class ThumbnailImageViewHolder extends RecyclerView.ViewHolder {
    private final ImageView mIvThumbnail;

    // Constructor for Detail and EditItemAdapter of bookmark
    public ThumbnailImageViewHolder(@NonNull View itemView) {
        super(itemView);
        mIvThumbnail = itemView.findViewById(R.id.imageview_thumbnail);
    }

    // Constructor for EditItemAdapter of recipe
    public ThumbnailImageViewHolder(@NonNull View itemView, final EditCallback mEditCallback) {
        this(itemView);
        itemView.findViewById(R.id.imageview_thumbnail_foreground).setVisibility(View.VISIBLE);
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mEditCallback.onChooseImages(Constants.VIEW_TYPE_IMAGE, Constants.VIEW_TYPE_IMAGE);
            }
        });
    }

    public void bindView(Context context, String imageUrl) {
        Picasso.with(context)
                .load(imageUrl.isEmpty() ? null : imageUrl)
                .error(R.drawable.all_picture_placeholder)
                .placeholder(R.drawable.all_picture_placeholder)
                .into(mIvThumbnail);
    }
}
