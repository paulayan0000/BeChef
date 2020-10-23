package com.paula.android.bechef.customChild;

import com.paula.android.bechef.ChildContract;
import com.paula.android.bechef.data.entity.BaseTab;
import com.paula.android.bechef.utils.Constants;

import java.util.ArrayList;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import static com.google.android.gms.common.internal.Preconditions.checkNotNull;

public class CustomChildPresenter<E> implements ChildContract.CustomChildPresenter {
    public int mTabUid;
    protected ArrayList<E> mDataArrayList = new ArrayList<>();
    protected int mDataFilterType = Constants.FILTER_WITH_TIME_DESC;
    protected ChildContract.CustomChildView<CustomChildPresenter, E> mCustomChildFragment;

    public CustomChildPresenter(ChildContract.CustomChildView<CustomChildPresenter, E> customChildFragment, BaseTab baseTab) {
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

    @Override
    public void loadSpecificItems(int type) {
    }

    @Override
    public void loadItems() {
        loadSpecificItems(mDataFilterType);
    }

    @Override
    public void start() {
        loadSpecificItems(Constants.FILTER_WITH_TIME_DESC);
    }

    public FragmentManager getFragmentManager() {
        return ((Fragment) mCustomChildFragment).getChildFragmentManager();
    }
}
