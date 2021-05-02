package com.paula.android.bechef.detail;

import com.paula.android.bechef.BaseContract;
import com.paula.android.bechef.data.entity.BaseItem;
import com.paula.android.bechef.data.entity.DiscoverItem;

public interface DetailContract extends BaseContract {
    interface View extends BaseView<Presenter> {
        void showDetailUi(BaseItem baseItem);

        void showErrorUi(String errorMsg);

        void updateUi(BaseItem baseItem);

        void updateButton(boolean isDiscoverTab);

        void showLoading(boolean isLoading);
    }

    interface Presenter extends BasePresenter {
        void setAllThreadCanceled();

        void refreshData(BaseItem baseItem);

        void addToDiscover(DiscoverItem discoverItem);

        void addToBookmark();

        Object getDataContent();
    }
}