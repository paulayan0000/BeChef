package com.paula.android.bechef.data.converter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.paula.android.bechef.data.Step;

import java.lang.reflect.Type;
import java.util.ArrayList;

import androidx.room.TypeConverter;

public class StepsConverter {
    private final Gson gson = new Gson();
    private final Type type = new TypeToken<ArrayList<Step>>(){}.getType();

    @TypeConverter
    public ArrayList<Step> stepsFromJsonString(String json) {
        if (json == null) {
            return (null);
        }
        return gson.fromJson(json, type);
    }

    @TypeConverter
    public String stepsToJsonArray(ArrayList<Step> steps) {
        if (steps == null) {
            return (null);
        }
        return gson.toJson(steps, type);
    }
}