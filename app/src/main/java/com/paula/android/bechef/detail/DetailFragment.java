package com.paula.android.bechef.detail;

import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.paula.android.bechef.R;
import com.paula.android.bechef.activities.BeChefActivity;
import com.paula.android.bechef.adapters.DetailAdapter;
import com.paula.android.bechef.data.entity.BookmarkItem;
import com.paula.android.bechef.data.SearchItem;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import static android.text.TextUtils.isEmpty;
import static com.google.android.gms.common.internal.Preconditions.checkNotNull;

public class DetailFragment extends Fragment implements DetailContract.View {
    private static final String LOG_TAG = DetailFragment.class.getSimpleName();
    private static final String KEY = "AIzaSyAajGSZR9eHL_IKeV34fd_nN58tYUVf5FQ";

    private DetailContract.Presenter mPresenter;
    private ImageView mIvThumbnail, mIvThumbnailForeground;
    private ImageButton mIbtnBack, mIbtnMore;
    private TextView mTvTags, mTvTitle, mTvTimeOrCount;
    private View mViewDivider;
    private RecyclerView mRvDetail;

//    private TextView mTvContent;

    public DetailFragment() {
        // Required empty public constructor
    }

    public static DetailFragment newInstance() {
        return new DetailFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getActivity() != null) ((BeChefActivity) getActivity()).showBottomNavigationView(false);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_detail, container, false);
        mIvThumbnail = root.findViewById(R.id.imageview_thumbnail);
        mIvThumbnailForeground = root.findViewById(R.id.imageview_thumbnail_foreground);
        mIbtnBack = root.findViewById(R.id.imagebotton_toolbar_back);
        mIbtnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getActivity() != null) getActivity().onBackPressed();
            }
        });
        mIbtnMore = root.findViewById(R.id.imagebutton_toolbar_more);
        mTvTags = root.findViewById(R.id.textview_tags);
        mTvTitle = root.findViewById(R.id.textview_title);
        mTvTimeOrCount = root.findViewById(R.id.textview_time_count);
        mViewDivider = root.findViewById(R.id.view_divider);

        mRvDetail = root.findViewById(R.id.recyclerview_detail);
        mRvDetail.setLayoutManager(new LinearLayoutManager(root.getContext()));
        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mPresenter.start();
    }

    @Override
    public void setPresenter(DetailContract.Presenter presenter) {
        mPresenter = checkNotNull(presenter);
    }

    @Override
    public void showDetailUi(Object content) {
        mViewDivider.setVisibility(View.VISIBLE);
        final BookmarkItem bookmarkItem;
        String className = content.getClass().getSimpleName();
        String timeAndCount = "";
        ArrayList<String> dataString = new ArrayList<>();

        switch (className) {
            case "BookmarkItem":
                bookmarkItem = (BookmarkItem) content;
                timeAndCount = bookmarkItem.getPublishedTime() + "    " + bookmarkItem.getRating() + "分";

                break;
            case "SearchItem":
            default:
                SearchItem detailSearchItem = (SearchItem) content;
                bookmarkItem = new BookmarkItem(detailSearchItem);
                timeAndCount = getFormatDate(detailSearchItem.getPublishedAt())+ " • "
                        + "觀看次數 : " + getFormatCount(detailSearchItem.getViewCount()) + "次";
//                mIvThumbnailForeground.setVisibility(View.GONE);
                break;
        }
        dataString.add(bookmarkItem.getDescription());
        DetailAdapter detailAdapter = new DetailAdapter(dataString, null, mPresenter);
        mRvDetail.setAdapter(detailAdapter);
        Picasso.with(getContext())
                .load(bookmarkItem.getImageUrl())
                .error(R.drawable.all_picture_placeholder)
                .placeholder(R.drawable.all_picture_placeholder)
                .into(mIvThumbnail);
        mIvThumbnailForeground.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO: open fullscreen landscape YouTubePlayerView
                Toast.makeText(getContext(), bookmarkItem.getVideoId(), Toast.LENGTH_SHORT).show();
            }
        });
        mTvTitle.setText(bookmarkItem.getTitle());
        mTvTags.setText(bookmarkItem.getTags());
        mTvTimeOrCount.setText(timeAndCount);
    }

    private String getFormatDate(String originalTime) {
        String dateStr = "";
        if (isEmpty(originalTime)) {
            return dateStr;
        }
        String[] timeArray = originalTime.split("[-T]");
        dateStr = timeArray[0] + "年" + timeArray[1] + "月" + timeArray[2] + "日";
        return dateStr;
    }

    private String getFormatCount(String originalCount) {
        String numStr = "";
        if (isEmpty(originalCount)) {
            return numStr;
        }
        NumberFormat nf = NumberFormat.getInstance();
        try {
            DecimalFormat df = new DecimalFormat("#,###");
            numStr = df.format(nf.parse(originalCount));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return numStr;
    }
}