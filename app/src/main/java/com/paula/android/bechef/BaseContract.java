package com.paula.android.bechef;

import android.content.Context;

import com.paula.android.bechef.data.entity.BaseTab;

import java.util.ArrayList;
import java.util.Collection;

public interface BaseContract {
    interface BaseView<T> {
        void setPresenter(T presenter);

        Context getContext();
    }

    interface BasePresenter {
        void start();
    }

    interface CustomView<T, E> extends BaseView<T> {
//        void showDefaultUi(ArrayList<String> tabTitles);
        void showDefaultUi(ArrayList<?> tabTitles);

        void setToolbar(boolean isShow);

        void refreshCurrentUi();

        void refreshUi(int tabIndex);

        ArrayList<E> getChosenItems();

        int getCurrentTabIndex();
    }

    interface CustomPresenter<E> extends BasePresenter {
        void refreshCurrentData();

        void refreshData(int tabIndex);

        void leaveChooseDialog();

        int getChosenItemsCount();

        ArrayList<E> getChosenItems();

//        ArrayList<String> getTabTitles();
        ArrayList<?> getOtherTabs();

        int getCurrentTabIndex();
    }
}
