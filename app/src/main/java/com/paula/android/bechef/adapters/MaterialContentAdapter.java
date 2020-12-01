package com.paula.android.bechef.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;

import com.paula.android.bechef.R;
import com.paula.android.bechef.utils.BeChefTextWatcher;
import com.paula.android.bechef.utils.EditTextChangeCallback;

import java.util.ArrayList;
import java.util.Collections;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import static androidx.recyclerview.widget.ItemTouchHelper.Callback.makeMovementFlags;

public class MaterialContentAdapter extends RecyclerView.Adapter implements EditTextChangeCallback, ItemTouchHelperAdapter {
    private Context mContext;
    private ArrayList<String> mMaterialContents;

    MaterialContentAdapter(ArrayList<String> materialContents) {
        mMaterialContents = materialContents;
        if (materialContents.size() == 0) {
            mMaterialContents.add("");
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_edit_material_contents, parent, false);
        return new ContentViewHolder(view, new BeChefTextWatcher(this));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ((ContentViewHolder) holder).bindView(position);
    }

    @Override
    public int getItemCount() {
        return mMaterialContents.size();
    }

    @Override
    public void afterTextChanged(int position, String textContent) {
        mMaterialContents.set(position, textContent);
    }

    @Override
    public boolean onItemMoved(int fromPosition, int toPosition) {
        if (fromPosition < toPosition) {
            for (int i = fromPosition; i < toPosition; i++) {
                Collections.swap(mMaterialContents, i, i + 1);
            }
        } else {
            for (int i = fromPosition; i > toPosition; i--) {
                Collections.swap(mMaterialContents, i, i - 1);
            }
        }
        notifyItemMoved(fromPosition, toPosition);
        return true;
    }

    @Override
    public void onItemSwiped(int position) {
        mMaterialContents.remove(position);
        notifyRemoved(position);
    }

    @Override
    public int getItemMovementFlags() {
        if (mMaterialContents.size() <= 1)
            return makeMovementFlags(ItemTouchHelper.ACTION_STATE_IDLE, ItemTouchHelper.ACTION_STATE_IDLE);
        else
            return makeMovementFlags(ItemTouchHelper.UP | ItemTouchHelper.DOWN,
                    ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT);
    }

    private class ContentViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private EditText mEtContent;
        private BeChefTextWatcher mTextWatcher;
        private ImageButton mIbtnRemove;

        ContentViewHolder(@NonNull View itemView, BeChefTextWatcher textWatcher) {
            super(itemView);
            itemView.findViewById(R.id.imagebutton_add).setOnClickListener(this);
            mIbtnRemove = itemView.findViewById(R.id.imagebutton_remove);
            mIbtnRemove.setOnClickListener(this);
            itemView.findViewById(R.id.imagebutton_clear).setOnClickListener(this);

            mEtContent = itemView.findViewById(R.id.edittext_material_content);
            mTextWatcher = textWatcher;
            mEtContent.addTextChangedListener(mTextWatcher);
        }

        void bindView(int position) {
            mTextWatcher.bindPosition(position);
            mEtContent.setText(mMaterialContents.get(position));
            mIbtnRemove.setImageDrawable(mContext.getResources()
                    .getDrawable(mMaterialContents.size() == 1 ? R.drawable.ic_remove_gray : R.drawable.ic_remove));
        }

        @Override
        public void onClick(View v) {
            int currentIndex = getAdapterPosition();
            if (currentIndex < 0) return;
            switch (v.getId()) {
                case R.id.imagebutton_add:
                    mMaterialContents.add(currentIndex + 1, "");
                    notifyAdded(currentIndex);
                    break;
                case R.id.imagebutton_remove:
                    if (mMaterialContents.size() == 1) return;
                    mMaterialContents.remove(currentIndex);
                    if (mMaterialContents.size() == 1) notifyItemRangeChanged(0, 2);
                    else notifyRemoved(currentIndex);
                    break;
                case R.id.imagebutton_clear:
                    mMaterialContents.set(currentIndex, "");
                    notifyItemChanged(currentIndex);
                    break;
            }
        }
    }
    private void notifyAdded(int currentPosition) {
        notifyItemInserted(currentPosition + 1);
        notifyItemRangeChanged(currentPosition, getItemCount() - currentPosition);
    }

    private void notifyRemoved(int currentPosition) {
        notifyItemRemoved(currentPosition);
        notifyItemRangeChanged(currentPosition, getItemCount() - currentPosition);
    }
}
