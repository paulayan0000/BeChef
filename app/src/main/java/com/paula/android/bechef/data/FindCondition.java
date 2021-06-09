package com.paula.android.bechef.data;

import com.paula.android.bechef.data.entity.BaseTab;

import java.util.ArrayList;

public class FindCondition {
    private String mConditionName;
    private ArrayList<BaseTab> mConditionContents;

    public FindCondition(String conditionName, String... tabNames) {
        mConditionName = conditionName;
        mConditionContents = new ArrayList<>();
        for (String tabName : tabNames) {
            mConditionContents.add(new BaseTab(tabName));
        }
    }

    public String getConditionName() {
        return mConditionName;
    }

    public void setConditionName(String conditionName) {
        mConditionName = conditionName;
    }

    public ArrayList<BaseTab> getConditionContents() {
        return mConditionContents;
    }

    public void setConditionContents(ArrayList<BaseTab> conditionContents) {
        mConditionContents = conditionContents;
    }
}