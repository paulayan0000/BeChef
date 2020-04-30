package com.paula.android.bechef.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.paula.android.bechef.R;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ChildAdapter extends RecyclerView.Adapter{

    private ArrayList<String> mArrayList;
    private Context mContext;

    ChildAdapter(ArrayList<String> arrayList) {
        mArrayList = arrayList;
    }

    void updateData(ArrayList<String> newDataArray) {
        if (newDataArray.size() > 0) {
            mArrayList = newDataArray;
            notifyDataSetChanged();
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_discover_recycler, parent, false);
        return new DiscoverViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ChildAdapter.DiscoverViewHolder) {
            if (mArrayList.size() > 0) {
                ((DiscoverViewHolder) holder).getTextView().setText(mArrayList.get(position));
            }
        }
    }

    @Override
    public int getItemCount() {
        return mArrayList.size();
    }

    private class DiscoverViewHolder extends RecyclerView.ViewHolder {

        private TextView mTextView;

        private DiscoverViewHolder(@NonNull View itemView) {
            super(itemView);
            mTextView = itemView.findViewById(R.id.textview_recycler);
        }

        private TextView getTextView() {
            return mTextView;
        }
    }
}
