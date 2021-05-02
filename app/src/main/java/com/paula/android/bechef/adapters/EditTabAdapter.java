package com.paula.android.bechef.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.paula.android.bechef.R;
import com.paula.android.bechef.data.dao.BookmarkItemDao;
import com.paula.android.bechef.data.dao.BookmarkTabDao;
import com.paula.android.bechef.data.dao.DiscoverTabDao;
import com.paula.android.bechef.data.dao.RecipeItemDao;
import com.paula.android.bechef.data.dao.RecipeTabDao;
import com.paula.android.bechef.data.database.ItemDatabase;
import com.paula.android.bechef.data.database.TabDatabase;
import com.paula.android.bechef.data.entity.BaseTab;
import com.paula.android.bechef.data.entity.BookmarkTab;
import com.paula.android.bechef.data.entity.RecipeTab;
import com.paula.android.bechef.dialog.AlertDialogClickCallback;
import com.paula.android.bechef.dialog.BeChefAlertDialogBuilder;
import com.paula.android.bechef.utils.BeChefTextWatcher;
import com.paula.android.bechef.utils.Constants;
import com.paula.android.bechef.utils.EditTextChangeCallback;
import com.paula.android.bechef.viewHolders.EditTabViewHolder;

import java.util.ArrayList;

public class EditTabAdapter extends RecyclerView.Adapter implements EditTextChangeCallback {
    private final ArrayList<BaseTab> mBaseTabs;
    private final ArrayList<Long> mRemoveTabUids = new ArrayList<>();
    private Context mContext;
    private int mTabType = Constants.TAB_TYPE_DISCOVER;

    public EditTabAdapter(ArrayList<BaseTab> baseTabs) {
        mBaseTabs = new ArrayList<>(baseTabs);
        setTabType();
    }

    private void setTabType() {
        if (mBaseTabs.size() == 0) return;
        BaseTab firstTab = mBaseTabs.get(0);
        if (firstTab instanceof BookmarkTab) {
            mTabType = Constants.TAB_TYPE_BOOKMARK;
        } else if (firstTab instanceof RecipeTab){
            mTabType = Constants.TAB_TYPE_RECIPE;
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        if (mTabType == Constants.TAB_TYPE_DISCOVER) {
            return new EditDiscoverTabViewHolder(LayoutInflater.from(mContext)
                    .inflate(R.layout.item_edit_material_contents, parent, false));
        }
        return new EditCustomTabViewHolder(LayoutInflater.from(mContext)
                .inflate(R.layout.item_edit_material_contents, parent, false)
                , new BeChefTextWatcher(this));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (mTabType == Constants.TAB_TYPE_DISCOVER) {
            ((EditDiscoverTabViewHolder) holder).bindView(mBaseTabs.get(position).getTabName());
        } else {
            ((EditCustomTabViewHolder) holder).bindCustomView(position);
        }
    }

    @Override
    public int getItemCount() {
        return mBaseTabs.size();
    }

    public boolean onCompleteClicked() {
        int viewType = mTabType;
        final ArrayList<Long> finalRemoveTabUids = mRemoveTabUids;
        final ArrayList<BaseTab> finalBaseTabs = mBaseTabs;

        // Set new tabs for DiscoverTab
        if (viewType == Constants.TAB_TYPE_DISCOVER) {
            Runnable runnable = new Runnable() {
                @Override
                public void run() {
                    DiscoverTabDao discoverTabDao = TabDatabase.getTabInstance(mContext).discoverDao();
                    for (long tabUid : finalRemoveTabUids)
                        discoverTabDao.deleteItemWithTabUid(tabUid);
                }
            };
            new Thread(runnable).start();
            return true;
        }

        // Check tab names not to be empty
        int tabNumber = finalBaseTabs.size();
        for (int i = 0; i < tabNumber; i++) {
            if (finalBaseTabs.get(i).getTabName().isEmpty()) {
                Toast.makeText(mContext, mContext.getString(R.string.toast_no_empty_tab_name),
                        Toast.LENGTH_SHORT).show();
                return false;
            }
        }

        // Set new tabs for BookmarkTab and RecipeTab
        Runnable runnable;
        if (mTabType == Constants.TAB_TYPE_BOOKMARK) {
            runnable = new Runnable() {
                @Override
                public void run() {
                    BookmarkTabDao bookmarkTabDao = TabDatabase.getTabInstance(mContext).bookmarkDao();
                    BookmarkItemDao bookmarkItemDao = ItemDatabase.getItemInstance(mContext).bookmarkDao();

                    for (long tabUid : finalRemoveTabUids) {
                        bookmarkTabDao.deleteItemWithTabUid(tabUid);
                        bookmarkItemDao.deleteItemWithTabUid(tabUid);
                    }
                    for (BaseTab tab : finalBaseTabs) {
                        if (tab.getUid() == 0) bookmarkTabDao.insert((BookmarkTab) tab);
                        else bookmarkTabDao.updateBaseTab((BookmarkTab) tab);
                    }
                }
            };
        } else {
            runnable = new Runnable() {
                @Override
                public void run() {
                    RecipeTabDao recipeTabDao = TabDatabase.getTabInstance(mContext).recipeDao();
                    RecipeItemDao recipeItemDao = ItemDatabase.getItemInstance(mContext).recipeDao();

                    for (long tabUid : finalRemoveTabUids) {
                        recipeTabDao.deleteItemWithTabUid(tabUid);
                        recipeItemDao.deleteItemWithTabUid(tabUid);
                    }
                    for (BaseTab tab : finalBaseTabs) {
                        if (tab.getUid() == 0) recipeTabDao.insert((RecipeTab) tab);
                        else recipeTabDao.updateBaseTab((RecipeTab) tab);
                    }
                }
            };
        }
        new Thread(runnable).start();
        return true;
    }

    @Override
    public void afterTextChanged(int position, String textContent) {
        mBaseTabs.get(position).setTabName(textContent);
    }

    private class EditCustomTabViewHolder extends EditTabViewHolder {

        EditCustomTabViewHolder(@NonNull View itemView, BeChefTextWatcher textWatcher) {
            super(itemView, textWatcher);
        }

        public void bindCustomView(int position) {
            mTextWatcher.bindPosition(position);
            bindView(mBaseTabs.get(position).getTabName());
            int removeDrawableId = getRemoveDrawableId(isOnlyOneTab());
            setImageDrawable(mIbtnRemove, itemView.getContext(), removeDrawableId);
        }

        @Override
        public void onClick(View v) {
            super.onClick(v);
            final int currentPosition = getAdapterPosition();
            int currentViewId = v.getId();
            if (currentViewId == R.id.imagebutton_remove) {
                if (isOnlyOneTab()) return;
                String tabName = mBaseTabs.get(currentPosition).getTabName();
                String shownTabName = tabName.isEmpty() ? mContext.getString(R.string.this_word) :
                        String.format(mContext.getString(R.string.tab_name), tabName);
                new BeChefAlertDialogBuilder(mContext).setButtons(new AlertDialogClickCallback() {
                    @Override
                    public boolean onPositiveButtonClick() {
                        removeTab(currentPosition);
                        return true;
                    }
                }).setMessage(String.format(mContext.getString(R.string.remove_tab_msg), shownTabName))
                        .setTitle(mContext.getString(R.string.remove_tab_and_its_content)).create().show();
            } else if (currentViewId == R.id.imagebutton_clear) {
                mBaseTabs.get(currentPosition).setTabName("");
                notifyItemChanged(currentPosition);
            }
            if (currentViewId == R.id.imagebutton_add) {
                mBaseTabs.add(currentPosition + 1, mTabType == Constants.TAB_TYPE_BOOKMARK ?
                        new BookmarkTab("") : new RecipeTab(""));
                notifyItemInserted(currentPosition + 1);
                notifyItemRangeChanged(currentPosition, getItemCount() - currentPosition);
            }
        }
    }

    private class EditDiscoverTabViewHolder extends EditTabViewHolder {

        public EditDiscoverTabViewHolder(@NonNull View itemView) {
            super(itemView);
        }

        @Override
        public void onClick(View v) {
            super.onClick(v);
            removeTab(getAdapterPosition());
        }
    }

    private boolean isOnlyOneTab() {
        return mBaseTabs.size() == 1;
    }

    private void removeTab(int currentPosition) {
        mRemoveTabUids.add(mBaseTabs.get(currentPosition).getUid());
        mBaseTabs.remove(currentPosition);
        if (isOnlyOneTab()) {
            notifyItemRangeChanged(0, 2);
        } else {
            notifyItemRemoved(currentPosition);
            notifyItemRangeChanged(currentPosition, getItemCount() - currentPosition);
        }
    }
}
