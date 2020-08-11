package com.paula.android.bechef;

public interface ChildContract extends BaseContract{
    interface ChildView<T> extends BaseView<T> {
    }
    interface ChildPresenter extends BasePresenter {
        void openDetail(Object content);
    }
}
