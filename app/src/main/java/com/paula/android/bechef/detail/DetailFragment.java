package com.paula.android.bechef.detail;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.Toast;

import com.paula.android.bechef.R;
import com.paula.android.bechef.activities.BeChefActivity;
import com.paula.android.bechef.adapters.DetailAdapter;
import com.paula.android.bechef.data.LoadDataCallback;
import com.paula.android.bechef.data.LoadDataTask;
import com.paula.android.bechef.data.dao.BookmarkTabDao;
import com.paula.android.bechef.data.dao.DiscoverTabDao;
import com.paula.android.bechef.data.database.TabDatabase;
import com.paula.android.bechef.data.entity.BookmarkItem;
import com.paula.android.bechef.data.entity.BookmarkTab;
import com.paula.android.bechef.data.entity.DiscoverItem;
import com.paula.android.bechef.data.entity.ReceiptItem;
import com.paula.android.bechef.dialog.AddToBookmarkDialog;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import static com.google.android.gms.common.internal.Preconditions.checkNotNull;

public class DetailFragment extends Fragment implements DetailContract.View {
    private DetailContract.Presenter mPresenter;
    private Context mContext;
    private RecyclerView mRvDetail;
    private ImageButton mIbtnMore;
    private boolean mIsBottomShown;

    private DetailFragment(boolean isBottomShown) {
        mIsBottomShown = isBottomShown;
    }

    public static DetailFragment newInstance(boolean isBottomShown) {
        return new DetailFragment(isBottomShown);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getActivity() != null) ((BeChefActivity) getActivity()).showBottomNavigationView(false);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_detail, container, false);
        mContext = root.getContext();
        root.findViewById(R.id.imagebutton_toolbar_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getActivity() != null) getActivity().onBackPressed();
            }
        });
        mIbtnMore = root.findViewById(R.id.imagebutton_toolbar_more);
        mRvDetail = root.findViewById(R.id.recyclerview_detail);
        mRvDetail.setLayoutManager(new LinearLayoutManager(mContext));
        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mPresenter.start();
    }

    @Override
    public void setPresenter(DetailContract.Presenter presenter) {
        mPresenter = checkNotNull(presenter);
    }

    @Override
    public void showDetailUi(final Object content) {
        DetailAdapter detailAdapter;
        if (content instanceof DiscoverItem) {
            detailAdapter = new DetailAdapter((DiscoverItem) content, mPresenter);
            mIbtnMore.setBackgroundResource(R.drawable.ic_bookmark_white);
        }
        else if (content instanceof BookmarkItem) {
            detailAdapter = new DetailAdapter((BookmarkItem) content, mPresenter);
            mIbtnMore.setBackgroundResource(R.drawable.ic_more_white);
        }
        else {
            detailAdapter = new DetailAdapter((ReceiptItem) content, mPresenter);
            mIbtnMore.setBackgroundResource(R.drawable.ic_more_white);
        }
        mRvDetail.setAdapter(detailAdapter);

        mIbtnMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (content instanceof DiscoverItem)
                    addToBookmark((DiscoverItem) content);
                else
                    Toast.makeText(mContext, "show more", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void addToBookmark(final DiscoverItem discoverItem) {
//        TabDatabase.getBookmarkInstance(mContext).bookmarkDao().getAll()
//                .observe(this, new Observer<List<BookmarkTab>>() {
//                    @Override
//                    public void onChanged(List<BookmarkTab> bookmarkTabs) {
//                        AddToBookmarkDialog addToBookmarkDialog = new AddToBookmarkDialog(new ArrayList<>(bookmarkTabs), discoverItem);
//                        addToBookmarkDialog.show(getChildFragmentManager(), "edit");
//                    }
//                });
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
                AddToBookmarkDialog addToBookmarkDialog = new AddToBookmarkDialog(mBookmarkTabs, discoverItem);
                addToBookmarkDialog.show(getChildFragmentManager(), "edit");
            }
        }).execute();
    }

    @Override
    public void onDestroy() {
        if (getActivity() != null)
            ((BeChefActivity) getActivity()).showBottomNavigationView(mIsBottomShown);
        super.onDestroy();
    }
}