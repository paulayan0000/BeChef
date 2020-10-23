package com.paula.android.bechef.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.paula.android.bechef.R;
import com.paula.android.bechef.dialog.EditCompleteCallback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class StepImageAdapter extends RecyclerView.Adapter {
    private Context mContext;
    private ArrayList<String> mImageUrls;
    private int mViewType;
    private EditCompleteCallback mCompleteCallback;

    StepImageAdapter(ArrayList<String> imageUrls, int viewType, EditCompleteCallback completeCallback) {
        mImageUrls = imageUrls;
        mViewType = viewType;
        if (imageUrls.size() == 0) imageUrls.add("");
        mCompleteCallback = completeCallback;
    }

    StepImageAdapter(ArrayList<String> imageUrls) {
        mImageUrls = imageUrls;
        mViewType = -1;
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

    @Override
    public int getItemViewType(int position) {
        return mViewType;
    }

    private class StepImageViewHolder extends RecyclerView.ViewHolder {
        private ImageView mIvStepImage;
        private ImageButton mIbtnAdd;

        private StepImageViewHolder(View itemView) {
            super(itemView);
            mIvStepImage = itemView.findViewById(R.id.imageview_step_image);
            ImageView ivForeground = itemView.findViewById(R.id.imageview_step_image_forground);
            if (mViewType != -1) {
                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mCompleteCallback.onChooseImages(mViewType, getAdapterPosition());
                    }
                });
                ivForeground.setVisibility(View.VISIBLE);
            }
            mIbtnAdd = itemView.findViewById(R.id.imagebutton_add);
            itemView.findViewById(R.id.imagebutton_add).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    mImageUrls.add(position + 1, "");
                    notifyItemInserted(position + 1);
                    notifyItemRangeChanged(position + 1, getItemCount() - position - 1);
                }
            });
        }

        void bindView(String imageUrl) {
            if (mViewType != -1) {
                mIbtnAdd.setVisibility(View.VISIBLE);
            }
            Picasso.with(mContext)
                    .load(imageUrl.isEmpty() ? null : imageUrl)
                    .error(R.drawable.all_picture_placeholder)
                    .placeholder(R.drawable.all_picture_placeholder)
                    .into(mIvStepImage);
        }
    }
}
