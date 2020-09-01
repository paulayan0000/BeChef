package com.paula.android.bechef.adapters;

import com.paula.android.bechef.bookmarkChild.BookmarkChildFragment;
import com.paula.android.bechef.data.entity.BaseTab;
import com.paula.android.bechef.data.entity.BookmarkTab;
import com.paula.android.bechef.data.entity.DiscoverTab;
import com.paula.android.bechef.data.entity.ReceiptTab;
import com.paula.android.bechef.discoverChild.DiscoverChildFragment;
import com.paula.android.bechef.receiptChild.ReceiptChildFragment;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public class FragmentAdapter extends FragmentStateAdapter {
    private Fragment mFragment;
    private ArrayList<?> mTabArrayList;
//    private ArrayList<String> mTabTitles;
//    private ArrayList<String> mChannelIds;

//    public FragmentAdapter(@NonNull Fragment fragment, ArrayList<String> tabTitles, ArrayList<String> channelIds) {
//        super(fragment);
//        mFragment = fragment;
//        mTabTitles = tabTitles;
//        mChannelIds = channelIds;
//    }
//
//    public FragmentAdapter(@NonNull Fragment fragment, ArrayList<String> tabTitles) {
//        super(fragment);
//        mFragment = fragment;
//        mTabTitles = tabTitles;
//    }


    public FragmentAdapter(@NonNull Fragment fragment, ArrayList<BaseTab> tabArrayList) {
        super(fragment);
        mFragment = fragment;
        mTabArrayList = tabArrayList;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        String className = mFragment.getClass().getSimpleName();
        switch (className) {
            case "DiscoverFragment":
//                return DiscoverChildFragment.newInstance(mChannelIds.get(position));
                return DiscoverChildFragment.newInstance((DiscoverTab) mTabArrayList.get(position));
            case "BookmarkFragment":
//                return BookmarkChildFragment.newInstance(position, mFragment);
                return BookmarkChildFragment.newInstance((BookmarkTab) mTabArrayList.get(position), mFragment);
            default:
//                return ReceiptChildFragment.newInstance(position, mFragment);
                return ReceiptChildFragment.newInstance((ReceiptTab) mTabArrayList.get(position), mFragment);
        }
    }

    @Override
    public int getItemCount() {
//        return mTabTitles.size();
        return mTabArrayList.size();
    }

//    public ArrayList<String> getTabTitles() {
//        return mTabTitles;
//    }


    public ArrayList<?> getTabArrayList() {
        return mTabArrayList;
    }

    public void updateData(ArrayList<?> tabArrayList) {
        if (tabArrayList != null) {
            mTabArrayList = tabArrayList;
            notifyDataSetChanged();
        }
    }

//    public void updateData(ArrayList<String> newTabTitles, ArrayList<String> newChannelIds) {
//        updateData(newTabTitles);
//        if (newChannelIds != null) {
//            mChannelIds = newChannelIds;
//            notifyDataSetChanged();
//        }
//    }
//
//    public void updateData(ArrayList<String> newTabTitles) {
//        if (newTabTitles != null) {
//            mTabTitles = newTabTitles;
//            notifyDataSetChanged();
//        }
//    }
}
