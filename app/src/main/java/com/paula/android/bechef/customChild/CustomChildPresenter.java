package com.paula.android.bechef.customChild;

import android.content.Context;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;

import com.paula.android.bechef.ChildContract;
import com.paula.android.bechef.utils.Constants;

import java.util.ArrayList;
import java.util.List;

import static com.google.android.gms.common.internal.Preconditions.checkNotNull;

public class CustomChildPresenter<I> implements ChildContract.CustomChildPresenter {
    private final ChildContract.CustomChildView<I> mCustomChildFragment;
    protected long mTabUid;
    protected int mDataFilterType = Constants.FILTER_WITH_TIME_DESC;

    public CustomChildPresenter(ChildContract.CustomChildView<I> customChildFragment, long tabUid) {
        mCustomChildFragment = checkNotNull(customChildFragment, "customChildView cannot be null!");
        customChildFragment.setCustomMainPresenter(this);
        mTabUid = tabUid;
    }

    @Override
    public Context getContext() {
        return mCustomChildFragment.getContext();
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
    public void start() {
        loadSpecificItems(Constants.FILTER_WITH_TIME_DESC);
    }

    public FragmentManager getFragmentManager() {
        return ((Fragment) mCustomChildFragment).getChildFragmentManager();
    }

    @Override
    public int getDataFilterType() {
        return mDataFilterType;
    }

    @Override
    public void setDataFilterType(int dataFilterType) {
        mDataFilterType = dataFilterType;
    }

    protected void addDataObserver(LiveData<List<I>> liveData) {
        liveData.observe((LifecycleOwner) mCustomChildFragment, new Observer<List<I>>() {
            @Override
            public void onChanged(List<I> baseItems) {
                mCustomChildFragment.updateItems(new ArrayList<>(baseItems));
            }
        });
    }

    @Override
    public long getTabUid() {
        return mTabUid;
    }
}
