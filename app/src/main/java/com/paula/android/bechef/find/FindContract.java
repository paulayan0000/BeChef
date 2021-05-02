package com.paula.android.bechef.find;

import androidx.recyclerview.widget.RecyclerView;

import com.paula.android.bechef.ChildContract;
import com.paula.android.bechef.api.beans.YouTubeData;
import com.paula.android.bechef.data.entity.BaseItem;
import com.paula.android.bechef.data.entity.BaseTab;

import java.util.ArrayList;

public interface FindContract extends ChildContract {
    interface View extends ChildView<Presenter> {
        void setFindConditions(ArrayList<BaseTab> baseTabs);

        void updateFilterResult(ArrayList<BaseItem> baseItems);

        void reQuery();

        void updateSearchResult(YouTubeData bean);

        void showLoadingUi();

        String getCurrentKeyword();

        ArrayList<BaseTab> getChosenTabs();

        String getChosenVideoType();

        String getChosenFilterRange();

        long getChosenTabUid();
    }

    interface Presenter extends ChildPresenter {
        void cancelTask();

        void loadResults();

        boolean isFromDiscover();

        void onScrollStateChanged(int childCount, int itemCount, int newState);

        void onScrolled(RecyclerView.LayoutManager layoutManager);
    }
}
