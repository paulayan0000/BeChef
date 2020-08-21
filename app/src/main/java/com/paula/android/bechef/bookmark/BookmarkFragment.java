package com.paula.android.bechef.bookmark;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.paula.android.bechef.R;
import com.paula.android.bechef.adapters.FragmentAdapter;
import java.util.ArrayList;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import static com.google.android.gms.common.internal.Preconditions.checkNotNull;

public class BookmarkFragment extends Fragment implements BookmarkContract.View {
    private BookmarkContract.Presenter mPresenter;
    private FragmentAdapter mDefaultMainAdapter;
    private ArrayList<String> mTabTitles = new ArrayList<>();
    private AppBarLayout mAppBarLayout;
    private TextView mTvInfoDescription;

    public BookmarkFragment() {
        // Required empty public constructor
    }

    public static BookmarkFragment newInstance() {
        return new BookmarkFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);
        mAppBarLayout = view.findViewById(R.id.appbar);

        TextView tvTitle = view.findViewById(R.id.textview_toolbar_title);
        tvTitle.setText(R.string.title_bookmark);
        mTvInfoDescription = view.findViewById(R.id.textview_info_description);

        TabLayout tabLayout = view.findViewById(R.id.tabLayout);
        new TabLayoutMediator(tabLayout, getViewPager(view), true, mTabConfigurationStrategy).attach();
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mPresenter.start();
    }

    private ViewPager2 getViewPager(View view) {
        ViewPager2 viewPager = view.findViewById(R.id.viewpager_main_container);
        mDefaultMainAdapter = new FragmentAdapter(this, mTabTitles);
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
    public void setToolbar() {
        mAppBarLayout.setExpanded(true, true);
    }

    @Override
    public void showDefaultUi(ArrayList<String> tabTitles) {
        mDefaultMainAdapter.updateData(tabTitles);
    }

    public void updateView(int size) {
        mTvInfoDescription.setText("共" + size + "道");
    }
}
