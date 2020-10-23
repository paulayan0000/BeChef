package com.paula.android.bechef.data.entity;

import com.paula.android.bechef.data.MaterialGroup;
import com.paula.android.bechef.data.Step;
import com.paula.android.bechef.data.converter.MaterialsConverter;
import com.paula.android.bechef.data.converter.StepsConverter;

import java.util.ArrayList;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.TypeConverters;

@Entity(tableName = "receipt_item")
public class ReceiptItem extends BaseItem {
    @TypeConverters(StepsConverter.class)
    @ColumnInfo(name = "steps")
    private ArrayList<Step> mSteps;

    @TypeConverters(MaterialsConverter.class)
    @ColumnInfo(name = "materials")
    private ArrayList<MaterialGroup> mMaterialGroups;

    @ColumnInfo(name = "duration")
    private String mDuration;

    @ColumnInfo(name = "weight")
    private int mWeight = -1;

    public ReceiptItem(ArrayList<Step> steps, ArrayList<MaterialGroup> materialGroups, String duration, int weight,
                       int tabUid, String title, String imageUrl, double rating, String tags,
                       int inTodayId, String createdTime, String description, String videoId) {
        super(tabUid, title, imageUrl, rating, tags, inTodayId, createdTime, description, videoId);
        mSteps = steps;
        mMaterialGroups = materialGroups;
        mDuration = duration;
        mWeight = weight;
    }

    @Ignore
    public ReceiptItem(ReceiptItem receiptItem) {
        super(receiptItem);
        super.setUid(receiptItem.getUid());
        mSteps = new ArrayList<>();
        ArrayList<Step> oldSteps = receiptItem.getSteps();
        Step newStep;
        for (Step step : oldSteps) {
            newStep = new Step(step);
            mSteps.add(newStep);
        }
        mMaterialGroups = new ArrayList<>();
        ArrayList<MaterialGroup> oldGroup = receiptItem.getMaterialGroups();
        MaterialGroup newGroup;
        for (MaterialGroup group : oldGroup) {
            newGroup = new MaterialGroup(group);
            mMaterialGroups.add(newGroup);
        }
        mDuration = receiptItem.getDuration();
        mWeight = receiptItem.getWeight();
    }

    @Ignore
    public ReceiptItem() {
        super();
        mSteps = new ArrayList<>();
        mMaterialGroups = new ArrayList<>();
        mDuration = "";
    }

    public void setParams(int index, String content) {
        switch (index) {
            case 0:
                setTags(content);
                break;
            case 1:
                setTitle(content);
                break;
            case 3:
                setDuration(content);
                break;
            case 4:
                if (!"".equals(content)) setWeight(Integer.parseInt(content));
                else setWeight(-1);
                break;
            case 5:
                setDescription(content);
                break;
        }
    }

    public String getParams(int index) {
        switch (index) {
            case 0:
                return getTags();
            case 1:
                return getTitle();
            case 3:
                return getDuration();
            case 4:
                return getWeight() > 0 ? String.valueOf(getWeight()) : "";
            case 5:
                return getDescription();
        }
        return "";
    }

    public ArrayList<Step> getSteps() {
        return mSteps;
    }

    public void setSteps(ArrayList<Step> steps) {
        mSteps = steps;
    }

    public ArrayList<MaterialGroup> getMaterialGroups() {
        return mMaterialGroups;
    }

    public void setMaterialGroups(ArrayList<MaterialGroup> materialGroups) {
        mMaterialGroups = materialGroups;
    }

    public String getDuration() {
        return mDuration;
    }

    public void setDuration(String duration) {
        mDuration = duration;
    }

    public int getWeight() {
        return mWeight;
    }

    public void setWeight(int weight) {
        mWeight = weight;
    }
}
