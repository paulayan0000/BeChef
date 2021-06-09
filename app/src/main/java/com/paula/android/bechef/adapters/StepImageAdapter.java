package com.paula.android.bechef.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.paula.android.bechef.R;
import com.paula.android.bechef.utils.Constants;
import com.paula.android.bechef.utils.EditCallback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class StepImageAdapter extends RecyclerView.Adapter<StepImageAdapter.StepImageViewHolder>
        implements ItemTouchHelperAdapter {
    private final ArrayList<String> mImageUrls;
    private final int mStepPos;
    private Context mContext;
    private EditCallback mCompleteCallback;

    // Constructor for step image of EditItemAdapter with various stepPos
    StepImageAdapter(ArrayList<String> imageUrls, int stepPos, EditCallback editCallback) {
        mImageUrls = imageUrls;
        mStepPos = stepPos;
        mCompleteCallback = editCallback;
        if (mImageUrls.size() == 0) mImageUrls.add("");
    }

    // Constructor for step image of DetailAdapter
    StepImageAdapter(ArrayList<String> imageUrls) {
        mImageUrls = imageUrls;
        mStepPos = Constants.STEP_POSITION_DETAIL;
    }

    @NonNull
    @Override
    public StepImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        return new StepImageViewHolder(LayoutInflater.from(mContext)
                .inflate(R.layout.item_step_images, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull StepImageViewHolder holder, int position) {
        holder.bindView(mImageUrls.get(position));
    }

    @Override
    public int getItemCount() {
        return mImageUrls.size();
    }

    @Override
    public ArrayList<String> getDataList() {
        return mImageUrls;
    }

    class StepImageViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private final ImageView mIvStepImage;

        private StepImageViewHolder(View itemView) {
            super(itemView);
            mIvStepImage = itemView.findViewById(R.id.imageview_step_image);
            itemView.setOnClickListener(this);
            if (mStepPos == Constants.STEP_POSITION_DETAIL) return;

            itemView.findViewById(R.id.imageview_step_image_forground).setVisibility(View.VISIBLE);
            ImageButton ibtnAdd = itemView.findViewById(R.id.imagebutton_add);
            ibtnAdd.setVisibility(View.VISIBLE);
            ibtnAdd.setOnClickListener(this);
        }

        void bindView(String imageUrl) {
            Picasso.with(mContext)
                    .load(imageUrl.isEmpty() ? null : imageUrl)
                    .error(R.drawable.all_picture_placeholder)
                    .placeholder(R.drawable.all_picture_placeholder)
                    .into(mIvStepImage);
        }

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            if (position < 0) return;
            if (v.getId() == R.id.imagebutton_add) {
                int newPos = position + 1;
                mImageUrls.add(newPos, "");
                notifyItemInserted(newPos);
                notifyItemRangeChanged(newPos, getItemCount() - newPos);
            } else {
                mCompleteCallback.onChooseImages(mStepPos, position);
            }
        }
    }

    public void notifyRemoved(int position) {
        mImageUrls.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, getItemCount() - position);
    }

    public void notifyChanged(int position, String urlString) {
        mImageUrls.set(position, urlString);
        notifyItemChanged(position);
    }
}