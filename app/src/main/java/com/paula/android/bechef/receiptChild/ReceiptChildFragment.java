package com.paula.android.bechef.receiptChild;

import android.content.Context;
import android.graphics.Rect;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.paula.android.bechef.R;
import com.paula.android.bechef.activities.BeChefActivity;
import com.paula.android.bechef.adapters.DefaultChildAdapter;
import com.paula.android.bechef.data.entity.BaseItem;
import com.paula.android.bechef.data.entity.BaseTab;
import com.paula.android.bechef.data.entity.ReceiptTab;
import com.paula.android.bechef.receipt.ReceiptFragment;
import com.paula.android.bechef.utils.Utils;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import static com.google.android.gms.common.internal.Preconditions.checkNotNull;

public class ReceiptChildFragment extends Fragment implements ReceiptChildFragmentContract.View {
    private ReceiptFragment mReceiptFragment;
    private ReceiptChildFragmentContract.Presenter mPresenter;
    private Context mContext;
    private DefaultChildAdapter mDefaultChildAdapter;
    private RecyclerView mRecyclerView;
    private Boolean mIsSelectable = false;
    private TextView mTvInfoDescription;


//    private ReceiptChildFragment(int tabIndex, Fragment fragment) {
//        if (mPresenter == null) {
//            mPresenter = new ReceiptChildPresenter(this, tabIndex);
//        }
//        mReceiptFragment = (ReceiptFragment) fragment;
//    }
//
//    public static ReceiptChildFragment newInstance(int tabIndex, Fragment fragment) {
//        return new ReceiptChildFragment(tabIndex, fragment);
//    }
    private ReceiptChildFragment(ReceiptTab receiptTab, Fragment fragment) {
    if (mPresenter == null) {
        mPresenter = new ReceiptChildPresenter(this, receiptTab);
    }
    mReceiptFragment = (ReceiptFragment) fragment;
}

    public static ReceiptChildFragment newInstance(ReceiptTab receiptTab, Fragment fragment) {
        return new ReceiptChildFragment(receiptTab, fragment);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_child, container, false);
        mContext = view.getContext();
        mTvInfoDescription = view.findViewById(R.id.textview_info_description);

        mRecyclerView = view.findViewById(R.id.recyclerview_discover_main);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        if (mRecyclerView.getItemDecorationCount() == 0) mRecyclerView.addItemDecoration(dec);
        mDefaultChildAdapter = new DefaultChildAdapter(mIsSelectable, new ArrayList<BaseItem>(), mPresenter);
        mRecyclerView.setAdapter(mDefaultChildAdapter);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mPresenter.start();
    }

    private RecyclerView.ItemDecoration dec = new RecyclerView.ItemDecoration() {
        @Override
        public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
            super.getItemOffsets(outRect, view, parent, state);
            if (outRect.top == 0) outRect.top = (int) Utils.convertDpToPixel((float) 8, mContext);

            if (parent.getChildAdapterPosition(view) == 0) outRect.top = 0;
        }
    };

    @Override
    public void setPresenter(ReceiptChildFragmentContract.Presenter presenter) {
        mPresenter = checkNotNull(presenter);
    }

    @Override
    public void showDetailUi(Object content, boolean isBottomShown) {
        ((BeChefActivity) mContext).transToDetail(content, isBottomShown);
    }

    @Override
    public void showSelectableUi(boolean isSelectable) {

    }

    @Override
    public void updateItems(ArrayList<?> items) {
        mDefaultChildAdapter.updateData(items);
        mTvInfoDescription.setText("共 " + items.size() + " 道");
    }

    @Override
    public void refreshData() {

    }
}
