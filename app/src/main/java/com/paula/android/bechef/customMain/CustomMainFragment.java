package com.paula.android.bechef.customMain;

import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;

import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.tabs.TabLayout;
import com.paula.android.bechef.BaseContract;
import com.paula.android.bechef.activities.BeChefActivity;
import com.paula.android.bechef.BaseMainFragment;
import com.paula.android.bechef.customChild.CustomChildFragment;
import com.paula.android.bechef.dialog.AlertDialogClickCallback;
import com.paula.android.bechef.dialog.BeChefAlertDialogBuilder;
import com.paula.android.bechef.dialog.EditTabAlertDialogBuilder;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import static com.google.android.gms.common.internal.Preconditions.checkNotNull;

public class CustomMainFragment<T> extends BaseMainFragment<T> implements BaseContract.CustomView<CustomMainPresenter<T>> {
    private CustomMainPresenter<T> mCustomPresenter;
    AlertDialog mEditTabAlertDialog;

    public CustomMainFragment() {
        mCustomPresenter = new CustomMainPresenter<>(this);
    }

    @Override
    public void setPresenter(CustomMainPresenter<T> presenter) {
        mCustomPresenter = checkNotNull(presenter);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mCustomPresenter.start();
    }

    @Override
    protected void customOnConfigureTab(TabLayout.Tab tab, int position) {
        tab.view.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(final View v, MotionEvent event) {
                if (!mIsSelectable) return false;
                if (!v.isSelected() && event.getAction() == MotionEvent.ACTION_DOWN) {
                    int chosenItemsCount = mCustomPresenter.getChosenItemsCount();
                    // No AlertDialog shown if nothing is chosen
                    if (chosenItemsCount == 0) {
                        showSelectable(false);
                        v.performClick();
                        return true;
                    }

                    new BeChefAlertDialogBuilder(mContext).setButtons(new AlertDialogClickCallback() {
                        @Override
                        public void onPositiveButtonClick() {
                            showSelectable(false);
                            v.performClick();
                        }
                    }).setTitle("取消選取")
                            .setMessage("是否將 " + chosenItemsCount + " 個項目取消選取？")
                            .create().show();
                    return false;
                }
                return true;
            }
        });
    }

    public void showSelectable(boolean selectable) {
        if (mIsSelectable == selectable) return;

        mIsSelectable = selectable;
        // Set toolbar and viewpager behavior
        if (mIsSelectable) {
            mLayoutParams.setScrollFlags(AppBarLayout.LayoutParams.SCROLL_FLAG_NO_SCROLL);
        } else {
            mLayoutParams.setScrollFlags(AppBarLayout.LayoutParams.SCROLL_FLAG_SCROLL
                    | AppBarLayout.LayoutParams.SCROLL_FLAG_SNAP
                    | AppBarLayout.LayoutParams.SCROLL_FLAG_ENTER_ALWAYS);
        }
        mAppBarLayout.requestLayout();
        mViewPager.setUserInputEnabled(!mIsSelectable);

        mCustomPresenter.transToAction(mIsSelectable, getChildFragmentManager());
        Fragment childFragment = getChildFragment(getCurrentTabIndex());
        if (!mIsSelectable && childFragment != null)
            ((CustomChildFragment) childFragment).showSelectableUi(false);
        ((BeChefActivity) mContext).showBottomNavigationView(!mIsSelectable);
    }

    protected Fragment getChildFragment(int tabIndex) {
        return getChildFragmentManager().findFragmentByTag("f" + tabIndex);
    }

    public int getCurrentTabIndex() {
        return mViewPager.getCurrentItem();
    }

    public ArrayList<Long> getChosenUids() {
        return new ArrayList<>();
    }

    @Override
    protected void editTab() {
        EditTabAlertDialogBuilder builder = new EditTabAlertDialogBuilder(mContext, mCustomPresenter);
        mEditTabAlertDialog = builder.create();
        mEditTabAlertDialog.show();
    }
}
