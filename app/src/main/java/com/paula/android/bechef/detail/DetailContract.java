package com.paula.android.bechef.detail;

import com.paula.android.bechef.BaseContract;
import com.paula.android.bechef.data.entity.BaseItem;

public interface DetailContract extends BaseContract {
    interface View extends BaseView<Presenter> {
        void showDetailUi(Object content);
        void updateUi(BaseItem baseItem);
    }

    interface Presenter extends BasePresenter {
        void refreshData(BaseItem baseItem);
    }
}