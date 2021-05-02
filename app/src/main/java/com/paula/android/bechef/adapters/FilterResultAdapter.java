package com.paula.android.bechef.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.paula.android.bechef.R;
import com.paula.android.bechef.data.entity.BaseItem;
import com.paula.android.bechef.find.FindPresenter;
import com.paula.android.bechef.utils.Constants;
import com.paula.android.bechef.viewHolders.DefaultViewHolder;

import java.util.ArrayList;

public class FilterResultAdapter extends RecyclerView.Adapter {
    private final FindPresenter mFindPresenter;
    private Context mContext;
    private ArrayList<BaseItem> mBaseItems;
    private boolean mLoading = false;

    public FilterResultAdapter(ArrayList<BaseItem> baseItems, FindPresenter findPresenter) {
        mBaseItems = baseItems;
        mFindPresenter = findPresenter;
    }

    public void showLoading() {
        mLoading = true;
        mBaseItems.clear();
        notifyDataSetChanged();
    }

    public void updateData(ArrayList<BaseItem> baseItems) {
        mBaseItems = baseItems;
        mLoading = false;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        if (viewType == Constants.VIEW_TYPE_NORMAL) {
            View view = LayoutInflater.from(mContext)
                    .inflate(R.layout.item_custom_child_recycler, parent, false);
            return new FilterResultViewHolder(view);
        }
        int viewResource;
        if (viewType == Constants.VIEW_TYPE_NO_RESULT) {
            viewResource = R.layout.item_search_no_result;
        } else {
            viewResource = R.layout.item_loading;
        }
        return new RecyclerView.ViewHolder(LayoutInflater.from(mContext)
                .inflate(viewResource, parent, false)) {};
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder.getItemViewType() == Constants.VIEW_TYPE_NORMAL) {
            ((FilterResultViewHolder) holder).bindView(mContext, mBaseItems.get(position));
        }
    }

    @Override
    public int getItemCount() {
        int itemCount = mBaseItems.size();
        return itemCount == 0 ? 1 : itemCount;
    }

    @Override
    public int getItemViewType(int position) {
        if (mLoading) return Constants.VIEW_TYPE_LOADING;
        if (mBaseItems.size() > 0) return Constants.VIEW_TYPE_NORMAL;
        return Constants.VIEW_TYPE_NO_RESULT;
    }

    private class FilterResultViewHolder extends DefaultViewHolder {

        public FilterResultViewHolder(@NonNull View itemView) {
            super(itemView);
            itemView.findViewById(R.id.imagebutton_edit).setVisibility(View.GONE);
        }

        @Override
        public void onClick(View v) {
            super.onClick(v);
            mFindPresenter.openDetail(mBaseItems.get(getAdapterPosition()), false);
        }
    }
}
