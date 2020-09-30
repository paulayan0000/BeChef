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
import com.paula.android.bechef.data.entity.BaseTab;
import com.paula.android.bechef.data.entity.BookmarkItem;
import com.paula.android.bechef.data.entity.BookmarkTab;
import com.paula.android.bechef.data.entity.DiscoverItem;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

public class AddToBookmarkDialog extends DialogFragment implements View.OnClickListener {
    private Context mContext;
    private EditText mEtTabName;
    private int mChosenTab = 0;
    private float mChosenRating = 0;
    private ArrayList<String> mTabNames = new ArrayList<>();
    private ArrayList<BookmarkTab> mBookmarkTabs;
    private DiscoverItem mDiscoverItem;

    public AddToBookmarkDialog(ArrayList<BookmarkTab> bookmarkTabs, DiscoverItem discoverItem) {
        mDiscoverItem = discoverItem;
        mBookmarkTabs = bookmarkTabs;
        mTabNames.add(0, "新增書籤");
        for (BaseTab baseTab : mBookmarkTabs) {
            mTabNames.add(baseTab.getTabName());
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

        RatingBar ratingBar = view.findViewById(R.id.rating_bar);
        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                mChosenRating = rating;
                Toast.makeText(mContext, "rating: " + rating, Toast.LENGTH_LONG).show();
            }
        });

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
                int tabUid = -1;
                if (mChosenTab == 0) {
                    if ("".equals(tabName)) {
                        Toast.makeText(mContext, "書籤名不可為空白", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    new LoadDataTask<>(new LoadDataCallback<BookmarkTabDao>() {
                        private BookmarkItemDao mBookmarkItemDao;

                        @Override
                        public BookmarkTabDao getDao() {
                            mBookmarkItemDao = ItemDatabase.getBookmarkInstance(mContext).bookmarkDao();
                            return TabDatabase.getBookmarkInstance(mContext).bookmarkDao();
                        }

                        @Override
                        public void doInBackground(BookmarkTabDao dao) {
                            long newUid = dao.insert(new BookmarkTab(tabName));
                            BookmarkItem newItem = new BookmarkItem(mDiscoverItem);
                            newItem.setTabUid((int) newUid);
                            newItem.setCreatedTime(new SimpleDateFormat("yyyy年MM月dd日", Locale.getDefault()).format(new Date()));
                            newItem.setRating(mChosenRating);
                            mBookmarkItemDao.insert(newItem);
                        }

                        @Override
                        public void onCompleted() {
                            dismiss();
                        }
                    }).execute();
                } else {
                    new LoadDataTask<>(new LoadDataCallback<BookmarkItemDao>() {
                        @Override
                        public BookmarkItemDao getDao() {
                            return ItemDatabase.getBookmarkInstance(mContext).bookmarkDao();
                        }

                        @Override
                        public void doInBackground(BookmarkItemDao dao) {
                            BookmarkItem bookmarkItem = new BookmarkItem(mDiscoverItem);
                            bookmarkItem.setRating(mChosenRating);
                            bookmarkItem.setTabUid(mBookmarkTabs.get(mChosenTab - 1).getUid());
                            bookmarkItem.setCreatedTime(new SimpleDateFormat("yyyy年MM月dd日", Locale.getDefault()).format(new Date()));
                            dao.insert(bookmarkItem);
                        }

                        @Override
                        public void onCompleted() {
                            dismiss();
                        }
                    }).execute();
//                    tabUid = mDiscoverTabs.get(mChosenTab - 1).getUid();
//                    String mTabName = mDiscoverTabs.get(mChosenTab - 1).getTabName();
                }
//                Toast.makeText(mContext, "tab: " + tabUid + ", " + tabName + "; rating: " + mChosenRating, Toast.LENGTH_SHORT).show();
//                dismiss();
        }
    }
}
