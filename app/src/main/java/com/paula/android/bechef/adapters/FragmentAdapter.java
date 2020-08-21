package com.paula.android.bechef.adapters;

import com.paula.android.bechef.bookmarkChild.BookmarkChildFragment;
import com.paula.android.bechef.discoverChild.DiscoverChildFragment;
import com.paula.android.bechef.receiptChild.ReceiptChildFragment;
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
                return BookmarkChildFragment.newInstance(position, mFragment);
            default:
                return ReceiptChildFragment.newInstance(position, mFragment);
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
        updateData(newTabTitles);
        if (newChannelIds != null) {
            mChannelIds = newChannelIds;
            notifyDataSetChanged();
        }
    }

    public void updateData(ArrayList<String> newTabTitles) {
        if (newTabTitles != null) {
            mTabTitles = newTabTitles;
            notifyDataSetChanged();
        }
    }
}
