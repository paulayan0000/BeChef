package com.paula.android.bechef.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import com.paula.android.bechef.ChildContract;
import com.paula.android.bechef.R;
import com.paula.android.bechef.data.entity.BaseItem;
import com.paula.android.bechef.data.entity.BookmarkItem;
import com.paula.android.bechef.data.entity.ReceiptItem;
import java.util.ArrayList;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class DefaultChildAdapter extends RecyclerView.Adapter{
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
        DefaultViewHolder viewHolder = (DefaultViewHolder) holder;
        viewHolder.bindView(mDataArrayList.get(position));
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
        private TextView mTvItemTitle, mTvTags, mTvTimeCount;
        private CheckBox mRbItemChoose;

        private DefaultViewHolder(@NonNull View itemView) {
            super(itemView);
            mTvItemTitle = itemView.findViewById(R.id.textview_title);
            mTvTags = itemView.findViewById(R.id.textview_tags);
            mTvTimeCount = itemView.findViewById(R.id.textview_time_count);
            mRbItemChoose = itemView.findViewById(R.id.radio_item);
        }

        void bindView(final Object data) {

            String timeAndCount;
            if (data instanceof BookmarkItem) {
                BookmarkItem bookmarkItem = (BookmarkItem) data;
                timeAndCount = bookmarkItem.getPublishedTime() + " • " + bookmarkItem.getRating() + "分";
            }
            else {
                ReceiptItem receiptItem = (ReceiptItem) data;
                timeAndCount = "耗時 : " + receiptItem.getDuration() + " • 份量 : "
                        + receiptItem.getWeight() + "人份 • " + receiptItem.getRating() + "分";
            }
            mTvTimeCount.setText(timeAndCount);

            BaseItem baseItem = (BaseItem) data;
            mTvItemTitle.setText(baseItem.getTitle());
            mTvTags.setText(baseItem.getTags());

            mRbItemChoose.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                }
            });
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mChildPresenter.openDetail(data);
                }
            });
        }
    }
}
