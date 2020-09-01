package com.paula.android.bechef.detail;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.Toast;

import com.paula.android.bechef.R;
import com.paula.android.bechef.activities.BeChefActivity;
import com.paula.android.bechef.adapters.DetailAdapter;
import com.paula.android.bechef.data.entity.BookmarkItem;
import com.paula.android.bechef.data.entity.DiscoverItem;
import com.paula.android.bechef.data.entity.ReceiptItem;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import static com.google.android.gms.common.internal.Preconditions.checkNotNull;

public class DetailFragment extends Fragment implements DetailContract.View {
    private DetailContract.Presenter mPresenter;
    private RecyclerView mRvDetail;
    private ImageButton mIbtnMore;
    private boolean mIsBottomShown;

    private DetailFragment(boolean isBottomShown) {
        mIsBottomShown = isBottomShown;
    }

    public static DetailFragment newInstance(boolean isBottomShown) {
        return new DetailFragment(isBottomShown);
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
        ImageButton ibtnBack = root.findViewById(R.id.imagebutton_toolbar_back);
        ibtnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getActivity() != null) getActivity().onBackPressed();
            }
        });
        mIbtnMore = root.findViewById(R.id.imagebutton_toolbar_more);
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
    public void showDetailUi(final Object content) {
        DetailAdapter detailAdapter;
        if (content instanceof DiscoverItem)
            detailAdapter = new DetailAdapter((DiscoverItem) content, mPresenter);
        else if (content instanceof BookmarkItem)
            detailAdapter = new DetailAdapter((BookmarkItem) content, mPresenter);
        else
            detailAdapter = new DetailAdapter((ReceiptItem) content, mPresenter);
        mRvDetail.setAdapter(detailAdapter);

        mIbtnMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), content.getClass().getSimpleName(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onDestroy() {
        if (getActivity() != null)
            ((BeChefActivity) getActivity()).showBottomNavigationView(mIsBottomShown);
        super.onDestroy();
    }
}