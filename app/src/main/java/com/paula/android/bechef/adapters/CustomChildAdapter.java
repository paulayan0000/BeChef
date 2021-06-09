package com.paula.android.bechef.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.paula.android.bechef.ChildContract;
import com.paula.android.bechef.R;
import com.paula.android.bechef.data.entity.BaseItem;
import com.paula.android.bechef.dialog.EditItemDialog;
import com.paula.android.bechef.viewHolders.DefaultViewHolder;

import java.util.ArrayList;

public class CustomChildAdapter<I>
        extends RecyclerView.Adapter<CustomChildAdapter<I>.DefaultChildViewHolder> {
    private final ChildContract.CustomChildPresenter mChildPresenter;
    private final ArrayList<Long> mChosenUids = new ArrayList<>();
    private Boolean mIsSelectable;
    private ArrayList<I> mBaseItems;

    public CustomChildAdapter(Boolean isSelectable, ArrayList<I> baseItems,
                              ChildContract.CustomChildPresenter presenter) {
        mIsSelectable = isSelectable;
        mBaseItems = baseItems;
        mChildPresenter = presenter;
    }

    @NonNull
    @Override
    public DefaultChildViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_custom_child_recycler, parent, false);
        return new DefaultChildViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CustomChildAdapter.DefaultChildViewHolder holder,
                                 int position) {
        holder.bindCustomView((BaseItem) mBaseItems.get(position));
    }

    @Override
    public int getItemCount() {
        return mBaseItems.size();
    }

    public void setSelectable(Boolean isSelectable) {
        mIsSelectable = isSelectable;
        notifyDataSetChanged();
    }

    public void updateData(ArrayList<I> items) {
        mBaseItems = items;
        notifyDataSetChanged();
    }

    public ArrayList<Long> getChosenItemUids() {
        return mChosenUids;
    }

    class DefaultChildViewHolder extends DefaultViewHolder {

        private DefaultChildViewHolder(@NonNull View itemView) {
            super(itemView);
            mIbtnChoose = itemView.findViewById(R.id.imagebutton_choose_box);
            mIbtnChoose.setOnClickListener(this);
            mIbtnEdit = itemView.findViewById(R.id.imagebutton_edit);
            mIbtnEdit.setOnClickListener(this);
            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    mChildPresenter.transToSelectable();
                    return false;
                }
            });
        }

        public void bindCustomView(BaseItem baseItem) {
            bindView(baseItem);
            if (mIsSelectable) {
                mIbtnChoose.setVisibility(View.VISIBLE);
                mIbtnChoose.setSelected(mChosenUids.contains(baseItem.getUid()));
                mIbtnEdit.setVisibility(View.GONE);
            } else {
                mIbtnChoose.setVisibility(View.GONE);
                mIbtnEdit.setVisibility(View.VISIBLE);
                baseItem.setSelected(false);
                mChosenUids.clear();
            }
        }

        @Override
        public void onClick(View v) {
            super.onClick(v);
            BaseItem baseItem = (BaseItem) mBaseItems.get(getAdapterPosition());
            int currentViewId = v.getId();
            if (currentViewId == R.id.imagebutton_choose_box) {
                boolean isSelectedNew = !v.isSelected();
                v.setSelected(isSelectedNew);
                baseItem.setSelected(isSelectedNew);
                if (isSelectedNew) {
                    mChosenUids.add(baseItem.getUid());
                } else {
                    mChosenUids.remove(baseItem.getUid());
                }
            } else if (currentViewId == R.id.imagebutton_edit) {
                new EditItemDialog(baseItem).show(mChildPresenter.getFragmentManager(),
                        baseItem.getClass().getSimpleName());
            } else {
                mChildPresenter.openDetail(baseItem, mIsSelectable);
            }
        }
    }
}