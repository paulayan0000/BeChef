package com.paula.android.bechef.bookmark;

import com.paula.android.bechef.R;
import com.paula.android.bechef.bookmarkChild.BookmarkChildFragment;
import com.paula.android.bechef.customMain.CustomMainFragment;
import com.paula.android.bechef.data.entity.BookmarkItem;
import com.paula.android.bechef.data.entity.BookmarkTab;

import java.util.ArrayList;

import androidx.fragment.app.Fragment;

public class BookmarkFragment extends CustomMainFragment<BookmarkTab, BookmarkItem> {
    public static BookmarkFragment newInstance() {
        return new BookmarkFragment();
    }

    @Override
    protected int getTitleText() {
        return R.string.title_bookmark;
    }

    @Override
    public ArrayList<BookmarkItem> getChosenItems() {
        Fragment childFragment = getChildFragment(mCurrentTabIndex);
        if (childFragment != null) return ((BookmarkChildFragment)childFragment).getChosenItems();
        return new ArrayList<>();
    }

    @Override
    public void refreshUi(int tabIndex) {
        Fragment childFragment = getChildFragment(tabIndex);
        if (childFragment != null) ((BookmarkChildFragment) childFragment).refreshData();
    }
}
