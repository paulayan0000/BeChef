package com.paula.android.bechef.data;

import androidx.room.Ignore;

import java.util.ArrayList;

public class Step {
    private String mStepDescription;
    private ArrayList<String> mImageUrls;

    public Step() {
        mStepDescription = "";
        mImageUrls = new ArrayList<>();
    }

    @Ignore
    public Step(Step step) {
        mStepDescription = step.getStepDescription();
        mImageUrls = new ArrayList<>();
        ArrayList<String> oldUrls = step.getImageUrls();
        String newUrl;
        for (String imageUrl : oldUrls) {
            newUrl = imageUrl;
            mImageUrls.add(newUrl);
        }
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