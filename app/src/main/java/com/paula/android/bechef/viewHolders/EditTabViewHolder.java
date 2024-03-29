package com.paula.android.bechef.viewHolders;

import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.paula.android.bechef.BeChef;
import com.paula.android.bechef.R;
import com.paula.android.bechef.utils.BeChefTextWatcher;

public class EditTabViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    protected EditText mEtTabName;
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
    public EditTabViewHolder(@NonNull final View itemView, BeChefTextWatcher textWatcher) {
        super(itemView);
        setBasicView();
        itemView.findViewById(R.id.imagebutton_add).setOnClickListener(this);
        mTextWatcher = textWatcher;
        mEtTabName.setHint(BeChef.getAppContext().getString(R.string.toast_no_empty_tab_name));
        mEtTabName.addTextChangedListener(mTextWatcher);
        itemView.findViewById(R.id.imagebutton_clear).setOnClickListener(this);
    }

    private void setBasicView() {
        mEtTabName = itemView.findViewById(R.id.edittext_material_content);
        mEtTabName.setSingleLine(true);
        mEtTabName.setEllipsize(TextUtils.TruncateAt.END);
        mIbtnRemove = itemView.findViewById(R.id.imagebutton_remove);
        mIbtnRemove.setOnClickListener(this);
    }

    public void bindView(String tabName) {
        mEtTabName.setText(tabName);
    }

    protected int getRemoveDrawableId(boolean isDisable) {
        return isDisable ? R.drawable.ic_remove_gray : R.drawable.ic_remove;
    }

    protected void setImageDrawable(ImageButton imageButton, int drawableId) {
        imageButton.setImageDrawable(ContextCompat.getDrawable(itemView.getContext(), drawableId));
    }

    @Override
    public void onClick(View v) {
        int currentPosition = getAdapterPosition();
        if (currentPosition < 0) return;
    }
}