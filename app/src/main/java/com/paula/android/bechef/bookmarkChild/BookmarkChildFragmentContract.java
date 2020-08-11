package com.paula.android.bechef.bookmarkChild;

import com.paula.android.bechef.ChildContract;
import java.util.ArrayList;

public interface BookmarkChildFragmentContract extends ChildContract {
    interface View extends ChildView<Presenter> {

        void updateData(ArrayList<?> newData);

        void scrollViewTo(int position);

        void showDetailUi(Object content);
    }
    interface Presenter extends ChildPresenter {

        void scrollTo(int position);
    }
}
