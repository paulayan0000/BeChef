package com.paula.android.bechef.activities;

import static com.google.android.gms.common.internal.Preconditions.checkNotNull;
public class BackupActiivity {

//public class BackupActiivity extends BaseActivity implements BeChefContract.View {
    private static final String LOG_TAG = BeChefActivity.class.getSimpleName();

//    private BeChefContract.Presenter mPresenter;
//    private TextView mToolbarTitle;
//    private TabLayout mTabLayout;
//    private MainAdapter mMainAdapter;
//
//    private ArrayList<String> mTabTitles = new ArrayList<>();
//    private GetSearchList mMainContents = new GetSearchList();
//    private GetSearchList mDefaultContents = new GetSearchList();
//
//    private int mCurrentPagerId = 0;
//
//
//    private void init() {
//        setContentView(R.layout.activity_bechef);
//
//        mPresenter = new BeChefPresenter(this, getSupportFragmentManager());
//
//        mToolbarTitle = findViewById(R.id.textview_toolbar_title);
//        setToolbar();
//
//
//        mTabLayout = findViewById(R.id.tabLayout);
//        new TabLayoutMediator(mTabLayout, getViewPager(), true, mTabConfigurationStrategy).attach();
//
//        mPresenter.start();
//
//
//        BottomNavigationView navigation = findViewById(R.id.navigation);
//        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
//    }
//
//    private ViewPager2 getViewPager() {
//        ViewPager2 viewPager = findViewById(R.id.viewpager_main_container);
//
//        mMainAdapter = new MainAdapter(mTabTitles, mMainContents, mPresenter);
//        viewPager.setAdapter(mMainAdapter);
//
//        viewPager.setOrientation(ViewPager2.ORIENTATION_HORIZONTAL);
//        return viewPager;
//    }
//
//
//    private TabLayoutMediator.TabConfigurationStrategy mTabConfigurationStrategy = new TabLayoutMediator.TabConfigurationStrategy() {
//        @Override
//        public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
//            tab.setText(mMainAdapter.getTabTitles().get(position));
//        }
//    };
//
//
//    @Override
//    public void showDiscoverUi(ArrayList<String> tabTitles, GetSearchList contents) {
//        setToolbarTitle(getResources().getString(R.string.title_discover));
//        mMainAdapter.updateData(tabTitles, contents);
//        setInfoBarVisibility(false);
//    }
//
//    @Override
//    public void showBookmarkUi(ArrayList<String> tabTitles, ArrayList<String> contents) {
//        setToolbarTitle(getResources().getString(R.string.title_bookmark));
//        mDefaultContents.setViewType(Constants.VIEWTYPE_BOOKMARK);
//        mDefaultContents.setTableName("bookmark default");
//        mMainAdapter.updateData(tabTitles, mDefaultContents);
//        setInfoBarVisibility(true);
//    }
//
//    @Override
//    public void showReceiptUi(ArrayList<String> tabTitles, ArrayList<String> contents) {
//        setToolbarTitle(getResources().getString(R.string.title_receipt));
//        mDefaultContents.setViewType(Constants.VIEWTYPE_RECEIPT);
//        mDefaultContents.setTableName("receipt default");
//        mMainAdapter.updateData(tabTitles, mDefaultContents);
//        setInfoBarVisibility(true);
//    }
//
//    @Override
//    public void showTodayUi() {
//        setToolbarTitle(getResources().getString(R.string.title_menu_list));
//        setInfoBarVisibility(true);
//    }
//
//    @Override
//    public void showMenuListUi(ArrayList<String> tabTitles, ArrayList<String> contents) {
//        mDefaultContents.setViewType(Constants.VIEWTYPE_MENULIST);
//        mDefaultContents.setTableName("menulist default");
//        mMainAdapter.updateData(tabTitles, mDefaultContents);
//    }
//
//    @Override
//    public void showBuyListUi(ArrayList<String> tabTitles, ArrayList<String> contents) {
//        mDefaultContents.setViewType(Constants.VIEWTYPE_BUYLIST);
//        mDefaultContents.setTableName("buylist default");
//        mMainAdapter.updateData(tabTitles, mDefaultContents);
//    }
//
//    private void setInfoBarVisibility(boolean visibility) {
//        findViewById(R.id.constraintlayout_info).setVisibility(visibility ? View.VISIBLE : View.GONE);
//    }
//
//    @Override
//    public void updateSearchItems(GetSearchList searchItems) {
//        mMainAdapter.updateData(null, searchItems);
//    }
//
//    @Override
//    public int getCustomViewType() {
//        return mMainAdapter.getViewType();
//    }
//
//    @Override
//    public void setPresenter(BeChefContract.Presenter presenter) {
//        mPresenter = checkNotNull(presenter);
//    }
//
//    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
//            = new BottomNavigationView.OnNavigationItemSelectedListener() {
//        @Override
//        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
//
//            // Show toolbar in AppBarLayout
//            AppBarLayout appBarLayout = findViewById(R.id.appbar);
//            appBarLayout.setExpanded(true, true);
//
//            if (mCurrentPagerId != item.getItemId()) {
//                switch (item.getItemId()) {
//                    case R.id.navigation_discover:
//                        mPresenter.transToDiscover();
//                        mMainAdapter.setAdpaters();
//                        break;
//                    case R.id.navigation_bookmark:
//                        mPresenter.transToBookmark();
//                        mMainAdapter.setAdpaters();
//                        break;
//                    case R.id.navigation_receipt:
//                        mPresenter.transToReceipt();
//                        mMainAdapter.setAdpaters();
//                        break;
//                    case R.id.navigation_today:
//                        mPresenter.transToToday();
//                        mMainAdapter.setAdpaters();
//                        break;
//                    default:
//                        return false;
//                }
//                mCurrentPagerId = item.getItemId();
//                return true;
//            }
//            return false;
//
//        }
//    };
//
//    private void setToolbarTitle(String title) {
//        mToolbarTitle.setText(title);
//        final TextView toolbarTitleAnother = findViewById(R.id.textview_toolbar_title_another);
//
//        if (getResources().getString(R.string.title_menu_list).equals(title)) {
//            toolbarTitleAnother.setVisibility(View.VISIBLE);
//            mToolbarTitle.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    mPresenter.transToMenuList();
//                    mToolbarTitle.setTextColor(getResources().getColor(R.color.white));
//                    toolbarTitleAnother.setTextColor(getResources().getColor(R.color.colorPrimaryLight));
//                }
//            });
//            toolbarTitleAnother.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    mPresenter.transToBuyList();
//                    mToolbarTitle.setTextColor(getResources().getColor(R.color.colorPrimaryLight));
//                    toolbarTitleAnother.setTextColor(getResources().getColor(R.color.white));
//                }
//            });
//        } else {
//            mToolbarTitle.setTextColor(getResources().getColor(R.color.white));
//            toolbarTitleAnother.setTextColor(getResources().getColor(R.color.colorPrimaryLight));
//            mToolbarTitle.setOnClickListener(null);
//            toolbarTitleAnother.setOnClickListener(null);
//            toolbarTitleAnother.setVisibility(View.GONE);
//        }
//    }
}
