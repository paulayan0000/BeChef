package com.paula.android.bechef;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.paula.android.bechef.adapters.BeChefFragmentStateAdapter;
import com.paula.android.bechef.data.entity.BaseTab;

import java.util.ArrayList;

public class BaseMainFragment extends Fragment implements View.OnClickListener {
    protected AppBarLayout mAppBarLayout;
    protected ViewPager2 mViewPager;
    protected AppBarLayout.LayoutParams mLayoutParams;
    protected BeChefFragmentStateAdapter mDefaultMainAdapter;
    protected boolean mIsSelectable = false;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);
        mAppBarLayout = view.findViewById(R.id.appbar);
        mLayoutParams = (AppBarLayout.LayoutParams) view
                .findViewById(R.id.constraintlayout_toolbar).getLayoutParams();
        ((TextView) view.findViewById(R.id.textview_toolbar_title)).setText(getTitleText());
        setButtons(view);
        setViewPagerAndTab(view);
        return view;
    }

    private void setButtons(View view) {
        setNewButton(view.findViewById(R.id.imagebutton_new));
        setRefreshButton(view.findViewById(R.id.imagebutton_refresh));
        view.findViewById(R.id.imagebutton_edit).setOnClickListener(this);
        view.findViewById(R.id.imagebutton_find).setOnClickListener(this);
    }

    protected void setRefreshButton(View view) {
    }

    protected void setNewButton(View view) {
    }

    private void setViewPagerAndTab(View view) {
        TabLayout tabLayout = view.findViewById(R.id.tabLayout);
        mViewPager = view.findViewById(R.id.viewpager_main_container);
        mDefaultMainAdapter = new BeChefFragmentStateAdapter(this, new ArrayList<BaseTab>());
        mViewPager.setAdapter(mDefaultMainAdapter);

        new TabLayoutMediator(tabLayout, mViewPager, true,
                new TabLayoutMediator.TabConfigurationStrategy() {
                    @Override
                    public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
                        tab.setText(mDefaultMainAdapter.getTabArrayList().get(position).getTabName());
                        customOnConfigureTab(tab, position);
                    }
                }).attach();
    }

    protected int getTitleText() {
        return R.string.title_discover;
    }

    protected void customOnConfigureTab(TabLayout.Tab tab, int position) {
    }

    protected Fragment getChildFragment(int tabIndex) {
        return getChildFragmentManager()
                .findFragmentByTag("f" + mDefaultMainAdapter.getItemId(tabIndex));
    }

    public int getCurrentTabIndex() {
        return mViewPager.getCurrentItem();
    }

    @Override
    public void onClick(View v) {
        int currentViewId = v.getId();
        if (currentViewId == R.id.imagebutton_edit) {
            if (!mIsSelectable) editTab();
            return;
        }
        if (currentViewId == R.id.imagebutton_find) {
            if (!mIsSelectable) find();
            return;
        }
    }

    protected void find() {
    }

    protected void editTab() {
    }

    public void showToolbar() {
        if (mAppBarLayout != null) mAppBarLayout.setExpanded(true, true);
    }

    public void showDefaultUi(ArrayList<BaseTab> tabs) {
        mDefaultMainAdapter.updateData(tabs);
    }
}
