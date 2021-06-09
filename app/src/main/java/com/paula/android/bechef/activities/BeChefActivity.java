package com.paula.android.bechef.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.paula.android.bechef.BaseContract;
import com.paula.android.bechef.BeChefContract;
import com.paula.android.bechef.BeChefPresenter;
import com.paula.android.bechef.R;
import com.paula.android.bechef.user.UserManager;
import com.paula.android.bechef.utils.Constants;

import static com.google.android.gms.common.internal.Preconditions.checkNotNull;

public class BeChefActivity extends BaseActivity implements BeChefContract.View {
    private BeChefContract.Presenter mPresenter;
    private BottomNavigationView mBottomNavigationView;
    private int mMenuId = R.id.navigation_bookmark;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (!UserManager.getInstance().isLoginStatus()) popLogin();
        else init();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Constants.LOGIN_ACTIVITY && resultCode == Constants.LOGIN_SUCCESS) {
            init();
        }
    }

    private void init() {
        setContentView(R.layout.activity_bechef);
        mPresenter = new BeChefPresenter(this, getSupportFragmentManager());

        TextView tvStatusbar = findViewById(R.id.textview_statusbar);
        tvStatusbar.setHeight(getStatusBarHeight());

        mBottomNavigationView = findViewById(R.id.bottom_navigation);
        mBottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int gotItemId = item.getItemId();
                if (mMenuId == gotItemId) {
                    mPresenter.showToolbar(mMenuId);
                } else if (gotItemId == R.id.navigation_discover) {
                    mPresenter.transToDiscover();
                } else if (gotItemId == R.id.navigation_bookmark) {
                    mPresenter.transToBookmark();
                } else if (gotItemId == R.id.navigation_recipe) {
                    mPresenter.transToRecipe();
                } else {
                    return false;
                }
                return true;
            }
        });
        mPresenter.start();
    }

    public void popLogin() {
        startActivityForResult(new Intent(this, LoginActivity.class), Constants.LOGIN_ACTIVITY);
    }

    private int getStatusBarHeight() {
        int statusBarHeight = 0;
        int resourceId = getResources()
                .getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) statusBarHeight = getResources().getDimensionPixelSize(resourceId);
        return statusBarHeight;
    }

    @Override
    public void setCustomMainPresenter(BeChefContract.Presenter customMainPresenter) {
        mPresenter = checkNotNull(customMainPresenter);
    }

    public void showDetailUi(Object content, boolean isBottomShown) {
        mPresenter.transToDetail(content, isBottomShown);
    }

    public void showSearchUi(BaseContract.MainPresenter presenter) {
        mPresenter.transToSearch(presenter);
    }

    public void showBottomNavigationView(Boolean isShow) {
        mBottomNavigationView.setVisibility(isShow ? View.VISIBLE : View.GONE);
    }

    @Override
    public void setMenuId(int menuId) {
        mMenuId = menuId;
    }
}