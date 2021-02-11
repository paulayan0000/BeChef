package com.paula.android.bechef.detail;

import com.paula.android.bechef.BaseContract;
import com.paula.android.bechef.data.entity.BaseItem;

public interface DetailContract extends BaseContract {
    interface View extends BaseView<Presenter> {
        void showDetailUi(BaseItem content);
        void showErrorUi(String errorMsg);
        void updateUi(BaseItem baseItem);
        void showLoading(boolean isLoading);
    }

    interface Presenter extends BasePresenter {
        void refreshData(BaseItem baseItem);
        void stopAllAsyncTasks();
    }
}