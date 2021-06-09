package com.paula.android.bechef.discoverChild;

import android.graphics.Rect;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.paula.android.bechef.activities.BeChefActivity;
import com.paula.android.bechef.adapters.DiscoverChildAdapter;
import com.paula.android.bechef.api.beans.YouTubeData;
import com.paula.android.bechef.ChildContract;
import com.paula.android.bechef.data.entity.DiscoverTab;
import com.paula.android.bechef.R;
import com.paula.android.bechef.utils.Constants;
import com.paula.android.bechef.utils.Utils;

import static com.google.android.gms.common.internal.Preconditions.checkNotNull;

public class DiscoverChildFragment extends Fragment implements ChildContract.DiscoverChildView {
    private ChildContract.DiscoverChildPresenter mPresenter;
    private DiscoverChildAdapter mDiscoverChildAdapter;

    public DiscoverChildFragment(DiscoverTab discoverTab) {
        if (mPresenter == null) {
            mPresenter = new DiscoverChildPresenter(this,
                    discoverTab.getChannelId());
        }
    }

    public static DiscoverChildFragment newInstance(DiscoverTab discoverTab) {
        return new DiscoverChildFragment(discoverTab);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_child, container, false);
        view.findViewById(R.id.constraintlayout_info).setVisibility(View.GONE);
        setRecyclerView(view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mPresenter.start();
    }

    public void refresh() {
        mPresenter.cancelTask();
        mDiscoverChildAdapter.clearData();
        mPresenter.start();
    }

    @Override
    public void setCustomMainPresenter(ChildContract.DiscoverChildPresenter customMainPresenter) {
        mPresenter = checkNotNull(customMainPresenter);
    }

    private void setRecyclerView(View view) {
        RecyclerView recyclerView = view.findViewById(R.id.recyclerview_child);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        if (recyclerView.getItemDecorationCount() == 0)
            recyclerView.addItemDecoration(new RecyclerView.ItemDecoration() {
                @Override
                public void getItemOffsets(@NonNull Rect outRect,
                                           @NonNull View view,
                                           @NonNull RecyclerView parent,
                                           @NonNull RecyclerView.State state) {
                    super.getItemOffsets(outRect, view, parent, state);
                    if (outRect.bottom == 0) {
                        outRect.bottom = Utils.convertDpToPixel(Constants.NORMAL_PADDING);
                    }
                    if (parent.getChildAdapterPosition(view) == 0) {
                        outRect.top = Utils.convertDpToPixel(Constants.NORMAL_PADDING);
                    }
                }
            });
        mDiscoverChildAdapter = new DiscoverChildAdapter(new YouTubeData(), mPresenter);
        recyclerView.setAdapter(mDiscoverChildAdapter);
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
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
        recyclerView.setHasFixedSize(true);
    }

    @Override
    public void updateSearchItems(YouTubeData bean) {
        mDiscoverChildAdapter.updateData(getContext(), bean);
    }

    @Override
    public void showLoadingUi() {
        mDiscoverChildAdapter.showLoading();
    }

    @Override
    public void showDetailUi(Object content, boolean isBottomShown) {
        ((BeChefActivity) getActivity()).showDetailUi(content, true);
    }
}