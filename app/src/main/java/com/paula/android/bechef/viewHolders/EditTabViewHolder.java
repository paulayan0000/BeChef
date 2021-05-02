package com.paula.android.bechef.viewHolders;

import android.content.Context;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.paula.android.bechef.R;
import com.paula.android.bechef.utils.BeChefTextWatcher;

public class EditTabViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    private EditText mEtTabName;
    protected ImageButton mIbtnRemove;
    protected BeChefTextWatcher mTextWatcher;

    // Constructor for edit discoverTab
    public EditTabViewHolder(@NonNull View itemView) {
        super(itemView);
        setBasicView();
        mEtTabName.setEnabled(false);
        itemView.findViewById(R.id.imagebutton_add).setVisibility(View.GONE);
        itemView.findViewById(R.id.imagebutton_clear).setVisibility(View.GONE);
    }

    // Constructor for edit bookmarkTab or recipeTab
    public EditTabViewHolder(@NonNull View itemView, BeChefTextWatcher textWatcher) {
        super(itemView);
        setBasicView();
        itemView.findViewById(R.id.imagebutton_add).setOnClickListener(this);
        mTextWatcher = textWatcher;
        mEtTabName.setHint(itemView.getContext().getString(R.string.toast_no_empty_tab_name));
        mEtTabName.addTextChangedListener(mTextWatcher);
        itemView.findViewById(R.id.imagebutton_clear).setOnClickListener(this);
    }

    private void setBasicView() {
        mEtTabName = itemView.findViewById(R.id.edittext_material_content);
        mIbtnRemove = itemView.findViewById(R.id.imagebutton_remove);
        mIbtnRemove.setOnClickListener(this);
    }

    public void bindView(String tabName) {
        mEtTabName.setText(tabName);
    }

    protected int getRemoveDrawableId(boolean isDisable) {
        return isDisable ? R.drawable.ic_remove_gray : R.drawable.ic_remove;
    }

    protected void setImageDrawable(ImageButton imageButton, Context context, int drawableId) {
        imageButton.setImageDrawable(ContextCompat.getDrawable(context, drawableId));
    }

    @Override
    public void onClick(View v) {
        int currentPosition = getAdapterPosition();
        if (currentPosition < 0) return;
    }
}
