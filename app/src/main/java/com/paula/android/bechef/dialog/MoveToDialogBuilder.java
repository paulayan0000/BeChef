package com.paula.android.bechef.dialog;

import android.app.Activity;
import android.content.Context;
import android.view.View;

import com.paula.android.bechef.BeChef;
import com.paula.android.bechef.R;
import com.paula.android.bechef.action.ActionContract;
import com.paula.android.bechef.data.database.ItemDatabase;
import com.paula.android.bechef.data.database.TabDatabase;
import com.paula.android.bechef.data.entity.BookmarkTab;
import com.paula.android.bechef.data.entity.RecipeTab;
import com.paula.android.bechef.thread.BeChefRunnableInterface;
import com.paula.android.bechef.thread.BeChefRunnable;

import java.util.ArrayList;

public class MoveToDialogBuilder extends AddToBookmarkDialogBuilder {
    private final ActionContract.Presenter mPresenter;
    private String mTabName;

    public MoveToDialogBuilder(Context context, ActionContract.Presenter actionPresenter) {
        super(context);
        mPresenter = actionPresenter;
    }

    @Override
    protected String getTitleText() {
        return BeChef.getAppContext().getString(R.string.action_move);
    }

    @Override
    protected void setRatingBar(View view) {
        view.findViewById(R.id.textview_rating).setVisibility(View.GONE);
        view.findViewById(R.id.rating_bar).setVisibility(View.GONE);
    }

    @Override
    protected void manipulateData(String tabName) {
        mTabName = tabName;
        final boolean finalIsForBookmark = mPresenter.isFromBookmark();
        final ArrayList<Long> finalChosenItemUids = mPresenter.getChosenItemUids();

        new Thread(new BeChefRunnable(new BeChefRunnableInterface() {
            @Override
            public void runTasksOnNewThread() {
                if (finalIsForBookmark) {
                    updateBookmarkTabUid(finalChosenItemUids);
                } else {
                    updateRecipeTabUid(finalChosenItemUids);
                }
                Activity activity = mPresenter.getActivity();
                if (activity != null) {
                    activity.runOnUiThread(new BeChefRunnable(new BeChefRunnableInterface() {
                        @Override
                        public void runTasksOnNewThread() {
                            mPresenter.leaveChooseMode();
                            mPresenter.dismissEditDialog();
                        }
                    }));
                }
            }
        })).start();
    }

    private void updateBookmarkTabUid(ArrayList<Long> chosenUids) {
        long tabUid;
        if (isChosenAddNew()) {
            tabUid = TabDatabase.getTabInstance().bookmarkDao()
                    .insert(new BookmarkTab(mTabName));
        } else {
            tabUid = mTabs.get(mChosenTabIndex).getUid();
        }
        for (long uid : chosenUids) {
            ItemDatabase.getItemInstance().bookmarkDao().setNewTabUid(uid, tabUid);
        }
    }

    private void updateRecipeTabUid(ArrayList<Long> chosenUids) {
        long tabUid;
        if (isChosenAddNew()) {
            tabUid = TabDatabase.getTabInstance().recipeDao()
                    .insert(new RecipeTab(mTabName));
        } else {
            tabUid = mTabs.get(mChosenTabIndex).getUid();
        }
        for (long uid : chosenUids) {
            ItemDatabase.getItemInstance().recipeDao().setNewTabUid(uid, tabUid);
        }
    }
}