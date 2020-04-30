package com.paula.android.bechef.activities;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.paula.android.bechef.BeChefContract;
import com.paula.android.bechef.BeChefPresenter;
import com.paula.android.bechef.R;
import com.paula.android.bechef.adapters.MainAdapter;
import com.paula.android.bechef.user.UserManager;
import com.paula.android.bechef.utils.Constants;

import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;

import static com.google.android.gms.common.internal.Preconditions.checkNotNull;

public class BeChefActivity extends BaseActivity implements BeChefContract.View {

    private static final String LOG_TAG = BeChefActivity.class.getSimpleName();

    private BeChefContract.Presenter mPresenter;
    private TextView mToolbarTitle;
    private TabLayout mTabLayout;
    private MainAdapter mMainAdapter;

    private ArrayList<String> mTabTitles = new ArrayList<>();
    private ArrayList<String> mRecyclerViewContents = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (UserManager.getInstance().isLoginStatus()) {
            init();
        } else {
            popLogin();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d(LOG_TAG, "onActivityResult| requestCode: " + requestCode + " | resultCode: " + resultCode);
        if (requestCode == Constants.LOGIN_ACTIVITY && resultCode == Constants.LOGIN_SUCCESS) {
            init();
        } else if (requestCode == Constants.LOGIN_ACTIVITY && resultCode == Constants.LOGIN_EXIT) {
            finish();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    private void init() {
        setContentView(R.layout.activity_bechef);

        mToolbarTitle = findViewById(R.id.textview_toolbar_title);
        setToolbar();

        mTabLayout = findViewById(R.id.tabLayout);
        new TabLayoutMediator(mTabLayout, getViewPager(), true, mTabConfigurationStrategy).attach();

        mPresenter = new BeChefPresenter(this, getSupportFragmentManager());
        mPresenter.start();

        BottomNavigationView navigation = findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
    }

    private ViewPager2 getViewPager() {
        ViewPager2 viewPager = findViewById(R.id.viewpager_main_container);
        mMainAdapter = new MainAdapter(mTabTitles, mRecyclerViewContents);
        viewPager.setAdapter(mMainAdapter);
        viewPager.setOrientation(ViewPager2.ORIENTATION_HORIZONTAL);
        return viewPager;
    }

    public void popLogin() {
        startActivityForResult(new Intent(mContext, LoginActivity.class), Constants.LOGIN_ACTIVITY);
    }

    private void setToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setPadding(16, 16 + getStatusBarHeight(), 16, 16);
        setToolbarTitle(getResources().getString(R.string.title_discover));
    }

    public int getStatusBarHeight() {
        int result = 0;
        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

    private TabLayoutMediator.TabConfigurationStrategy mTabConfigurationStrategy = new TabLayoutMediator.TabConfigurationStrategy() {
        @Override
        public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
            tab.setText(mMainAdapter.getTabTitles().get(position));
        }
    };

    @Override
    public void showDiscoverUi(ArrayList<String> tabTitles, ArrayList<String> contents) {
        setToolbarTitle(getResources().getString(R.string.title_discover));
        mMainAdapter.updateData(tabTitles, contents);
    }


    @Override
    public void showBookmarkUi(ArrayList<String> tabTitles, ArrayList<String> contents) {
        setToolbarTitle(getResources().getString(R.string.title_bookmark));
        mMainAdapter.updateData(tabTitles, contents);
    }

    @Override
    public void showReceiptUi(ArrayList<String> tabTitles, ArrayList<String> contents) {
        setToolbarTitle(getResources().getString(R.string.title_receipt));
        mMainAdapter.updateData(tabTitles, contents);
    }

    @Override
    public void showTodayUi() {
        setToolbarTitle(getResources().getString(R.string.title_menu_list));
    }

    @Override
    public void showMenuListUi(ArrayList<String> tabTitles, ArrayList<String> contents) {
        mMainAdapter.updateData(tabTitles, contents);
    }

    @Override
    public void showBuyListUi(ArrayList<String> tabTitles, ArrayList<String> contents) {
        mMainAdapter.updateData(tabTitles, contents);
    }

    @Override
    public void setPresenter(BeChefContract.Presenter presenter) {
        mPresenter = checkNotNull(presenter);
    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            // Set tablayout and recyclerview initially
            TabLayout.Tab firstTab = mTabLayout.getTabAt(0);
            if (firstTab != null) firstTab.select();

            switch (item.getItemId()) {
                case R.id.navigation_discover:
                    mPresenter.transToDiscover();
                    return true;
                case R.id.navigation_bookmark:
                    mPresenter.transToBookmark();
                    return true;
                case R.id.navigation_receipt:
                    mPresenter.transToReceipt();
                    return true;
                case R.id.navigation_today:
                    mPresenter.transToToday();
                    return true;
                default:
            }
            return false;
        }
    };

    private void setToolbarTitle(String title) {
        mToolbarTitle.setText(title);
        final TextView toolbarTitleAnother = findViewById(R.id.textview_toolbar_title_another);

        if (getResources().getString(R.string.title_menu_list).equals(title)) {
            toolbarTitleAnother.setVisibility(View.VISIBLE);
            mToolbarTitle.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mPresenter.transToMenuList();
                    mToolbarTitle.setTextColor(getResources().getColor(R.color.white));
                    toolbarTitleAnother.setTextColor(getResources().getColor(R.color.colorPrimaryLight));
                }
            });
            toolbarTitleAnother.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mPresenter.transToBuyList();
                    mToolbarTitle.setTextColor(getResources().getColor(R.color.colorPrimaryLight));
                    toolbarTitleAnother.setTextColor(getResources().getColor(R.color.white));
                }
            });
        } else {
            mToolbarTitle.setOnClickListener(null);
            toolbarTitleAnother.setOnClickListener(null);
            toolbarTitleAnother.setVisibility(View.GONE);
        }
    }
}
