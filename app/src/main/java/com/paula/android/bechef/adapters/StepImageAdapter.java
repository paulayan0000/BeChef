package com.paula.android.bechef.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.paula.android.bechef.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class StepImageAdapter extends RecyclerView.Adapter {
    private Context mContext;
    private ArrayList<String> mImageUrls;

    StepImageAdapter(ArrayList<String> imageUrls) {
        mImageUrls = imageUrls;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_detail_step_images, parent, false);
        return new StepImageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ((StepImageViewHolder) holder).bindView(mImageUrls.get(position));
    }

    @Override
    public int getItemCount() {
        return mImageUrls.size();
    }

    private class StepImageViewHolder extends RecyclerView.ViewHolder {
        private ImageView mIvStepImage;

        private StepImageViewHolder(View itemView) {
            super(itemView);
            mIvStepImage = itemView.findViewById(R.id.imageview_step_image);
        }

        void bindView(String imageUrl) {
            Picasso.with(mContext)
                    .load(imageUrl)
                    .error(R.drawable.all_picture_placeholder)
                    .placeholder(R.drawable.all_picture_placeholder)
                    .into(mIvStepImage);
        }
    }
}
