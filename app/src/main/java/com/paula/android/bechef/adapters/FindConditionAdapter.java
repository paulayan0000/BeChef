package com.paula.android.bechef.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.paula.android.bechef.R;
import com.paula.android.bechef.data.FindCondition;
import com.paula.android.bechef.data.entity.BaseTab;
import com.paula.android.bechef.find.FindPresenter;

import java.util.ArrayList;

public class FindConditionAdapter extends RecyclerView.Adapter<FindConditionAdapter.FilterItemViewHolder> {
    private final ArrayList<BaseTab> mChosenTabs = new ArrayList<>();
    private final FindPresenter mFindPresenter;
    private Context mContext;
    private ArrayList<FindCondition> mFindConditions;

    public FindConditionAdapter(ArrayList<FindCondition> findConditions, FindPresenter findPresenter) {
        mFindConditions = findConditions;
        mFindPresenter = findPresenter;
    }

    @NonNull
    @Override
    public FilterItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        View view = LayoutInflater.from(mContext)
                .inflate(R.layout.item_find_conditions, parent, false);
        return new FilterItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FilterItemViewHolder holder, int position) {
        holder.bindView(position);
    }

    @Override
    public int getItemCount() {
        return mFindConditions.size();
    }

    public void updateData(ArrayList<FindCondition> findConditions) {
        mFindConditions = findConditions;
        notifyDataSetChanged();
    }

    public ArrayList<BaseTab> getChosenTabs() {
        return mChosenTabs;
    }

    class FilterItemViewHolder extends RecyclerView.ViewHolder {
        private final TextView mTvName;
        private final Spinner mSpinner;

        public FilterItemViewHolder(@NonNull View itemView) {
            super(itemView);
            mTvName = itemView.findViewById(R.id.textview_filter_name);
            mSpinner = itemView.findViewById(R.id.spinner_tab_name);
        }

        void bindView(final int index) {
            FindCondition findCondition = mFindConditions.get(index);
            mTvName.setText(findCondition.getConditionName());
            final ArrayList<BaseTab> filterContents = findCondition.getConditionContents();

            mSpinner.setAdapter(new ArrayAdapter<>(mContext,
                    android.R.layout.simple_spinner_dropdown_item,
                    filterContents));
            mSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                    if (mChosenTabs.size() <= index)
                        mChosenTabs.add(index, filterContents.get(pos));
                    else {
                        mChosenTabs.set(index, filterContents.get(pos));
                        mFindPresenter.startQuery();
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                }
            });
        }
    }
}
