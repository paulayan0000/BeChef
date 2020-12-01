package com.paula.android.bechef.action;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.paula.android.bechef.customMain.CustomMainPresenter;
import com.paula.android.bechef.R;
import com.paula.android.bechef.bookmark.BookmarkPresenter;
import com.paula.android.bechef.data.LoadDataCallback;
import com.paula.android.bechef.data.LoadDataTask;
import com.paula.android.bechef.data.database.ItemDatabase;
import com.paula.android.bechef.dialog.AlertDialogClickCallback;
import com.paula.android.bechef.dialog.AlertDialogItemsCallback;
import com.paula.android.bechef.dialog.BeChefAlertDialogBuilder;
import com.paula.android.bechef.dialog.MoveToDialog;

import java.util.ArrayList;

public class ActionChooseFragment<T> extends Fragment implements View.OnClickListener {
    private CustomMainPresenter<T> mPresenter;
    private Context mContext;

    public ActionChooseFragment(CustomMainPresenter<T> presenter) {
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
                MoveToDialog<T> moveToDialog = new MoveToDialog<>(mPresenter);
                moveToDialog.setTabs(mPresenter.getOtherTabs());
                moveToDialog.show(getChildFragmentManager(), "move");
                return;
            case R.id.textview_action_delete:
                clickCallback = new AlertDialogClickCallback() {
                    @Override
                    public void onPositiveButtonClick() {
                        new LoadDataTask<>(new LoadDataCallback<ItemDatabase>() {
                            @Override
                            public ItemDatabase getDao() {
                                if (mPresenter instanceof BookmarkPresenter)
                                    return ItemDatabase.getBookmarkInstance(mContext);
                                else
                                    return ItemDatabase.getReceiptInstance(mContext);
                            }

                            @Override
                            public void doInBackground(ItemDatabase database) {
                                ArrayList<Long> chosenUids = mPresenter.getChosenUids();
                                if (mPresenter instanceof BookmarkPresenter)
                                    for (long uid : chosenUids) database.bookmarkDao().deleteItemWithUid(uid);
                                else
                                    for (long uid : chosenUids) database.receiptDao().deleteItemWithUid(uid);
                            }

                            @Override
                            public void onCompleted() {
                                mPresenter.leaveChooseDialog();
                            }
                        }).execute();
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
}
