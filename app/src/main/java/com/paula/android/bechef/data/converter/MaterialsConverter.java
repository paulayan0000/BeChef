package com.paula.android.bechef.data.converter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.paula.android.bechef.data.MaterialGroup;

import java.lang.reflect.Type;
import java.util.ArrayList;

import androidx.room.TypeConverter;

public class MaterialsConverter {
    private Gson gson = new Gson();
    private Type type = new TypeToken<ArrayList<MaterialGroup>>(){}.getType();

    @TypeConverter
    public ArrayList<MaterialGroup> materialsFromJsonArray(String json) {
        if (json == null) {
            return (null);
        }
        return gson.fromJson(json, type);
    }

    @TypeConverter
    public String materialsToJsonArray(ArrayList<MaterialGroup> materialGroups) {
        if (materialGroups == null) {
            return (null);
        }
        return gson.toJson(materialGroups);
    }
}
