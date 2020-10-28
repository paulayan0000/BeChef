package com.paula.android.bechef.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.paula.android.bechef.R;
import com.paula.android.bechef.customChild.CustomChildPresenter;
import com.paula.android.bechef.data.entity.BaseItem;
import com.paula.android.bechef.data.entity.BookmarkItem;
import com.paula.android.bechef.data.entity.ReceiptItem;
import com.paula.android.bechef.dialog.EditItemDialog;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class DefaultChildAdapter<T> extends RecyclerView.Adapter {
    private CustomChildPresenter mChildPresenter;
    private Context mContext;
    private Boolean mIsSelectable;
    private ArrayList<T> mDataArrayList;
    private ArrayList<T> mChosenDataArrayList;

    public DefaultChildAdapter(Boolean isSelectable, ArrayList<T> bean, CustomChildPresenter presenter) {
        mIsSelectable = isSelectable;
        mDataArrayList = bean;
        mChildPresenter = presenter;
        mChosenDataArrayList = new ArrayList<>();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_default_recycler, parent, false);
        return new DefaultViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ((DefaultViewHolder) holder).bindView(mDataArrayList.get(position));
    }

    @Override
    public int getItemCount() {
        return mDataArrayList.size();
    }

    public void updateData(ArrayList<T> newData) {
        mDataArrayList = newData;
        notifyDataSetChanged();
    }

    public void setSelectable(Boolean isSelectable) {
        mIsSelectable = isSelectable;
        notifyDataSetChanged();
    }

    private class DefaultViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView mTvItemTitle, mTvTags, mTvTimeCount;
        private ImageButton mIbtnChoose, mIbtnEdit;
        private ImageView mIvImage;

        private DefaultViewHolder(@NonNull View itemView) {
            super(itemView);
            mTvItemTitle = itemView.findViewById(R.id.textview_title);
            mTvTags = itemView.findViewById(R.id.textview_tags);
            mTvTimeCount = itemView.findViewById(R.id.textview_time_count);
            mIvImage = itemView.findViewById(R.id.imageview_thumbnail);
            mIbtnChoose = itemView.findViewById(R.id.imagebutton_choose_box);
            mIbtnChoose.setOnClickListener(this);
            mIbtnEdit = itemView.findViewById(R.id.imagebutton_edit);
            mIbtnEdit.setOnClickListener(this);
            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    mChildPresenter.transToSelectable();
                    return false;
                }
            });
        }

        void bindView(final T data) {
            final BaseItem baseItem = (BaseItem) data;
            String rating = baseItem.getRating() == 0.0 ? "--" : String.valueOf(baseItem.getRating());
            mTvTimeCount.setText(baseItem.getCreatedTime() + " • " + rating + "分");
            mTvItemTitle.setText(baseItem.getTitle());
            if ("".equals(baseItem.getTags()))
                mTvTags.setVisibility(View.GONE);
            else
                mTvTags.setText(baseItem.getTags());

            if (mIsSelectable) {
                mIbtnChoose.setVisibility(View.VISIBLE);
                mIbtnEdit.setVisibility(View.GONE);
                mIbtnChoose.setSelected(baseItem.getSelected());
            } else {
                mIbtnChoose.setVisibility(View.GONE);
                mIbtnEdit.setVisibility(View.VISIBLE);
                baseItem.setSelected(false);
                clearChosenItems();
            }

            String imageUrl = baseItem.getImageUrl();
            Picasso.with(mContext)
                    .load(imageUrl.isEmpty() ? null : imageUrl)
                    .error(R.drawable.all_picture_placeholder)
                    .placeholder(R.drawable.all_picture_placeholder)
                    .into(mIvImage);
        }

        @Override
        public void onClick(View v) {
            T data = mDataArrayList.get(getAdapterPosition());
            switch (v.getId()) {
                case R.id.imagebutton_choose_box:
                    boolean isSelected = !v.isSelected();
                    v.setSelected(isSelected);
                    ((BaseItem) data).setSelected(isSelected);
                    if (isSelected) mChosenDataArrayList.add(data);
                    else mChosenDataArrayList.remove(data);
                    break;
                case R.id.imagebutton_edit:
                    if (data instanceof ReceiptItem) {
                        EditItemDialog editItemDialog = new EditItemDialog((ReceiptItem) mDataArrayList.get(getAdapterPosition()));
                        editItemDialog.show(mChildPresenter.getFragmentManager(), "edit_receipt");
                    } else if (data instanceof BookmarkItem) {
                        EditItemDialog editItemDialog = new EditItemDialog((BookmarkItem) mDataArrayList.get(getAdapterPosition()));
                        editItemDialog.show(mChildPresenter.getFragmentManager(), "edit_bookmark");
                    }
                    break;
                default:
                    mChildPresenter.openDetail(data, mIsSelectable);
                    break;

            }

        }
    }

    public ArrayList<T> getChosenItems() {
        return mChosenDataArrayList;
    }

    private void clearChosenItems() {
        if (mChosenDataArrayList != null)
            mChosenDataArrayList.clear();
    }
}
