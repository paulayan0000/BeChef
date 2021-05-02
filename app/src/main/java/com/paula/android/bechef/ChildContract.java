package com.paula.android.bechef;

import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.paula.android.bechef.api.beans.YouTubeData;

import java.util.ArrayList;

public interface ChildContract extends BaseContract {
    interface ChildView<P> extends BaseView<P> {
        void showDetailUi(Object content, boolean isBottomShown);
    }

    interface ChildPresenter extends BasePresenter {
        void openDetail(Object content, boolean isBottomShown);
    }

    interface CustomChildView<I> extends ChildView<CustomChildPresenter> {
        void showSelectableUi(boolean isSelectable);

        void updateItems(ArrayList<I> items);

        ArrayList<Long> getChosenItemUids();
    }

    interface CustomChildPresenter extends ChildPresenter {
        void transToSelectable();

        int getDataFilterType();

        void setDataFilterType(int index);

        void loadSpecificItems(int index);

        FragmentManager getFragmentManager();

        long getTabUid();
    }

    interface DiscoverChildView extends ChildView<DiscoverChildPresenter> {
        void updateSearchItems(YouTubeData bean);

        void showLoadingUi();
    }

    interface DiscoverChildPresenter extends ChildPresenter {
        void onScrollStateChanged(int childCount, int itemCount, int newState);

        void onScrolled(RecyclerView.LayoutManager layoutManager);

        void cancelTask();
    }
}
