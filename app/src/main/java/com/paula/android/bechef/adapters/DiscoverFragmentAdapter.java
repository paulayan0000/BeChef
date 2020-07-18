package com.paula.android.bechef.adapters;

import android.content.Context;
import android.os.AsyncTask;

import com.paula.android.bechef.bookmarkChild.BookmarkChildFragment;
import com.paula.android.bechef.discoverChild.DiscoverChildFragment;
import com.paula.android.bechef.objects.DiscoverItem;

import java.util.ArrayList;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.room.Room;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public class DiscoverFragmentAdapter extends FragmentStateAdapter {

    private Fragment mFragment;
    private ArrayList<String> mTabTitles;
    private Context mContext;

    public DiscoverFragmentAdapter(@NonNull Fragment fragment, ArrayList<String> tabTitles) {
        super(fragment);
        mFragment = fragment;
        mTabTitles = tabTitles;
        mContext = fragment.getContext();
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        String className = mFragment.getClass().getSimpleName();

        switch (className) {
            case "DiscoverFragment":
                return DiscoverChildFragment.newInstance(position);
            case "BookmarkFragment":
                return BookmarkChildFragment.newInstance(mTabTitles.get(position));

            default:
                return BookmarkChildFragment.newInstance("");
        }
    }

    @Override
    public int getItemCount() {
        return mTabTitles.size();
    }

    public ArrayList<String> getTabTitles() {
        return mTabTitles;
    }

    public void updateTabTitles(ArrayList<String> newTitleArray) {
        if (newTitleArray != null) {
            mTabTitles = newTitleArray;
            notifyDataSetChanged();
        }
    }
}
