package com.paula.android.bechef;

import android.content.Context;

import androidx.fragment.app.FragmentManager;

import com.paula.android.bechef.data.entity.BaseTab;

import java.util.ArrayList;

public interface BaseContract {
    interface BaseView<P> {
        void setCustomMainPresenter(P customMainPresenter);

        Context getContext();
    }

    interface BasePresenter {
        void start();

        Context getContext();
    }

    interface MainPresenter extends BasePresenter {
        ArrayList<BaseTab> getTabs();
    }

    interface CustomView extends BaseView<CustomPresenter> {
        void showSelectable(boolean selectable);

        ArrayList<Long> getChosenItemUids();
    }

    interface CustomPresenter extends MainPresenter {
        void transToAction(boolean isSelectable, FragmentManager childFragmentManager);

        int getChosenItemsCount();
    }

    interface CustomPresenterForAction extends BasePresenter{
        void leaveChooseMode();

        ArrayList<BaseTab> getOtherTabs();

        ArrayList<Long> getChosenItemUids();

        void dismissEditDialog();
    }
}
