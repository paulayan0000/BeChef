package com.paula.android.bechef.activities;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.paula.android.bechef.BeChefContract;
import com.paula.android.bechef.BeChefPresenter;
import com.paula.android.bechef.R;
import com.paula.android.bechef.user.UserManager;
import com.paula.android.bechef.utils.Constants;

import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import static com.google.android.gms.common.internal.Preconditions.checkNotNull;

public class BeChefActivity extends BaseActivity implements BeChefContract.View {

    private static final String LOG_TAG = BeChefActivity.class.getSimpleName();

    private BeChefContract.Presenter mPresenter;
    private TextView mToolbarTitle;

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

        mPresenter = new BeChefPresenter(this, getSupportFragmentManager());
        mPresenter.start();

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
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



    @Override
    public void showDiscoverUi() {
        setToolbarTitle(getResources().getString(R.string.title_discover));
    }

    @Override
    public void showBookmarkUi() {
        setToolbarTitle(getResources().getString(R.string.title_bookmark));
    }

    @Override
    public void showReceiptUi() {
        setToolbarTitle(getResources().getString(R.string.title_receipt));
    }

    @Override
    public void showTodayUi() {
        setToolbarTitle(getResources().getString(R.string.title_today));
    }

    @Override
    public void setPresenter(BeChefContract.Presenter presenter) {
        mPresenter = checkNotNull(presenter);
    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
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
    }

}
