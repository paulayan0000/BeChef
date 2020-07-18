package com.paula.android.bechef.discover;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.paula.android.bechef.R;
import com.paula.android.bechef.adapters.DiscoverFragmentAdapter;
import com.paula.android.bechef.api.beans.GetSearchList;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import static com.google.android.gms.common.internal.Preconditions.checkNotNull;

public class DiscoverFragment extends Fragment implements DiscoverContract.View{

    private DiscoverContract.Presenter mPresenter;
    private DiscoverFragmentAdapter mDiscoverMainAdapter;
    private ArrayList<String> mTabTitles = new ArrayList<>();

    public DiscoverFragment() {
        // Required empty public constructor
    }

        public static DiscoverFragment newInstance() {
        return new DiscoverFragment();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_discover, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        TabLayout tabLayout = view.findViewById(R.id.tabLayout);
        new TabLayoutMediator(tabLayout, getViewPager(view), true, mTabConfigurationStrategy).attach();
        mPresenter.start();
    }

    private ViewPager2 getViewPager(View view) {
        ViewPager2 viewPager = view.findViewById(R.id.viewpager_main_container);

        mDiscoverMainAdapter = new DiscoverFragmentAdapter(this, mTabTitles);
        viewPager.setAdapter(mDiscoverMainAdapter);

        viewPager.setOrientation(ViewPager2.ORIENTATION_HORIZONTAL);
        return viewPager;
    }

    private TabLayoutMediator.TabConfigurationStrategy mTabConfigurationStrategy = new TabLayoutMediator.TabConfigurationStrategy() {
        @Override
        public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
            tab.setText(mDiscoverMainAdapter.getTabTitles().get(position));
        }
    };

    @Override
    public void setPresenter(DiscoverContract.Presenter presenter) {
        mPresenter = checkNotNull(presenter);
    }

    @Override
    public void showDiscoverUi(ArrayList<String> tabTitles) {
        mDiscoverMainAdapter.updateTabTitles(tabTitles);
    }

}
