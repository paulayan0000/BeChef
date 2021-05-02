package com.paula.android.bechef.recipe;

import com.paula.android.bechef.customMain.CustomMainFragment;
import com.paula.android.bechef.customMain.CustomMainPresenter;
import com.paula.android.bechef.data.database.TabDatabase;
import com.paula.android.bechef.data.entity.BaseTab;
import com.paula.android.bechef.data.entity.RecipeTab;

import androidx.lifecycle.Observer;

import java.util.ArrayList;
import java.util.List;

public class RecipePresenter extends CustomMainPresenter {

    public RecipePresenter(CustomMainFragment customView) {
        super(customView);
    }

    @Override
    public void start() {
        loadRecipeTabs();
    }

    private void loadRecipeTabs() {
        TabDatabase.getTabInstance(getContext()).recipeDao().getAllLive()
                .observe(mCustomMainView, new Observer<List<RecipeTab>>() {
                    @Override
                    public void onChanged(List<RecipeTab> recipeTabs) {
                        ArrayList<BaseTab> baseTabs = new ArrayList<BaseTab>(recipeTabs);
                        setTabs(baseTabs);
                        mCustomMainView.showDefaultUi(baseTabs);
                    }
                });
    }
}
