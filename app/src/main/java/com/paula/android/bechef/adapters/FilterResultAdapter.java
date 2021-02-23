package com.paula.android.bechef.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.paula.android.bechef.R;
import com.paula.android.bechef.data.entity.BaseItem;
import com.paula.android.bechef.data.entity.DiscoverItem;
import com.paula.android.bechef.search.SearchPresenter;
import com.paula.android.bechef.utils.Constants;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class FilterResultAdapter extends RecyclerView.Adapter {
    private Context mContext;
    private ArrayList<BaseItem> mBaseItems;
    private SearchPresenter mSearchPresenter;
    private boolean mIsSearching = false;

    public FilterResultAdapter(ArrayList<BaseItem> baseItems, SearchPresenter searchPresenter) {
        mBaseItems = baseItems;
        mSearchPresenter = searchPresenter;
    }

    public void setLoading(boolean searching) {
        mIsSearching = searching;
        notifyDataSetChanged();
    }

    public void updateData(ArrayList<BaseItem> baseItems) {
        mBaseItems = baseItems;
        mIsSearching = false;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        if (viewType == Constants.VIEW_TYPE_NORMAL) {
            View view = LayoutInflater.from(mContext).inflate(R.layout.item_default_recycler, parent, false);
            return new FilterResultViewHolder(view);
        } else if (viewType == Constants.VIEW_TYPE_NO_RESULT) {
            View view = LayoutInflater.from(mContext).inflate(R.layout.item_search_no_result, parent, false);
            return new NoResultViewHolder(view);
        } else {
            View view = LayoutInflater.from(mContext).inflate(R.layout.item_all_loading, parent, false);
            return new LoadingViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof FilterResultViewHolder) ((FilterResultViewHolder) holder).bindView(position);
    }

    @Override
    public int getItemCount() {
        int itemCount = mBaseItems.size();
        return itemCount == 0 ? 1 : itemCount;
    }

    @Override
    public int getItemViewType(int position) {
        if (mBaseItems.size() > 0) return Constants.VIEW_TYPE_NORMAL;
        else if (mIsSearching) return Constants.VIEW_TYPE_LOADING;
        else return Constants.VIEW_TYPE_NO_RESULT;
    }

    private class FilterResultViewHolder extends RecyclerView.ViewHolder {
        private TextView mTvItemTitle, mTvTags, mTvTimeCount;
        private ImageView mIvImage;

        private FilterResultViewHolder(@NonNull View itemView) {
            super(itemView);
            mTvItemTitle = itemView.findViewById(R.id.textview_title);
            mTvTags = itemView.findViewById(R.id.textview_tags);
            mTvTimeCount = itemView.findViewById(R.id.textview_time_count);
            mIvImage = itemView.findViewById(R.id.imageview_thumbnail);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (getAdapterPosition() < 0) return;
                    BaseItem data = mBaseItems.get(getAdapterPosition());
                    mSearchPresenter.openDetail(data, false);
                }
            });
            itemView.findViewById(R.id.imagebutton_edit).setVisibility(View.GONE);
        }
        void bindView(int position) {
            BaseItem baseItem = mBaseItems.get(position);
            if (baseItem instanceof DiscoverItem) {
                mTvTimeCount.setText(baseItem.getCreatedTime());
            } else {
                String rating = baseItem.getRating() == 0.0 ? "--" : String.valueOf(baseItem.getRating());
                mTvTimeCount.setText(baseItem.getCreatedTime() + " • " + rating + "分");
            }
            mTvItemTitle.setText(baseItem.getTitle());
            if ("".equals(baseItem.getTags()))
                mTvTags.setVisibility(View.GONE);
            else
                mTvTags.setText(baseItem.getTags());

            String imageUrl = baseItem.getImageUrl();
            Picasso.with(mContext)
                    .load(imageUrl.isEmpty() ? null : imageUrl)
                    .error(R.drawable.all_picture_placeholder)
                    .placeholder(R.drawable.all_picture_placeholder)
                    .into(mIvImage);
        }
    }

    private class NoResultViewHolder extends RecyclerView.ViewHolder {
        private NoResultViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }

    private class LoadingViewHolder extends RecyclerView.ViewHolder {
        private LoadingViewHolder(View itemView) {
            super(itemView);
        }
    }
}
