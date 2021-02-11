package com.paula.android.bechef.search;

import com.paula.android.bechef.BaseContract;
import com.paula.android.bechef.ChildContract;
import com.paula.android.bechef.api.beans.GetSearchList;
import com.paula.android.bechef.data.entity.BaseItem;
import com.paula.android.bechef.data.entity.BaseTab;

import java.util.ArrayList;

import androidx.recyclerview.widget.RecyclerView;

public interface SearchContract extends ChildContract {
    interface View extends ChildView<Presenter> {
        void updateFilterView(ArrayList<BaseTab> baseTabs);

        void updateResultView(ArrayList<BaseItem> baseItems);
//        void updateResultView(GetSearchList getSearchList);

        void reQuery();

        void updateSearchItems(GetSearchList bean);

        void showLoading(boolean isLoading);

        String getCurrentKeyword();
    }

    interface Presenter extends ChildPresenter {
        void cancelTask();

        void loadResults(ArrayList<BaseTab> chosenTabs);

        boolean isInDiscover();

        void onScrollStateChanged(int childCount, int itemCount, int newState);

        void onScrolled(RecyclerView.LayoutManager layoutManager);
    }
}
