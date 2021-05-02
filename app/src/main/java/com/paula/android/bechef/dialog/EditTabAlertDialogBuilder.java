package com.paula.android.bechef.dialog;

import android.content.Context;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.paula.android.bechef.R;
import com.paula.android.bechef.adapters.EditTabAdapter;
import com.paula.android.bechef.data.entity.BaseTab;
import com.paula.android.bechef.data.entity.BookmarkTab;
import com.paula.android.bechef.data.entity.DiscoverTab;
import com.paula.android.bechef.data.entity.RecipeTab;

import java.util.ArrayList;

public class EditTabAlertDialogBuilder extends BeChefAlertDialogBuilder {
    private final EditTabAdapter mEditTabAdapter;

    public EditTabAlertDialogBuilder(@NonNull Context context, ArrayList<BaseTab> baseTabs) {
        super(context, R.style.AlertDialogTheme);
        mEditTabAdapter = new EditTabAdapter(getEditBaseTabs(baseTabs));
        initBuilder();
    }

    private ArrayList<BaseTab> getEditBaseTabs(ArrayList<BaseTab> baseTabs) {
        ArrayList<BaseTab> editBaseTabs = new ArrayList<>();
        if (baseTabs.get(0) instanceof DiscoverTab) {
            for (BaseTab baseTab : baseTabs) {
                editBaseTabs.add(new DiscoverTab((DiscoverTab) baseTab));
            }
        } else if (baseTabs.get(0) instanceof BookmarkTab) {
            for (BaseTab baseTab : baseTabs) {
                editBaseTabs.add(new BookmarkTab((BookmarkTab) baseTab));
            }
        } else {
            for (BaseTab baseTab : baseTabs) {
                editBaseTabs.add(new RecipeTab((RecipeTab) baseTab));
            }
        }
        return editBaseTabs;
    }

    private void initBuilder() {
        setTitle(mContext.getString(R.string.edit_tab));
        setButtons(new AlertDialogClickCallback() {
            @Override
            public boolean onPositiveButtonClick() {
                return mEditTabAdapter.onCompleteClicked();
            }
        });
    }

    @Override
    protected String getPositiveWord() {
        return mContext.getString(R.string.complete);
    }

    @Override
    protected String getNegativeWord() {
        return mContext.getString(R.string.cancel);
    }

    @Override
    protected void setCustomView(AlertDialog alertDialog) {
        View view = alertDialog.getLayoutInflater().inflate(R.layout.alert_dialog_edit_tab, null);
        RecyclerView recyclerView = view.findViewById(R.id.recyclerview_edit_tab);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(mEditTabAdapter);
        alertDialog.setView(view);
    }
}
