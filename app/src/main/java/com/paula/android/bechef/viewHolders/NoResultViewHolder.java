package com.paula.android.bechef.viewHolders;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.paula.android.bechef.R;

public class NoResultViewHolder extends RecyclerView.ViewHolder {

    public NoResultViewHolder(@NonNull View itemView, String errorMsg) {
        super(itemView);
        ((TextView) itemView.findViewById(R.id.textview_no_result)).setText(errorMsg);
    }
}