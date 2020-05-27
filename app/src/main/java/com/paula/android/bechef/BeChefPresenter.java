package com.paula.android.bechef;

import android.net.Uri;
import android.util.Log;

import com.paula.android.bechef.api.GetChannelIdTask;
import com.paula.android.bechef.api.beans.GetSearchList;
import com.paula.android.bechef.api.callbacks.GetChannelIdCallback;
import com.paula.android.bechef.objects.SearchItem;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import static com.google.android.gms.common.internal.Preconditions.checkNotNull;

public class BeChefPresenter implements BeChefContract.Presenter {

    private static final String LOG_TAG = BeChefPresenter.class.getSimpleName();
    private final BeChefContract.View mMainView;

    private ArrayList<String> mTabtitles = new ArrayList<>();
    private GetSearchList mDiscoverItems = new GetSearchList();
    private boolean mLoading = false;
    private String mNextPagingId = "";
    private int mlastVisibleItemPosition;

    public BeChefPresenter(BeChefContract.View mainView, FragmentManager fragmentManager) {
        mMainView = checkNotNull(mainView, "mainView cannot be null!");
        mMainView.setPresenter(this);
    }

    @Override
    public void transToDiscover() {
        mTabtitles.clear();
        mTabtitles.add("Wecook123 料理123");
        mTabtitles.add("MASAの料理ABC"); //UCr90FXGOO8nAE9B6FAUeTNA
        mTabtitles.add("iCook 愛料理"); //UCReIdTavsve16EJlilnTPNg
        mTabtitles.add("楊桃美食網"); //UCctVKh07hVAyQtqpl75pxYA
        mTabtitles.add("乾杯與小菜的日常"); //UCOJDuGX9SqzPkureXZfS60w

        mMainView.showDiscoverUi(mTabtitles, mDiscoverItems);

        // TODO: Use String constants and objects, modify onCompleted function
        Map<String, String> queryParameters = new HashMap<>();

//        queryParameters.put("q", "wecook123");
//        queryParameters.put("type", "channel");

        queryParameters.put("pageToken", "");
        queryParameters.put("channelId", "UCQGVzUNv0UTn-t0Xzd06E4Q");

        loadDiscoverItems(queryParameters);
    }

    private void loadDiscoverItems(Map<String, String> queryParameters) {
        if (!mLoading) {
            mLoading = true;
            Log.d(LOG_TAG, "Loading...");
            queryParameters.put("part", "snippet");
            queryParameters.put("maxResults", "10");

            new GetChannelIdTask(queryParameters, new GetChannelIdCallback() {
                @Override
                public void onCompleted(GetSearchList bean) {
                    Log.d(LOG_TAG, "size: " + bean.getSearchItems().size());
                    mMainView.updateSearchItems(bean);
                    mNextPagingId = bean.getNextPageToken();
                    mLoading = false;
                    Log.d(LOG_TAG, "loading done: " + mNextPagingId);
                }

                @Override
                public void onError(String errorMessage) {
                    Log.d(LOG_TAG, "Error: " + errorMessage);
                    mLoading = false;
                }
            }).execute();
        }
    }

    @Override
    public void transToBookmark() {
        mTabtitles.clear();
        mTabtitles.add("bookmark one");

        mMainView.showBookmarkUi(mTabtitles, new ArrayList<String>());
    }

    @Override
    public void transToReceipt() {
        mTabtitles.clear();
        mTabtitles.add("receipt one");
        mMainView.showReceiptUi(mTabtitles, new ArrayList<String>());
    }

    @Override
    public void transToToday() {
        transToMenuList();
        mMainView.showTodayUi();
    }

    @Override
    public void transToDetail(Uri uri) {
        Log.d(LOG_TAG, "Uri is: " + uri);
    }

    @Override
    public void transToMenuList() {
        mTabtitles.clear();
        mTabtitles.add("menu one");
        mMainView.showMenuListUi(mTabtitles, new ArrayList<String>());
    }

    @Override
    public void transToBuyList() {
        mTabtitles.clear();
        mTabtitles.add("buylist one");
        mMainView.showBuyListUi(mTabtitles, new ArrayList<String>());
    }

    @Override
    public void onScrollStateChanged(int visibleItemCount, int totalItemCount, int newState) {
        if (newState == RecyclerView.SCROLL_STATE_IDLE && visibleItemCount > 0) {

            if (!mLoading && mlastVisibleItemPosition == totalItemCount - 1 && !"".equals(mNextPagingId)) {
                Map<String, String> queryParameters = new HashMap<>();
                queryParameters.put("pageToken", mNextPagingId);
                queryParameters.put("channelId", "UCQGVzUNv0UTn-t0Xzd06E4Q");

                loadDiscoverItems(queryParameters);
            }
        }
    }

    @Override
    public void onScrolled(RecyclerView.LayoutManager layoutManager) {
        mlastVisibleItemPosition = ((LinearLayoutManager) layoutManager).findLastVisibleItemPosition();
    }

    @Override
    public void start() {
        transToDiscover();
    }
}
