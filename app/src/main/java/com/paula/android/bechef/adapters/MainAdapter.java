package com.paula.android.bechef.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.paula.android.bechef.R;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class MainAdapter extends RecyclerView.Adapter {

    private ArrayList<String> mTabTitles;
    private Context mContext;
    private ChildAdapter mChildAdapter;

    public MainAdapter(ArrayList<String> tabTitles, ArrayList<String> contentArrayList) {
        mTabTitles = tabTitles;
        mChildAdapter = new ChildAdapter(contentArrayList);
    }

    public void updateData(ArrayList<String> newTitleArray, ArrayList<String> newDataArray) {
        if (newTitleArray.size() > 0) {
            mTabTitles = newTitleArray;
            notifyDataSetChanged();
        }
        if (newDataArray.size() > 0) {
            mChildAdapter.updateData(newDataArray);
        }
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
        if (holder instanceof ViewPagerViewHolder) {
            RecyclerView recyclerView = ((ViewPagerViewHolder) holder).getRecyclerView();
            recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
            recyclerView.setAdapter(mChildAdapter);
        }
    }

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
