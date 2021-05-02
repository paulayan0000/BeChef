package com.paula.android.bechef.data;

import androidx.room.Ignore;

import java.util.ArrayList;

public class MaterialGroup {
    private String mGroupName;
    private ArrayList<String> mMaterialContents;

    public MaterialGroup() {
        mGroupName = "";
        mMaterialContents = new ArrayList<>();
    }

    @Ignore
    public MaterialGroup(String groupName) {
        mGroupName = groupName;
        mMaterialContents = new ArrayList<>();
    }

    @Ignore
    public MaterialGroup(MaterialGroup materialGroup) {
        mGroupName = materialGroup.getGroupName();
        mMaterialContents = new ArrayList<>();
        mMaterialContents.addAll(materialGroup.getMaterialContents());
    }

    public String getGroupName() {
        return mGroupName;
    }

    public void setGroupName(String groupName) {
        mGroupName = groupName;
    }

    public ArrayList<String> getMaterialContents() {
        return mMaterialContents;
    }

    public void setMaterialContents(ArrayList<String> materialContents) {
        mMaterialContents = materialContents;
    }
}
