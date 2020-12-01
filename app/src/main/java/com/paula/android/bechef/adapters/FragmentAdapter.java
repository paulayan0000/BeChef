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
    private ArrayList<BaseTab> mTabArrayList;

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
                return DiscoverChildFragment.newInstance((DiscoverTab) mTabArrayList.get(position));
            case "BookmarkFragment":
                return BookmarkChildFragment.newInstance((BookmarkTab) mTabArrayList.get(position), mFragment);
            default:
                return ReceiptChildFragment.newInstance((ReceiptTab) mTabArrayList.get(position), mFragment);
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
            notifyDataSetChanged();
        }
    }

    @Override
    public long getItemId(int position) {
        return mTabArrayList.get(position).getUid();
    }
}
