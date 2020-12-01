package com.paula.android.bechef.dialog;

import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RatingBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.paula.android.bechef.R;
import com.paula.android.bechef.data.LoadDataCallback;
import com.paula.android.bechef.data.LoadDataTask;
import com.paula.android.bechef.data.dao.BookmarkItemDao;
import com.paula.android.bechef.data.dao.BookmarkTabDao;
import com.paula.android.bechef.data.database.ItemDatabase;
import com.paula.android.bechef.data.database.TabDatabase;
import com.paula.android.bechef.data.entity.BaseItem;
import com.paula.android.bechef.data.entity.BaseTab;
import com.paula.android.bechef.data.entity.BookmarkItem;
import com.paula.android.bechef.data.entity.BookmarkTab;
import com.paula.android.bechef.detail.DetailPresenter;
import com.paula.android.bechef.utils.Constants;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class AddToBookmarkDialog extends BeChefDialog {
    private DetailPresenter mDetailPresenter;
    private EditText mEtTabName;
    private ImageButton mIbtnClear;
    int mChosenTab = 0;
    private float mChosenRating = 0;
    private ArrayList<String> mTabNames = new ArrayList<>();
    ArrayList<?> mBaseTabs;

    public AddToBookmarkDialog(DetailPresenter presenter) {
        mDetailPresenter = presenter;
    }

    AddToBookmarkDialog() {
    }

    public void setTabs(ArrayList<?> baseTabs) {
        mBaseTabs = baseTabs;
        mTabNames.add(0, "新增書籤");
        for (Object baseTab : mBaseTabs) {
            mTabNames.add(((BaseTab) baseTab).getTabName());
        }
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.add_to_bookmark_dialog;
    }

    @Override
    protected String getTitleText() {
        return "加入書籤...";
    }

    @Override
    public void setView(View view) {
        setRatingBar(view);

        mEtTabName = view.findViewById(R.id.edittext_new_tab_name);
        mIbtnClear = view.findViewById(R.id.imagebutton_clear);
        mIbtnClear.setOnClickListener(this);

        Spinner spinnerTab = view.findViewById(R.id.spinner_tab_name);
        spinnerTab.setAdapter(new ArrayAdapter<>(mContext, android.R.layout.simple_spinner_dropdown_item, mTabNames));
        spinnerTab.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mChosenTab = position;
                showTabNameEditText(position == 0);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    protected void setRatingBar(View view) {
        ((RatingBar) view.findViewById(R.id.rating_bar)).setOnRatingBarChangeListener(
                new RatingBar.OnRatingBarChangeListener() {
                    @Override
                    public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                        mChosenRating = rating;
                    }
                });
    }

    private void showTabNameEditText(boolean isShown) {
        mEtTabName.setVisibility(isShown ? View.VISIBLE : View.GONE);
        mIbtnClear.setVisibility(isShown ? View.VISIBLE : View.GONE);
    }

    @Override
    protected void setPositiveButton() {
        final String tabName = mEtTabName.getText().toString();
        if (mChosenTab == 0 && "".equals(tabName)) {
            Toast.makeText(mContext, "書籤名不可為空白", Toast.LENGTH_SHORT).show();
            return;
        }
        manipulateData(tabName);
    }

    protected void manipulateData(final String tabName) {
        new LoadDataTask<>(new LoadDataCallback<BookmarkItemDao>() {
            private BookmarkTabDao mBookmarkTabDao;
            private BookmarkItem mBookmarkItem;

            @Override
            public BookmarkItemDao getDao() {
                if (mChosenTab == 0)
                    mBookmarkTabDao = TabDatabase.getBookmarkInstance(mContext).bookmarkDao();
                return ItemDatabase.getBookmarkInstance(mContext).bookmarkDao();
            }

            @Override
            public void doInBackground(BookmarkItemDao dao) {
                if (mChosenTab == 0) {
                    long newUid = mBookmarkTabDao.insert(new BookmarkTab(tabName));
                    mBookmarkItem = getBookmarkItem((int) newUid);
                } else {
                    mBookmarkItem = getBookmarkItem(((BaseTab) mBaseTabs.get(mChosenTab - 1)).getUid());
                }
                long uid = dao.insert(mBookmarkItem);
                mBookmarkItem.setUid((int) uid);
            }

            @Override
            public void onCompleted() {
                dismiss();
                mDetailPresenter.transDetailUi(mBookmarkItem);
            }
        }).execute();
    }

    @Override
    protected void setOtherButtons(int viewId) {
        if (viewId == R.id.imagebutton_clear) mEtTabName.setText("");
    }

    private BookmarkItem getBookmarkItem(long tabUid) {
        BookmarkItem bookmarkItem = new BookmarkItem((BaseItem) mDetailPresenter.getDataContent());
        bookmarkItem.setRating(mChosenRating);
        bookmarkItem.setTabUid(tabUid);
        bookmarkItem.setCreatedTime(new SimpleDateFormat(Constants.DATE_FORMAT, Locale.getDefault()).format(new Date()));
        return bookmarkItem;
    }
}
