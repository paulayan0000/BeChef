package com.paula.android.bechef.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import jp.wasabeef.picasso.transformations.RoundedCornersTransformation;

import com.paula.android.bechef.BeChef;
import com.paula.android.bechef.ChildContract;
import com.paula.android.bechef.R;
import com.paula.android.bechef.api.beans.YouTubeData;
import com.paula.android.bechef.data.entity.DiscoverItem;
import com.paula.android.bechef.discoverChild.DiscoverChildPresenter;
import com.paula.android.bechef.utils.Constants;
import com.paula.android.bechef.utils.Utils;
import com.paula.android.bechef.viewHolders.NoResultViewHolder;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import java.util.ArrayList;

public class DiscoverChildAdapter extends RecyclerView.Adapter {
    private final ChildContract.ChildPresenter mPresenter;
    private final ArrayList<DiscoverItem> mDiscoverItems;
    private final Transformation mTransformation;
    private boolean mIsLoading;
    private Context mContext;
    private String mNextPageToken;
    private String mErrorMsg = "";

    public DiscoverChildAdapter(YouTubeData bean, ChildContract.ChildPresenter presenter) {
        mDiscoverItems = bean.getDiscoverItems();
        mNextPageToken = bean.getNextPageToken();
        mPresenter = presenter;
        mIsLoading = false;
        int radius = BeChef.getAppContext().getResources()
                .getDimensionPixelOffset(R.dimen.cardview_radius);
        mTransformation = new RoundedCornersTransformation(radius, 0);
    }

    public void updateData(Context context, YouTubeData newBean) {
        mIsLoading = false;
        mErrorMsg = newBean.getErrorMsg();
        if ("".equals(mErrorMsg)) {
            int oldDiscoverItemCount = mDiscoverItems.size();
            mDiscoverItems.addAll(newBean.getDiscoverItems());
            mNextPageToken = newBean.getNextPageToken();
            notifyItemRangeChanged(oldDiscoverItemCount, newBean.getDiscoverItems().size());
        } else if (mDiscoverItems.size() > 0) {
            // Toast error message if there are already some items loaded
            Toast.makeText(context, BeChef.getAppContext().getString(R.string.adapter_error), Toast.LENGTH_SHORT).show();
        } else {
            notifyDataSetChanged();
        }
    }

    public void clearData() {
        mErrorMsg = "";
        mDiscoverItems.clear();
        mNextPageToken = "";
        mIsLoading = false;
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
        switch (viewType) {
            case Constants.VIEW_TYPE_NO_RESULT:
                view = LayoutInflater.from(mContext)
                        .inflate(R.layout.item_search_no_result, parent, false);
                return new NoResultViewHolder(view, mErrorMsg);
            case Constants.VIEW_TYPE_NORMAL:
                view = LayoutInflater.from(mContext)
                        .inflate(R.layout.item_discover_child_recycler, parent, false);
                return new DiscoverViewHolder(view);
            default:
                view = LayoutInflater.from(mContext)
                        .inflate(R.layout.item_loading, parent, false);
                return new RecyclerView.ViewHolder(view) {
                };
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (mDiscoverItems.size() == 0 && !mIsLoading) return Constants.VIEW_TYPE_NO_RESULT;
        return (position < mDiscoverItems.size()) ? Constants.VIEW_TYPE_NORMAL
                : Constants.VIEW_TYPE_LOADING;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder.getItemViewType() == Constants.VIEW_TYPE_NORMAL) {
            ((DiscoverViewHolder) holder).bindView(mDiscoverItems.get(position));
        }
    }

    @Override
    public int getItemCount() {
        if (mDiscoverItems.size() == 0) return 1;
        return !"".equals(mNextPageToken) ? mDiscoverItems.size() + 1 : mDiscoverItems.size();
    }

    public void showLoading() {
        mIsLoading = true;
        notifyDataSetChanged();
    }

    private class DiscoverViewHolder extends RecyclerView.ViewHolder {
        private final ImageView mIvThumbnail;
        private final TextView mTvVideoTitle, mTvVideoTime;

        private DiscoverViewHolder(View itemView) {
            super(itemView);
            mIvThumbnail = itemView.findViewById(R.id.imageview_thumbnail);
            mTvVideoTitle = itemView.findViewById(R.id.textview_video_title);
            mTvVideoTime = itemView.findViewById(R.id.textview_video_time);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if (position < 0) return;
                    if (mPresenter instanceof DiscoverChildPresenter) {
                        mPresenter.openDetail(mDiscoverItems.get(position).getVideoId(),
                                true);
                    } else {
                        mPresenter.openDetail(mDiscoverItems.get(position), true);
                    }
                }
            });
        }

        void bindView(DiscoverItem discoverItem) {
            mTvVideoTitle.setText(discoverItem.getTitle());
            mTvVideoTime.setText(Utils.getRelevantTime(discoverItem.getPublishedAt()));

            Picasso.with(mContext)
                    .load(discoverItem.getImageUrl())
                    .error(R.drawable.all_picture_placeholder)
                    .placeholder(R.drawable.all_picture_placeholder)
                    .transform(mTransformation)
                    .into(mIvThumbnail);
        }
    }
}