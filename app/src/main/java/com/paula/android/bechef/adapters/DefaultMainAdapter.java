package com.paula.android.bechef.adapters;

import android.content.Context;
import android.graphics.Rect;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.paula.android.bechef.BasePresenter;
import com.paula.android.bechef.R;
import com.paula.android.bechef.api.beans.GetSearchList;
import com.paula.android.bechef.utils.Utils;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class DefaultMainAdapter extends RecyclerView.Adapter {

    private ArrayList<String> mTabTitles;
    private Context mContext;
    private DefaultChildAdapter mDefaultChildAdapter;
    private BasePresenter mPresenter;
    private RecyclerView mRecyclerView;
    private ArrayList<RecyclerView> mRecyclerViews;

    public DefaultMainAdapter(ArrayList<String> tabTitles, GetSearchList bean, BasePresenter presenter) {
        mTabTitles = tabTitles;
        mPresenter = presenter;
        // TODO: Simplify bean
        mDefaultChildAdapter = new DefaultChildAdapter(bean, presenter);
        mRecyclerViews = new ArrayList<>();
    }

    public void updateData(ArrayList<String> newTitleArray, ArrayList<GetSearchList> newBeans) {
        if (newTitleArray == null && newBeans == null) {
            return;
        }
        if (newTitleArray != null) {
            mTabTitles = newTitleArray;
            notifyDataSetChanged();
        }
        if (newBeans != null) {
            mDefaultChildAdapter.updateData(newBeans.get(0));
        }
        Log.d("MainAdapter", "before notify");
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
        Log.d("MainAdapter", "onBindViewHolder: " + position);
        mRecyclerView = ((ViewPagerViewHolder) holder).getRecyclerView();

        mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        if (mRecyclerView.getItemDecorationCount() == 0) mRecyclerView.addItemDecoration(dec);
        mRecyclerView.setAdapter(mDefaultChildAdapter);

        mRecyclerViews.add(position, mRecyclerView);
    }

    public RecyclerView getSpecificRecyclerView(int index) {
        return mRecyclerViews.get(index);
    }

    private RecyclerView.ItemDecoration dec = new RecyclerView.ItemDecoration() {
        @Override
        public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
            super.getItemOffsets(outRect, view, parent, state);
            if (outRect.top == 0) outRect.top = (int) Utils.convertDpToPixel((float) 8, mContext);

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
