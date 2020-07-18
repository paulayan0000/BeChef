package com.paula.android.bechef.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.paula.android.bechef.R;
import com.paula.android.bechef.api.beans.GetSearchList;
import com.paula.android.bechef.objects.SearchItem;
import com.paula.android.bechef.utils.Constants;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class DiscoverChildAdapter extends RecyclerView.Adapter{

    private ArrayList<SearchItem> mArrayList;
    private String mNextPageToken;
    private Context mContext;

    public DiscoverChildAdapter(GetSearchList bean) {
        mArrayList = bean.getSearchItems();
        mNextPageToken = bean.getNextPageToken();
    }

    public void updateData(GetSearchList newBean) {
        if (!mNextPageToken.equals(newBean.getNextPageToken())) {
            mArrayList.addAll(newBean.getSearchItems());
            mNextPageToken = newBean.getNextPageToken();
            notifyDataSetChanged();
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        if (viewType == Constants.VIEWTYPE_NORMAL) {
            View view = LayoutInflater.from(mContext).inflate(R.layout.item_discover_recycler, parent, false);
            return new DiscoverViewHolder(view);
        } else {
            View view = LayoutInflater.from(mContext).inflate(R.layout.item_all_loading, parent, false);
            return new LoadingViewHolder(view);
        }
    }

    @Override
    public int getItemViewType(int position) {
        return (position < mArrayList.size()) ? Constants.VIEWTYPE_NORMAL : Constants.VIEWTYPE_LOADING;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, final int position) {
        if (mArrayList.size() > 0) {
            if (holder instanceof DiscoverChildAdapter.DiscoverViewHolder) {
                DiscoverViewHolder viewHolder = (DiscoverViewHolder) holder;
                viewHolder.getTvVideoTitle().setText(mArrayList.get(position).getSnippet().getTitle());
                viewHolder.getTvVideoDescription().setText(mArrayList.get(position).getSnippet().getDescription());
                viewHolder.getTvVideoTime().setText(mArrayList.get(position).getSnippet().getPublishedAt());

                Picasso.with(mContext)
                        .load(mArrayList.get(position).getSnippet().getThumbnailMediumUrl())
                        .error(R.drawable.all_picture_placeholder)
                        .placeholder(R.drawable.all_picture_placeholder)
                        .into(viewHolder.getIvThumbnail());
                viewHolder.getIvThumbnail().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(mContext, mArrayList.get(position).getId(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }
    }

    @Override
    public int getItemCount() {
        if (mArrayList.size() == 0) return 1;
        return !"".equals(mNextPageToken) ? mArrayList.size() + 1 : mArrayList.size();
    }

    private class DiscoverViewHolder extends RecyclerView.ViewHolder {

        private ImageView mIvThumbnail;
        private TextView mTvVideoTitle;
        private TextView mTvVideoDescription;
        private TextView mTvVideoTime;

        private DiscoverViewHolder(View itemView) {
            super(itemView);

            mIvThumbnail = itemView.findViewById(R.id.imageview_thumbnail);
            mTvVideoTitle = itemView.findViewById(R.id.textview_video_title);
            mTvVideoDescription = itemView.findViewById(R.id.textview_video_description);
            mTvVideoTime = itemView.findViewById(R.id.textview_video_time);
        }

        private ImageView getIvThumbnail() {
            return mIvThumbnail;
        }

        private TextView getTvVideoTitle() {
            return mTvVideoTitle;
        }

        private TextView getTvVideoDescription() {
            return mTvVideoDescription;
        }

        private TextView getTvVideoTime() {
            return mTvVideoTime;
        }
    }

    private class LoadingViewHolder extends RecyclerView.ViewHolder {
        private LoadingViewHolder(View itemView) {
            super(itemView);
        }
    }
}
