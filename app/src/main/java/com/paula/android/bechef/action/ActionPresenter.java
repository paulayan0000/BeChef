package com.paula.android.bechef.action;

import android.app.Activity;

import com.paula.android.bechef.BaseContract;
import com.paula.android.bechef.bookmark.BookmarkPresenter;
import com.paula.android.bechef.customMain.CustomMainPresenter;
import com.paula.android.bechef.data.database.ItemDatabase;
import com.paula.android.bechef.data.entity.BaseTab;
import com.paula.android.bechef.thread.BeChefRunnableInterface;
import com.paula.android.bechef.thread.BeChefRunnable;

import java.util.ArrayList;

import static com.google.android.gms.common.internal.Preconditions.checkNotNull;

public class ActionPresenter implements ActionContract.Presenter, BaseContract.CustomPresenterForAction {
    private final ActionContract.View mActionView;
    private final CustomMainPresenter mCustomMainPresenter;
    private boolean mIsTaskCanceled;

    public ActionPresenter(ActionContract.View actionView, CustomMainPresenter customMainPresenter) {
        mActionView = checkNotNull(actionView, "actionView cannot be null!");
        mActionView.setCustomMainPresenter(this);
        mCustomMainPresenter = customMainPresenter;
        mIsTaskCanceled = false;
    }

    public void setTaskCanceled(boolean taskCanceled) {
        mIsTaskCanceled = taskCanceled;
    }

    @Override
    public void deleteData() {
        final ArrayList<Long> chosenUids = mCustomMainPresenter.getChosenItemUids();
        final boolean isFromBookmark = isFromBookmark();
        new Thread(new BeChefRunnable(new BeChefRunnableInterface() {
            @Override
            public void runTasksOnNewThread() {
                if (isFromBookmark) {
                    ItemDatabase.getItemInstance().bookmarkDao()
                            .deleteItemsWithUid(chosenUids);
                } else {
                    ItemDatabase.getItemInstance().recipeDao()
                            .deleteItemsWithUid(chosenUids);
                }
                if (mIsTaskCanceled || mActionView == null) return;
                if (getActivity() != null) (getActivity()).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (mCustomMainPresenter == null) return;
                        mCustomMainPresenter.leaveChooseMode();
                    }
                });
            }
        })).start();

    }

    @Override
    public int getChosenItemsCount() {
        return mCustomMainPresenter.getChosenItemsCount();
    }

    @Override
    public void leaveChooseMode() {
        mCustomMainPresenter.leaveChooseMode();
    }

    @Override
    public ArrayList<BaseTab> getOtherTabs() {
        return mCustomMainPresenter.getOtherTabs();
    }

    @Override
    public ArrayList<Long> getChosenItemUids() {
        return mCustomMainPresenter.getChosenItemUids();
    }

    @Override
    public void dismissEditDialog() {
        mCustomMainPresenter.dismissEditDialog();
    }

    @Override
    public void start() {
    }

    @Override
    public Activity getActivity() {
        return ((ActionFragment) mActionView).getActivity();
    }

    public boolean isFromBookmark() {
        return mCustomMainPresenter instanceof BookmarkPresenter;
    }
}