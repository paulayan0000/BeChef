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
import java.util.Collections;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import static androidx.recyclerview.widget.ItemTouchHelper.Callback.makeMovementFlags;

public class StepImageAdapter extends RecyclerView.Adapter implements ItemTouchHelperAdapter {
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

    @Override
    public boolean onItemMoved(int fromPosition, int toPosition) {
        if (fromPosition < toPosition) {
            for (int i = fromPosition; i < toPosition; i++) {
                Collections.swap(mImageUrls, i, i + 1);
            }
        } else {
            for (int i = fromPosition; i > toPosition; i--) {
                Collections.swap(mImageUrls, i, i - 1);
            }
        }
        notifyItemMoved(fromPosition, toPosition);
        return true;
    }

    @Override
    public void onItemSwiped(int position) {

    }

    @Override
    public int getItemMovementFlags() {
        if (mImageUrls.size() <= 1)
            return makeMovementFlags(ItemTouchHelper.ACTION_STATE_IDLE, ItemTouchHelper.ACTION_STATE_IDLE);
        else
            return makeMovementFlags(ItemTouchHelper.UP | ItemTouchHelper.DOWN,
                    ItemTouchHelper.ACTION_STATE_IDLE);
    }

    private class StepImageViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private ImageView mIvStepImage;
        private ImageButton mIbtnAdd;

        private StepImageViewHolder(View itemView) {
            super(itemView);
            mIvStepImage = itemView.findViewById(R.id.imageview_step_image);
            ImageView ivForeground = itemView.findViewById(R.id.imageview_step_image_forground);
            if (mViewType != -1) {
                itemView.setOnClickListener(this);
                ivForeground.setVisibility(View.VISIBLE);
            }
            mIbtnAdd = itemView.findViewById(R.id.imagebutton_add);
            itemView.findViewById(R.id.imagebutton_add).setOnClickListener(this);
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

        @Override
        public void onClick(View v) {
            if (v.getId() == R.id.imagebutton_add) {
                int position = getAdapterPosition();
                mImageUrls.add(position + 1, "");
                notifyItemInserted(position + 1);
                notifyItemRangeChanged(position + 1, getItemCount() - position - 1);
            } else {
                mCompleteCallback.onChooseImages(mViewType, getAdapterPosition());
            }

        }
    }
}
