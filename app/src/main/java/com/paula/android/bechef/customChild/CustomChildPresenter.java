package com.paula.android.bechef.customChild;

import com.paula.android.bechef.ChildContract;
import com.paula.android.bechef.data.entity.BaseTab;
import com.paula.android.bechef.utils.Constants;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import static com.google.android.gms.common.internal.Preconditions.checkNotNull;

public class CustomChildPresenter<I> implements ChildContract.CustomChildPresenter {
    public long mTabUid;
    protected int mDataFilterType = Constants.FILTER_WITH_TIME_DESC;
    protected ChildContract.CustomChildView<CustomChildPresenter, I> mCustomChildFragment;

    public CustomChildPresenter(ChildContract.CustomChildView<CustomChildPresenter, I> customChildFragment, BaseTab baseTab) {
        mCustomChildFragment = checkNotNull(customChildFragment, "customChildView cannot be null!");
        customChildFragment.setPresenter(this);
        mTabUid = baseTab.getUid();
    }

    @Override
    public void openDetail(Object content, boolean isBottomShown) {
        mCustomChildFragment.showDetailUi(content, isBottomShown);
    }

    @Override
    public void transToSelectable() {
        mCustomChildFragment.showSelectableUi(true);
    }

    public void loadSpecificItems(int type) {
    }

    @Override
    public void start() {
        loadSpecificItems(Constants.FILTER_WITH_TIME_DESC);
    }

    public FragmentManager getFragmentManager() {
        return ((Fragment) mCustomChildFragment).getChildFragmentManager();
    }

    int getDataFilterType() {
        return mDataFilterType;
    }

    void setDataFilterType(int dataFilterType) {
        mDataFilterType = dataFilterType;
    }
}
