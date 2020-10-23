package com.paula.android.bechef.detail;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.paula.android.bechef.R;
import com.paula.android.bechef.activities.BeChefActivity;
import com.paula.android.bechef.adapters.DetailAdapter;
import com.paula.android.bechef.data.LoadDataCallback;
import com.paula.android.bechef.data.LoadDataTask;
import com.paula.android.bechef.data.dao.BookmarkTabDao;
import com.paula.android.bechef.data.database.TabDatabase;
import com.paula.android.bechef.data.entity.BaseItem;
import com.paula.android.bechef.data.entity.BookmarkItem;
import com.paula.android.bechef.data.entity.BookmarkTab;
import com.paula.android.bechef.data.entity.DiscoverItem;
import com.paula.android.bechef.data.entity.ReceiptItem;
import com.paula.android.bechef.dialog.AddToBookmarkDialog;
import com.paula.android.bechef.dialog.EditReceiptItemDialog;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import static com.google.android.gms.common.internal.Preconditions.checkNotNull;

public class DetailFragment extends Fragment implements DetailContract.View {
    private DetailContract.Presenter mPresenter;
    private Context mContext;
    private RecyclerView mRvDetail;
    private ImageButton mIbtnMore;
    private boolean mIsBottomShown;
    private DetailAdapter mDetailAdapter;

    private DetailFragment() {
    }

    public static DetailFragment newInstance() {
        return new DetailFragment();
    }

//    @Override
//    public void onCreate(@Nullable Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        if (getActivity() != null) ((BeChefActivity) getActivity()).showBottomNavigationView(false);
//    }

    public void setBottomShown(boolean bottomShown) {
        mIsBottomShown = bottomShown;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_detail, container, false);
//        if (getArguments() != null) mIsBottomShown = (boolean) getArguments().get("isBottomShown");

        mContext = root.getContext();
        root.findViewById(R.id.imagebutton_toolbar_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((BeChefActivity) mContext).onBackPressed();
            }
        });
        mIbtnMore = root.findViewById(R.id.imagebutton_toolbar_more);
        mRvDetail = root.findViewById(R.id.recyclerview_detail);
        mRvDetail.setLayoutManager(new LinearLayoutManager(mContext));
        return root;
    }

    @Override
    public void onStart() {
        super.onStart();
        mPresenter.start();
        ((BeChefActivity) mContext).showBottomNavigationView(false);
    }

    @Override
    public void setPresenter(DetailContract.Presenter presenter) {
        mPresenter = checkNotNull(presenter);
    }

    @Override
    public void showDetailUi(final Object content) {
        if (content instanceof DiscoverItem) {
            mDetailAdapter = new DetailAdapter((DiscoverItem) content);
            mIbtnMore.setBackgroundResource(R.drawable.ic_bookmark_white);
        }
        else if (content instanceof BookmarkItem) {
            mDetailAdapter = new DetailAdapter((BookmarkItem) content);
            mIbtnMore.setBackgroundResource(R.drawable.ic_more_white);
        }
        else {
            mDetailAdapter = new DetailAdapter((ReceiptItem) content);
            mIbtnMore.setBackgroundResource(R.drawable.ic_more_white);
        }
        mRvDetail.setAdapter(mDetailAdapter);

        mIbtnMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (content instanceof DiscoverItem)
                    addToBookmark();
                else if (content instanceof ReceiptItem) {
                    // TODO: pop item action dialog
                    EditReceiptItemDialog editReceiptItemDialog
                            = new EditReceiptItemDialog(mDetailAdapter.getBaseItem(), mPresenter);
                    editReceiptItemDialog.show(getChildFragmentManager(), "edit");
                }
            }
        });
    }

    private void addToBookmark() {
        new LoadDataTask<>(new LoadDataCallback<BookmarkTabDao>() {
            private ArrayList<BookmarkTab> mBookmarkTabs = new ArrayList<>();
            @Override
            public BookmarkTabDao getDao() {
                return TabDatabase.getBookmarkInstance(mContext).bookmarkDao();
            }

            @Override
            public void doInBackground(BookmarkTabDao dao) {
                mBookmarkTabs.addAll(dao.getAll());
            }

            @Override
            public void onCompleted() {
                AddToBookmarkDialog addToBookmarkDialog = new AddToBookmarkDialog(mBookmarkTabs, (DetailPresenter) mPresenter);
                addToBookmarkDialog.show(getChildFragmentManager(), "edit");
            }
        }).execute();
    }

    @Override
    public void onStop() {
        super.onStop();
        ((BeChefActivity) mContext).showBottomNavigationView(mIsBottomShown);
    }

    @Override
    public void updateUi(BaseItem baseItem) {
        mDetailAdapter.updateData(baseItem);
    }

    //    @Override
//    public void onDestroy() {
//        super.onDestroy();
//        Log.d("BechefPresenter", "detail ondestroy");
//        ((BeChefActivity) mContext).showBottomNavigationView(mIsBottomShown);
//    }
}