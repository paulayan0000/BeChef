package com.paula.android.bechef.activities;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.paula.android.bechef.BeChefContract;
import com.paula.android.bechef.BeChefPresenter;
import com.paula.android.bechef.R;
import com.paula.android.bechef.user.UserManager;
import com.paula.android.bechef.utils.Constants;

import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;

import static com.google.android.gms.common.internal.Preconditions.checkNotNull;

public class BeChefActivity extends BaseActivity implements BeChefContract.View {

    private static final String LOG_TAG = BeChefActivity.class.getSimpleName();

    private BeChefContract.Presenter mPresenter;

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
        mPresenter.start();

        setToolbar();

        BottomNavigationView navigation = findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        navigation.setOnNavigationItemReselectedListener(mOnNavigationItemReselectedListener);
    }

    public void popLogin() {
        startActivityForResult(new Intent(mContext, LoginActivity.class), Constants.LOGIN_ACTIVITY);
    }

    private void setToolbar() {
        TextView statusbarTextView = findViewById(R.id.textview_statusbar);
        statusbarTextView.setHeight(getStatusBarHeight());
    }

    public int getStatusBarHeight() {
        int result = 0;
        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

    @Override
    public void showDiscoverUi() {
    }

    @Override
    public void showBookmarkUi() {
    }

    @Override
    public void showReceiptUi() {
    }

    @Override
    public void showTodayUi() {
    }

    @Override
    public void showMenuUi() {
    }

    @Override
    public void showBuyUi() {
    }

    @Override
    public void setPresenter(BeChefContract.Presenter presenter) {
        mPresenter = checkNotNull(presenter);
    }

    private BottomNavigationView.OnNavigationItemReselectedListener mOnNavigationItemReselectedListener
            = new BottomNavigationView.OnNavigationItemReselectedListener() {
        @Override
        public void onNavigationItemReselected(@NonNull MenuItem item) {
            AppBarLayout appBarLayout = null;
            switch (item.getItemId()) {
                case R.id.navigation_discover:
                    appBarLayout = findViewById(R.id.discover_appbar);
                    break;
                case R.id.navigation_bookmark:
                    appBarLayout = findViewById(R.id.bookmark_appbar);
                    break;
                case R.id.navigation_receipt:
                case R.id.navigation_today:
                    break;
                default:
            }
            if (appBarLayout != null) appBarLayout.setExpanded(true, true);
        }
    };
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
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
            return true;
        }
    };

//    private void setToolbarTitle(String title) {
//        mToolbarTitle.setText(title);
//        final TextView toolbarTitleAnother = findViewById(R.id.textview_toolbar_title_another);
//
//        if (getResources().getString(R.string.title_menu_list).equals(title)) {
//            toolbarTitleAnother.setVisibility(View.VISIBLE);
//            mToolbarTitle.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    mPresenter.transToMenuList();
//                    mToolbarTitle.setTextColor(getResources().getColor(R.color.white));
//                    toolbarTitleAnother.setTextColor(getResources().getColor(R.color.colorPrimaryLight));
//                }
//            });
//            toolbarTitleAnother.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    mPresenter.transToBuyList();
//                    mToolbarTitle.setTextColor(getResources().getColor(R.color.colorPrimaryLight));
//                    toolbarTitleAnother.setTextColor(getResources().getColor(R.color.white));
//                }
//            });
//        } else {
//            mToolbarTitle.setTextColor(getResources().getColor(R.color.white));
//            toolbarTitleAnother.setTextColor(getResources().getColor(R.color.colorPrimaryLight));
//            mToolbarTitle.setOnClickListener(null);
//            toolbarTitleAnother.setOnClickListener(null);
//            toolbarTitleAnother.setVisibility(View.GONE);
//        }
//    }
}
