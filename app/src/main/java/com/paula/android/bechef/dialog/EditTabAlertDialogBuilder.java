package com.paula.android.bechef.dialog;

import android.content.Context;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.paula.android.bechef.BeChef;
import com.paula.android.bechef.R;
import com.paula.android.bechef.adapters.EditTabAdapter;
import com.paula.android.bechef.data.dao.BookmarkItemDao;
import com.paula.android.bechef.data.dao.BookmarkTabDao;
import com.paula.android.bechef.data.dao.DiscoverTabDao;
import com.paula.android.bechef.data.dao.RecipeItemDao;
import com.paula.android.bechef.data.dao.RecipeTabDao;
import com.paula.android.bechef.data.database.ItemDatabase;
import com.paula.android.bechef.data.database.TabDatabase;
import com.paula.android.bechef.data.entity.BaseTab;
import com.paula.android.bechef.data.entity.BookmarkTab;
import com.paula.android.bechef.data.entity.DiscoverTab;
import com.paula.android.bechef.data.entity.RecipeTab;
import com.paula.android.bechef.thread.BeChefRunnableInterface;
import com.paula.android.bechef.thread.BeChefRunnable;
import com.paula.android.bechef.utils.Constants;

import java.util.ArrayList;

public class EditTabAlertDialogBuilder extends BeChefAlertDialogBuilder {
    private final EditTabAdapter mEditTabAdapter;

    public EditTabAlertDialogBuilder(@NonNull Context context, ArrayList<BaseTab> baseTabs) {
        super(context, R.style.AlertDialogTheme);
        mEditTabAdapter = new EditTabAdapter(getEditBaseTabs(baseTabs));
        initBuilder();
    }

    private ArrayList<BaseTab> getEditBaseTabs(ArrayList<BaseTab> baseTabs) {
        ArrayList<BaseTab> editBaseTabs = new ArrayList<>();
        if (baseTabs.get(0) instanceof DiscoverTab) {
            for (BaseTab baseTab : baseTabs) {
                editBaseTabs.add(new DiscoverTab((DiscoverTab) baseTab));
            }
        } else if (baseTabs.get(0) instanceof BookmarkTab) {
            for (BaseTab baseTab : baseTabs) {
                editBaseTabs.add(new BookmarkTab((BookmarkTab) baseTab));
            }
        } else {
            for (BaseTab baseTab : baseTabs) {
                editBaseTabs.add(new RecipeTab((RecipeTab) baseTab));
            }
        }
        return editBaseTabs;
    }

    private void initBuilder() {
        setTitle(BeChef.getAppContext().getString(R.string.edit_tab));
        setButtons(new AlertDialogClickCallback() {
            @Override
            public boolean onPositiveButtonClick() {
                return saveData();
            }
        });
    }

    private boolean saveData() {
        final int finalTabType = mEditTabAdapter.getTabType();
        final ArrayList<Long> finalRemoveTabUids = mEditTabAdapter.getRemoveTabUids();
        final ArrayList<BaseTab> finalBaseTabs = mEditTabAdapter.getBaseTabs();
        // Set new tabs for DiscoverTab
        if (finalTabType == Constants.TAB_TYPE_DISCOVER) {
            if (finalRemoveTabUids.size() == 0) return true;
            new Thread(new BeChefRunnable(new BeChefRunnableInterface() {
                @Override
                public void runTasksOnNewThread() {
                    DiscoverTabDao discoverTabDao = TabDatabase.getTabInstance().discoverDao();
                    for (long tabUid : finalRemoveTabUids)
                        discoverTabDao.deleteItemWithTabUid(tabUid);
                }
            })).start();
            return true;
        }

        // Set new tabs for BookmarkTab and RecipeTab
        if (!isTabCompleted(finalBaseTabs)) return false;
        BeChefRunnableInterface mInterface = new BeChefRunnableInterface() {
            @Override
            public void runTasksOnNewThread() {
                if (finalTabType == Constants.TAB_TYPE_BOOKMARK) {
                    BookmarkTabDao bookmarkTabDao = TabDatabase.getTabInstance().bookmarkDao();
                    BookmarkItemDao bookmarkItemDao = ItemDatabase.getItemInstance().bookmarkDao();

                    for (long tabUid : finalRemoveTabUids) {
                        bookmarkTabDao.deleteItemWithTabUid(tabUid);
                        bookmarkItemDao.deleteItemWithTabUid(tabUid);
                    }
                    for (BaseTab tab : finalBaseTabs) {
                        if (tab.getUid() == 0) bookmarkTabDao.insert((BookmarkTab) tab);
                        else bookmarkTabDao.updateBaseTab((BookmarkTab) tab);
                    }
                } else {
                    RecipeTabDao recipeTabDao = TabDatabase.getTabInstance().recipeDao();
                    RecipeItemDao recipeItemDao = ItemDatabase.getItemInstance().recipeDao();

                    for (long tabUid : finalRemoveTabUids) {
                        recipeTabDao.deleteItemWithTabUid(tabUid);
                        recipeItemDao.deleteItemWithTabUid(tabUid);
                    }
                    for (BaseTab tab : finalBaseTabs) {
                        if (tab.getUid() == 0) recipeTabDao.insert((RecipeTab) tab);
                        else recipeTabDao.updateBaseTab((RecipeTab) tab);
                    }
                }
            }
        };
        new Thread(new BeChefRunnable(mInterface)).start();
        return true;
    }

    private boolean isTabCompleted(ArrayList<BaseTab> finalBaseTabs) {
        for (BaseTab baseTab : finalBaseTabs) {
            if (baseTab.getTabName().isEmpty()) {
                Toast.makeText(mContext, BeChef.getAppContext().getString(R.string.toast_no_empty_tab_name),
                        Toast.LENGTH_SHORT).show();
                return false;
            }
        }
        return true;
    }

    @Override
    protected String getPositiveWord() {
        return BeChef.getAppContext().getString(R.string.complete);
    }

    @Override
    protected String getNegativeWord() {
        return BeChef.getAppContext().getString(R.string.cancel);
    }

    @Override
    protected void setCustomView(AlertDialog alertDialog) {
        View view = alertDialog.getLayoutInflater().inflate(R.layout.alert_dialog_edit_tab, null);
        RecyclerView recyclerView = view.findViewById(R.id.recyclerview_edit_tab);
        recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        recyclerView.setAdapter(mEditTabAdapter);
        alertDialog.setView(view);
    }
}