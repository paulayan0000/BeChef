package com.paula.android.bechef.data;

import java.util.ArrayList;

public class Step {
    private int mStepNumber;
    private String mStepDescription;
    private ArrayList<String> mImageUrls;

    public Step(int stepNumber, String stepDescription, ArrayList<String> imageUrls) {
        mStepNumber = stepNumber;
        mStepDescription = stepDescription;
        mImageUrls = imageUrls;
    }

    public int getStepNumber() {
        return mStepNumber;
    }

    public void setStepNumber(int stepNumber) {
        mStepNumber = stepNumber;
    }

    public String getStepDescription() {
        return mStepDescription;
    }

    public void setStepDescription(String stepDescription) {
        mStepDescription = stepDescription;
    }

    public ArrayList<String> getImageUrls() {
        return mImageUrls;
    }

    public void setImageUrls(ArrayList<String> imageUrls) {
        mImageUrls = imageUrls;
    }
}
