package com.paula.android.bechef.receipt;

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
import com.paula.android.bechef.data.entity.BaseItem;
import com.paula.android.bechef.data.entity.BaseTab;
import com.paula.android.bechef.data.entity.ReceiptItem;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import static com.google.android.gms.common.internal.Preconditions.checkNotNull;

public class ReceiptFragment extends Fragment implements ReceiptContract.View {

    private ReceiptContract.Presenter mPresenter;
    private FragmentAdapter mDefaultMainAdapter;
//    private ArrayList<String> mTabTitles = new ArrayList<>();
    private AppBarLayout mAppBarLayout;

    public ReceiptFragment() {
        // Required empty public constructor
    }

    public static ReceiptFragment newInstance() {
        return new ReceiptFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);
        mAppBarLayout = view.findViewById(R.id.appbar);

        ((TextView) view.findViewById(R.id.textview_toolbar_title)).setText(R.string.title_receipt);
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
//        mDefaultMainAdapter = new FragmentAdapter(this, mTabTitles);
        mDefaultMainAdapter = new FragmentAdapter(this, new ArrayList<BaseTab>());
        viewPager.setAdapter(mDefaultMainAdapter);
        viewPager.setOrientation(ViewPager2.ORIENTATION_HORIZONTAL);
        return viewPager;
    }

    private TabLayoutMediator.TabConfigurationStrategy mTabConfigurationStrategy = new TabLayoutMediator.TabConfigurationStrategy() {
        @Override
        public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
            tab.setText(((BaseTab) mDefaultMainAdapter.getTabArrayList().get(position)).getTabName());
        }
    };

    @Override
    public void setPresenter(ReceiptContract.Presenter presenter) {
        mPresenter = checkNotNull(presenter);
    }

    @Override
    public void setToolbar(boolean isShow) {
        mAppBarLayout.setExpanded(isShow, true);
    }

    @Override
    public void refreshCurrentUi() {

    }

    @Override
    public void refreshUi(int tabIndex) {

    }

    @Override
    public ArrayList<ReceiptItem> getChosenItems() {
        return null;
    }

    @Override
    public int getCurrentTabIndex() {
        return 0;
    }

    @Override
    public void showDefaultUi(ArrayList<?> tabs) {
        mDefaultMainAdapter.updateData(tabs);
    }
}