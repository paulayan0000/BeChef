package com.paula.android.bechef.dialog;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
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
import com.paula.android.bechef.detail.DetailContract;
import com.paula.android.bechef.detail.DetailFragment;
import com.paula.android.bechef.detail.DetailPresenter;
import com.paula.android.bechef.utils.Constants;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

public class AddToBookmarkDialog extends DialogFragment implements View.OnClickListener {
    private DetailPresenter mDetailPresenter;
    protected Context mContext;
    private EditText mEtTabName;
    int mChosenTab = 0;
    private float mChosenRating = 0;
    private ArrayList<String> mTabNames = new ArrayList<>();
    ArrayList<?> mBaseTabs;

    public AddToBookmarkDialog(ArrayList<?> baseTabs, DetailPresenter presenter) {
        mDetailPresenter = presenter;
        setTabs(baseTabs);
    }

    public AddToBookmarkDialog(ArrayList<?> baseTabs) {
        setTabs(baseTabs);
    }

    private void setTabs(ArrayList<?> baseTabs) {
        mBaseTabs = baseTabs;
        mTabNames.add(0, "新增書籤");
        for (Object baseTab : mBaseTabs) {
            mTabNames.add(((BaseTab) baseTab).getTabName());
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NO_TITLE, R.style.MyDialogTheme);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.add_to_bookmark_dialog, container, false);
        mContext = view.getContext();

        view.findViewById(R.id.negative_button).setOnClickListener(this);
        view.findViewById(R.id.positive_button).setOnClickListener(this);

        setRatingBar(view);

        mEtTabName = view.findViewById(R.id.edittext_new_tab_name);

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

        return view;
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
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.negative_button:
                dismiss();
                break;
            case R.id.positive_button:
                final String tabName = mEtTabName.getText().toString();
                if (mChosenTab == 0 && "".equals(tabName)) {
                    Toast.makeText(mContext, "書籤名不可為空白", Toast.LENGTH_SHORT).show();
                    return;
                }
                manipulateData(tabName);
        }
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
                dao.insert(mBookmarkItem);
            }

            @Override
            public void onCompleted() {
                dismiss();
                mDetailPresenter.transDetailUi(mBookmarkItem);
            }
        }).execute();
    }

    private BookmarkItem getBookmarkItem(int tabUid) {
        BookmarkItem bookmarkItem = new BookmarkItem((BaseItem) mDetailPresenter.getDataContent());
        bookmarkItem.setRating(mChosenRating);
        bookmarkItem.setTabUid(tabUid);
        bookmarkItem.setCreatedTime(new SimpleDateFormat(Constants.DATE_FORMAT, Locale.getDefault()).format(new Date()));
        return bookmarkItem;
    }
}
