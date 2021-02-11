package com.paula.android.bechef;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.paula.android.bechef.adapters.FragmentAdapter;
import com.paula.android.bechef.data.entity.BaseTab;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

public class BaseMainFragment extends Fragment implements View.OnClickListener {
    protected Context mContext;
    protected AppBarLayout mAppBarLayout;
    protected ViewPager2 mViewPager;
    protected AppBarLayout.LayoutParams mLayoutParams;
    protected boolean mIsSelectable = false;
    protected FragmentAdapter mDefaultMainAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);
        mContext = view.getContext();
        mAppBarLayout = view.findViewById(R.id.appbar);
        mLayoutParams = (AppBarLayout.LayoutParams) view.findViewById(R.id.toolbar).getLayoutParams();
        ((TextView) view.findViewById(R.id.textview_toolbar_title)).setText(getTitleText());
        ImageButton ibtnNew = view.findViewById(R.id.imagebutton_new);
        ibtnNew.setOnClickListener(this);
        ibtnNew.setVisibility(getNewVisibility());
        view.findViewById(R.id.imagebutton_edit).setOnClickListener(this);
        view.findViewById(R.id.imagebutton_find).setOnClickListener(this);
        view.findViewById(R.id.imagebutton_toolbar_menu).setOnClickListener(this);
        setViewPagerAndTab(view);
        return view;
    }

    protected int getNewVisibility() {
        return View.GONE;
    }

    private void setViewPagerAndTab(View view) {
        TabLayout tabLayout = view.findViewById(R.id.tabLayout);
        mViewPager = view.findViewById(R.id.viewpager_main_container);
        mDefaultMainAdapter = new FragmentAdapter(this, new ArrayList<BaseTab>());
        mViewPager.setAdapter(mDefaultMainAdapter);

        TabLayoutMediator.TabConfigurationStrategy tabConfigurationStrategy = new TabLayoutMediator.TabConfigurationStrategy() {
            @Override
            public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
                BaseTab baseTab = mDefaultMainAdapter.getTabArrayList().get(position);
                tab.setText(baseTab.getTabName());
                customOnConfigureTab(tab, position);
            }
        };
        new TabLayoutMediator(tabLayout, mViewPager, true, tabConfigurationStrategy).attach();
    }

    protected int getTitleText() {
        return R.string.title_discover;
    }

    protected void customOnConfigureTab(TabLayout.Tab tab, int position) {
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.imagebutton_new:
                if (!mIsSelectable) createNewData();
                break;
            case R.id.imagebutton_edit:
                if (!mIsSelectable) editTab();
                break;
            case R.id.imagebutton_find:
//                if (!mIsSelectable) Toast.makeText(getContext(), "find", Toast.LENGTH_SHORT).show();
                if (!mIsSelectable) find();
                break;
            case R.id.imagebutton_toolbar_menu:
                if (!mIsSelectable) Toast.makeText(getContext(), "menu", Toast.LENGTH_SHORT).show();
                break;
        }
    }

    protected void find() {

    }

    protected void editTab() {
    }

    protected void createNewData() {
    }

    public void showToolbar() {
        if (mAppBarLayout != null) mAppBarLayout.setExpanded(true, true);
    }

    public void showDefaultUi(ArrayList<BaseTab> tabs) {
        mDefaultMainAdapter.updateData(tabs);
    }
}
