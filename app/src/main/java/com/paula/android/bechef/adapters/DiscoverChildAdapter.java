package com.paula.android.bechef.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.paula.android.bechef.R;
import com.paula.android.bechef.api.beans.GetSearchList;
import com.paula.android.bechef.discoverChild.DiscoverChildFragmentContract;
import com.paula.android.bechef.data.DiscoverItem;
import com.paula.android.bechef.utils.Constants;
import com.paula.android.bechef.utils.Utils;
import com.squareup.picasso.Picasso;
import java.util.ArrayList;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class DiscoverChildAdapter extends RecyclerView.Adapter{
    private DiscoverChildFragmentContract.Presenter mPresenter;
    private ArrayList<DiscoverItem> mArrayList;
    private String mNextPageToken;
    private Context mContext;

    public DiscoverChildAdapter(GetSearchList bean, DiscoverChildFragmentContract.Presenter presenter) {
        mArrayList = bean.getDiscoverItems();
        mNextPageToken = bean.getNextPageToken();
        mPresenter = presenter;
    }

    public void updateData(GetSearchList newBean) {
        if (!mNextPageToken.equals(newBean.getNextPageToken())) {
            mArrayList.addAll(newBean.getDiscoverItems());
            mNextPageToken = newBean.getNextPageToken();

            notifyItemRangeInserted(getItemCount(), newBean.getDiscoverItems().size());
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
                viewHolder.mTvVideoTitle.setText(mArrayList.get(position).getTitle());
                viewHolder.mTvVideoTime.setText(Utils.getCreatedTime(mArrayList.get(position).getPublishedAt()));

                Picasso.with(mContext)
                        .load(mArrayList.get(position).getImageUrl())
                        .error(R.drawable.all_picture_placeholder)
                        .placeholder(R.drawable.all_picture_placeholder)
                        .into(viewHolder.mIvThumbnail);
                viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mPresenter.openDetail(mArrayList.get(position).getId());
                    }
                });
                viewHolder.mIbtnBookmark.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // TODO: take full description from YouTube Data API, and transTo edit page
                        Toast.makeText(mContext, "add " + mArrayList.get(position).getId(), Toast.LENGTH_SHORT).show();
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
        private TextView mTvVideoTime;
        private ImageButton mIbtnBookmark;

        private DiscoverViewHolder(View itemView) {
            super(itemView);
            mIvThumbnail = itemView.findViewById(R.id.imageview_thumbnail);
            mTvVideoTitle = itemView.findViewById(R.id.textview_video_title);
            mTvVideoTime = itemView.findViewById(R.id.textview_video_time);
            mIbtnBookmark = itemView.findViewById(R.id.imagebutton_bookmark);
        }
    }

    private class LoadingViewHolder extends RecyclerView.ViewHolder {
        private LoadingViewHolder(View itemView) {
            super(itemView);
        }
    }
}
