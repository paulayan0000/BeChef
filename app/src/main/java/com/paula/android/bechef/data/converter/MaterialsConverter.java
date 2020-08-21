package com.paula.android.bechef.data.converter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.paula.android.bechef.data.Material;
import java.lang.reflect.Type;
import java.util.ArrayList;

import androidx.room.TypeConverter;

public class MaterialsConverter {
    private Gson gson = new Gson();
    private Type type = new TypeToken<ArrayList<Material>>() {}.getType();

    @TypeConverter
    public ArrayList<Material> materialsFromJsonArray(String json) {
        if (json == null) {
            return (null);
        }
        return gson.fromJson(json, type);
    }

    @TypeConverter
    public String materialsToJsonArray(ArrayList<Material> materials) {
        if (materials == null) {
            return (null);
        }
        return gson.toJson(materials);
    }
}
