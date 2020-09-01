package com.paula.android.bechef.baseMain;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.paula.android.bechef.BaseContract;
import com.paula.android.bechef.R;
import com.paula.android.bechef.adapters.FragmentAdapter;
import com.paula.android.bechef.data.entity.BaseTab;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import static com.google.android.gms.common.internal.Preconditions.checkNotNull;

public class BaseMainFragment extends Fragment implements View.OnClickListener {
    //    private BaseContract.BasePresenter mPresenter;
    private Context mContext;
    private AppBarLayout mAppBarLayout;
    private ViewPager2 mViewPager;
    private FragmentAdapter mDefaultMainAdapter;
//    private ArrayList<String> mTabTitles = new ArrayList<>();
    private boolean mIsSelectable = false;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);
        mContext = view.getContext();
        mAppBarLayout = view.findViewById(R.id.appbar);
        ((TextView) view.findViewById(R.id.textview_toolbar_title)).setText(getTitleText());
        view.findViewById(R.id.imagebutton_edit).setOnClickListener(this);
        view.findViewById(R.id.imagebutton_find).setOnClickListener(this);
        view.findViewById(R.id.imagebutton_toolbar_menu).setOnClickListener(this);

        TabLayout tabLayout = view.findViewById(R.id.tabLayout);
        mViewPager = view.findViewById(R.id.viewpager_main_container);
//        mDefaultMainAdapter = new FragmentAdapter(this, mTabTitles);
        mDefaultMainAdapter = getViewPagerAdapter();
        mViewPager.setAdapter(mDefaultMainAdapter);

        TabLayoutMediator.TabConfigurationStrategy tabConfigurationStrategy = new TabLayoutMediator.TabConfigurationStrategy() {
            @Override
            public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
//                tab.setText(mDefaultMainAdapter.getTabTitles().get(position));
                tab.setText(((BaseTab) mDefaultMainAdapter.getTabArrayList().get(position)).getTabName());
                customOnConfigureTab(tab, position);
            }
        };
        new TabLayoutMediator(tabLayout, mViewPager, true, tabConfigurationStrategy).attach();

        return view;
    }

    private int getTitleText() {
        return R.string.title_discover;
    }

    public FragmentAdapter getViewPagerAdapter() {
//        return new FragmentAdapter(this, mTabTitles);
        return new FragmentAdapter(this, new ArrayList<BaseTab>());
    }

    private void customOnConfigureTab(TabLayout.Tab tab, int position) {
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.imagebutton_edit:
                if (!mIsSelectable) Toast.makeText(getContext(), "edit", Toast.LENGTH_SHORT).show();
                break;
            case R.id.imagebutton_find:
                if (!mIsSelectable) Toast.makeText(getContext(), "find", Toast.LENGTH_SHORT).show();
                break;
            case R.id.imagebutton_toolbar_menu:
                if (!mIsSelectable) Toast.makeText(getContext(), "menu", Toast.LENGTH_SHORT).show();
                break;
        }
    }

    public void setToolbar() {
        mAppBarLayout.setExpanded(true, true);
    }

//    public void showDefaultUi(ArrayList<String> tabTitles, ArrayList<String> channelIds) {
//        mDefaultMainAdapter.updateData(tabTitles, channelIds);
//    }

    public void showDefaultUi(ArrayList<?> tabs) {
        mDefaultMainAdapter.updateData(tabs);
    }
}
