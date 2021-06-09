package com.paula.android.bechef;

import android.app.Activity;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.paula.android.bechef.activities.BeChefActivity;
import com.paula.android.bechef.bookmark.BookmarkFragment;
import com.paula.android.bechef.bookmark.BookmarkPresenter;
import com.paula.android.bechef.detail.DetailFragment;
import com.paula.android.bechef.detail.DetailPresenter;
import com.paula.android.bechef.discover.DiscoverFragment;
import com.paula.android.bechef.discover.DiscoverPresenter;
import com.paula.android.bechef.recipe.RecipeFragment;
import com.paula.android.bechef.recipe.RecipePresenter;
import com.paula.android.bechef.find.FindFragment;
import com.paula.android.bechef.find.FindPresenter;

import static com.google.android.gms.common.internal.Preconditions.checkNotNull;

public class BeChefPresenter implements BeChefContract.Presenter {
    private final BeChefContract.View mMainView;
    private final FragmentManager mFragmentManager;

    private static final String DISCOVER = "DISCOVER";
    private static final String BOOKMARK = "BOOKMARK";
    private static final String RECIPE = "RECIPE";
    private static final String DETAIL = "DETAIL";
    private static final String SEARCH = "SEARCH";

    private DiscoverFragment mDiscoverFragment;
    private BookmarkFragment mBookmarkFragment;
    private RecipeFragment mRecipeFragment;

    private DiscoverPresenter mDiscoverPresenter;
    private BookmarkPresenter mBookmarkPresenter;
    private RecipePresenter mRecipePresenter;

    public BeChefPresenter(BeChefContract.View mainView, FragmentManager fragmentManager) {
        mMainView = checkNotNull(mainView, "mainView cannot be null!");
        mMainView.setCustomMainPresenter(this);
        mFragmentManager = fragmentManager;
    }

    @Override
    public void transToDiscover() {
        FragmentTransaction transaction = mFragmentManager.beginTransaction();

        Fragment mDetailFragment = mFragmentManager.findFragmentByTag(DETAIL);
        if (mDetailFragment != null) {
            transaction.remove(mDetailFragment);
            mFragmentManager.popBackStack();
        }

        Fragment mFindFragment = mFragmentManager.findFragmentByTag(SEARCH);
        if (mFindFragment != null) {
            transaction.remove(mFindFragment);
            mFragmentManager.popBackStack();
        }

        if (mDiscoverFragment == null) mDiscoverFragment = DiscoverFragment.newInstance();
        if (mBookmarkFragment != null) transaction.hide(mBookmarkFragment);
        if (mRecipeFragment != null) transaction.hide(mRecipeFragment);
        if (!mDiscoverFragment.isAdded()) {
            transaction.add(R.id.linearlayout_main_container, mDiscoverFragment, DISCOVER);
        } else {
            transaction.show(mDiscoverFragment);
        }
        transaction.commit();

        if (mDiscoverPresenter == null)
            mDiscoverPresenter = new DiscoverPresenter(mDiscoverFragment);
        mMainView.setMenuId(R.id.navigation_discover);
    }

    @Override
    public void transToBookmark() {
        FragmentTransaction transaction = mFragmentManager.beginTransaction();

        Fragment mDetailFragment = mFragmentManager.findFragmentByTag(DETAIL);
        if (mDetailFragment != null) {
            transaction.remove(mDetailFragment);
            mFragmentManager.popBackStack();
        }

        Fragment mFindFragment = mFragmentManager.findFragmentByTag(SEARCH);
        if (mFindFragment != null) {
            transaction.remove(mFindFragment);
            mFragmentManager.popBackStack();
        }

        if (mBookmarkFragment == null) mBookmarkFragment = BookmarkFragment.newInstance();
        if (mDiscoverFragment != null) transaction.hide(mDiscoverFragment);
        if (mRecipeFragment != null) transaction.hide(mRecipeFragment);
        if (!mBookmarkFragment.isAdded()) {
            transaction.add(R.id.linearlayout_main_container, mBookmarkFragment, BOOKMARK);
        } else {
            transaction.show(mBookmarkFragment);
        }
        transaction.commit();

        if (mBookmarkPresenter == null)
            mBookmarkPresenter = new BookmarkPresenter(mBookmarkFragment);
        mMainView.setMenuId(R.id.navigation_bookmark);
    }

    @Override
    public void transToRecipe() {
        FragmentTransaction transaction = mFragmentManager.beginTransaction();

        Fragment mDetailFragment = mFragmentManager.findFragmentByTag(DETAIL);
        if (mDetailFragment != null) {
            transaction.remove(mDetailFragment);
            mFragmentManager.popBackStack();
        }

        Fragment mFindFragment = mFragmentManager.findFragmentByTag(SEARCH);
        if (mFindFragment != null) {
            transaction.remove(mFindFragment);
            mFragmentManager.popBackStack();
        }

        if (mRecipeFragment == null) mRecipeFragment = RecipeFragment.newInstance();
        if (mDiscoverFragment != null) transaction.hide(mDiscoverFragment);
        if (mBookmarkFragment != null) transaction.hide(mBookmarkFragment);
        if (!mRecipeFragment.isAdded()) {
            transaction.add(R.id.linearlayout_main_container, mRecipeFragment, RECIPE);
        } else {
            transaction.show(mRecipeFragment);
        }
        transaction.commit();

        if (mRecipePresenter == null) mRecipePresenter = new RecipePresenter(mRecipeFragment);
        mMainView.setMenuId(R.id.navigation_recipe);
    }

    @Override
    public void transToDetail(Object content, boolean isBottomShown) {
        FragmentTransaction transaction = mFragmentManager.beginTransaction();

        if (mDiscoverFragment != null && !mDiscoverFragment.isHidden()) {
            transaction.hide(mDiscoverFragment);
            transaction.addToBackStack(DISCOVER);
        }
        if (mBookmarkFragment != null && !mBookmarkFragment.isHidden()) {
            transaction.hide(mBookmarkFragment);
            transaction.addToBackStack(BOOKMARK);
        }
        if (mRecipeFragment != null && !mRecipeFragment.isHidden()) {
            transaction.hide(mRecipeFragment);
            transaction.addToBackStack(RECIPE);
        }

        Fragment mFindFragment = mFragmentManager.findFragmentByTag(SEARCH);
        if (mFindFragment != null && !mFindFragment.isHidden()) {
            transaction.hide(mFindFragment);
            transaction.addToBackStack(SEARCH);
        }

        DetailFragment mDetailFragment = new DetailFragment();
        mDetailFragment.setBottomShown(isBottomShown);
        transaction.add(R.id.linearlayout_main_container, mDetailFragment, DETAIL);

        DetailPresenter mDetailPresenter = new DetailPresenter(mDetailFragment, content);

        transaction.commit();
    }

    @Override
    public void transToSearch(BaseContract.MainPresenter presenter) {
        FragmentTransaction transaction = mFragmentManager.beginTransaction();

        Fragment mDetailFragment = mFragmentManager.findFragmentByTag(DETAIL);
        if (mDetailFragment != null) {
            transaction.remove(mDetailFragment);
            mFragmentManager.popBackStack();
        }

        if (mDiscoverFragment != null && !mDiscoverFragment.isHidden()) {
            transaction.hide(mDiscoverFragment);
            transaction.addToBackStack(DISCOVER);
        }
        if (mBookmarkFragment != null && !mBookmarkFragment.isHidden()) {
            transaction.hide(mBookmarkFragment);
            transaction.addToBackStack(BOOKMARK);
        }
        if (mRecipeFragment != null && !mRecipeFragment.isHidden()) {
            transaction.hide(mRecipeFragment);
            transaction.addToBackStack(RECIPE);
        }

        FindFragment mFindFragment = new FindFragment();
        transaction.add(R.id.linearlayout_main_container, mFindFragment, SEARCH);

        FindPresenter mFindPresenter = new FindPresenter(mFindFragment);
        if (!(presenter instanceof DiscoverPresenter))
            mFindPresenter.setBaseTabs(presenter.getTabs());

        transaction.commit();
    }

    @Override
    public void showToolbar(int itemId) {
        if (itemId == R.id.navigation_discover) {
            mDiscoverFragment.showToolbar();
        } else if (itemId == R.id.navigation_bookmark) {
            mBookmarkFragment.showToolbar();
        } else if (itemId == R.id.navigation_recipe) {
            mRecipeFragment.showToolbar();
        }
    }

    @Override
    public void start() {
        transToDiscover();
    }

    @Override
    public Activity getActivity() {
        return (BeChefActivity) mMainView;
    }
}
