package com.paula.android.bechef.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.paula.android.bechef.R;
import com.paula.android.bechef.data.FilterItem;
import com.paula.android.bechef.data.entity.BaseTab;
import com.paula.android.bechef.search.SearchPresenter;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class FilterItemAdapter extends RecyclerView.Adapter<FilterItemAdapter.FilterItemViewHolder> {
    private Context mContext;
    private SearchPresenter mSearchPresenter;
    private ArrayList<FilterItem> mFilterItems;
    private ArrayList<BaseTab> mChosenTabs = new ArrayList<>();

    public FilterItemAdapter(ArrayList<FilterItem> filterItems, SearchPresenter searchPresenter) {
        mFilterItems = filterItems;
        mSearchPresenter = searchPresenter;
    }

    @NonNull
    @Override
    public FilterItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_search_filters, parent, false);
        return new FilterItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FilterItemViewHolder holder, int position) {
        holder.bindView(position);
    }


    @Override
    public int getItemCount() {
        return mFilterItems.size();
    }

    public void updateData(ArrayList<FilterItem> filterItems) {
        mFilterItems = filterItems;
        notifyDataSetChanged();
    }

    public ArrayList<BaseTab> getChosenTabs() {
        return mChosenTabs;
    }

    class FilterItemViewHolder extends RecyclerView.ViewHolder {
        private TextView mTvName;
        private Spinner mSpinner;

        public FilterItemViewHolder(@NonNull View itemView) {
            super(itemView);
            mTvName = itemView.findViewById(R.id.textview_filter_name);
            mSpinner = itemView.findViewById(R.id.spinner_tab_name);
        }

        void bindView(final int index) {
            FilterItem filterItem = mFilterItems.get(index);
            mTvName.setText(filterItem.getFilterType());
             final ArrayList<BaseTab> filterContents = filterItem.getFilterContents();

            mSpinner.setAdapter(new ArrayAdapter<>(mContext, android.R.layout.simple_spinner_dropdown_item, filterContents));
            mSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    // TODO: set filter condition with chosen item
                    if (mChosenTabs.size() <= index)
                        mChosenTabs.add(index, filterContents.get(position));
                    else {
                        mChosenTabs.set(index, filterContents.get(position));
                        mSearchPresenter.startQuery();
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                }
            });
        }
    }
}
