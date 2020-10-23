package com.paula.android.bechef.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;

import com.paula.android.bechef.R;
import com.paula.android.bechef.dialog.BeChefTextWatcher;
import com.paula.android.bechef.dialog.EditTextChangeCallback;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class MaterialContentAdapter extends RecyclerView.Adapter implements EditTextChangeCallback {
    private Context mContext;
    private ArrayList<String> mMaterialContents;

    public MaterialContentAdapter(ArrayList<String> materialContents) {
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
            switch (v.getId()) {
                case R.id.imagebutton_add:
                    mMaterialContents.add(currentIndex + 1, "");
                    notifyItemInserted(currentIndex + 1);
                    notifyItemRangeChanged(currentIndex, mMaterialContents.size() - currentIndex);
                    break;
                case R.id.imagebutton_remove:
                    if (mMaterialContents.size() == 1) return;
                    mMaterialContents.remove(currentIndex);
                    if (mMaterialContents.size() == 1) {
                        notifyItemRangeChanged(0, 2);
                    } else {
                        notifyItemRemoved(currentIndex);
                        notifyItemRangeChanged(currentIndex, mMaterialContents.size() - currentIndex);
                    }
                    break;
                case R.id.imagebutton_clear:
                    mMaterialContents.set(currentIndex, "");
                    notifyItemChanged(currentIndex);
                    break;
            }
        }
    }
}
