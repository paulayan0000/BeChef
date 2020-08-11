package com.paula.android.bechef.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import com.paula.android.bechef.ChildContract;
import com.paula.android.bechef.R;
import com.paula.android.bechef.data.entity.BookmarkItem;
import java.util.ArrayList;
import java.util.List;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class DefaultChildAdapter extends RecyclerView.Adapter{
//    private int mExpandedPosition = -1;
//    private int mPreviousExpandedPosition = -1;
    private ChildContract.ChildPresenter mChildPresenter;
    private ArrayList mDataArrayList;

    public DefaultChildAdapter(ArrayList<?> bean, ChildContract.ChildPresenter presenter) {
        mDataArrayList = bean;
        mChildPresenter = presenter;
    }
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_default_recycler, parent, false);
        return new DefaultViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, final int position, @NonNull List payloads) {
        BookmarkItem bookmarkItem = (BookmarkItem) mDataArrayList.get(position);
        DefaultViewHolder viewHolder = (DefaultViewHolder) holder;
        if (payloads.isEmpty()) {
            viewHolder.mTvItemTitle.setText(bookmarkItem.getTitle());
            viewHolder.mRbItemChoose.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                }
            });
        }
//        final boolean isExpanded = position == mExpandedPosition;
//        viewHolder.mClItemDetail.setVisibility(isExpanded ? View.VISIBLE : View.GONE);
//        if (isExpanded) mPreviousExpandedPosition = position;
//        if (!payloads.isEmpty() && (int) payloads.get(0) == getItemCount() - 1)
//            ((BookmarkChildFragmentContract.Presenter) mChildPresenter).scrollTo(position);
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mChildPresenter.openDetail(mDataArrayList.get(position));
//                mExpandedPosition = isExpanded ? -1 : position;
//                notifyItemChanged(mPreviousExpandedPosition, mPreviousExpandedPosition);
//                notifyItemChanged(position, position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mDataArrayList.size();
    }

    public void updateData(ArrayList<?> newData) {
        mDataArrayList = newData;
        notifyDataSetChanged();
    }

    private class DefaultViewHolder extends RecyclerView.ViewHolder {
        private TextView mTvItemTitle;
        private CheckBox mRbItemChoose;

        private DefaultViewHolder(@NonNull View itemView) {
            super(itemView);
            mTvItemTitle = itemView.findViewById(R.id.textview_title);
            mRbItemChoose = itemView.findViewById(R.id.radio_item);
        }
    }
}
