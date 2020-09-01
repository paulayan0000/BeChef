package com.paula.android.bechef.bookmark;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.paula.android.bechef.R;
import com.paula.android.bechef.activities.BeChefActivity;
import com.paula.android.bechef.adapters.FragmentAdapter;
import com.paula.android.bechef.bookmarkChild.BookmarkChildFragment;
import com.paula.android.bechef.data.entity.BaseTab;
import com.paula.android.bechef.data.entity.BookmarkItem;
import com.paula.android.bechef.dialog.BeChefAlertDialogBuilder;
import com.paula.android.bechef.dialog.AlertDialogClickCallback;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import static com.google.android.gms.common.internal.Preconditions.checkNotNull;

public class BookmarkFragment extends Fragment implements BookmarkContract.View, View.OnClickListener {
    private BookmarkContract.Presenter mPresenter;
    private FragmentAdapter mDefaultMainAdapter;
    private AppBarLayout mAppBarLayout;
    private ViewPager2 mViewPager;
    private boolean mIsSelectable = false;
    private BookmarkChildFragment mBookmarkChildFragment;
    private Context mContext;
    private int mCurrentTabIndex = 0;

    public BookmarkFragment() {
        // Required empty public constructor
    }

    public static BookmarkFragment newInstance() {
        return new BookmarkFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);
        mContext = view.getContext();
        mAppBarLayout = view.findViewById(R.id.appbar);
        ((TextView) view.findViewById(R.id.textview_toolbar_title)).setText(R.string.title_bookmark);
        view.findViewById(R.id.imagebutton_edit).setOnClickListener(this);
        view.findViewById(R.id.imagebutton_find).setOnClickListener(this);
        view.findViewById(R.id.imagebutton_toolbar_menu).setOnClickListener(this);
        setViewPagerAndTab(view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mPresenter.start();
    }

    private void setViewPagerAndTab(View view) {
        TabLayout tabLayout = view.findViewById(R.id.tabLayout);
        mViewPager = view.findViewById(R.id.viewpager_main_container);
        mDefaultMainAdapter = new FragmentAdapter(this, new ArrayList<BaseTab>());
        mViewPager.setAdapter(mDefaultMainAdapter);
        mViewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                mCurrentTabIndex = position;
            }
        });
        TabLayoutMediator.TabConfigurationStrategy tabConfigurationStrategy = new TabLayoutMediator.TabConfigurationStrategy() {
            @Override
            public void onConfigureTab(@NonNull final TabLayout.Tab tab, int position) {
//                tab.setText(mDefaultMainAdapter.getTabTitles().get(position));
                tab.setText(((BaseTab) mDefaultMainAdapter.getTabArrayList().get(position)).getTabName());

                tab.view.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(final View v, MotionEvent event) {
                        if (!mIsSelectable) return false;
                        if (!v.isSelected() && event.getAction() == MotionEvent.ACTION_DOWN) {
                            int chosenItemsCount = mPresenter.getChosenItemsCount();
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
        };

        new TabLayoutMediator(tabLayout, mViewPager, true, tabConfigurationStrategy).attach();
    }

    @Override
    public void setPresenter(BookmarkContract.Presenter presenter) {
        mPresenter = checkNotNull(presenter);
    }

    @Override
    public void setToolbar(boolean isShow) {
        mAppBarLayout.setExpanded(isShow, true);
    }

    @Override
    public void showDefaultUi(ArrayList<?> tabs) {
        mDefaultMainAdapter.updateData(tabs);
    }

    @Override
    public void showSelectable(boolean selectable) {
        if (mIsSelectable == selectable) return;

        mIsSelectable = selectable;
        mPresenter.transToAction(mIsSelectable, getChildFragmentManager());
        mViewPager.setUserInputEnabled(!mIsSelectable);
        if (!mIsSelectable) mBookmarkChildFragment.showSelectableUi(false);
        ((BeChefActivity) mContext).showBottomNavigationView(!mIsSelectable);
    }

    @Override
    public ArrayList<BookmarkItem> getChosenItems() {
        return mBookmarkChildFragment.getChosenItems();
    }

    @Override
    public void refreshCurrentUi() {
//        mBookmarkChildFragment.refreshData();
        refreshUi(mCurrentTabIndex);
    }

    @Override
    public void refreshUi(int tabIndex) {
        Fragment childFragment = getChildFragmentManager().findFragmentByTag("f" + tabIndex);
        if (childFragment != null) ((BookmarkChildFragment) childFragment).refreshData();
    }

    public void setBookmarkChildFragment(BookmarkChildFragment bookmarkChildFragment) {
        mBookmarkChildFragment = bookmarkChildFragment;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.imagebutton_edit:
                if (!mIsSelectable) Toast.makeText(getContext(), "edit", Toast.LENGTH_SHORT).show();
                break;
            case R.id.imagebutton_find:
                if (!mIsSelectable) Toast.makeText(getContext(), "find", Toast.LENGTH_SHORT).show();
                break;
            case R.id.imagebutton_toolbar_menu:
                if (!mIsSelectable) Toast.makeText(getContext(), "menu", Toast.LENGTH_SHORT).show();
                break;
        }
    }

    @Override
    public int getCurrentTabIndex() {
        return mCurrentTabIndex;
    }
}
