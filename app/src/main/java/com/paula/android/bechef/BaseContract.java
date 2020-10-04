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
        ArrayList<?> getChosenItems();

        int getCurrentTabIndex();

        void showSelectable(boolean selectable);
    }

    interface CustomPresenter<E> extends BasePresenter {
        void leaveChooseDialog();

        int getChosenItemsCount();

        ArrayList<E> getChosenItems();

        ArrayList<?> getOtherTabs();

        void transToAction(boolean isTrans, FragmentManager childFragmentManager);
    }
}
