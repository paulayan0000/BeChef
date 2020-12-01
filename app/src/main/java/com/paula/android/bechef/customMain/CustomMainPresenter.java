package com.paula.android.bechef.customMain;

import com.paula.android.bechef.BaseContract;
import com.paula.android.bechef.R;
import com.paula.android.bechef.action.ActionChooseFragment;
import com.paula.android.bechef.data.entity.BaseTab;

import java.util.ArrayList;

import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import static com.google.android.gms.common.internal.Preconditions.checkNotNull;

public class CustomMainPresenter<T> implements BaseContract.CustomPresenter {
    protected CustomMainFragment<T> mCustomView;
    private ArrayList<BaseTab> mTabs;
    private ActionChooseFragment mActionChooseFragment;
    private FragmentTransaction mTransaction;

    public CustomMainPresenter(CustomMainFragment<T> customView) {
        mCustomView = checkNotNull(customView, "customView cannot be null!");
        mCustomView.setPresenter(this);
    }

    void transToAction(boolean isTrans, FragmentManager fragmentManager) {
        if (fragmentManager != null) mTransaction = fragmentManager.beginTransaction();

        if (isTrans && mActionChooseFragment != null) {
            mTransaction.show(mActionChooseFragment).commit();
        } else if (isTrans) {
            mActionChooseFragment = new ActionChooseFragment<>(this);
            mTransaction.add(R.id.toolbar, mActionChooseFragment).commit();
        } else if (mActionChooseFragment != null) {
            mTransaction.hide(mActionChooseFragment).commit();
        }
    }

    public void leaveChooseDialog() {
        mCustomView.showSelectable(false);
    }

    public int getChosenItemsCount() {
        return getChosenUids().size();
    }

    public ArrayList<Long> getChosenUids() {
        return mCustomView.getChosenUids();
    }

    public ArrayList<BaseTab> getOtherTabs() {
        return getOtherTabs(getCurrentTabIndex());
    }

    public ArrayList<BaseTab> getOtherTabs(int removedIndex) {
        ArrayList<BaseTab> otherTabs = new ArrayList<>(mTabs);
        otherTabs.remove(removedIndex);
        return otherTabs;
    }

    public void setTabs(ArrayList<BaseTab> tabs) {
        mTabs = tabs;
    }

    public ArrayList<BaseTab> getTabs() {
        return mTabs;
    }

    private int getCurrentTabIndex() {
        return mCustomView.getCurrentTabIndex();
    }

    public void start() {
    }

    public FragmentManager getFragmentManager() {
        return mCustomView.getChildFragmentManager();
    }

    public void dismissEditDialog() {
        if (mCustomView.mEditTabAlertDialog != null) mCustomView.mEditTabAlertDialog.dismiss();
    }
}
