package com.paula.android.bechef.data;

import java.util.ArrayList;

import androidx.room.Ignore;

public class Step {
//    private int mStepNumber;
    private String mStepDescription;
    private ArrayList<String> mImageUrls;

//    public Step(int stepNumber, String stepDescription, ArrayList<String> imageUrls) {
    public Step(String stepDescription, ArrayList<String> imageUrls) {

//        mStepNumber = stepNumber;
        mStepDescription = stepDescription;
        mImageUrls = imageUrls;
    }

    @Ignore
    public Step(Step step) {
//        mStepNumber = step.getStepNumber();
        mStepDescription = step.getStepDescription();
        mImageUrls = new ArrayList<>();
        ArrayList<String> oldUrls = step.getImageUrls();
        String newUrl;
        for (String imageUrl : oldUrls) {
            newUrl = imageUrl;
            mImageUrls.add(newUrl);
        }
    }

    @Ignore
    public Step() {
        mStepDescription = "";
        mImageUrls = new ArrayList<>();
    }

//    public int getStepNumber() {
//        return mStepNumber;
//    }
//
//    public void setStepNumber(int stepNumber) {
//        mStepNumber = stepNumber;
//    }

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
