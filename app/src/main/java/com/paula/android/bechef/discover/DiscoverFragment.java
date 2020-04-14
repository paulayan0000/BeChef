package com.paula.android.bechef.discover;


import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.paula.android.bechef.R;

import static com.google.android.gms.common.internal.Preconditions.checkNotNull;

public class DiscoverFragment extends Fragment implements DiscoverContract.View{

    private DiscoverContract.Presenter mPresenter;

    public DiscoverFragment() {
        // Required empty public constructor
    }

    public static DiscoverFragment newInstance() {
        return new DiscoverFragment();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_discover, container, false);
        return root;
    }

    @Override
    public void setPresenter(DiscoverContract.Presenter presenter) {
        mPresenter = checkNotNull(presenter);
    }
}
