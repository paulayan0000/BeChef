package com.paula.android.bechef.discoverChild;

import com.paula.android.bechef.BasePresenter;
import com.paula.android.bechef.BaseView;
import com.paula.android.bechef.api.beans.GetSearchList;

import androidx.recyclerview.widget.RecyclerView;

public interface DiscoverChildFragmentContract {
    interface View extends BaseView<Presenter> {

        void updateSearchItems(GetSearchList bean);
    }
    interface Presenter extends BasePresenter {

        void onScrollStateChanged(int childCount, int itemCount, int newState);

        void onScrolled(RecyclerView.LayoutManager layoutManager);
    }
}
