package com.paula.android.bechef;

public interface BeChefContract extends BaseContract {
    interface View extends BaseView<Presenter> {
        void setMenuId(int menuId);
    }

    interface Presenter extends BasePresenter {
        void transToDiscover();

        void transToBookmark();

        void transToReceipt();

        void transToDetail(Object content, boolean isBottomShown);

        void transToSearch(BaseContract.MainPresenter presenter);

        void showToolbar(int itemId);
    }
}
