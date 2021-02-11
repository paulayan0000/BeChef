package com.paula.android.bechef.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.paula.android.bechef.ChildContract;
import com.paula.android.bechef.R;
import com.paula.android.bechef.api.beans.GetSearchList;
import com.paula.android.bechef.data.entity.DiscoverItem;
import com.paula.android.bechef.discoverChild.DiscoverChildPresenter;
import com.paula.android.bechef.utils.Constants;
import com.paula.android.bechef.utils.Utils;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class DiscoverChildAdapter extends RecyclerView.Adapter {
    private ChildContract.ChildPresenter mPresenter;
    private ArrayList<DiscoverItem> mDiscoverItems;
    private String mNextPageToken;
    private Context mContext;
    private boolean mIsLoading;
    private String mErrorMsg = "";

    public DiscoverChildAdapter(GetSearchList bean, ChildContract.ChildPresenter presenter) {
        mDiscoverItems = bean.getDiscoverItems();
        mNextPageToken = bean.getNextPageToken();
        mPresenter = presenter;
        mIsLoading = false;
    }

    public void updateData(GetSearchList newBean) {
        mErrorMsg = newBean.getErrorMsg();

//        if (!mNextPageToken.equals(newBean.getNextPageToken())) {
            mDiscoverItems.addAll(newBean.getDiscoverItems());
            mNextPageToken = newBean.getNextPageToken();
            notifyItemRangeInserted(getItemCount(), newBean.getDiscoverItems().size());
//        }
    }

    public void clearData(){
        mErrorMsg = "";
        mDiscoverItems.clear();
        mNextPageToken = "";
        notifyDataSetChanged();
    }

    public int getBaseItemCounts() {
        return mDiscoverItems.size();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        View view;
        if (viewType == Constants.VIEW_TYPE_NO_RESULT) {
            view = LayoutInflater.from(mContext).inflate(R.layout.item_search_no_result, parent, false);
            return new NoResultViewHolder(view);
        } else if (viewType == Constants.VIEW_TYPE_NORMAL) {
            view = LayoutInflater.from(mContext).inflate(R.layout.item_discover_recycler, parent, false);
            return new DiscoverViewHolder(view);
        } else {
            view = LayoutInflater.from(mContext).inflate(R.layout.item_all_loading, parent, false);
            return new LoadingViewHolder(view);
        }
    }

    @Override
    public int getItemViewType(int position) {
//        return (mIsLoading ? Constants.VIEW_TYPE_LOADING : Constants.VIEW_TYPE_NORMAL);
//        return (position < mDiscoverItems.size()) ? Constants.VIEW_TYPE_NORMAL : Constants.VIEW_TYPE_LOADING;
        if (mDiscoverItems.size() == 0 && !mIsLoading) return Constants.VIEW_TYPE_NO_RESULT;
        return (position < mDiscoverItems.size()) ? Constants.VIEW_TYPE_NORMAL : Constants.VIEW_TYPE_LOADING;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (mDiscoverItems.size() > 0 && holder instanceof DiscoverChildAdapter.DiscoverViewHolder) {
            ((DiscoverViewHolder) holder).bindView(mDiscoverItems.get(position));
        } else if (holder instanceof NoResultViewHolder){
            ((NoResultViewHolder) holder).bindView();
        }
    }

    @Override
    public int getItemCount() {
//        if (mDiscoverItems.size() == 0) return mIsLoading ? 1 : 0;
        if (mDiscoverItems.size() == 0) return 1;
        return !"".equals(mNextPageToken) ? mDiscoverItems.size() + 1 : mDiscoverItems.size();
    }

    public void setLoading(boolean loading) {
        mIsLoading = loading;
        notifyDataSetChanged();
    }

    private class DiscoverViewHolder extends RecyclerView.ViewHolder {
        private ImageView mIvThumbnail;
        private TextView mTvVideoTitle;
        private TextView mTvVideoTime;

        private DiscoverViewHolder(View itemView) {
            super(itemView);
            mIvThumbnail = itemView.findViewById(R.id.imageview_thumbnail);
            mTvVideoTitle = itemView.findViewById(R.id.textview_video_title);
            mTvVideoTime = itemView.findViewById(R.id.textview_video_time);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (getAdapterPosition() < 0) return;
                    if (mPresenter instanceof DiscoverChildPresenter)
                        mPresenter.openDetail(mDiscoverItems.get(getAdapterPosition()).getVideoId(), true);
                    else
                        mPresenter.openDetail(mDiscoverItems.get(getAdapterPosition()), true);
                }
            });
        }

        void bindView(final DiscoverItem discoverItem) {
            mTvVideoTitle.setText(discoverItem.getTitle());
            mTvVideoTime.setText(Utils.getCreatedTime(discoverItem.getPublishedAt()));

            Picasso.with(mContext)
                    .load(discoverItem.getImageUrl())
                    .error(R.drawable.all_picture_placeholder)
                    .placeholder(R.drawable.all_picture_placeholder)
                    .into(mIvThumbnail);
        }
    }

    private class LoadingViewHolder extends RecyclerView.ViewHolder {
        private LoadingViewHolder(View itemView) {
            super(itemView);
        }
    }

    private class NoResultViewHolder extends RecyclerView.ViewHolder {
        private TextView mTvNoResult;
        private NoResultViewHolder(@NonNull View itemView) {
            super(itemView);
            mTvNoResult = itemView.findViewById(R.id.textview_no_result);
        }
        public void bindView() {
            mTvNoResult.setText(mErrorMsg);
        }
    }
}
