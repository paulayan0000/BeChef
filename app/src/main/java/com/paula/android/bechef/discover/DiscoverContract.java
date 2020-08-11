package com.paula.android.bechef.discover;

import com.paula.android.bechef.BaseContract;
import java.util.ArrayList;

public interface DiscoverContract extends BaseContract {
    interface View extends BaseView<Presenter> {
        void showDiscoverUi(ArrayList<String> tabtitles, ArrayList<String> channelIds);
    }

    interface Presenter extends BasePresenter {
    }
}
