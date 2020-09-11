package com.paula.android.bechef;

import android.content.Context;
import java.util.ArrayList;
import androidx.fragment.app.FragmentManager;

public interface BaseContract {
    interface BaseView<T> {
        void setPresenter(T presenter);

        Context getContext();
    }

    interface BasePresenter {
        void start();
    }

    interface CustomView<T> extends BaseView<T> {
        void refreshCurrentUi();

        void refreshUi(int tabIndex);

        ArrayList<?> getChosenItems();

        int getCurrentTabIndex();

        void showSelectable(boolean selectable);
    }

    interface CustomPresenter<E> extends BasePresenter {
        void refreshCurrentData();

        void refreshData(int tabIndex);

        void leaveChooseDialog();

        int getChosenItemsCount();

        ArrayList<E> getChosenItems();

        ArrayList<?> getOtherTabs();

        int getCurrentTabIndex();

        void transToAction(boolean isTrans, FragmentManager childFragmentManager);
    }
}
