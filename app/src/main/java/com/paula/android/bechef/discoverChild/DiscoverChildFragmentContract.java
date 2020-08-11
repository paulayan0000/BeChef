package com.paula.android.bechef.discoverChild;

import com.paula.android.bechef.ChildContract;
import com.paula.android.bechef.api.beans.GetSearchList;
import androidx.recyclerview.widget.RecyclerView;

public interface DiscoverChildFragmentContract extends ChildContract{
    interface View extends ChildView<Presenter> {

        void updateSearchItems(GetSearchList bean);

        void showDetailUi(Object content);
    }
    interface Presenter extends ChildPresenter {

        void onScrollStateChanged(int childCount, int itemCount, int newState);

        void onScrolled(RecyclerView.LayoutManager layoutManager);
    }
}
