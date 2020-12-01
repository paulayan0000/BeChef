package com.paula.android.bechef;

import java.util.ArrayList;

public interface ChildContract extends BaseContract {
    interface ChildView<P> extends BaseView<P> {
        void showDetailUi(Object content, boolean isBottomShown);
    }

    interface ChildPresenter extends BasePresenter {
        void openDetail(Object content, boolean isBottomShown);
    }

    interface CustomChildView<P, I> extends ChildView<P> {
        void showSelectableUi(boolean isSelectable);

        void updateItems(ArrayList<I> items);
    }

    interface CustomChildPresenter extends ChildPresenter {
        void transToSelectable();
    }
}
