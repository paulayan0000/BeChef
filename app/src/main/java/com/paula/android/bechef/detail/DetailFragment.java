package com.paula.android.bechef.detail;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerFragmentX;
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
import com.paula.android.bechef.dialog.EditItemDialog;
import com.paula.android.bechef.utils.Constants;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
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


    private YouTubePlayerFragmentX mYouTubePlayerFragment;
    private YouTubePlayer mYouTubePlayer;
    private FragmentManager mFragmentManager;

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

        mFragmentManager = getChildFragmentManager();
        mYouTubePlayerFragment = (YouTubePlayerFragmentX) mFragmentManager.findFragmentById(R.id.youtube_player_fragment);
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

            if (mYouTubePlayerFragment != null) {
                mYouTubePlayerFragment.initialize(Constants.DEVELOPER_KEY, new YouTubePlayer.OnInitializedListener() {
                    @Override
                    public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer player,
                                                        boolean wasRestored) {
                        if (!wasRestored) {
                            mYouTubePlayer = player;
                            //set the player style default
                            mYouTubePlayer.setPlayerStyle(YouTubePlayer.PlayerStyle.DEFAULT);
                            //cue the 1st video by default
                            mYouTubePlayer.cueVideo(((DiscoverItem) content).getVideoId());
                        }
                    }

                    @Override
                    public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {
                        Log.e("DetailFragment", "Youtube Player View initialization failed");
                    }
                });
            }
        } else if (content instanceof BookmarkItem) {
            mDetailAdapter = new DetailAdapter((BookmarkItem) content);
            mIbtnMore.setBackgroundResource(R.drawable.ic_more_white);
            mFragmentManager.beginTransaction().hide(mYouTubePlayerFragment).commit();
        } else {
            mDetailAdapter = new DetailAdapter((ReceiptItem) content);
            mIbtnMore.setBackgroundResource(R.drawable.ic_more_white);
            mFragmentManager.beginTransaction().hide(mYouTubePlayerFragment).commit();
        }
        mRvDetail.setAdapter(mDetailAdapter);

        mIbtnMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (content instanceof DiscoverItem) {
                    addToBookmark();
                    return;
                }
                EditItemDialog editItemDialog
                        = new EditItemDialog(mDetailAdapter.getBaseItem(), mPresenter);
                if (content instanceof ReceiptItem)
                    editItemDialog.show(getChildFragmentManager(), "edit_receipt");
                else
                    editItemDialog.show(getChildFragmentManager(), "edit_bookmark");

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
                AddToBookmarkDialog addToBookmarkDialog = new AddToBookmarkDialog((DetailPresenter) mPresenter);
                addToBookmarkDialog.setTabs(mBookmarkTabs);
                addToBookmarkDialog.show(getChildFragmentManager(), "add");
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