package com.paula.android.bechef.bookmarkChild;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Rect;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.paula.android.bechef.R;
import com.paula.android.bechef.activities.BeChefActivity;
import com.paula.android.bechef.adapters.DefaultChildAdapter;
import com.paula.android.bechef.bookmark.BookmarkFragment;
import com.paula.android.bechef.data.entity.BaseItem;
import com.paula.android.bechef.data.entity.BaseTab;
import com.paula.android.bechef.data.entity.BookmarkItem;
import com.paula.android.bechef.data.entity.BookmarkTab;
import com.paula.android.bechef.dialog.BeChefAlertDialogBuilder;
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
    private BookmarkFragment mBookmarkFragment;
    private Context mContext;
    private DefaultChildAdapter mDefaultChildAdapter;
    private Boolean mIsSelectable = false;
    private TextView mTvInfoDescription;
    private ImageButton mIbtnFilter;
    private int mCurrentFileterIndex = 0;

    private BookmarkChildFragment(BookmarkTab bookmarkTab, Fragment fragment) {
        if (mPresenter == null) {
            mPresenter = new BookmarkChildPresenter(this, bookmarkTab);
        }
        mBookmarkFragment = (BookmarkFragment) fragment;
    }

    public static BookmarkChildFragment newInstance(BookmarkTab bookmarkTab, Fragment fragment) {
        return new BookmarkChildFragment(bookmarkTab, fragment);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_child, container, false);
        mContext = view.getContext();

        RecyclerView recyclerView = view.findViewById(R.id.recyclerview_discover_main);
        recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        if (recyclerView.getItemDecorationCount() == 0) recyclerView.addItemDecoration(dec);
        mDefaultChildAdapter = new DefaultChildAdapter(mIsSelectable, new ArrayList<BaseItem>(), mPresenter);
        recyclerView.setAdapter(mDefaultChildAdapter);

        mTvInfoDescription = view.findViewById(R.id.textview_info_description);
        mIbtnFilter = view.findViewById(R.id.imagebutton_filter);
        mIbtnFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String[] itemString = new String[]{"時間由新至舊", "時間由舊至新", "評分由高至低", "評分由低至高"};
                itemString[mCurrentFileterIndex] = " * " + itemString[mCurrentFileterIndex];
                new BeChefAlertDialogBuilder(mContext).setTitle("排序依...")
                        .setItems(itemString,
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        if (which != mCurrentFileterIndex) {
                                            mCurrentFileterIndex = which;
                                            mPresenter.loadSpecificItems(which);
                                        }
                                    }
                                }).create().show();
            }
        });
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

            if (parent.getChildAdapterPosition(view) == 0) outRect.top = 0;
        }
    };

    @Override
    public void setPresenter(BookmarkChildFragmentContract.Presenter presenter) {
        mPresenter = checkNotNull(presenter);
    }

    @Override
    public void updateItems(ArrayList<?> newData) {
        mDefaultChildAdapter.updateData(newData);
        mTvInfoDescription.setText("共 " + newData.size() + " 道");
    }

    @Override
    public void refreshData() {
        mPresenter.loadItems();
    }

    @Override
    public void showDetailUi(Object content, boolean isBottomShown) {
        ((BeChefActivity) mContext).transToDetail(content, !mIsSelectable);
    }

    @Override
    public void showSelectableUi(boolean isSelectable) {
        if (mIsSelectable == isSelectable) return;

        mIsSelectable = isSelectable;
        mDefaultChildAdapter.setSelectable(mIsSelectable);
        mIbtnFilter.setVisibility(mIsSelectable ? View.GONE : View.VISIBLE);
        mBookmarkFragment.setBookmarkChildFragment(this);
        mBookmarkFragment.showSelectable(mIsSelectable);
    }

    public ArrayList<BookmarkItem> getChosenItems() {
        return mDefaultChildAdapter.getChosenBookmarkItems();
    }
}
