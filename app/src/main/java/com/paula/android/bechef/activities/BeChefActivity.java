package com.paula.android.bechef.activities;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.paula.android.bechef.R;
import com.paula.android.bechef.user.UserManager;
import com.paula.android.bechef.utils.Constants;

import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;

public class BeChefActivity extends BaseActivity {

    private static final String LOG_TAG = BeChefActivity.class.getSimpleName();
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

    private void init() {
        setContentView(R.layout.activity_bechef);

        mToolbarTitle = findViewById(R.id.textview_toolbar_title);
        setToolbar();
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
    }

    public void popLogin() {
        startActivityForResult(new Intent(mContext, LoginActivity.class), Constants.LOGIN_ACTIVITY);
    }


    private void setToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setPadding(16, 16 + getStatusBarHeight(), 16, 16);
        mToolbarTitle.setText(getResources().getString(R.string.title_discover));
    }

    public int getStatusBarHeight() {
        int result = 0;
        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }


    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_discover:
                    mToolbarTitle.setText(R.string.title_discover);
                    return true;
                case R.id.navigation_bookmark:
                    mToolbarTitle.setText(R.string.title_bookmark);
                    return true;
                case R.id.navigation_receipt:
                    mToolbarTitle.setText(R.string.title_receipt);
                    return true;
                case R.id.navigation_today:
                    mToolbarTitle.setText(R.string.title_today);
                    return true;
                case R.id.navigation_profile:
                    mToolbarTitle.setText(R.string.title_profile);
                    return true;
            }
            return false;
        }
    };

}
