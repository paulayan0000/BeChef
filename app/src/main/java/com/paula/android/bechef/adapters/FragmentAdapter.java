package com.paula.android.bechef.adapters;

import android.content.Context;
import com.paula.android.bechef.bookmarkChild.BookmarkChildFragment;
import com.paula.android.bechef.discoverChild.DiscoverChildFragment;
import java.util.ArrayList;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public class FragmentAdapter extends FragmentStateAdapter {
    private Fragment mFragment;
    private ArrayList<String> mTabTitles;
    private ArrayList<String> mChannelIds;

    public FragmentAdapter(@NonNull Fragment fragment, ArrayList<String> tabTitles, ArrayList<String> channelIds) {
        super(fragment);
        mFragment = fragment;
        mTabTitles = tabTitles;
        mChannelIds = channelIds;
    }

    public FragmentAdapter(@NonNull Fragment fragment, ArrayList<String> tabTitles) {
        super(fragment);
        mFragment = fragment;
        mTabTitles = tabTitles;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        String className = mFragment.getClass().getSimpleName();

        switch (className) {
            case "DiscoverFragment":
                return DiscoverChildFragment.newInstance(mChannelIds.get(position));
            case "BookmarkFragment":
            default:
                return BookmarkChildFragment.newInstance(position);
        }
    }

    @Override
    public int getItemCount() {
        return mTabTitles.size();
    }

    public ArrayList<String> getTabTitles() {
        return mTabTitles;
    }

    public void updateData(ArrayList<String> newTabTitles, ArrayList<String> newChannelIds) {
        if (newTabTitles != null) {
            mTabTitles = newTabTitles;
            notifyDataSetChanged();
        }
        if (newChannelIds != null) {
            mChannelIds = newChannelIds;
            notifyDataSetChanged();
        }
    }
}
