package com.paula.android.bechef.bookmark;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.paula.android.bechef.R;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import static com.google.android.gms.common.internal.Preconditions.checkNotNull;

public class BookmarkFragment extends Fragment implements BookmarkContract.View {

    private BookmarkContract.Presenter mPresenter;

    public BookmarkFragment() {
        // Required empty public constructor
    }

    public static BookmarkFragment newInstance() {
        return new BookmarkFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_bookmark, container, false);
        return root;
    }

    @Override
    public void setPresenter(BookmarkContract.Presenter presenter) {
        mPresenter = checkNotNull(presenter);
    }
}
