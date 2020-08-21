package com.paula.android.bechef.data.entity;

import com.paula.android.bechef.data.Material;
import com.paula.android.bechef.data.Step;
import com.paula.android.bechef.data.converter.MaterialsConverter;
import com.paula.android.bechef.data.converter.StepsConverter;
import java.util.ArrayList;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.TypeConverters;

@Entity(tableName = "receipt_item")
public class ReceiptItem extends BaseItem {

    @TypeConverters(StepsConverter.class)
    @ColumnInfo(name = "steps")
    private ArrayList<Step> mSteps;

    @TypeConverters(MaterialsConverter.class)
    @ColumnInfo(name = "materials")
    private ArrayList<Material> mMaterials;

    @ColumnInfo(name = "duration")
    private String mDuration;

    @ColumnInfo(name = "weight")
    private double mWeight;

    public ReceiptItem(int uid, ArrayList<Step> steps, ArrayList<Material> materials, String duration, double weight,
                       int tabUid, String title, String imageUrl, double rating, String tags,
                       int inTodayId, int itemIndex, String description, String videoId) {
        super(uid, tabUid, title, imageUrl, rating, tags, inTodayId, itemIndex, description, videoId);
        mSteps = steps;
        mMaterials = materials;
        mDuration = duration;
        mWeight = weight;
    }

    public ArrayList<Step> getSteps() {
        return mSteps;
    }

    public void setSteps(ArrayList<Step> steps) {
        mSteps = steps;
    }

    public ArrayList<Material> getMaterials() {
        return mMaterials;
    }

    public void setMaterials(ArrayList<Material> materials) {
        mMaterials = materials;
    }

    public String getDuration() {
        return mDuration;
    }

    public void setDuration(String duration) {
        mDuration = duration;
    }

    public double getWeight() {
        return mWeight;
    }

    public void setWeight(double weight) {
        mWeight = weight;
    }
}
