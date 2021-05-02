package com.paula.android.bechef.data.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Query;

import com.paula.android.bechef.data.entity.RecipeItem;

import java.util.ArrayList;
import java.util.List;

@Dao
public interface RecipeItemDao extends BaseDao<RecipeItem> {
    @Query("UPDATE recipe_item SET tab_uid = :newTabUid WHERE uid = :uid")
    void setNewTabUid(long uid, long newTabUid);

    @Query("SELECT * FROM recipe_item WHERE tab_uid = :tabUid ORDER BY created_time ASC")
    LiveData<List<RecipeItem>> getAllWithTimeAscLive(long tabUid);

    @Query("SELECT * FROM recipe_item WHERE tab_uid = :tabUid ORDER BY created_time DESC")
    LiveData<List<RecipeItem>> getAllWithTimeDescLive(long tabUid);

    @Query("SELECT * FROM recipe_item WHERE tab_uid = :tabUid ORDER BY rating ASC")
    LiveData<List<RecipeItem>> getAllWithRatingAscLive(long tabUid);

    @Query("SELECT * FROM recipe_item WHERE tab_uid = :tabUid ORDER BY rating DESC")
    LiveData<List<RecipeItem>> getAllWithRatingDescLive(long tabUid);

    @Query("DELETE FROM recipe_item WHERE uid IN (:uids)")
    void deleteItemsWithUid(ArrayList<Long> uids);

    @Query("DELETE FROM recipe_item WHERE tab_uid = :tabUid")
    void deleteItemWithTabUid(long tabUid);

    @Query("SELECT * FROM recipe_item WHERE (tab_uid = :tabUid OR :tabUid = 0) AND title LIKE '%' || :keyword || '%'")
    List<RecipeItem> findRelatedTitles(String keyword, Long tabUid);

    @Query("SELECT * FROM recipe_item WHERE (tab_uid = :tabUid OR :tabUid = 0) AND tags LIKE '%' || :keyword || '%'")
    List<RecipeItem> findRelatedTags(String keyword, Long tabUid);

    @Query("SELECT * FROM recipe_item WHERE (tab_uid = :tabUid OR :tabUid = 0) AND description LIKE '%' || :keyword || '%'")
    List<RecipeItem> findRelatedDescriptions(String keyword, Long tabUid);
}
