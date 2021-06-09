package com.paula.android.bechef.dialog;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RatingBar;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;

import com.paula.android.bechef.BeChef;
import com.paula.android.bechef.data.database.ItemDatabase;
import com.paula.android.bechef.data.database.TabDatabase;
import com.paula.android.bechef.data.entity.BaseItem;
import com.paula.android.bechef.data.entity.BaseTab;
import com.paula.android.bechef.data.entity.BookmarkItem;
import com.paula.android.bechef.data.entity.BookmarkTab;
import com.paula.android.bechef.detail.DetailPresenter;
import com.paula.android.bechef.R;
import com.paula.android.bechef.thread.BeChefRunnable;
import com.paula.android.bechef.thread.BeChefRunnableInterface;
import com.paula.android.bechef.utils.Constants;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class AddToBookmarkDialogBuilder extends BeChefAlertDialogBuilder {
    ArrayList<BaseTab> mTabs;
    int mChosenTabIndex = Constants.ADD_NEW_TAB_INDEX;
    private DetailPresenter mDetailPresenter;
    private EditText mEtTabName;
    private ImageButton mIbtnClear;
    private float mRating = Constants.DEFAULT_RATING;

    public AddToBookmarkDialogBuilder(@NonNull Context context) {
        super(context, R.style.AlertDialogWithRecyclerViewTheme);
        initBuilder();
    }

    public AddToBookmarkDialogBuilder(Context context, DetailPresenter presenter) {
        super(context, R.style.AlertDialogWithRecyclerViewTheme);
        mDetailPresenter = presenter;
        initBuilder();
    }

    private void initBuilder() {
        setTitle(getTitleText());
        setButtons(new AlertDialogClickCallback() {
            @Override
            public boolean onPositiveButtonClick() {
                String tabName = mEtTabName.getText().toString();
                if (isChosenAddNew() && "".equals(tabName)) {
                    Toast.makeText(mContext, BeChef.getAppContext().getString(R.string.toast_no_empty_tab_name), Toast.LENGTH_SHORT).show();
                    return false;
                }
                manipulateData(tabName);
                return true;
            }
        });
    }

    @Override
    protected void setCustomView(AlertDialog alertDialog) {
        View view = alertDialog.getLayoutInflater()
                .inflate(R.layout.alert_dialog_add_to_bookmark, null);
        setRatingBar(view);
        setSpinner(view);
        mEtTabName = view.findViewById(R.id.edittext_new_tab_name);
        mIbtnClear = view.findViewById(R.id.imagebutton_clear);
        mIbtnClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mEtTabName.setText("");
            }
        });
        alertDialog.setView(view);
    }

    protected void setRatingBar(View view) {
        ((RatingBar) view.findViewById(R.id.rating_bar)).setOnRatingBarChangeListener(
                new RatingBar.OnRatingBarChangeListener() {
                    @Override
                    public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                        mRating = rating;
                    }
                });
    }

    private void setSpinner(View view) {
        Spinner spinnerTab = view.findViewById(R.id.spinner_tab_name);
        spinnerTab.setAdapter(new ArrayAdapter<>(mContext,
                android.R.layout.simple_spinner_dropdown_item,
                mTabs));
        spinnerTab.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mChosenTabIndex = position;
                showTabNameEditText(isChosenAddNew());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    public AddToBookmarkDialogBuilder setTabs(ArrayList<BaseTab> baseTabs) {
        mTabs = new ArrayList<>();
        mTabs.addAll(baseTabs);
        mTabs.add(0, new BaseTab(BeChef.getAppContext().getString(R.string.add_new_tab)));
        return this;
    }

    protected CharSequence getTitleText() {
        return BeChef.getAppContext().getString(R.string.add_to_bookmark);
    }

    private void showTabNameEditText(boolean isShown) {
        mEtTabName.setVisibility(isShown ? View.VISIBLE : View.GONE);
        mIbtnClear.setVisibility(isShown ? View.VISIBLE : View.GONE);
    }

    protected void manipulateData(final String tabName) {
        final BookmarkItem finalBookmarkItem
                = new BookmarkItem((BaseItem) mDetailPresenter.getDataContent());
        final long chosenTabUid = mTabs.get(mChosenTabIndex).getUid();

        new Thread(new BeChefRunnable(new BeChefRunnableInterface() {
            @Override
            public void runTasksOnNewThread() {
                final BookmarkItem newBookmarkItem;
                if (isChosenAddNew()) {
                    long newUid = TabDatabase.getTabInstance().bookmarkDao()
                            .insert(new BookmarkTab(tabName));
                    newBookmarkItem = modifyBookmarkItem(finalBookmarkItem, newUid);
                } else {
                    newBookmarkItem = modifyBookmarkItem(finalBookmarkItem, chosenTabUid);
                }
                // Insert new bookmarkItem into database
                long uid = ItemDatabase.getItemInstance().bookmarkDao().insert(newBookmarkItem);
                newBookmarkItem.setUid((int) uid);

                if (mDetailPresenter == null) return;
                Activity activity = mDetailPresenter.getActivity();
                if (activity != null) {
                    activity.runOnUiThread(new BeChefRunnable(new BeChefRunnableInterface() {
                        @Override
                        public void runTasksOnNewThread() {
                            mDetailPresenter.updateData(newBookmarkItem);
                        }
                    }));
                }
            }
        })).start();
    }

    private BookmarkItem modifyBookmarkItem(BookmarkItem bookmarkItem, long tabUid) {
        bookmarkItem.setRating(mRating);
        bookmarkItem.setTabUid(tabUid);
        bookmarkItem.setCreatedTime(new SimpleDateFormat(BeChef.getAppContext().getString(R.string.date_format),
                Locale.getDefault()).format(new Date()));
        return bookmarkItem;
    }

    boolean isChosenAddNew() {
        return mChosenTabIndex == 0;
    }
}