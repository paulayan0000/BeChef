package com.paula.android.bechef.action;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.paula.android.bechef.BaseContract;
import com.paula.android.bechef.R;
import com.paula.android.bechef.bookmark.BookmarkPresenter;
import com.paula.android.bechef.data.LoadDataCallback;
import com.paula.android.bechef.data.LoadDataTask;
import com.paula.android.bechef.data.dao.BaseDao;
import com.paula.android.bechef.data.dao.BookmarkItemDao;
import com.paula.android.bechef.data.dao.ReceiptItemDao;
import com.paula.android.bechef.data.database.ItemDatabase;
import com.paula.android.bechef.data.entity.BaseTab;
import com.paula.android.bechef.data.entity.BookmarkItem;
import com.paula.android.bechef.data.entity.ReceiptItem;
import com.paula.android.bechef.dialog.AlertDialogClickCallback;
import com.paula.android.bechef.dialog.AlertDialogItemsCallback;
import com.paula.android.bechef.dialog.BeChefAlertDialogBuilder;
import com.paula.android.bechef.receipt.ReceiptPresenter;

import java.util.ArrayList;

public class ActionChooseFragment<E> extends Fragment implements View.OnClickListener {
    private BaseContract.CustomPresenter<E> mPresenter;
    private Context mContext;

    public ActionChooseFragment(BaseContract.CustomPresenter<E> presenter) {
        mPresenter = presenter;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_action_choose, container, false);
        mContext = view.getContext();
        view.findViewById(R.id.textview_action_cancel).setOnClickListener(this);
        view.findViewById(R.id.textview_action_move).setOnClickListener(this);
        view.findViewById(R.id.textview_action_delete).setOnClickListener(this);
        return view;
    }

    @Override
    public void onClick(View v) {
        int chosenItemsCount = mPresenter.getChosenItemsCount();

        // Click some action (except cancel) before choose anything
        if (chosenItemsCount == 0 && v.getId() != R.id.textview_action_cancel) {
            Toast.makeText(getContext(), "您尚未選擇任何項目", Toast.LENGTH_SHORT).show();
            return;
        }
        // Click cancel without choose anything
        if (chosenItemsCount == 0 && v.getId() == R.id.textview_action_cancel) {
            mPresenter.leaveChooseDialog();
            return;
        }

        String title = ((TextView) v).getText().toString();
        String message = "是否將 " + chosenItemsCount + " 個項目" + title + "？";
        AlertDialogClickCallback clickCallback = null;
        AlertDialogItemsCallback itemsCallback = null;

        // Need to delete or move something
        switch (v.getId()) {
            case R.id.textview_action_cancel:
                clickCallback = new AlertDialogClickCallback() {
                    @Override
                    public void onPositiveButtonClick() {
                        mPresenter.leaveChooseDialog();
                    }
                };
                break;
            case R.id.textview_action_move:
                message = null;
                final ArrayList<?> baseTabs = mPresenter.getOtherTabs();
                itemsCallback = new AlertDialogItemsCallback() {
                    @Override
                    public String[] getItems() {
                        String[] tabTitleArray = new String[baseTabs.size() + 1];
                        tabTitleArray[0] = " + 新增標籤";
                        for (int i = 0; i < baseTabs.size(); i++) {
                            tabTitleArray[i + 1] = ((BaseTab) baseTabs.get(i)).getTabName();
                        }
                        return tabTitleArray;
                    }

                    @Override
                    public DialogInterface.OnClickListener getItemOnClickListener() {
                        return new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, final int which) {
                                // TODO: 新增書籤
                                if (which == 0) {
                                    Toast.makeText(getContext(), "新增標籤對話框", Toast.LENGTH_SHORT).show();
                                } else {
                                    if (mPresenter instanceof BookmarkPresenter) {
                                        new LoadDataTask<>(new LoadDataCallback<BookmarkItemDao>() {
                                            @Override
                                            public BookmarkItemDao getDao() {
                                                return ItemDatabase.getBookmarkInstance(mContext).bookmarkDao();
                                            }

                                            @Override
                                            public void doInBackground(BookmarkItemDao dao) {
                                                updateBookmarkTabUid(dao, ((BaseTab) baseTabs.get(which - 1)).getUid());
                                            }

                                            @Override
                                            public void onCompleted() {
                                                mPresenter.refreshData(which - 1);
                                                refreshCurrentPage();
                                            }
                                        }).execute();
                                    } else {
                                        new LoadDataTask<>(new LoadDataCallback<ReceiptItemDao>() {
                                            @Override
                                            public ReceiptItemDao getDao() {
                                                return ItemDatabase.getReceiptInstance(mContext).receiptDao();
                                            }

                                            @Override
                                            public void doInBackground(ReceiptItemDao dao) {
                                                updateReceiptTabUid(dao, ((BaseTab) baseTabs.get(which - 1)).getUid());
                                            }

                                            @Override
                                            public void onCompleted() {
                                                mPresenter.refreshData(which - 1);
                                                refreshCurrentPage();
                                            }
                                        }).execute();
                                    }
                                }
                            }
                        };
                    }
                };
                break;
            case R.id.textview_action_delete:
                clickCallback = new AlertDialogClickCallback() {
                    @Override
                    public void onPositiveButtonClick() {
                        if (mPresenter instanceof BookmarkPresenter) {
                            new LoadDataTask<>(new LoadDataCallback<BaseDao<BookmarkItem>>() {
                                @Override
                                public BaseDao<BookmarkItem> getDao() {
                                    return ItemDatabase.getBookmarkInstance(mContext).bookmarkDao();
                                }

                                @Override
                                public void doInBackground(BaseDao<BookmarkItem> dao) {
                                    dao.deleteItems(((BookmarkPresenter) mPresenter).getChosenItems());
                                }

                                @Override
                                public void onCompleted() {
                                    refreshCurrentPage();
                                }
                            }).execute();
                        } else {
                            new LoadDataTask<>(new LoadDataCallback<BaseDao<ReceiptItem>>() {
                                @Override
                                public BaseDao<ReceiptItem> getDao() {
                                    return ItemDatabase.getReceiptInstance(mContext).receiptDao();
                                }

                                @Override
                                public void doInBackground(BaseDao<ReceiptItem> dao) {
                                    dao.deleteItems(((ReceiptPresenter) mPresenter).getChosenItems());
                                }

                                @Override
                                public void onCompleted() {
                                    refreshCurrentPage();
                                }
                            }).execute();
                        }
                    }
                };
                break;
            default:
                break;
        }
        new BeChefAlertDialogBuilder(mContext).setButtons(clickCallback)
                .setCustomItems(itemsCallback)
                .setMessage(message)
                .setTitle(title)
                .create()
                .show();
    }

    private void updateBookmarkTabUid(BookmarkItemDao dao, int newTabUid) {
        ArrayList<BookmarkItem> chosenBookmarkItems = ((BookmarkPresenter) mPresenter).getChosenItems();
        for (BookmarkItem chosenBookmarkItem : chosenBookmarkItems) {
            dao.setNewTabUid(chosenBookmarkItem.getUid(), newTabUid);
        }
    }

    private void updateReceiptTabUid(ReceiptItemDao dao, int newTabUid) {
        ArrayList<ReceiptItem> chosenReceiptItems = ((ReceiptPresenter) mPresenter).getChosenItems();
        for (ReceiptItem chosenReceiptItem : chosenReceiptItems) {
            dao.setNewTabUid(chosenReceiptItem.getUid(), newTabUid);
        }
    }

    private void refreshCurrentPage() {
        mPresenter.refreshCurrentData();
        mPresenter.leaveChooseDialog();
    }
}
