package com.paula.android.bechef.bookmark;

import androidx.lifecycle.Observer;

import com.paula.android.bechef.customMain.CustomMainFragment;
import com.paula.android.bechef.customMain.CustomMainPresenter;
import com.paula.android.bechef.data.database.TabDatabase;
import com.paula.android.bechef.data.entity.BaseTab;
import com.paula.android.bechef.data.entity.BookmarkTab;

import java.util.ArrayList;
import java.util.List;

public class BookmarkPresenter extends CustomMainPresenter {

    public BookmarkPresenter(CustomMainFragment customView) {
        super(customView);
    }

    @Override
    public void start() {
        loadBookmarkTabs();
    }

    private void loadBookmarkTabs() {
        TabDatabase.getTabInstance(getContext()).bookmarkDao().getAllLive()
                .observe(mCustomMainView, new Observer<List<BookmarkTab>>() {
                    @Override
                    public void onChanged(List<BookmarkTab> bookmarkTabs) {
                        ArrayList<BaseTab> baseTabs = new ArrayList<BaseTab>(bookmarkTabs);
                        setTabs(baseTabs);
                        mCustomMainView.showDefaultUi(baseTabs);
                    }
                });
    }
}
