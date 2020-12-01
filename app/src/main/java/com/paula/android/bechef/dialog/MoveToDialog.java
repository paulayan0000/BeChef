package com.paula.android.bechef.dialog;

import android.view.View;

import com.paula.android.bechef.R;
import com.paula.android.bechef.bookmark.BookmarkPresenter;
import com.paula.android.bechef.customMain.CustomMainPresenter;
import com.paula.android.bechef.data.LoadDataCallback;
import com.paula.android.bechef.data.LoadDataTask;
import com.paula.android.bechef.data.dao.BookmarkItemDao;
import com.paula.android.bechef.data.dao.ReceiptItemDao;
import com.paula.android.bechef.data.database.ItemDatabase;
import com.paula.android.bechef.data.database.TabDatabase;
import com.paula.android.bechef.data.entity.BaseTab;
import com.paula.android.bechef.data.entity.BookmarkTab;
import com.paula.android.bechef.data.entity.ReceiptTab;
import com.paula.android.bechef.receipt.ReceiptPresenter;

import java.util.ArrayList;

public class MoveToDialog<T> extends AddToBookmarkDialog {
    private CustomMainPresenter<T> mPresenter;
    private String mTabName;

    public MoveToDialog(CustomMainPresenter<T> presenter) {
        mPresenter = presenter;
    }

    @Override
    protected String getTitleText() {
        return "移動至...";
    }

    @Override
    protected void setRatingBar(View view) {
        view.findViewById(R.id.textview_rating).setVisibility(View.GONE);
        view.findViewById(R.id.rating_bar).setVisibility(View.GONE);
    }

    @Override
    protected void manipulateData(String tabName) {
        mTabName = tabName;
        // TODO:
        String dialogTag = getTag();
        new LoadDataTask<>(new LoadDataCallback<ItemDatabase>() {
            private TabDatabase mTabDatabase;

            @Override
            public ItemDatabase getDao() {
                if (mPresenter instanceof BookmarkPresenter) {
                    if (mChosenTab == 0) mTabDatabase = TabDatabase.getBookmarkInstance(mContext);
                    return ItemDatabase.getBookmarkInstance(mContext);
                } else if (mPresenter instanceof ReceiptPresenter) {
                    if (mChosenTab == 0) mTabDatabase = TabDatabase.getReceiptInstance(mContext);
                    return ItemDatabase.getReceiptInstance(getContext());
                }
                return null;
            }

            @Override
            public void doInBackground(ItemDatabase database) {
                if (mPresenter instanceof BookmarkPresenter)
                    updateBookmarkTabUid(database.bookmarkDao(), mTabDatabase);
                else if (mPresenter instanceof ReceiptPresenter)
                    updateReceiptTabUid(database.receiptDao(), mTabDatabase);
            }

            @Override
            public void onCompleted() {
                dismiss();
                mPresenter.leaveChooseDialog();
                mPresenter.dismissEditDialog();
            }
        }).execute();
    }

    private void updateBookmarkTabUid(BookmarkItemDao dao, TabDatabase tabDatabase) {
        long tabUid;
        if (mChosenTab == 0) {
            tabUid = tabDatabase.bookmarkDao().insert(new BookmarkTab(mTabName));
        } else {
            tabUid = ((BaseTab) mBaseTabs.get(mChosenTab - 1)).getUid();
        }
        ArrayList<Long> chosenUids = mPresenter.getChosenUids();
        for (long uid : chosenUids) dao.setNewTabUid(uid, tabUid);
    }

    private void updateReceiptTabUid(ReceiptItemDao dao, TabDatabase tabDatabase) {
        long tabUid;
        if (mChosenTab == 0) {
            tabUid = tabDatabase.receiptDao().insert(new ReceiptTab(mTabName));
        } else {
            tabUid = ((BaseTab) mBaseTabs.get(mChosenTab - 1)).getUid();
        }
        ArrayList<Long> chosenUids = mPresenter.getChosenUids();
        for (long uid : chosenUids) dao.setNewTabUid(uid, tabUid);
    }
}
