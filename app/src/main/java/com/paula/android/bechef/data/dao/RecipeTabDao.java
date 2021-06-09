package com.paula.android.bechef.data.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.paula.android.bechef.data.entity.RecipeTab;

import java.util.List;

@Dao
public interface RecipeTabDao extends BaseDao<RecipeTab> {
    @Query("SELECT * FROM recipe_tab ORDER BY uid ASC")
    LiveData<List<RecipeTab>> getAllLive();

    @Query("DELETE FROM recipe_tab WHERE uid = :tabUid")
    void deleteItemWithTabUid(long tabUid);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void updateBaseTab(RecipeTab baseTab);
}