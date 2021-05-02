package com.paula.android.bechef.recipe;

import android.view.View;

import androidx.fragment.app.Fragment;

import com.paula.android.bechef.customMain.CustomMainFragment;
import com.paula.android.bechef.data.entity.RecipeItem;
import com.paula.android.bechef.dialog.EditItemDialog;
import com.paula.android.bechef.R;
import com.paula.android.bechef.recipeChild.RecipeChildFragment;
import com.paula.android.bechef.utils.Constants;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class RecipeFragment extends CustomMainFragment {
    public static RecipeFragment newInstance() {
        return new RecipeFragment();
    }

    @Override
    protected int getTitleText() {
        return R.string.title_recipe;
    }

    private void createNewData() {
        RecipeItem newRecipeItem = new RecipeItem();
        Fragment childFragment = getChildFragment(getCurrentTabIndex());
        if (childFragment != null) {
            newRecipeItem.setTabUid(((RecipeChildFragment) childFragment).getTabUid());
            newRecipeItem.setCreatedTime(new SimpleDateFormat(getString(R.string.date_format),
                    Locale.getDefault()).format(new Date()));
            new EditItemDialog(newRecipeItem).show(getChildFragmentManager(), Constants.DIALOG_TAG_NEW);
        }
    }

    @Override
    protected void setNewButton(View view) {
        view.setVisibility(View.VISIBLE);
        view.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        if (v.getId() == R.id.imagebutton_new) {
            if (!mIsSelectable) createNewData();
        }
    }
}