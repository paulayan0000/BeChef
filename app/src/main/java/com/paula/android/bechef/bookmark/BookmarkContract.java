package com.paula.android.bechef.bookmark;

import com.paula.android.bechef.BasePresenter;
import com.paula.android.bechef.BaseView;
import com.paula.android.bechef.api.beans.GetSearchList;

import java.util.ArrayList;

public interface BookmarkContract {
    interface View extends BaseView<Presenter> {


        void showDefaultUi(ArrayList<String> tabtitles, ArrayList<GetSearchList> discoverItems);
    }
    interface Presenter extends BasePresenter {

    }
}
