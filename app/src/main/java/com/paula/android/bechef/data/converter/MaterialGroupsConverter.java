package com.paula.android.bechef.data.converter;

import androidx.room.TypeConverter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.paula.android.bechef.data.MaterialGroup;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class MaterialGroupsConverter {
    private final Gson gson = new Gson();
    private final Type type = new TypeToken<ArrayList<MaterialGroup>>(){}.getType();

    @TypeConverter
    public ArrayList<MaterialGroup> materialGroupsFromJsonString(String json) {
        if (json == null) {
            return (null);
        }
        return gson.fromJson(json, type);
    }

    @TypeConverter
    public String materialGroupsToJsonString(ArrayList<MaterialGroup> materialGroups) {
        if (materialGroups == null) {
            return (null);
        }
        return gson.toJson(materialGroups);
    }
}