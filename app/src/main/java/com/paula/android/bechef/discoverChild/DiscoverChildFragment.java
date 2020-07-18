package com.paula.android.bechef.discoverChild;

import android.content.Context;
import android.graphics.Rect;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.paula.android.bechef.R;
import com.paula.android.bechef.adapters.DiscoverChildAdapter;
import com.paula.android.bechef.api.beans.GetSearchList;
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
    private int mTabIndex;

    private DiscoverChildFragment(int index) {
        mTabIndex = index;
    }
    public static DiscoverChildFragment newInstance(int index) {
        return new DiscoverChildFragment(index);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_main, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mContext = view.getContext();
        if (mPresenter == null) {
//            mPresenter = new DiscoverChildPresenter(this, mChannelId);
            mPresenter = new DiscoverChildPresenter(this, mTabIndex);
        }
        mPresenter.start();

        RecyclerView recyclerView = view.findViewById(R.id.recyclerview_discover_main);
        recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        if (recyclerView.getItemDecorationCount() == 0) recyclerView.addItemDecoration(dec);
        mDiscoverChildAdapter = new DiscoverChildAdapter(new GetSearchList());
        recyclerView.setAdapter(mDiscoverChildAdapter);
        recyclerView.addOnScrollListener(mOnScrollListener);
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
            if (outRect.top == 0) outRect.top = (int) Utils.convertDpToPixel((float) 16, mContext);

            if (parent.getChildAdapterPosition(view) == 0)
                outRect.top = 0;
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
}
