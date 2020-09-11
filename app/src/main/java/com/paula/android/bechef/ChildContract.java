package com.paula.android.bechef;

import java.util.ArrayList;

public interface ChildContract extends BaseContract {
    interface ChildView<T> extends BaseView<T> {
        void showDetailUi(Object content, boolean isBottomShown);
    }

    interface ChildPresenter extends BasePresenter {
        void openDetail(Object content, boolean isBottomShown);
    }

    interface CustomChildView<T, E> extends ChildView<T> {
        void showSelectableUi(boolean isSelectable);

        void updateItems(ArrayList<E> items);

        void refreshData();

        ArrayList<E> getChosenItems();
    }

    interface CustomChildPresenter extends ChildPresenter {
        void transToSelectable();

        void loadSpecificItems(int type);

        void loadItems();
    }
}
