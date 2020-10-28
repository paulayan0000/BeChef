package com.paula.android.bechef.receipt;

import android.view.View;

import com.paula.android.bechef.customMain.CustomMainFragment;
import com.paula.android.bechef.R;
import com.paula.android.bechef.data.entity.ReceiptItem;
import com.paula.android.bechef.data.entity.ReceiptTab;
import com.paula.android.bechef.dialog.EditItemDialog;
import com.paula.android.bechef.receiptChild.ReceiptChildFragment;
import com.paula.android.bechef.utils.Constants;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import androidx.fragment.app.Fragment;

public class ReceiptFragment extends CustomMainFragment<ReceiptTab, ReceiptItem> {
    public static ReceiptFragment newInstance() {
        return new ReceiptFragment();
    }

    @Override
    protected int getTitleText() {
        return R.string.title_receipt;
    }

    @Override
    public ArrayList<ReceiptItem> getChosenItems() {
        Fragment childFragment = getChildFragment(mCurrentTabIndex);
        if (childFragment != null) return ((ReceiptChildFragment)childFragment).getChosenItems();
        return new ArrayList<>();
    }

    @Override
    protected void createNewData() {
        ReceiptItem newItem = new ReceiptItem();
        Fragment childFragment = getChildFragment(mCurrentTabIndex);
        if (childFragment != null) {
            newItem.setTabUid(((ReceiptChildFragment) childFragment).getTabUid());
            newItem.setCreatedTime(new SimpleDateFormat(Constants.DATE_FORMAT, Locale.getDefault()).format(new Date()));
            EditItemDialog editItemDialog = new EditItemDialog(newItem);
            editItemDialog.show(getChildFragmentManager(), "new");
        }
    }

    @Override
    protected int getNewVisibility() {
        return View.VISIBLE;
    }
}