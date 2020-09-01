package com.paula.android.bechef.discoverChild;

import android.content.Context;
import android.graphics.Rect;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.paula.android.bechef.R;
import com.paula.android.bechef.activities.BeChefActivity;
import com.paula.android.bechef.adapters.DiscoverChildAdapter;
import com.paula.android.bechef.api.beans.GetSearchList;
import com.paula.android.bechef.data.entity.BaseTab;
import com.paula.android.bechef.data.entity.DiscoverTab;
import com.paula.android.bechef.utils.Utils;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import static com.google.android.gms.common.internal.Preconditions.checkNotNull;

public class DiscoverChildFragment extends Fragment implements DiscoverChildFragmentContract.View {
    private DiscoverChildFragmentContract.Presenter mPresenter;
    private Context mContext;
    private DiscoverChildAdapter mDiscoverChildAdapter;

//    private DiscoverChildFragment(String channelId) {
//        if (mPresenter == null) {
//            mPresenter = new DiscoverChildPresenter(this, channelId);
//        }
//    }
//
//    public static DiscoverChildFragment newInstance(String channelId) {
//        return new DiscoverChildFragment(channelId);
//    }
    private DiscoverChildFragment(DiscoverTab discoverTab) {
        if (mPresenter == null) {
            mPresenter = new DiscoverChildPresenter(this, discoverTab);
        }
    }

    public static DiscoverChildFragment newInstance(DiscoverTab discoverTab) {
        return new DiscoverChildFragment(discoverTab);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_child, container, false);
        mContext = view.getContext();
        view.findViewById(R.id.constraintlayout_info).setVisibility(View.GONE);

        RecyclerView recyclerView = view.findViewById(R.id.recyclerview_discover_main);
        recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        if (recyclerView.getItemDecorationCount() == 0) recyclerView.addItemDecoration(dec);
        mDiscoverChildAdapter = new DiscoverChildAdapter(new GetSearchList(), mPresenter);
        recyclerView.setAdapter(mDiscoverChildAdapter);
        recyclerView.addOnScrollListener(mOnScrollListener);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mPresenter.start();
    }

    private RecyclerView.OnScrollListener mOnScrollListener = new RecyclerView.OnScrollListener() {
        @Override
        public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
            super.onScrollStateChanged(recyclerView, newState);
            if (recyclerView.getLayoutManager() != null) {
                mPresenter.onScrollStateChanged(
                        recyclerView.getLayoutManager().getChildCount(),
                        recyclerView.getLayoutManager().getItemCount(),
                        newState);
            }
        }

        @Override
        public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);
            mPresenter.onScrolled(recyclerView.getLayoutManager());
        }
    };

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

    @Override
    public void setPresenter(DiscoverChildFragmentContract.Presenter presenter) {
        mPresenter = checkNotNull(presenter);
    }

    @Override
    public void updateSearchItems(GetSearchList bean) {
        mDiscoverChildAdapter.updateData(bean);
    }

    @Override
    public void showDetailUi(Object content, boolean isBottomShown) {
        ((BeChefActivity) mContext).transToDetail(content, true);
    }
}
