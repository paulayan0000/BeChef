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

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import static com.google.android.gms.common.internal.Preconditions.checkNotNull;

public class CustomMainFragment<T, E> extends BaseMainFragment implements BaseContract.CustomView<CustomMainPresenter> {
    private CustomMainPresenter mCustomPresenter;
    protected int mCurrentTabIndex = 0;

    public CustomMainFragment() {
        mCustomPresenter = new CustomMainPresenter<>(this);
    }

    @Override
    public void setPresenter(CustomMainPresenter presenter) {
        mCustomPresenter = checkNotNull(presenter);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mCustomPresenter.start();
    }

    @Override
    protected void setOnPageChangeCallback() {
        mViewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                mCurrentTabIndex = position;
            }
        });
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

    @Override
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
        Fragment childFragment = getChildFragment(mCurrentTabIndex);
        if (!mIsSelectable && childFragment != null)
            ((CustomChildFragment) childFragment).showSelectableUi(false);
        ((BeChefActivity) mContext).showBottomNavigationView(!mIsSelectable);
    }

    protected Fragment getChildFragment(int tabIndex) {
        return getChildFragmentManager().findFragmentByTag("f" + tabIndex);
    }

    @Override
    public void refreshCurrentUi() {
        refreshUi(mCurrentTabIndex);
    }

    @Override
    public void refreshUi(int tabIndex) {
    }

    public int getCurrentTabIndex() {
        return mCurrentTabIndex;
    }

    public ArrayList<E> getChosenItems() {
        return new ArrayList<>();
    }
}
