package com.paula.android.bechef.bookmark;

import com.paula.android.bechef.customMain.CustomMainFragment;
import com.paula.android.bechef.R;

public class BookmarkFragment extends CustomMainFragment {
    public static BookmarkFragment newInstance() {
        return new BookmarkFragment();
    }

    @Override
    protected int getTitleText() {
        return R.string.title_bookmark;
    }
}