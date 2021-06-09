package com.paula.android.bechef.data.entity;

import androidx.room.Entity;

@Entity(tableName = "recipe_tab")
public class RecipeTab extends BaseTab {
    public RecipeTab(String tabName) {
        super(tabName);
    }

    public RecipeTab(RecipeTab recipeTab) {
        super(recipeTab);
    }
}