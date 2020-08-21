package com.paula.android.bechef.bookmark;

import com.paula.android.bechef.BaseContract;
import java.util.ArrayList;

public interface BookmarkContract extends BaseContract {
    interface View extends BaseView<Presenter> {
        void showDefaultUi(ArrayList<String> tabTitles);
        void setToolbar();
    }
    interface Presenter extends BasePresenter {
    }
}
