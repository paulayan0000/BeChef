package com.paula.android.bechef.bookmarkChild;

import android.content.Context;
import android.graphics.Rect;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.paula.android.bechef.R;
import com.paula.android.bechef.activities.BeChefActivity;
import com.paula.android.bechef.adapters.DefaultChildAdapter;
import com.paula.android.bechef.data.entity.BookmarkItem;
import com.paula.android.bechef.utils.Utils;
import java.util.ArrayList;
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
    private RecyclerView mRecyclerView;

    private BookmarkChildFragment(int tabIndex) {
        if (mPresenter == null) {
            mPresenter = new BookmarkChildPresenter(this, tabIndex);
        }
    }

    public static BookmarkChildFragment newInstance(int tabIndex) {
        return new BookmarkChildFragment(tabIndex);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);
        mContext = view.getContext();
        mRecyclerView = view.findViewById(R.id.recyclerview_discover_main);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        if (mRecyclerView.getItemDecorationCount() == 0) mRecyclerView.addItemDecoration(dec);
        mDefaultChildAdapter = new DefaultChildAdapter(new ArrayList<BookmarkItem>(), mPresenter);
        mRecyclerView.setAdapter(mDefaultChildAdapter);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mPresenter.start();
    }

    private RecyclerView.ItemDecoration dec = new RecyclerView.ItemDecoration() {
        @Override
        public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
            super.getItemOffsets(outRect, view, parent, state);
            if (outRect.top == 0) outRect.top = (int) Utils.convertDpToPixel((float) 8, mContext);

            if (parent.getChildAdapterPosition(view) == 0)
                outRect.top = 0;
        }
    };

    @Override
    public void setPresenter(BookmarkChildFragmentContract.Presenter presenter) {
        mPresenter = checkNotNull(presenter);
    }

    @Override
    public void updateData(ArrayList<?> newData) {
        mDefaultChildAdapter.updateData(newData);
    }

    @Override
    public void scrollViewTo(int position) {
        mRecyclerView.smoothScrollToPosition(position);
    }

    @Override
    public void showDetailUi(Object content) {
        ((BeChefActivity) getActivity()).transToDetail(content);
    }
}
