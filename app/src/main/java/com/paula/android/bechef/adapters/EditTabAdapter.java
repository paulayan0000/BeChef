package com.paula.android.bechef.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.paula.android.bechef.R;
import com.paula.android.bechef.data.LoadDataCallback;
import com.paula.android.bechef.data.LoadDataTask;
import com.paula.android.bechef.data.dao.BookmarkItemDao;
import com.paula.android.bechef.data.dao.BookmarkTabDao;
import com.paula.android.bechef.data.dao.DiscoverTabDao;
import com.paula.android.bechef.data.dao.ReceiptItemDao;
import com.paula.android.bechef.data.dao.ReceiptTabDao;
import com.paula.android.bechef.data.database.ItemDatabase;
import com.paula.android.bechef.data.database.TabDatabase;
import com.paula.android.bechef.data.entity.BaseTab;
import com.paula.android.bechef.data.entity.BookmarkTab;
import com.paula.android.bechef.data.entity.DiscoverTab;
import com.paula.android.bechef.data.entity.ReceiptTab;
import com.paula.android.bechef.dialog.AlertDialogClickCallback;
import com.paula.android.bechef.dialog.BeChefAlertDialogBuilder;
import com.paula.android.bechef.utils.BeChefTextWatcher;
import com.paula.android.bechef.utils.Constants;
import com.paula.android.bechef.utils.EditTextChangeCallback;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class EditTabAdapter extends RecyclerView.Adapter implements EditTextChangeCallback {
    private Context mContext;
    private ArrayList<BaseTab> mBaseTabs;
    private ArrayList<Long> mRemoveTabUids = new ArrayList<>();

    public EditTabAdapter(ArrayList<BaseTab> baseTabs) {
        mBaseTabs = baseTabs;
        BaseTab baseTab = mBaseTabs.get(0);
    }

    @Override
    public int getItemViewType(int position) {
        if (mBaseTabs.get(0) instanceof DiscoverTab) return Constants.VIEW_TYPE_DISCOVER;
        else if (mBaseTabs.get(0) instanceof BookmarkTab) return Constants.VIEW_TYPE_BOOKMARK;
        else return Constants.VIEW_TYPE_RECEIPT;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        if (viewType == Constants.VIEW_TYPE_DISCOVER) {
            View view = LayoutInflater.from(mContext).inflate(R.layout.item_edit_discover_tab, parent, false);
            return new EditDiscoverTabViewHolder(view);
        } else {
            View view = LayoutInflater.from(mContext).inflate(R.layout.item_edit_material_contents, parent, false);
            return new EditTabViewHolder(view, new BeChefTextWatcher(this));
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof EditDiscoverTabViewHolder) ((EditDiscoverTabViewHolder) holder).bindView(position);
        else ((EditTabViewHolder) holder).bindView(position);
    }

    @Override
    public int getItemCount() {
        return mBaseTabs.size();
    }

    public boolean onCompleteClicked() {
        final int viewType = getItemViewType(0);

        if (viewType != Constants.VIEW_TYPE_DISCOVER) {
            for (BaseTab baseTab : mBaseTabs) {
                if (baseTab.getTabName().isEmpty()) {
                    Toast.makeText(mContext, "請輸入書籤名", Toast.LENGTH_SHORT).show();
                    return false;
                }
            }
        }
        new LoadDataTask<>(new LoadDataCallback<TabDatabase>() {
            private BookmarkItemDao mBookmarkItemDao;
            private ReceiptItemDao mReceiptItemDao;

            @Override
            public TabDatabase getDao() {
                if (viewType == Constants.VIEW_TYPE_DISCOVER)
                    return TabDatabase.getDiscoverInstance(mContext);
                else if (viewType == Constants.VIEW_TYPE_BOOKMARK) {
                    mBookmarkItemDao = ItemDatabase.getBookmarkInstance(mContext).bookmarkDao();
                    return TabDatabase.getBookmarkInstance(mContext);
                } else {
                    mReceiptItemDao = ItemDatabase.getReceiptInstance(mContext).receiptDao();
                    return TabDatabase.getReceiptInstance(mContext);
                }
            }

            @Override
            public void doInBackground(TabDatabase database) {
                if (viewType == Constants.VIEW_TYPE_DISCOVER) {
                    DiscoverTabDao discoverTabDao = database.discoverDao();
                    for (long tabUid : mRemoveTabUids)
                        discoverTabDao.deleteItemWithTabUid(tabUid);
                } else if (viewType == Constants.VIEW_TYPE_BOOKMARK) {
                    BookmarkTabDao bookmarkTabDao = database.bookmarkDao();
                    for (long tabUid : mRemoveTabUids) {
                        bookmarkTabDao.deleteItemWithTabUid(tabUid);
                        mBookmarkItemDao.deleteItemWithTabUid(tabUid);
                    }
                    for (BaseTab tab : mBaseTabs) {
                        if (tab.getUid() == 0) bookmarkTabDao.insert((BookmarkTab) tab);
                        else bookmarkTabDao.updateBaseTab((BookmarkTab) tab);
                    }
                } else {
                    ReceiptTabDao receiptTabDao = database.receiptDao();
                    for (long tabUid : mRemoveTabUids) {
                        receiptTabDao.deleteItemWithTabUid(tabUid);
                        mReceiptItemDao.deleteItemWithTabUid(tabUid);
                    }
                    for (BaseTab tab : mBaseTabs) {
                        if (tab.getUid() == 0) receiptTabDao.insert((ReceiptTab) tab);
                        else receiptTabDao.updateBaseTab((ReceiptTab) tab);
                    }
                }
            }

            @Override
            public void onCompleted() {
            }
        }).execute();
        return true;
    }

    @Override
    public void afterTextChanged(int position, String textContent) {
        mBaseTabs.get(position).setTabName(textContent);
    }

    private class EditTabViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private EditText mEtTabName;
        private ImageButton mIbtnRemove;
        private BeChefTextWatcher mTextWatcher;

        EditTabViewHolder(@NonNull View itemView, BeChefTextWatcher textWatcher) {
            super(itemView);
            mEtTabName = itemView.findViewById(R.id.edittext_material_content);
            mTextWatcher = textWatcher;
            mEtTabName.setHint("請輸入書籤名稱");
            mEtTabName.addTextChangedListener(mTextWatcher);

            itemView.findViewById(R.id.imagebutton_add).setOnClickListener(this);
            mIbtnRemove = itemView.findViewById(R.id.imagebutton_remove);
            mIbtnRemove.setOnClickListener(this);
            itemView.findViewById(R.id.imagebutton_clear).setOnClickListener(this);
        }

        void bindView(int position) {
            mTextWatcher.bindPosition(position);
            mEtTabName.setText(mBaseTabs.get(position).getTabName());
            mIbtnRemove.setImageDrawable(mContext.getResources()
                    .getDrawable(mBaseTabs.size() == 1 ? R.drawable.ic_remove_gray : R.drawable.ic_remove));
        }

        @Override
        public void onClick(View v) {
            final int currentPosition = getAdapterPosition();
            if (currentPosition < 0) return;
            switch (v.getId()) {
                case R.id.imagebutton_add:
                    BaseTab baseTab = mBaseTabs.get(0);
                    if (baseTab instanceof BookmarkTab)
                        mBaseTabs.add(currentPosition + 1, new BookmarkTab(""));
                    else if (baseTab instanceof ReceiptTab)
                        mBaseTabs.add(currentPosition + 1, new ReceiptTab(""));
                    notifyAdded(currentPosition);
                    break;
                case R.id.imagebutton_remove:
                    if (mBaseTabs.size() == 1) return;
                    if (mBaseTabs.get(0) instanceof DiscoverTab) {
                        removeTab(currentPosition);
                    } else {
                        BeChefAlertDialogBuilder builder = new BeChefAlertDialogBuilder(mContext);
                        String tabName = mBaseTabs.get(currentPosition).getTabName();
                        builder.setButtons(new AlertDialogClickCallback() {
                            @Override
                            public boolean onPositiveButtonClick() {
                                removeTab(currentPosition);
                                return true;
                            }
                        }).setMessage("是否要刪除" + (!tabName.isEmpty() ? "「" + tabName + "」" : "此") + "書籤內所有內容？")
                                .setTitle("刪除書籤及其內容").create().show();
                    }
                    break;
                case R.id.imagebutton_clear:
                    mBaseTabs.get(currentPosition).setTabName("");
                    notifyItemChanged(currentPosition);
                    break;
            }
        }
    }

    private void removeTab(int currentPosition) {
        mRemoveTabUids.add(mBaseTabs.get(currentPosition).getUid());
        mBaseTabs.remove(currentPosition);
        if (mBaseTabs.size() == 1) notifyItemRangeChanged(0, 2);
        else notifyRemoved(currentPosition);
    }

    private void notifyAdded(int currentPosition) {
        notifyItemInserted(currentPosition + 1);
        notifyItemRangeChanged(currentPosition, getItemCount() - currentPosition);
    }

    private void notifyRemoved(int currentPosition) {
        notifyItemRemoved(currentPosition);
        notifyItemRangeChanged(currentPosition, getItemCount() - currentPosition);
    }

    private class EditDiscoverTabViewHolder extends RecyclerView.ViewHolder {
        private TextView mTvTabName;
        private ImageButton mIbtnRemove;

        public EditDiscoverTabViewHolder(View view) {
            super(view);
            mTvTabName = view.findViewById(R.id.textview_tab_name);
            mIbtnRemove = view.findViewById(R.id.imagebutton_remove);
            mIbtnRemove.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int currentPosition = getAdapterPosition();
                    if (currentPosition < 0 || mBaseTabs.size() <= 1) return;
                    removeTab(currentPosition);
                }
            });
        }

        public void bindView(int position) {
            mTvTabName.setText(mBaseTabs.get(position).getTabName());
            mIbtnRemove.setImageDrawable(mContext.getResources()
                    .getDrawable(mBaseTabs.size() == 1 ? R.drawable.ic_remove_gray : R.drawable.ic_remove));
        }
    }
}
