package com.paula.android.bechef.detail;

import android.app.Activity;
import android.content.Context;

import com.paula.android.bechef.api.beans.YouTubeData;
import com.paula.android.bechef.api.BeChefApiHelper;
import com.paula.android.bechef.api.exceptions.NoResourceException;
import com.paula.android.bechef.data.database.ItemDatabase;
import com.paula.android.bechef.data.database.TabDatabase;
import com.paula.android.bechef.data.entity.BaseItem;
import com.paula.android.bechef.data.entity.BaseTab;
import com.paula.android.bechef.data.entity.BookmarkItem;
import com.paula.android.bechef.data.entity.DiscoverItem;
import com.paula.android.bechef.data.entity.DiscoverTab;
import com.paula.android.bechef.data.entity.RecipeItem;
import com.paula.android.bechef.R;
import com.paula.android.bechef.dialog.AddToBookmarkDialogBuilder;
import com.paula.android.bechef.utils.Constants;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static com.google.android.gms.common.internal.Preconditions.checkNotNull;

public class DetailPresenter implements DetailContract.Presenter {
    private final DetailContract.View mDetailView;
    private Object mDataContent;
    private boolean mIsAllThreadCancel = false;

    public DetailPresenter(DetailContract.View detailView, Object dataContent) {
        mDetailView = checkNotNull(detailView, "detailView cannot be null!");
        mDetailView.setCustomMainPresenter(this);
        mDataContent = dataContent;
    }

    @Override
    public Context getContext() {
        return mDetailView.getContext();
    }

    @Override
    public void setAllThreadCanceled() {
        mIsAllThreadCancel = true;
    }

    @Override
    public void start() {
        if (mDataContent instanceof RecipeItem) {
            mDetailView.showDetailUi((RecipeItem) mDataContent);
        } else if (mDataContent instanceof BookmarkItem) {
            mDetailView.showDetailUi((BookmarkItem) mDataContent);
        } else {
            mDetailView.showLoading(true);
            Runnable runnable = getRunnableOnNewThread();
            new Thread(runnable).start();
        }
    }

    private Runnable getRunnableOnNewThread() {
        Runnable runnable;
        if (mDataContent instanceof DiscoverItem) {
            runnable = new Runnable() {
                @Override
                public void run() {
                    // Runnable for detail from search
                    DiscoverTab discoverTab = TabDatabase.getTabInstance(getContext()).discoverDao()
                            .getTabWithChannelId(((DiscoverItem) mDataContent).getChannelId());
                    DiscoverItem discoverItem = (DiscoverItem) mDataContent;
                    discoverItem.setChannelInBeChef(discoverTab != null);

                    if (mIsAllThreadCancel) return;
                    mDataContent = discoverItem;
                    Map<String, String> queryParameters = new HashMap<>();
                    queryParameters.put("pageToken", "");
                    Runnable runnable;
                    if (discoverItem.getVideoId().isEmpty()) {
                        // load channel
                        queryParameters.put("id", discoverItem.getChannelId());
                        runnable = loadVideo(queryParameters, Constants.API_CHANNELS);
                    } else {
                        // load video
                        queryParameters.put("id", discoverItem.getVideoId());
                        runnable = loadVideo(queryParameters, Constants.API_VIDEOS);
                    }
                    if (!mIsAllThreadCancel && getContext() != null) {
                        ((Activity) getContext()).runOnUiThread(runnable);
                    }
                }
            };
        } else {
            runnable = new Runnable() {
                @Override
                public void run() {
                    // Runnable for detail from discover
                    BookmarkItem bookmarkItem = ItemDatabase.getItemInstance(getContext())
                            .bookmarkDao().getItemWithVideoId((String) mDataContent);
                    if (mIsAllThreadCancel) return;
                    Runnable runnable;
                    if (bookmarkItem != null) {
                        mDataContent = bookmarkItem;
                        runnable = new Runnable() {
                            @Override
                            public void run() {
                                mDetailView.showDetailUi((BookmarkItem) mDataContent);
                            }
                        };
                    } else {
                        Map<String, String> queryParameters = new HashMap<>();
                        queryParameters.put("pageToken", "");
                        queryParameters.put("id", (String) mDataContent);
                        runnable = loadVideo(queryParameters, Constants.API_VIDEOS);
                    }
                    if (!mIsAllThreadCancel && getContext() != null)
                        ((Activity) getContext()).runOnUiThread(runnable);
                }
            };
        }
        return runnable;
    }

    private Runnable loadVideo(Map<String, String> queryParameters, final String apiType) {
        queryParameters.put("part", Constants.API_PART_ALL);
        queryParameters.put("maxResults", Constants.API_MAX_RESULTS);

        Runnable runnableOnUiThread;
        if (mIsAllThreadCancel) return null;
        try {
            YouTubeData bean = BeChefApiHelper.GetYoutubeData(queryParameters, apiType);
            final DiscoverItem discoverItem = bean.getDiscoverItems().get(0);
            if (mDataContent instanceof DiscoverItem) {
                discoverItem.setChannelInBeChef(((DiscoverItem) mDataContent).isChannelInBeChef());
            }
            mDataContent = discoverItem;
            runnableOnUiThread = new Runnable() {
                @Override
                public void run() {
                    mDetailView.showDetailUi(discoverItem);
                }
            };
        } catch (final Exception error) {
            runnableOnUiThread = new Runnable() {
                @Override
                public void run() {
                    showErrorMsg(error);
                }
            };
        }
        if (mIsAllThreadCancel) return null;
        return runnableOnUiThread;
    }

    private void showErrorMsg(Exception error) {
        if (error instanceof NoResourceException) {
            mDetailView.showErrorUi(getContext().getResources().getString(R.string.adapter_nothing));
        } else {
            mDetailView.showErrorUi(getContext().getResources().getString(R.string.adapter_error));
        }
    }

    public void updateData(BaseItem baseItem) {
        mDataContent = baseItem;
        mDetailView.updateUi(baseItem);
        mDetailView.updateButton(baseItem.getVideoId().isEmpty());
    }

    public Object getDataContent() {
        return mDataContent;
    }

    @Override
    public void refreshData(BaseItem baseItem) {
        mDetailView.updateUi(baseItem);
    }

    @Override
    public void addToDiscover(final DiscoverItem discoverItem) {
        discoverItem.setChannelInBeChef(true);
        new Thread(new Runnable() {
            @Override
            public void run() {
                TabDatabase.getTabInstance(getContext()).discoverDao()
                        .insert(new DiscoverTab(discoverItem.getChannelId(), discoverItem.getTitle()));
                if (getContext() != null) {
                    ((Activity) getContext()).runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            updateData(discoverItem);
                        }
                    });
                }
            }
        }).start();
    }

    @Override
    public void addToBookmark() {
        final DetailPresenter detailPresenter = this;
        new Thread(new Runnable() {
            @Override
            public void run() {
                final ArrayList<BaseTab> baseTabs = new ArrayList<BaseTab>(TabDatabase
                        .getTabInstance(getContext()).bookmarkDao().getAll());
                if (getContext() != null) {
                    ((Activity) getContext()).runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            new AddToBookmarkDialogBuilder(detailPresenter).setTabs(baseTabs)
                                    .create().show();
                        }
                    });
                }
            }
        }).start();
    }
}
