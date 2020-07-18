package com.paula.android.bechef.discover;

import com.paula.android.bechef.BasePresenter;
import com.paula.android.bechef.BaseView;
import java.util.ArrayList;

public interface DiscoverContract {
    interface View extends BaseView<Presenter> {
        void showDiscoverUi(ArrayList<String> tabtitles);
    }

    interface Presenter extends BasePresenter {
    }
}
