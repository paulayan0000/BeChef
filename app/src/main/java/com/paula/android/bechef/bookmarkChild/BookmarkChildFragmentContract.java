package com.paula.android.bechef.bookmarkChild;

import com.paula.android.bechef.BasePresenter;
import com.paula.android.bechef.BaseView;
import com.paula.android.bechef.api.beans.GetSearchList;

import androidx.recyclerview.widget.RecyclerView;

public interface BookmarkChildFragmentContract {
    interface View extends BaseView<Presenter> {

        void updateData(GetSearchList bean);

        void scrollViewTo(int position);
    }
    interface Presenter extends BasePresenter {
        void scrollTo(int position);
    }
}
