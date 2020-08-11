package com.paula.android.bechef.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.paula.android.bechef.R;
import com.paula.android.bechef.detail.DetailContract;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class DetailAdapter extends RecyclerView.Adapter {
    private DetailContract.Presenter mDetailPresenter;
    private ArrayList<?> mDataArrayFirst;
    private ArrayList<?> mDataArraySecond;

    public DetailAdapter(ArrayList<?> dataArrayFirst, ArrayList<?> dataArraySecond, DetailContract.Presenter detailPresenter) {
        mDetailPresenter = detailPresenter;
        mDataArrayFirst = dataArrayFirst;
        mDataArraySecond = dataArraySecond;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == 2) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_detail_steps, parent, false);
            return new StepsViewHolder(view);
        } else if (viewType == 1) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_detail_materials, parent, false);
            return new MaterialsViewHolder(view);
        } else {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_detail_description, parent, false);
            return new DescriptionViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof DescriptionViewHolder) {
            ((DescriptionViewHolder) holder).mTvDescription.setText((String) mDataArrayFirst.get(0));
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (mDataArraySecond == null) {
            return 0; // from discover or bookmark
        } else {
            return position > mDataArrayFirst.size() - 1 ? 1 : 2; // from receipt
        }
    }

    @Override
    public int getItemCount() {
        if (mDataArraySecond == null) return 1;
        else return mDataArrayFirst.size() + mDataArraySecond.size();
    }

    private class DescriptionViewHolder extends RecyclerView.ViewHolder {
        private TextView mTvDescription;

        private DescriptionViewHolder(@NonNull View itemView) {
            super(itemView);
            mTvDescription = itemView.findViewById(R.id.textview_info_description);
        }
    }

    private class StepsViewHolder extends RecyclerView.ViewHolder {
        public StepsViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }

    private class MaterialsViewHolder extends RecyclerView.ViewHolder {
        public MaterialsViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}
