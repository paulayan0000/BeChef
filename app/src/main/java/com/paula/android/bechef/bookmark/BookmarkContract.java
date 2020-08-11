package com.paula.android.bechef.bookmark;

import com.paula.android.bechef.BaseContract;
import com.paula.android.bechef.api.beans.GetSearchList;
import java.util.ArrayList;

public interface BookmarkContract extends BaseContract {
    interface View extends BaseView<Presenter> {
        void showDefaultUi(ArrayList<String> tabtitles, ArrayList<GetSearchList> discoverItems);
    }
    interface Presenter extends BasePresenter {

    }
}
