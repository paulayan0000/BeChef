package com.paula.android.bechef.action;

import android.app.Activity;
import android.content.Context;

import com.paula.android.bechef.BaseContract;
import com.paula.android.bechef.bookmark.BookmarkPresenter;
import com.paula.android.bechef.customMain.CustomMainPresenter;
import com.paula.android.bechef.data.database.ItemDatabase;
import com.paula.android.bechef.data.entity.BaseTab;

import java.util.ArrayList;

import static com.google.android.gms.common.internal.Preconditions.checkNotNull;

public class ActionPresenter implements ActionContract.Presenter, BaseContract.CustomPresenterForAction {
    private final ActionContract.View mActionView;
    private final CustomMainPresenter mCustomMainPresenter;

    public ActionPresenter(ActionContract.View actionView, CustomMainPresenter customMainPresenter) {
        mActionView = checkNotNull(actionView, "actionView cannot be null!");
        mActionView.setCustomMainPresenter(this);
        mCustomMainPresenter = customMainPresenter;
    }

    @Override
    public void deleteData() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                ArrayList<Long> chosenUids = mCustomMainPresenter.getChosenItemUids();
                if (mCustomMainPresenter instanceof BookmarkPresenter) {
                    ItemDatabase.getItemInstance(getContext()).bookmarkDao()
                            .deleteItemsWithUid(chosenUids);
                } else {
                    ItemDatabase.getItemInstance(getContext()).recipeDao()
                            .deleteItemsWithUid(chosenUids);
                }
                if (getContext() != null) ((Activity) getContext()).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mCustomMainPresenter.leaveChooseMode();
                    }
                });
            }
        }).start();
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
    public Context getContext() {
        return mActionView.getContext();
    }

    public boolean isFromBookmark() {
        return mCustomMainPresenter instanceof BookmarkPresenter;
    }
}
