package com.paula.android.bechef.today;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.paula.android.bechef.R;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import static com.google.android.gms.common.internal.Preconditions.checkNotNull;

public class TodayFragment extends Fragment implements TodayContract.View {
    private TodayContract.Presenter mPresenter;

    public TodayFragment() {
        // Required empty public constructor
    }

    public static TodayFragment newInstance() {
        return new TodayFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_today, container, false);
        return root;
    }

    @Override
    public void setPresenter(TodayContract.Presenter presenter) {
        mPresenter = checkNotNull(presenter);
    }
}
