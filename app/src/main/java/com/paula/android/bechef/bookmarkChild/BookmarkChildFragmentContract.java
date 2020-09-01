package com.paula.android.bechef.bookmarkChild;

import com.paula.android.bechef.ChildContract;

public interface BookmarkChildFragmentContract extends ChildContract {
    interface View extends CustomChildView<Presenter> {
    }

    interface Presenter extends CustomChildPresenter {
    }
}
