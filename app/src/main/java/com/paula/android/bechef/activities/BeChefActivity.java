package com.paula.android.bechef.activities;

import android.content.Context;
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
import android.view.View;
import android.view.animation.TranslateAnimation;
import android.widget.TextView;

import static com.google.android.gms.common.internal.Preconditions.checkNotNull;

public class BeChefActivity extends BaseActivity implements BeChefContract.View {
    private static final String LOG_TAG = BeChefActivity.class.getSimpleName();
    private BeChefContract.Presenter mPresenter;
    private BottomNavigationView mBottomNavigationView;

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
        if (mPresenter.isDetailShown()) showBottomNavigationView(true);
        super.onBackPressed();
    }

    private void init() {
        setContentView(R.layout.activity_bechef);
        mPresenter = new BeChefPresenter(this, getSupportFragmentManager());
        setToolbar();
        mBottomNavigationView = setBottomNavigationView();
        mPresenter.start();
    }

    public void popLogin() {
        startActivityForResult(new Intent(mContext, LoginActivity.class), Constants.LOGIN_ACTIVITY);
    }

    private void setToolbar() {
        TextView statusbarTextView = findViewById(R.id.textview_statusbar);
        statusbarTextView.setHeight(getStatusBarHeight());
    }

    private BottomNavigationView setBottomNavigationView() {
        BottomNavigationView bottomNavigationView = findViewById(R.id.navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        bottomNavigationView.setOnNavigationItemReselectedListener(mOnNavigationItemReselectedListener);
        return bottomNavigationView;
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
    public void setPresenter(BeChefContract.Presenter presenter) {
        mPresenter = checkNotNull(presenter);
    }

    @Override
    public Context getContext() {
        return getContext();
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
                default:
                    return false;
            }
            return true;
        }
    };

    public void transToDetail(Object content) {
        mPresenter.transToDetail(content);
    }

    public void showBottomNavigationView(Boolean isShow) {
        mBottomNavigationView.setVisibility(isShow ? View.VISIBLE : View.GONE);
    }
}
