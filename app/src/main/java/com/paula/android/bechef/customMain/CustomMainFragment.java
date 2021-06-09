package com.paula.android.bechef.customMain;

import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.tabs.TabLayout;
import com.paula.android.bechef.R;
import com.paula.android.bechef.activities.BeChefActivity;
import com.paula.android.bechef.BaseContract;
import com.paula.android.bechef.BaseMainFragment;
import com.paula.android.bechef.customChild.CustomChildFragment;
import com.paula.android.bechef.dialog.AlertDialogClickCallback;
import com.paula.android.bechef.dialog.BeChefAlertDialogBuilder;
import com.paula.android.bechef.dialog.EditTabAlertDialogBuilder;

import java.util.ArrayList;

import static com.google.android.gms.common.internal.Preconditions.checkNotNull;

public class CustomMainFragment extends BaseMainFragment implements BaseContract.CustomView {
    private BaseContract.CustomPresenter mCustomPresenter;
    private AlertDialog mEditTabAlertDialog;

    public CustomMainFragment() {
        mCustomPresenter = new CustomMainPresenter(this);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mCustomPresenter.start();
    }

    @Override
    public void setCustomMainPresenter(BaseContract.CustomPresenter customMainPresenter) {
        mCustomPresenter = checkNotNull(customMainPresenter);
    }

    @Override
    protected void customOnConfigureTab(TabLayout.Tab tab, int position) {
        tab.view.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(final View v, MotionEvent event) {
                if (!mIsSelectable) return false;
                // If item is not selected and touched in choose mode
                if (!v.isSelected() && event.getAction() == MotionEvent.ACTION_DOWN) {
                    int chosenItemsCount = mCustomPresenter.getChosenItemsCount();
                    // No AlertDialog shown if nothing is chosen
                    if (chosenItemsCount == 0) {
                        showSelectable(false);
                        v.performClick();
                        return true;
                    }

                    new BeChefAlertDialogBuilder(getActivity()).setButtons(new AlertDialogClickCallback() {
                        @Override
                        public boolean onPositiveButtonClick() {
                            showSelectable(false);
                            v.performClick();
                            return true;
                        }
                    }).setTitle(getString(R.string.action_cancel_choose))
                            .setMessage(String.format(getString(R.string.msg_action),
                                    chosenItemsCount,
                                    getString(R.string.action_cancel_choose)))
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
        Fragment childFragment = getChildFragment(getCurrentTabIndex());
        if (!mIsSelectable && childFragment != null) {
            ((CustomChildFragment) childFragment).showSelectableUi(false);
        }
        ((BeChefActivity) getActivity()).showBottomNavigationView(!mIsSelectable);
    }

    public ArrayList<Long> getChosenItemUids() {
        Fragment childFragment = getChildFragment(getCurrentTabIndex());
        if (childFragment != null) {
            return ((CustomChildFragment) childFragment).getChosenItemUids();
        }
        return new ArrayList<>();
    }

    @Override
    protected void editTab() {
        EditTabAlertDialogBuilder builder
                = new EditTabAlertDialogBuilder(getActivity(), mCustomPresenter.getTabs());
        mEditTabAlertDialog = builder.create();
        mEditTabAlertDialog.show();
        Window window = mEditTabAlertDialog.getWindow();
        if (window == null) return;
        window.clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                | WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
    }

    @Override
    protected void find() {
        ((BeChefActivity) getActivity()).showSearchUi(mCustomPresenter);
    }

    AlertDialog getEditTabAlertDialog() {
        return mEditTabAlertDialog;
    }
}