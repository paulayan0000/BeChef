package com.paula.android.bechef.search;

import android.app.Activity;
import android.content.Context;
import android.graphics.Rect;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.paula.android.bechef.R;
import com.paula.android.bechef.activities.BeChefActivity;
import com.paula.android.bechef.adapters.DiscoverChildAdapter;
import com.paula.android.bechef.adapters.FilterItemAdapter;
import com.paula.android.bechef.adapters.FilterResultAdapter;
import com.paula.android.bechef.api.beans.GetSearchList;
import com.paula.android.bechef.data.FilterItem;
import com.paula.android.bechef.data.entity.BaseItem;
import com.paula.android.bechef.data.entity.BaseTab;
import com.paula.android.bechef.utils.Constants;
import com.paula.android.bechef.utils.Utils;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import static com.google.android.gms.common.internal.Preconditions.checkNotNull;

public class SearchFragment extends Fragment implements SearchContract.View {
    private SearchContract.Presenter mPresenter;
    private FilterResultAdapter mFilterResultAdapter;
    private RecyclerView mRecyclerViewResult;
    private DiscoverChildAdapter mSearchResultAdapter;
    private FilterItemAdapter mFilterItemAdapter;
    private SearchView mSearchView;
    private TextView mTvSearchResult;
    private Context mContext;
    private View mViewMask;
    private String mOldKeyword;

    private SearchFragment() {
    }

    public static SearchFragment newInstance() {
        return new SearchFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_search, container, false);
        mContext = root.getContext();
        root.findViewById(R.id.imagebutton_toolbar_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((BeChefActivity) mContext).onBackPressed();
            }
        });

        RecyclerView recyclerViewFilter = root.findViewById(R.id.recyclerview_search_filter);
        recyclerViewFilter.setLayoutManager(new LinearLayoutManager(mContext));
        if (recyclerViewFilter.getItemDecorationCount() == 0) recyclerViewFilter.addItemDecoration(dec);
        mFilterItemAdapter = new FilterItemAdapter(new ArrayList<FilterItem>(), (SearchPresenter) mPresenter);
        recyclerViewFilter.setAdapter(mFilterItemAdapter);

        mRecyclerViewResult = root.findViewById(R.id.recyclerview_search_result);
        initResultView();

        mTvSearchResult = root.findViewById(R.id.textview_search_result);
        mViewMask = root.findViewById(R.id.view_search_translucent_mask);
        mSearchView = root.findViewById(R.id.search_view);
        setSearchView();

        return root;
    }

    private RecyclerView.ItemDecoration dec = new RecyclerView.ItemDecoration() {
        @Override
        public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
            super.getItemOffsets(outRect, view, parent, state);
            if (outRect.bottom == 0)
                outRect.bottom = (int) Utils.convertDpToPixel((float) 8, mContext);
            if (parent.getChildAdapterPosition(view) == 0)
                outRect.top = (int) Utils.convertDpToPixel((float) 8, mContext);
        }
    };

    private RecyclerView.ItemDecoration decWithSpan = new RecyclerView.ItemDecoration() {
        @Override
        public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
            super.getItemOffsets(outRect, view, parent, state);
            if (outRect.bottom == 0)
                outRect.bottom = (int) Utils.convertDpToPixel((float) 8, mContext);
            if (parent.getChildAdapterPosition(view) <= 1)
                outRect.top = (int) Utils.convertDpToPixel((float) 8, mContext);
        }
    };

    @Override
    public void onStart() {
        super.onStart();
        mPresenter.start();
        mSearchView.setQuery("", false);
        ((BeChefActivity) mContext).showBottomNavigationView(false);
    }

    @Override
    public void setPresenter(SearchContract.Presenter presenter) {
        mPresenter = checkNotNull(presenter);
    }

    @Override
    public void onStop() {
        super.onStop();
        mPresenter.cancelTask();
        ((BeChefActivity) mContext).showBottomNavigationView(true);
    }

    @Override
    public void updateFilterView(ArrayList<BaseTab> baseTabs) {
        ArrayList<FilterItem> videoFilter = new ArrayList<>();

        if (baseTabs == null) {
            FilterItem filterResultType = new FilterItem();
            filterResultType.setFilterType("類型 :");
            ArrayList<BaseTab> typeFilterContents = new ArrayList<>();
            typeFilterContents.add(new BaseTab("影片"));
            typeFilterContents.add(new BaseTab("頻道"));
            filterResultType.setFilterContents(typeFilterContents);
            videoFilter.add(filterResultType);
        } else {
            FilterItem filterTab = new FilterItem();
            filterTab.setFilterType("書籤 :");
            ArrayList<BaseTab> tabFilterContents = new ArrayList<>();
            tabFilterContents.add(new BaseTab("全部"));
            tabFilterContents.addAll(baseTabs);
            filterTab.setFilterContents(tabFilterContents);
            videoFilter.add(filterTab);

            FilterItem filterRange = new FilterItem();
            filterRange.setFilterType("搜尋範圍 :");
            ArrayList<BaseTab> rangeFilterContents = new ArrayList<>();
            rangeFilterContents.add(new BaseTab("標題"));
            rangeFilterContents.add(new BaseTab("標籤"));
            rangeFilterContents.add(new BaseTab("說明"));
            filterRange.setFilterContents(rangeFilterContents);
            videoFilter.add(filterRange);
        }
        mFilterItemAdapter.updateData(videoFilter);
    }

    @Override
    public void updateResultView(ArrayList<BaseItem> baseItems) {
//        mFilterResultAdapter.setLoading(false);
        mTvSearchResult.setText("搜尋結果 -- 共 " + baseItems.size() + " 筆結果");
        mFilterResultAdapter.updateData(baseItems);
    }

    private void setSearchView() {
        mSearchView.onActionViewExpanded();

        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                mSearchView.clearFocus();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }

        });

        AutoCompleteTextView mAutoCompleteTextView = mSearchView.findViewById(R.id.search_src_text);
        mAutoCompleteTextView.setBackgroundColor(getResources().getColor(R.color.white));
        mAutoCompleteTextView.getLayoutParams().height = LinearLayout.LayoutParams.MATCH_PARENT;
        mAutoCompleteTextView.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                mViewMask.setVisibility(hasFocus ? View.VISIBLE : View.INVISIBLE);
                if (!hasFocus) {
                    InputMethodManager inputMethodManager = (InputMethodManager) mContext.getSystemService(Activity.INPUT_METHOD_SERVICE);
                    inputMethodManager.hideSoftInputFromWindow(v.getWindowToken(), 0);
                    doQuery();
                }
            }
        });
        mAutoCompleteTextView.clearFocus();
        mAutoCompleteTextView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    Log.d("SearchFragment", "search empty");
                    mSearchView.clearFocus();
                }
                return false;
            }
        });
        ImageView deleteButton = mSearchView.findViewById(R.id.search_close_btn);
        deleteButton.setBackgroundColor(getResources().getColor(R.color.white));
    }

    @Override
    public void showDetailUi(Object content, boolean isBottomShown) {
        ((BeChefActivity) mContext).transToDetail(content, false);
    }

    @Override
    public void reQuery() {
        initOldKeyword();
        doQuery();
    }

    public void initOldKeyword() {
        mOldKeyword = null;
    }

    private void doQuery() {
        String keyword = getCurrentKeyword();
        if (keyword.equals(mOldKeyword)) return;
        if (keyword.equals("")) {
            mPresenter.cancelTask();
            if (mSearchResultAdapter != null) {
                mTvSearchResult.setText("搜尋結果");
                mSearchResultAdapter.clearData();
            } else updateResultView(new ArrayList<BaseItem>());
        } else {
            mTvSearchResult.setText("搜尋結果");
            if (mSearchResultAdapter != null) {
                mSearchResultAdapter.clearData();
            }
            mPresenter.loadResults(mFilterItemAdapter.getChosenTabs());
        }
        mOldKeyword = keyword;
    }

    public String getCurrentKeyword() {
        return mSearchView.getQuery().toString();
    }

    @Override
    public void updateSearchItems(GetSearchList bean) {
//        mSearchResultAdapter.setLoading(false);
        mSearchResultAdapter.updateData(bean);
        mTvSearchResult.setText("搜尋結果 -- 共顯示 " + mSearchResultAdapter.getBaseItemCounts() + " 筆結果");
    }

    @Override
    public void showLoading(boolean isLoading) {
        if (mSearchResultAdapter != null) mSearchResultAdapter.setLoading(isLoading);
        else mFilterResultAdapter.setLoading(isLoading);
    }

    private void initResultView() {
        if (mPresenter.isInDiscover()) {
            mSearchResultAdapter = new DiscoverChildAdapter(new GetSearchList(), mPresenter);
            GridLayoutManager gridLayoutManager = new GridLayoutManager(mContext, 2);
            gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                @Override
                public int getSpanSize(int position) {
                    if (mSearchResultAdapter.getItemViewType(position) == Constants.VIEW_TYPE_NORMAL) {
                        return 1;
                    }
                    return 2;
                }
            });
            if (mRecyclerViewResult.getItemDecorationCount() == 0) mRecyclerViewResult.addItemDecoration(decWithSpan);
            mRecyclerViewResult.setLayoutManager(gridLayoutManager);
            mRecyclerViewResult.setAdapter(mSearchResultAdapter);
            mRecyclerViewResult.addOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                    super.onScrollStateChanged(recyclerView, newState);
                    RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
                    if (layoutManager != null) {
                        mPresenter.onScrollStateChanged(
                                layoutManager.getChildCount(),
                                layoutManager.getItemCount(),
                                newState);
                    }
                }

                @Override
                public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                    super.onScrolled(recyclerView, dx, dy);
                    mPresenter.onScrolled(recyclerView.getLayoutManager());
                }
            });
        } else {
            mRecyclerViewResult.setLayoutManager(new LinearLayoutManager(mContext));
            if (mRecyclerViewResult.getItemDecorationCount() == 0) mRecyclerViewResult.addItemDecoration(dec);
            mFilterResultAdapter = new FilterResultAdapter(new ArrayList<BaseItem>(), (SearchPresenter) mPresenter);
            mRecyclerViewResult.setAdapter(mFilterResultAdapter);
            mRecyclerViewResult.clearOnScrollListeners();
        }
    }
}
