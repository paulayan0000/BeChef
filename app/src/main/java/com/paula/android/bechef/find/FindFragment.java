package com.paula.android.bechef.find;

import android.content.Context;
import android.graphics.Rect;
import android.os.Bundle;
import android.view.inputmethod.InputMethodManager;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.paula.android.bechef.activities.BeChefActivity;
import com.paula.android.bechef.adapters.DiscoverChildAdapter;
import com.paula.android.bechef.adapters.FindConditionAdapter;
import com.paula.android.bechef.adapters.FilterResultAdapter;
import com.paula.android.bechef.api.beans.YouTubeData;
import com.paula.android.bechef.data.entity.BaseItem;
import com.paula.android.bechef.data.entity.BaseTab;
import com.paula.android.bechef.data.FindCondition;
import com.paula.android.bechef.R;
import com.paula.android.bechef.utils.Constants;
import com.paula.android.bechef.utils.Utils;

import java.util.ArrayList;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;
import static android.view.inputmethod.EditorInfo.IME_ACTION_SEARCH;
import static com.google.android.gms.common.internal.Preconditions.checkNotNull;

public class FindFragment extends Fragment implements FindContract.View {
    private FindContract.Presenter mPresenter;
    private Context mContext;
    private FindConditionAdapter mFindConditionAdapter;
    private FilterResultAdapter mFilterResultAdapter;
    private DiscoverChildAdapter mSearchResultAdapter;
    private SearchView mSearchView;
    private TextView mTvFindResult;
    private View mViewMask;
    private String mOldKeyword;

    private FindFragment() {
    }

    public static FindFragment newInstance() {
        return new FindFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_find, container, false);
        mContext = root.getContext();
        root.findViewById(R.id.imagebutton_toolbar_back).setOnClickListener(new android.view.View.OnClickListener() {
            @Override
            public void onClick(android.view.View v) {
                ((BeChefActivity) mContext).onBackPressed();
            }
        });
        mTvFindResult = root.findViewById(R.id.textview_total_find_result);
        mViewMask = root.findViewById(R.id.view_find_translucent_mask);
        initFindConditionRecyclerView(root);
        initResultRecyclerView(root);
        setSearchView(root);
        return root;
    }

    private void initFindConditionRecyclerView(View root) {
        RecyclerView recyclerViewFindCondition = root.findViewById(R.id.recyclerview_find_condition);
        recyclerViewFindCondition.setLayoutManager(
                new LinearLayoutManager(mContext, RecyclerView.HORIZONTAL, false));
        if (recyclerViewFindCondition.getItemDecorationCount() == 0)
            recyclerViewFindCondition.addItemDecoration(new RecyclerView.ItemDecoration() {
                @Override
                public void getItemOffsets(@NonNull Rect outRect,
                                           @NonNull View view,
                                           @NonNull RecyclerView parent,
                                           @NonNull RecyclerView.State state) {
                    super.getItemOffsets(outRect, view, parent, state);
                    if (parent.getChildAdapterPosition(view) != 0) {
                        outRect.left = Utils.convertDpToPixel(Constants.NORMAL_PADDING, mContext);
                    }
                }
            });
        mFindConditionAdapter = new FindConditionAdapter(new ArrayList<FindCondition>(),
                (FindPresenter) mPresenter);
        recyclerViewFindCondition.setAdapter(mFindConditionAdapter);
    }

    private void initResultRecyclerView(android.view.View root) {
        RecyclerView recyclerViewResult = root.findViewById(R.id.recyclerview_find_result);
        GridLayoutManager gridLayoutManager;
        if (mPresenter.isFromDiscover()) {
            // Search from discover
            mSearchResultAdapter = new DiscoverChildAdapter(new YouTubeData(), mPresenter);
            recyclerViewResult.setAdapter(mSearchResultAdapter);
            gridLayoutManager = new GridLayoutManager(mContext, 2);
            gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                @Override
                public int getSpanSize(int position) {
                    return mSearchResultAdapter.getItemViewType(position) ==
                            Constants.VIEW_TYPE_NORMAL ? 1 : 2;
                }
            });
            recyclerViewResult.addOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                    super.onScrollStateChanged(recyclerView, newState);
                    RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
                    if (layoutManager != null) {
                        mPresenter.onScrollStateChanged(layoutManager.getChildCount(),
                                layoutManager.getItemCount(), newState);
                    }
                }

                @Override
                public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                    super.onScrolled(recyclerView, dx, dy);
                    mPresenter.onScrolled(recyclerView.getLayoutManager());
                }
            });
        } else {
            // Filter from bookmark or recipe
            mFilterResultAdapter = new FilterResultAdapter(new ArrayList<BaseItem>(),
                    (FindPresenter) mPresenter);
            recyclerViewResult.setAdapter(mFilterResultAdapter);
            gridLayoutManager = new GridLayoutManager(mContext, 1);
            recyclerViewResult.clearOnScrollListeners();
        }
        recyclerViewResult.setLayoutManager(gridLayoutManager);
        if (recyclerViewResult.getItemDecorationCount() == 0)
            recyclerViewResult.addItemDecoration(new RecyclerView.ItemDecoration() {
                @Override
                public void getItemOffsets(@NonNull Rect outRect,
                                           @NonNull android.view.View view,
                                           @NonNull RecyclerView parent,
                                           @NonNull RecyclerView.State state) {
                    super.getItemOffsets(outRect, view, parent, state);
                    if (outRect.bottom == 0) {
                        outRect.bottom = Utils.convertDpToPixel(Constants.NORMAL_PADDING, mContext);
                    }
                }
            });
        recyclerViewResult.setHasFixedSize(true);
    }

    private void setSearchView(android.view.View root) {
        mSearchView = root.findViewById(R.id.search_view);
        mSearchView.onActionViewExpanded();
        ImageView deleteButton = mSearchView.findViewById(R.id.search_close_btn);
        deleteButton.setBackgroundColor(getResources().getColor(R.color.white));

        // Set AutoCompleteTextView: do query once not hasFocus
        AutoCompleteTextView autoCompleteTextView = mSearchView.findViewById(R.id.search_src_text);
        autoCompleteTextView.setBackgroundColor(getResources().getColor(R.color.white));
        autoCompleteTextView.getLayoutParams().height = MATCH_PARENT;
        autoCompleteTextView.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    mViewMask.setVisibility(View.VISIBLE);
                } else {
                    mViewMask.setVisibility(View.GONE);
                    ((InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE))
                            .hideSoftInputFromWindow(v.getWindowToken(), 0);
                    doQuery();
                }
            }
        });
        autoCompleteTextView.clearFocus();
        autoCompleteTextView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == IME_ACTION_SEARCH) mSearchView.clearFocus();
                return false;
            }
        });
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
    }

    @Override
    public void onStart() {
        super.onStart();
        mPresenter.start();
        ((BeChefActivity) mContext).showBottomNavigationView(false);
    }

    @Override
    public void setCustomMainPresenter(FindContract.Presenter customMainPresenter) {
        mPresenter = checkNotNull(customMainPresenter);
    }

    @Override
    public void onStop() {
        super.onStop();
        mPresenter.cancelTask();
        ((BeChefActivity) mContext).showBottomNavigationView(true);
    }

    @Override
    public void showDetailUi(Object content, boolean isBottomShown) {
        ((BeChefActivity) mContext).showDetailUi(content, false);
    }

    @Override
    public void reQuery() {
        initOldKeyword();
        doQuery();
    }

    private void initOldKeyword() {
        mOldKeyword = null;
    }

    private void doQuery() {
        String keyword = getCurrentKeyword();
        if (keyword.equals(mOldKeyword)) return;
        mTvFindResult.setText(getString(R.string.total_find_result));
        mPresenter.cancelTask();
        // clear all result for discover
        if (mSearchResultAdapter != null) mSearchResultAdapter.clearData();

        if (!keyword.isEmpty()) {
            mPresenter.loadResults();
        } else if (mFilterResultAdapter != null) {
            updateFilterResult(new ArrayList<BaseItem>());
        } else {
            updateSearchResult(new YouTubeData());
        }
        mOldKeyword = keyword;
    }

    public String getCurrentKeyword() {
        return mSearchView.getQuery().toString();
    }

    @Override
    public ArrayList<BaseTab> getChosenTabs() {
        return mFindConditionAdapter.getChosenTabs();
    }

    @Override
    public String getChosenVideoType() {
        return getString(R.string.channel).equals(getChosenTabs().get(0).getTabName()) ?
                Constants.API_TYPE_CHANNEL : Constants.API_TYPE_VIDEO;
    }

    @Override
    public String getChosenFilterRange() {
        String chosenFilterRange = getChosenTabs().get(1).getTabName();
        if (getString(R.string.title).equals(chosenFilterRange))
            return Constants.VARIABLE_NAME_TITLE;
        else if (getString(R.string.tags).equals(chosenFilterRange))
            return Constants.VARIABLE_NAME_TAGS;
        else
            return Constants.VARIABLE_NAME_DESCRIPTION;
    }

    @Override
    public long getChosenTabUid() {
        return getChosenTabs().get(0).getUid();
    }

    @Override
    public void showLoadingUi() {
        if (mSearchResultAdapter != null) {
            mSearchResultAdapter.showLoading();
        } else {
            mFilterResultAdapter.showLoading();
        }
    }

    @Override
    public void setFindConditions(ArrayList<BaseTab> baseTabs) {
        ArrayList<FindCondition> findConditions = new ArrayList<>();

        if (mPresenter.isFromDiscover()) {
            FindCondition resourceType = new FindCondition(getString(R.string.search_with_type),
                    getResources().getStringArray(R.array.resource_type));
            findConditions.add(resourceType);
        } else {
            // Filter from bookmark or recipe
            FindCondition filterTab = new FindCondition(getString(R.string.filter_with_tab), getString(R.string.all));
            filterTab.getConditionContents().addAll(baseTabs);
            findConditions.add(filterTab);

            FindCondition filterRange = new FindCondition(getString(R.string.filter_with_range),
                    getResources().getStringArray(R.array.filter_range));
            findConditions.add(filterRange);
        }
        mFindConditionAdapter.updateData(findConditions);
    }

    @Override
    public void updateFilterResult(ArrayList<BaseItem> baseItems) {
        mFilterResultAdapter.updateData(baseItems);
        mTvFindResult.setText(String.format(getString(R.string.total_filter_result),
                baseItems.size()));
    }

    @Override
    public void updateSearchResult(YouTubeData bean) {
        mSearchResultAdapter.updateData(bean);
        mTvFindResult.setText(String.format(getString(R.string.total_search_result),
                mSearchResultAdapter.getBaseItemCounts()));
    }
}
