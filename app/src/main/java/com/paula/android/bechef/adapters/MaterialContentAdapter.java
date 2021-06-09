package com.paula.android.bechef.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.paula.android.bechef.BeChef;
import com.paula.android.bechef.R;
import com.paula.android.bechef.utils.BeChefTextWatcher;
import com.paula.android.bechef.utils.EditTextChangeCallback;

import java.util.ArrayList;

public class MaterialContentAdapter
        extends RecyclerView.Adapter<MaterialContentAdapter.MaterialContentViewHolder>
        implements EditTextChangeCallback, ItemTouchHelperAdapter {
    private final ArrayList<String> mMaterialContents;

    MaterialContentAdapter(ArrayList<String> materialContents) {
        mMaterialContents = materialContents;
        if (materialContents.size() == 0) mMaterialContents.add("");
    }

    @NonNull
    @Override
    public MaterialContentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_edit_material_contents, parent, false);
        return new MaterialContentViewHolder(view, new BeChefTextWatcher(this));
    }

    @Override
    public void onBindViewHolder(@NonNull MaterialContentViewHolder holder, int position) {
        holder.bindView(position);
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
    public ArrayList<String> getDataList() {
        return mMaterialContents;
    }

    class MaterialContentViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private final EditText mEtContent;
        private final BeChefTextWatcher mTextWatcher;
        private final ImageButton mIbtnRemove;

        MaterialContentViewHolder(@NonNull View itemView, BeChefTextWatcher textWatcher) {
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
            int removeDrawableId = getRemoveDrawableId(mMaterialContents.size() == 1);
            setImageDrawable(mIbtnRemove, removeDrawableId);
        }

        private int getRemoveDrawableId(boolean isDisable) {
            return isDisable ? R.drawable.ic_remove_gray : R.drawable.ic_remove;
        }

        private void setImageDrawable(ImageButton imageButton, int drawableId) {
            imageButton.setImageDrawable(ContextCompat.getDrawable(BeChef.getAppContext(), drawableId));
        }

        @Override
        public void onClick(View v) {
            int currentIndex = getAdapterPosition();
            if (currentIndex < 0) return;
            int currentViewId = v.getId();
            if (currentViewId == R.id.imagebutton_add) {
                mMaterialContents.add(currentIndex + 1, "");
                notifyItemInserted(currentIndex + 1);
                notifyItemRangeChanged(currentIndex, getItemCount() - currentIndex);

            } else if (currentViewId == R.id.imagebutton_remove) {
                if (mMaterialContents.size() == 1) return;
                mMaterialContents.remove(currentIndex);
                if (mMaterialContents.size() == 1) {
                    notifyItemRangeChanged(0, 2);
                } else {
                    notifyItemRemoved(currentIndex);
                    notifyItemRangeChanged(currentIndex, getItemCount() - currentIndex);
                }
            } else if (currentViewId == R.id.imagebutton_clear) {
                mMaterialContents.set(currentIndex, "");
                notifyItemChanged(currentIndex);
            }
        }
    }
}