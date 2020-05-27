package com.paula.android.bechef.activities;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.paula.android.bechef.BeChefContract;
import com.paula.android.bechef.BeChefPresenter;
import com.paula.android.bechef.R;
import com.paula.android.bechef.adapters.MainAdapter;
import com.paula.android.bechef.api.beans.GetSearchList;
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
    private GetSearchList mMainContents = new GetSearchList();

    private int mCurrentPagerId = -1;

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

        mPresenter = new BeChefPresenter(this, getSupportFragmentManager());

        mToolbarTitle = findViewById(R.id.textview_toolbar_title);
        setToolbar();


        mTabLayout = findViewById(R.id.tabLayout);
        new TabLayoutMediator(mTabLayout, getViewPager(), true, mTabConfigurationStrategy).attach();

        mPresenter.start();


        BottomNavigationView navigation = findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
    }

    private ViewPager2 getViewPager() {
        ViewPager2 viewPager = findViewById(R.id.viewpager_main_container);
        mMainAdapter = new MainAdapter(mTabTitles, mMainContents, mPresenter);
        viewPager.setAdapter(mMainAdapter);
        viewPager.setOrientation(ViewPager2.ORIENTATION_HORIZONTAL);
        return viewPager;
    }

    public void popLogin() {
        startActivityForResult(new Intent(mContext, LoginActivity.class), Constants.LOGIN_ACTIVITY);
    }

    private void setToolbar() {
        TextView statusbarTextView = findViewById(R.id.textview_statusbar);
        statusbarTextView.setHeight(getStatusBarHeight());
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
    public void showDiscoverUi(ArrayList<String> tabTitles, GetSearchList contents) {
        setToolbarTitle(getResources().getString(R.string.title_discover));
        mMainAdapter.updateData(tabTitles, contents);
        setInfoBarVisibility(false);
    }

    @Override
    public void showBookmarkUi(ArrayList<String> tabTitles, ArrayList<String> contents) {
        setToolbarTitle(getResources().getString(R.string.title_bookmark));
        mMainAdapter.updateData(tabTitles, new GetSearchList());
        setInfoBarVisibility(true);
    }

    @Override
    public void showReceiptUi(ArrayList<String> tabTitles, ArrayList<String> contents) {
        setToolbarTitle(getResources().getString(R.string.title_receipt));
        mMainAdapter.updateData(tabTitles, new GetSearchList());
        setInfoBarVisibility(true);
    }

    @Override
    public void showTodayUi() {
        setToolbarTitle(getResources().getString(R.string.title_menu_list));
        setInfoBarVisibility(true);
    }

    @Override
    public void showMenuListUi(ArrayList<String> tabTitles, ArrayList<String> contents) {
        mMainAdapter.updateData(tabTitles, new GetSearchList());
    }

    @Override
    public void showBuyListUi(ArrayList<String> tabTitles, ArrayList<String> contents) {
        mMainAdapter.updateData(tabTitles, new GetSearchList());
    }

    private void setInfoBarVisibility(boolean visibility) {
        findViewById(R.id.constraintlayout_info).setVisibility(visibility ? View.VISIBLE : View.GONE);
    }

    @Override
    public void updateSearchItems(GetSearchList searchItems) {
        mMainAdapter.updateData(null, searchItems);
    }

    @Override
    public void setPresenter(BeChefContract.Presenter presenter) {
        mPresenter = checkNotNull(presenter);
    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {

            // Show toolbar in AppBarLayout
            AppBarLayout appBarLayout = findViewById(R.id.appbar);
            appBarLayout.setExpanded(true, true);

            if (mCurrentPagerId != item.getItemId()) {
                switch (item.getItemId()) {
                    case R.id.navigation_discover:
                        mPresenter.transToDiscover();
                        break;
                    case R.id.navigation_bookmark:
                        mPresenter.transToBookmark();
                        break;
                    case R.id.navigation_receipt:
                        mPresenter.transToReceipt();
                        break;
                    case R.id.navigation_today:
                        mPresenter.transToToday();
                        break;
                    default:
                        return false;
                }
                mCurrentPagerId = item.getItemId();
                return true;
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
            mToolbarTitle.setTextColor(getResources().getColor(R.color.white));
            toolbarTitleAnother.setTextColor(getResources().getColor(R.color.colorPrimaryLight));
            mToolbarTitle.setOnClickListener(null);
            toolbarTitleAnother.setOnClickListener(null);
            toolbarTitleAnother.setVisibility(View.GONE);
        }
    }
}
