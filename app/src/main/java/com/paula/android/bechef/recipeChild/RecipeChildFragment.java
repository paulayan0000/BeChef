package com.paula.android.bechef.recipeChild;

import androidx.fragment.app.Fragment;

import com.paula.android.bechef.customChild.CustomChildFragment;
import com.paula.android.bechef.data.entity.RecipeItem;

public class RecipeChildFragment extends CustomChildFragment<RecipeItem> {
    public RecipeChildFragment(long tabUid, Fragment fragment) {
        super(fragment);
        mCustomChildPresenter = new RecipeChildPresenter(this, tabUid);
    }

    public static RecipeChildFragment newInstance(long tabUid, Fragment fragment) {
        return new RecipeChildFragment(tabUid, fragment);
    }

    public long getTabUid() {
        return mCustomChildPresenter.getTabUid();
    }
}