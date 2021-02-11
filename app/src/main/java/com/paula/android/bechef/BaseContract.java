package com.paula.android.bechef;

import android.content.Context;

import com.paula.android.bechef.data.entity.BaseTab;

import java.util.ArrayList;

public interface BaseContract {
    interface BaseView<P> {
        void setPresenter(P presenter);

        Context getContext();
    }

    interface BasePresenter {
        void start();
    }

    interface MainPresenter extends BasePresenter {
        ArrayList<BaseTab> getTabs();

        void openDetail(Object data);
    }

    interface CustomView<P> extends BaseView<P> {
        int getCurrentTabIndex();

        void showSelectable(boolean selectable);

        ArrayList<Long> getChosenUids();
    }

    interface CustomPresenter extends MainPresenter {
    }
}
