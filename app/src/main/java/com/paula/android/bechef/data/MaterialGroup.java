package com.paula.android.bechef.data;

import java.util.ArrayList;

import androidx.room.Ignore;

public class MaterialGroup {
//    private int mMaterialIndex;
//    private String mMaterialType;
//    private String mMaterialName;
//    private String mMaterialAmount;
    private String mGroupName;
    private ArrayList<String> mMaterialContents;

//    public Material(int materialIndex, String materialType, String materialName, String materialAmount) {
    public MaterialGroup(String groupName, ArrayList<String> materialContents) {
//        mMaterialIndex = materialIndex;
//        mMaterialType = materialType;
//        mMaterialName = materialName;
//        mMaterialAmount = materialAmount;
        mGroupName = groupName;
        mMaterialContents = materialContents;
    }

    @Ignore
    public MaterialGroup() {
        mGroupName = "";
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

    //    public int getMaterialIndex() {
//        return mMaterialIndex;
//    }
//
//    public void setMaterialIndex(int materialIndex) {
//        mMaterialIndex = materialIndex;
//    }
//
//    public String getMaterialType() {
//        return mMaterialType;
//    }
//
//    public void setMaterialType(String materialType) {
//        mMaterialType = materialType;
//    }
//
//    public String getMaterialName() {
//        return mMaterialName;
//    }
//
//    public void setMaterialName(String materialName) {
//        mMaterialName = materialName;
//    }
//    public String getMaterialAmount() {
//        return mMaterialAmount;
//    }
//
//    public void setMaterialAmount(String materialAmount) {
//        mMaterialAmount = materialAmount;
//    }
}
