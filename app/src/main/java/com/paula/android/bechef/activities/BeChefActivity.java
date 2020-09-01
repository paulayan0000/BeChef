package com.paula.android.bechef.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

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
    private BottomNavigationView mBottomNavigationView;
    private int mMenuId = R.id.navigation_bookmark;

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

    private void init() {
        setContentView(R.layout.activity_bechef);
        mPresenter = new BeChefPresenter(this, getSupportFragmentManager());
        setToolbar();
        mBottomNavigationView = findViewById(R.id.navigation);
        ;
        mBottomNavigationView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        mPresenter.start();
    }

    public void popLogin() {
        startActivityForResult(new Intent(mContext, LoginActivity.class), Constants.LOGIN_ACTIVITY);
    }

    public void setToolbar() {
        TextView statusbarTextView = findViewById(R.id.textview_statusbar);
        statusbarTextView.setHeight(getStatusBarHeight());
    }

    public int getStatusBarHeight() {
        int statusBarHeight = 0;
        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            statusBarHeight = getResources().getDimensionPixelSize(resourceId);
        }
        return statusBarHeight;
    }

    @Override
    public void setPresenter(BeChefContract.Presenter presenter) {
        mPresenter = checkNotNull(presenter);
    }

    @Override
    public Context getContext() {
        return mContext;
    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            int gotItemId = item.getItemId();
            if (mMenuId != gotItemId) {
                switch (gotItemId) {
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
            } else mPresenter.showToolbar(mMenuId);
            return true;
        }
    };

    public void transToDetail(Object content, boolean isBottomShown) {
        mPresenter.transToDetail(content, isBottomShown);
    }

    public void showBottomNavigationView(Boolean isShow) {
        mBottomNavigationView.setVisibility(isShow ? View.VISIBLE : View.GONE);
    }

    @Override
    public void setMenuId(int menuId) {
        this.mMenuId = menuId;
    }
}
