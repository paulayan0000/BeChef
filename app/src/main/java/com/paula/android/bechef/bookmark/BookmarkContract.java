package com.paula.android.bechef.bookmark;

import com.paula.android.bechef.BaseContract;
import com.paula.android.bechef.data.entity.BookmarkItem;

import java.util.ArrayList;

import androidx.fragment.app.FragmentManager;

public interface BookmarkContract extends BaseContract {
    interface View extends CustomView<Presenter, BookmarkItem> {
        void showSelectable(boolean selectable);
    }

    interface Presenter extends CustomPresenter<BookmarkItem> {
        void transToAction(boolean isTrans, FragmentManager childFragmentManager);
    }
}
