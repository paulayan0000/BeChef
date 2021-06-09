package com.paula.android.bechef.customMain;

import android.app.Activity;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.paula.android.bechef.action.ActionFragment;
import com.paula.android.bechef.BaseContract;
import com.paula.android.bechef.action.ActionPresenter;
import com.paula.android.bechef.data.entity.BaseTab;
import com.paula.android.bechef.R;

import java.util.ArrayList;

import static com.google.android.gms.common.internal.Preconditions.checkNotNull;

public class CustomMainPresenter
        implements BaseContract.CustomPresenter, BaseContract.CustomPresenterForAction {
    private ArrayList<BaseTab> mTabs;
    private ActionFragment mActionFragment;
    private FragmentTransaction mTransaction;
    protected CustomMainFragment mCustomMainView;

    public CustomMainPresenter(CustomMainFragment customMainView) {
        mCustomMainView = checkNotNull(customMainView, "customMainView cannot be null!");
        mCustomMainView.setCustomMainPresenter(this);
    }

    @Override
    public void transToAction(boolean isTrans, FragmentManager fragmentManager) {
        if (fragmentManager != null) mTransaction = fragmentManager.beginTransaction();
        if (!isTrans && mActionFragment != null) {
            mTransaction.hide(mActionFragment);
            mTransaction.commit();
            return;
        }

        // If it's in selectable mode
        mActionFragment = new ActionFragment();
        if (!mActionFragment.isAdded()) {
            mTransaction.add(R.id.constraintlayout_toolbar, mActionFragment);
        } else {
            mTransaction.show(mActionFragment);
        }
        new ActionPresenter(mActionFragment, this);
        mTransaction.commit();
    }

    @Override
    public void leaveChooseMode() {
        mCustomMainView.showSelectable(false);
    }

    @Override
    public int getChosenItemsCount() {
        return getChosenItemUids().size();
    }

    @Override
    public ArrayList<Long> getChosenItemUids() {
        return mCustomMainView.getChosenItemUids();
    }

    @Override
    public ArrayList<BaseTab> getOtherTabs() {
        ArrayList<BaseTab> otherTabs = new ArrayList<>(mTabs);
        otherTabs.remove(mCustomMainView.getCurrentTabIndex());
        return otherTabs;
    }

    public void setTabs(ArrayList<BaseTab> tabs) {
        mTabs = tabs;
    }

    @Override
    public ArrayList<BaseTab> getTabs() {
        return mTabs;
    }

    @Override
    public void start() {
    }

    @Override
    public Activity getActivity() {
        return ((CustomMainFragment) mCustomMainView).getActivity();
    }

    @Override
    public void dismissEditDialog() {
        AlertDialog alertDialog = mCustomMainView.getEditTabAlertDialog();
        if (alertDialog != null) alertDialog.dismiss();
    }
}