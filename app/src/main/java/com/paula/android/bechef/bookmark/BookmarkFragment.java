package com.paula.android.bechef.bookmark;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

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

public class BookmarkFragment extends Fragment implements BookmarkContract.View {

    private BookmarkContract.Presenter mPresenter;
//    private DefaultMainAdapter mDefaultMainAdapter;
    private DiscoverFragmentAdapter mDefaultMainAdapter;
    private ArrayList<String> mTabTitles = new ArrayList<>();
    private GetSearchList mMainContents = new GetSearchList();


    public BookmarkFragment() {
        // Required empty public constructor
    }

    public static BookmarkFragment newInstance() {
        return new BookmarkFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_bookmark, container, false);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        TabLayout tabLayout = view.findViewById(R.id.tabLayout);
        new TabLayoutMediator(tabLayout, getViewPager(view), true, mTabConfigurationStrategy).attach();
        ImageButton imageButtonHide = view.findViewById(R.id.imagebottom_first);
        imageButtonHide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });
        mPresenter.start();
    }

    private ViewPager2 getViewPager(View view) {
        ViewPager2 viewPager = view.findViewById(R.id.viewpager_main_container);

//        mDefaultMainAdapter = new DefaultMainAdapter(mTabTitles, mMainContents, mPresenter);
        mDefaultMainAdapter = new DiscoverFragmentAdapter(this, mTabTitles);
        viewPager.setAdapter(mDefaultMainAdapter);

        viewPager.setOrientation(ViewPager2.ORIENTATION_HORIZONTAL);
        return viewPager;
    }

    private TabLayoutMediator.TabConfigurationStrategy mTabConfigurationStrategy = new TabLayoutMediator.TabConfigurationStrategy() {
        @Override
        public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
            tab.setText(mDefaultMainAdapter.getTabTitles().get(position));
        }
    };

    @Override
    public void setPresenter(BookmarkContract.Presenter presenter) {
        mPresenter = checkNotNull(presenter);
    }


    @Override
    public void showDefaultUi(ArrayList<String> tabtitles, ArrayList<GetSearchList> contents) {
//        mDefaultMainAdapter.updateData(tabtitles, contents);
        mDefaultMainAdapter.updateTabTitles(tabtitles);
    }
}
