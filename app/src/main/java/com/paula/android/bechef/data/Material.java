package com.paula.android.bechef.data;

public class Material {
    private int mMaterialIndex;
    private String mMaterialType;
    private String mMaterialName;
    private String mMaterialAmount;

    public Material(int materialIndex, String materialType, String materialName, String materialAmount) {
        mMaterialIndex = materialIndex;
        mMaterialType = materialType;
        mMaterialName = materialName;
        mMaterialAmount = materialAmount;
    }

    public int getMaterialIndex() {
        return mMaterialIndex;
    }

    public void setMaterialIndex(int materialIndex) {
        mMaterialIndex = materialIndex;
    }

    public String getMaterialType() {
        return mMaterialType;
    }

    public void setMaterialType(String materialType) {
        mMaterialType = materialType;
    }

    public String getMaterialName() {
        return mMaterialName;
    }

    public void setMaterialName(String materialName) {
        mMaterialName = materialName;
    }

    public String getMaterialAmount() {
        return mMaterialAmount;
    }

    public void setMaterialAmount(String materialAmount) {
        mMaterialAmount = materialAmount;
    }
}
