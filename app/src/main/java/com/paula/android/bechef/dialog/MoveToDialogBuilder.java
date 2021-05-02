package com.paula.android.bechef.dialog;

import android.app.Activity;
import android.view.View;

import com.paula.android.bechef.R;
import com.paula.android.bechef.action.ActionContract;
import com.paula.android.bechef.data.database.ItemDatabase;
import com.paula.android.bechef.data.database.TabDatabase;
import com.paula.android.bechef.data.entity.BookmarkTab;
import com.paula.android.bechef.data.entity.RecipeTab;

import java.util.ArrayList;

public class MoveToDialogBuilder extends AddToBookmarkDialogBuilder {
    private final ActionContract.Presenter mPresenter;
    private String mTabName;


    public MoveToDialogBuilder(ActionContract.Presenter actionPresenter) {
        super(actionPresenter.getContext());
        mPresenter = actionPresenter;
    }

    @Override
    protected String getTitleText() {
        return mContext.getString(R.string.action_move);
    }

    @Override
    protected void setRatingBar(View view) {
        view.findViewById(R.id.textview_rating).setVisibility(View.GONE);
        view.findViewById(R.id.rating_bar).setVisibility(View.GONE);
    }

    @Override
    protected void manipulateData(String tabName) {
        mTabName = tabName;
        new Thread(new Runnable() {
            @Override
            public void run() {
                ArrayList<Long> chosenUids = mPresenter.getChosenItemUids();
                if (mPresenter.isFromBookmark()) {
                    updateBookmarkTabUid(chosenUids);
                } else {
                    updateRecipeTabUid(chosenUids);
                }
                if (mContext != null) {
                    ((Activity) mContext).runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mPresenter.leaveChooseMode();
                            mPresenter.dismissEditDialog();
                        }
                    });
                }
            }
        }).start();
    }

    private void updateBookmarkTabUid(ArrayList<Long> chosenUids) {
        long tabUid;
        if (isChosenAddNew()) {
            tabUid = TabDatabase.getTabInstance(mContext).bookmarkDao()
                    .insert(new BookmarkTab(mTabName));
        } else {
            tabUid = mTabs.get(mChosenTabIndex).getUid();
        }
        for (long uid : chosenUids) {
            ItemDatabase.getItemInstance(mContext).bookmarkDao().setNewTabUid(uid, tabUid);
        }
    }

    private void updateRecipeTabUid(ArrayList<Long> chosenUids) {
        long tabUid;
        if (isChosenAddNew()) {
            tabUid = TabDatabase.getTabInstance(mContext).recipeDao()
                    .insert(new RecipeTab(mTabName));
        } else {
            tabUid = mTabs.get(mChosenTabIndex).getUid();
        }
        for (long uid : chosenUids) {
            ItemDatabase.getItemInstance(mContext).recipeDao().setNewTabUid(uid, tabUid);
        }
    }
}
