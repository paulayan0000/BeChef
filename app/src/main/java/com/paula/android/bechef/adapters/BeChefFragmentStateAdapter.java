package com.paula.android.bechef.adapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.paula.android.bechef.bookmark.BookmarkFragment;
import com.paula.android.bechef.bookmarkChild.BookmarkChildFragment;
import com.paula.android.bechef.data.entity.BaseTab;
import com.paula.android.bechef.data.entity.DiscoverTab;
import com.paula.android.bechef.discover.DiscoverFragment;
import com.paula.android.bechef.discoverChild.DiscoverChildFragment;
import com.paula.android.bechef.recipeChild.RecipeChildFragment;

import java.util.ArrayList;

public class BeChefFragmentStateAdapter extends FragmentStateAdapter {
    private final Fragment mFragment;
    private ArrayList<BaseTab> mTabArrayList;

    public BeChefFragmentStateAdapter(@NonNull Fragment fragment, ArrayList<BaseTab> tabArrayList) {
        super(fragment);
        mFragment = fragment;
        mTabArrayList = tabArrayList;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        if (mFragment instanceof DiscoverFragment) {
            return DiscoverChildFragment.newInstance((DiscoverTab) mTabArrayList.get(position));
        } else if (mFragment instanceof BookmarkFragment) {
            return BookmarkChildFragment.newInstance(mTabArrayList.get(position).getUid(), mFragment);
        } else {
            return RecipeChildFragment.newInstance(mTabArrayList.get(position).getUid(), mFragment);
        }
    }

    @Override
    public int getItemCount() {
        return mTabArrayList.size();
    }

    public ArrayList<BaseTab> getTabArrayList() {
        return mTabArrayList;
    }

    public void updateData(ArrayList<BaseTab> tabArrayList) {
        if (tabArrayList != null) {
            mTabArrayList = tabArrayList;
        } else {
            mTabArrayList = new ArrayList<>();
        }
        notifyDataSetChanged();
    }

    @Override
    public long getItemId(int position) {
        return mTabArrayList.get(position).getUid();
    }
}
