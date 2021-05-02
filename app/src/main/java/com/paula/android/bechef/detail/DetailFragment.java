package com.paula.android.bechef.detail;

import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerFragmentX;
import com.paula.android.bechef.ApiKey;
import com.paula.android.bechef.activities.BeChefActivity;
import com.paula.android.bechef.adapters.DetailAdapter;
import com.paula.android.bechef.data.entity.BaseItem;
import com.paula.android.bechef.data.entity.DiscoverItem;
import com.paula.android.bechef.data.entity.RecipeItem;
import com.paula.android.bechef.dialog.AlertDialogClickCallback;
import com.paula.android.bechef.dialog.BeChefAlertDialogBuilder;
import com.paula.android.bechef.dialog.EditItemDialog;
import com.paula.android.bechef.R;

import static android.content.res.Configuration.ORIENTATION_PORTRAIT;
import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;
import static com.google.android.gms.common.internal.Preconditions.checkNotNull;
import static com.google.android.youtube.player.YouTubePlayer.FULLSCREEN_FLAG_CUSTOM_LAYOUT;

public class DetailFragment extends Fragment implements DetailContract.View {
    private static final int RECOVERY_DIALOG_REQUEST = 1;
    private Context mContext;
    private DetailContract.Presenter mPresenter;
    private DetailAdapter mDetailAdapter;
    private ImageButton mIbtnMore;
    private ConstraintLayout mClToolbar;
    private RecyclerView mRvDetail;
    private FragmentManager mChildFragmentManager;
    private YouTubePlayer mYouTubePlayer;
    private YouTubePlayerFragmentX mYouTubePlayerFragment;
    private boolean mIsBottomShown;
    private boolean mIsFullscreen;

    private DetailFragment() {
    }

    public static DetailFragment newInstance() {
        return new DetailFragment();
    }

    public void setBottomShown(boolean bottomShown) {
        mIsBottomShown = bottomShown;
    }

    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        setFullscreenUi();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_detail, container, false);
        mContext = root.getContext();
        mChildFragmentManager = getChildFragmentManager();
        root.findViewById(R.id.imagebutton_toolbar_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((BeChefActivity) mContext).onBackPressed();
            }
        });
        mIbtnMore = root.findViewById(R.id.imagebutton_toolbar_more);
        mClToolbar = root.findViewById(R.id.constraintlayout_toolbar);
        setRecyclerView(root);
        return root;
    }

    private void setRecyclerView(View root) {
        mRvDetail = root.findViewById(R.id.recyclerview_detail);
        mRvDetail.setLayoutManager(new LinearLayoutManager(mContext));
        mDetailAdapter = new DetailAdapter(null);
        mRvDetail.setAdapter(mDetailAdapter);
    }

    @Override
    public void onStart() {
        super.onStart();
        mPresenter.start();
        ((BeChefActivity) mContext).showBottomNavigationView(false);
    }

    @Override
    public void setCustomMainPresenter(DetailContract.Presenter customMainPresenter) {
        mPresenter = checkNotNull(customMainPresenter);
    }

    @Override
        public void showDetailUi(final BaseItem content) {
        // Set ibtnMore onClick method
        mIbtnMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!(content instanceof DiscoverItem)) {
                    // onClick for bookmarkItem or recipeItem
                    BaseItem baseItem = mDetailAdapter.getBaseItem();
                    new EditItemDialog(baseItem).setDetailPresenter(mPresenter)
                            .show(mChildFragmentManager, baseItem.getClass().getSimpleName());
                } else if (content.getVideoId().isEmpty()) {
                    // onClick for channel
                    new BeChefAlertDialogBuilder(mContext).setButtons(new AlertDialogClickCallback() {
                        @Override
                        public boolean onPositiveButtonClick() {
                            mPresenter.addToDiscover((DiscoverItem) content);
                            return true;
                        }
                    }).setMessage(String.format(getString(R.string.msg_follow_channel), content.getTitle()))
                            .setTitle(getString(R.string.title_follow_channel)).create().show();
                } else {
                    // onClick for discoverItem
                    mPresenter.addToBookmark();
                }
            }
        });

        // Set detail UI
        mDetailAdapter.setLoading(false);
        mDetailAdapter.updateData(content);
        mYouTubePlayerFragment = (YouTubePlayerFragmentX) mChildFragmentManager
                .findFragmentById(R.id.youtube_player_fragment);

        // detail of discover and bookmark
        if (!content.getVideoId().isEmpty()) {
            if (content instanceof DiscoverItem) {
                mIbtnMore.setBackgroundResource(R.drawable.ic_bookmark);
            } else {
                mIbtnMore.setBackgroundResource(R.drawable.ic_more);
            }
            if (mYouTubePlayerFragment != null) initYouTubePlayFragment(content);
            return;
        }

        // detail of recipe and channel
        mChildFragmentManager.beginTransaction().hide(mYouTubePlayerFragment).commit();
        if (content instanceof RecipeItem) {
            // detail of recipe
            mIbtnMore.setBackgroundResource(R.drawable.ic_more);
        } else if (!((DiscoverItem) content).isChannelInBeChef()) {
            // detail of channel not in BeChef
            mIbtnMore.setBackgroundResource(R.drawable.ic_bookmark);
        } else {
            // detail of channel in BeChef
            mIbtnMore.setVisibility(View.GONE);
        }
    }

    private void initYouTubePlayFragment(final BaseItem content) {
        mYouTubePlayerFragment.initialize(ApiKey.DEVELOPER_KEY,
                new YouTubePlayer.OnInitializedListener() {
                    @Override
                    public void onInitializationSuccess(YouTubePlayer.Provider provider,
                                                        final YouTubePlayer player,
                                                        boolean wasRestored) {
                        player.setFullscreenControlFlags(FULLSCREEN_FLAG_CUSTOM_LAYOUT);
                        player.setOnFullscreenListener(new YouTubePlayer.OnFullscreenListener() {
                            @Override
                            public void onFullscreen(boolean isFullscreen) {
                                mIsFullscreen = isFullscreen;
                                setFullscreenUi();
                            }
                        });
                        if (!wasRestored) {
                            mYouTubePlayer = player;
                            mYouTubePlayer.setPlayerStyle(YouTubePlayer.PlayerStyle.DEFAULT);
                            mYouTubePlayer.cueVideo(content.getVideoId());
                            mChildFragmentManager.beginTransaction().show(mYouTubePlayerFragment)
                                    .commitAllowingStateLoss();
                        }
                        if (mYouTubePlayer != null) mYouTubePlayer.setFullscreen(false);
                        if (!isCurrentOrientationPortrait()) setFullscreenUi();
                    }

                    @Override
                    public void onInitializationFailure(YouTubePlayer.Provider provider,
                                                        YouTubeInitializationResult initResult) {
                        if (initResult.isUserRecoverableError()) {
                            initResult.getErrorDialog((Activity) mContext, RECOVERY_DIALOG_REQUEST).show();
                        } else {
                            String errorMessage = getString(R.string.toast_require_youtube_installed);
                            Toast.makeText(mContext, errorMessage, Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }

    private boolean isCurrentOrientationPortrait() {
        return mContext.getResources().getConfiguration().orientation == ORIENTATION_PORTRAIT;
    }

    private void setFullscreenUi() {
        if (mYouTubePlayer == null ||
                mYouTubePlayerFragment == null ||
                mYouTubePlayerFragment.getView() == null) return;
        /* If it's in portrait and not fullscreen, show normal layout with fullcreen button.
         Otherwise, show layout in fullscreen without fullscreen button.*/
        LinearLayout.LayoutParams layoutParams =
                (LinearLayout.LayoutParams) mYouTubePlayerFragment.getView().getLayoutParams();
        mYouTubePlayer.setShowFullscreenButton(isCurrentOrientationPortrait());
        if (!mIsFullscreen && isCurrentOrientationPortrait()) {
            setOtherViewVisibility(View.VISIBLE);
            layoutParams.height = mContext.getResources().getDimensionPixelOffset(R.dimen.detail_image_height);
            mYouTubePlayer.setShowFullscreenButton(true);
        } else {
            setOtherViewVisibility(View.GONE);
            layoutParams.height = MATCH_PARENT;
        }
    }

    private void setOtherViewVisibility(int visibility) {
        mRvDetail.setVisibility(visibility);
        mClToolbar.setVisibility(visibility);
    }

    @Override
    public void showErrorUi(String errorMsg) {
        mDetailAdapter.updateError(errorMsg);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mYouTubePlayer != null) {
            mYouTubePlayer.release();
            mYouTubePlayer = null;
        }
        mPresenter.setAllThreadCanceled();
        ((BeChefActivity) mContext).showBottomNavigationView(mIsBottomShown);
    }

    @Override
    public void updateUi(BaseItem baseItem) {
        mDetailAdapter.updateData(baseItem);
    }

    @Override
    public void updateButton(boolean isDiscoverTab) {
        if (isDiscoverTab) {
            mIbtnMore.setVisibility(View.GONE);
        } else {
            mIbtnMore.setBackgroundResource(R.drawable.ic_more);
            mIbtnMore.setVisibility(View.VISIBLE);
            mIbtnMore.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    BaseItem baseItem = mDetailAdapter.getBaseItem();
                    new EditItemDialog(baseItem).setDetailPresenter(mPresenter)
                            .show(mChildFragmentManager, baseItem.getClass().getSimpleName());
                }
            });
        }
    }

    @Override
    public void showLoading(boolean isLoading) {
        mDetailAdapter.setLoading(isLoading);
    }
}