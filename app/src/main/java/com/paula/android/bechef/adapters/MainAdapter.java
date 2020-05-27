package com.paula.android.bechef.adapters;

import android.content.Context;
import android.graphics.Rect;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.paula.android.bechef.BeChefContract;
import com.paula.android.bechef.R;
import com.paula.android.bechef.api.beans.GetSearchList;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.paula.android.bechef.utils.Utils;

public class MainAdapter extends RecyclerView.Adapter {

    private ArrayList<String> mTabTitles;
    private Context mContext;
    private ChildAdapter mChildAdapter;
    private BeChefContract.Presenter mPresenter;
    private int mViewType;

    public MainAdapter(ArrayList<String> tabTitles, GetSearchList bean, BeChefContract.Presenter presenter) {
        mTabTitles = tabTitles;
        mPresenter = presenter;
        mViewType = bean.getViewType();
        mChildAdapter = new ChildAdapter(bean);
    }

    public void updateData(ArrayList<String> newTitleArray, GetSearchList newBean) {
        if (newTitleArray != null) {
            mTabTitles = newTitleArray;
            notifyDataSetChanged();
        }
        if (newBean != null) {
            mChildAdapter.updateData(newBean);
        }
    }

    @Override
    public int getItemViewType(int position) {
        return mViewType;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_main_viewpager, parent, false);
        return new ViewPagerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        RecyclerView recyclerView = ((ViewPagerViewHolder) holder).getRecyclerView();
        recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        if(recyclerView.getItemDecorationCount() == 0) recyclerView.addItemDecoration(dec);
        recyclerView.setAdapter(mChildAdapter);
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                mPresenter.onScrollStateChanged(
                        recyclerView.getLayoutManager().getChildCount(),
                        recyclerView.getLayoutManager().getItemCount(),
                        newState);
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                mPresenter.onScrolled(recyclerView.getLayoutManager());
            }
        });
    }

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
    public int getItemCount() {
        return mTabTitles.size();
    }

    public ArrayList<String> getTabTitles() {
        return mTabTitles;
    }

    private class ViewPagerViewHolder extends RecyclerView.ViewHolder {

        private RecyclerView mRecyclerView;

        private ViewPagerViewHolder(View itemView) {
            super(itemView);
            mRecyclerView = itemView.findViewById(R.id.recycler_viewpager);
        }

        RecyclerView getRecyclerView() {
            return mRecyclerView;
        }
    }
}
