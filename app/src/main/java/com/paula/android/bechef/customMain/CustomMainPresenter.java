package com.paula.android.bechef.customMain;

import com.paula.android.bechef.BaseContract;
import com.paula.android.bechef.R;
import com.paula.android.bechef.action.ActionChooseFragment;
import com.paula.android.bechef.data.entity.BaseItem;
import com.paula.android.bechef.data.entity.BookmarkItem;

import java.util.ArrayList;

import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import static com.google.android.gms.common.internal.Preconditions.checkNotNull;

public class CustomMainPresenter<T, E> implements BaseContract.CustomPresenter {
    protected CustomMainFragment<T, E> mCustomView;
    private ArrayList<T> mTabs;
    private ActionChooseFragment mActionChooseFragment;
    private FragmentTransaction mTransaction;

    public CustomMainPresenter(CustomMainFragment<T, E> customView) {
        mCustomView = checkNotNull(customView, "customView cannot be null!");
        mCustomView.setPresenter(this);
    }

    public void transToAction(boolean isTrans, FragmentManager fragmentManager) {
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
        return getChosenItems().size();
    }

    public ArrayList<E> getChosenItems() {
        return mCustomView.getChosenItems();
    }

    public ArrayList<?> getOtherTabs() {
        ArrayList<T> otherTabs = new ArrayList<>(mTabs);
        otherTabs.remove(getCurrentTabIndex());
        return otherTabs;
    }

    public void refreshCurrentData() {
        mCustomView.refreshCurrentUi();
    }

    public void refreshData(int tabIndex) {
        mCustomView.refreshUi(tabIndex);
    }

    public int getCurrentTabIndex() {
        return mCustomView.getCurrentTabIndex();
    }

    public void start() {
    }

    public void setTabs(ArrayList<T> tabs) {
        mTabs = tabs;
    }
}
