package com.paula.android.bechef.bookmarkChild;

import android.content.Context;
import android.graphics.Rect;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.paula.android.bechef.R;
import com.paula.android.bechef.adapters.DefaultChildAdapter;
import com.paula.android.bechef.api.beans.GetSearchList;
import com.paula.android.bechef.utils.Utils;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import static com.google.android.gms.common.internal.Preconditions.checkNotNull;

public class BookmarkChildFragment extends Fragment implements BookmarkChildFragmentContract.View {
    private BookmarkChildFragmentContract.Presenter mPresenter;
    private Context mContext;
    private DefaultChildAdapter mDefaultChildAdapter;
    private String mtableName;
    private RecyclerView mRecyclerView;

    private BookmarkChildFragment(String tableName) {
        mtableName = tableName;
    }

    public static BookmarkChildFragment newInstance(String tableName) {
        return new BookmarkChildFragment(tableName);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_main, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mContext = view.getContext();
        if (mPresenter == null) {
            mPresenter = new BookmarkChildPresenter(this, mtableName);
        }
        mPresenter.start();

        mRecyclerView = view.findViewById(R.id.recyclerview_discover_main);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        if (mRecyclerView.getItemDecorationCount() == 0) mRecyclerView.addItemDecoration(dec);
        GetSearchList getSearchList = new GetSearchList();
        getSearchList.setTableName(mtableName);
        mDefaultChildAdapter = new DefaultChildAdapter(getSearchList, mPresenter);
        mRecyclerView.setAdapter(mDefaultChildAdapter);
    }

    private RecyclerView.ItemDecoration dec = new RecyclerView.ItemDecoration() {
        @Override
        public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
            super.getItemOffsets(outRect, view, parent, state);
            if (outRect.top == 0) outRect.top = (int) Utils.convertDpToPixel((float) 16, mContext);

            if (parent.getChildAdapterPosition(view) == 0)
                outRect.top = 0;
        }
    };

    @Override
    public void setPresenter(BookmarkChildFragmentContract.Presenter presenter) {
        mPresenter = checkNotNull(presenter);
    }

    @Override
    public void updateData(GetSearchList bean) {
        mDefaultChildAdapter.updateData(bean);
    }

    @Override
    public void scrollViewTo(int position) {
        mRecyclerView.smoothScrollToPosition(position);
    }
}
