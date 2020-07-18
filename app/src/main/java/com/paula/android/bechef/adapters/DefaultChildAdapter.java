package com.paula.android.bechef.adapters;

import android.content.Context;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.paula.android.bechef.BasePresenter;
import com.paula.android.bechef.R;
import com.paula.android.bechef.api.beans.GetSearchList;
import com.paula.android.bechef.bookmarkChild.BookmarkChildFragmentContract;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;
import androidx.transition.Slide;
import androidx.transition.Transition;
import androidx.transition.TransitionManager;

public class DefaultChildAdapter extends RecyclerView.Adapter{

    private String mTableName;
    private Context mContext;
    private int mExpandedPosition = -1;
    private int mPreviousExpandedPosition = -1;
    private BasePresenter mBasePresenter;

    public DefaultChildAdapter(GetSearchList bean, BasePresenter presenter) {
        mTableName = bean.getTableName();
        mBasePresenter = presenter;
    }
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_default_recycler, parent, false);
        return new DefaultViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, final int position) {

    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, final int position, @NonNull List payloads) {
        final DefaultViewHolder viewHolder = (DefaultViewHolder) holder;
        if (payloads.isEmpty()) {
            viewHolder.getTvItemTitle().setText("table name: " + mTableName);
            viewHolder.getRbItemChoose().setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                }
            });
        }
        final boolean isExpanded = position == mExpandedPosition;
        viewHolder.mClItemDetail.setVisibility(isExpanded ? View.VISIBLE : View.GONE);
        if (isExpanded) mPreviousExpandedPosition = position;
        if (!payloads.isEmpty() && (int) payloads.get(0) == getItemCount() - 1)
            ((BookmarkChildFragmentContract.Presenter) mBasePresenter).scrollTo(position);
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mExpandedPosition = isExpanded ? -1 : position;
                notifyItemChanged(mPreviousExpandedPosition, mPreviousExpandedPosition);
                notifyItemChanged(position, position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return 10;
    }

    public void updateData(GetSearchList newBean) {
        mTableName = newBean.getTableName();
    }

    private class DefaultViewHolder extends RecyclerView.ViewHolder {

        private TextView mTvItemTitle;
        private CheckBox mRbItemChoose;
        private ConstraintLayout mClItemDetail;

        private DefaultViewHolder(@NonNull final View itemView) {
            super(itemView);
            mClItemDetail = itemView.findViewById(R.id.constraintlayout_detail);
            mTvItemTitle = itemView.findViewById(R.id.textview_video_title);
            mRbItemChoose = itemView.findViewById(R.id.radio_item);
        }

        private TextView getTvItemTitle() {
            return mTvItemTitle;
        }

        private CheckBox getRbItemChoose() {
            return mRbItemChoose;
        }

        public ConstraintLayout getClItemDetail() {
            return mClItemDetail;
        }
    }
}
