package com.paula.android.bechef.recipeChild;

import androidx.lifecycle.LiveData;

import com.paula.android.bechef.customChild.CustomChildPresenter;
import com.paula.android.bechef.data.database.ItemDatabase;
import com.paula.android.bechef.data.entity.RecipeItem;
import com.paula.android.bechef.utils.Constants;

import java.util.List;

public class RecipeChildPresenter extends CustomChildPresenter<RecipeItem> {
    RecipeChildPresenter(RecipeChildFragment recipeChildFragment, long tabUid) {
        super(recipeChildFragment, tabUid);
    }

    @Override
    public void loadSpecificItems(int type) {
        mDataFilterType = type;
        LiveData<List<RecipeItem>> liveData;
        switch (mDataFilterType) {
            case Constants.FILTER_WITH_TIME_ASC:
                liveData = ItemDatabase.getItemInstance().recipeDao()
                        .getAllWithTimeAscLive(mTabUid);
                break;
            case Constants.FILTER_WITH_TIME_DESC:
                liveData = ItemDatabase.getItemInstance().recipeDao()
                        .getAllWithTimeDescLive(mTabUid);
                break;
            case Constants.FILTER_WITH_RATING_ASC:
                liveData = ItemDatabase.getItemInstance().recipeDao()
                        .getAllWithRatingAscLive(mTabUid);
                break;
            case Constants.FILTER_WITH_RATING_DESC:
                liveData = ItemDatabase.getItemInstance().recipeDao()
                        .getAllWithRatingDescLive(mTabUid);
                break;
            default:
                return;
        }
        addDataObserver(liveData);
    }
}