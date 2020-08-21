package com.paula.android.bechef.detail;

import com.paula.android.bechef.BaseContract;

public interface DetailContract extends BaseContract {
    interface View extends BaseView<Presenter> {
        void showDetailUi(Object content);
    }
    interface Presenter extends BasePresenter {
    }
}