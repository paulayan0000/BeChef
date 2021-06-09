package com.paula.android.bechef.action;

import com.paula.android.bechef.BaseContract;

public interface ActionContract extends BaseContract {

    interface View extends BaseView<Presenter> {
    }

    interface Presenter extends CustomPresenterForAction {
        void deleteData();

        boolean isFromBookmark();

        int getChosenItemsCount();
    }
}