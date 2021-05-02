package com.paula.android.bechef.customChild;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Rect;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.paula.android.bechef.activities.BeChefActivity;
import com.paula.android.bechef.adapters.CustomChildAdapter;
import com.paula.android.bechef.BaseMainFragment;
import com.paula.android.bechef.ChildContract;
import com.paula.android.bechef.customMain.CustomMainFragment;
import com.paula.android.bechef.dialog.BeChefAlertDialogBuilder;
import com.paula.android.bechef.R;
import com.paula.android.bechef.utils.Constants;
import com.paula.android.bechef.utils.Utils;

import java.util.ArrayList;

import static com.google.android.gms.common.internal.Preconditions.checkNotNull;

public class CustomChildFragment<I> extends BaseMainFragment implements ChildContract.CustomChildView<I> {
    private final CustomMainFragment mCustomMainFragment;
    private CustomChildAdapter<I> mCustomChildAdapter;
    private TextView mTvInfoDescription;
    private ImageButton mIbtnFilter;
    protected ChildContract.CustomChildPresenter mCustomChildPresenter;
    protected Context mContext;

    public CustomChildFragment(Fragment fragment) {
        mCustomMainFragment = (CustomMainFragment) fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_child, container, false);
        mContext = view.getContext();
        mTvInfoDescription = view.findViewById(R.id.textview_total_description);
        mIbtnFilter = view.findViewById(R.id.imagebutton_filter);
        mIbtnFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onIbtnFilterClick();
            }
        });
        setRecyclerView(view);
        return view;
    }

    private void onIbtnFilterClick() {
        String[] itemString = getResources().getStringArray(R.array.filter_type);
        int currentFilterIndex = mCustomChildPresenter.getDataFilterType();
        itemString[currentFilterIndex] = getString(R.string.star) + itemString[currentFilterIndex];
        new BeChefAlertDialogBuilder(mContext).setTitle(getString(R.string.filter_with))
                .setItems(itemString, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int index) {
                        int currentFilterIndex = mCustomChildPresenter.getDataFilterType();
                        if (index != currentFilterIndex) {
                            mCustomChildPresenter.setDataFilterType(index);
                            mCustomChildPresenter.loadSpecificItems(index);
                        }
                    }
                }).create().show();
    }

    private void setRecyclerView(View view) {
        RecyclerView recyclerView = view.findViewById(R.id.recyclerview_child);
        recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        if (recyclerView.getItemDecorationCount() == 0)
            recyclerView.addItemDecoration(new RecyclerView.ItemDecoration() {
                @Override
                public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
                    super.getItemOffsets(outRect, view, parent, state);
                    if (outRect.top == 0) {
                        outRect.top = Utils.convertDpToPixel(Constants.NORMAL_PADDING, mContext);
                    }
                    if (parent.getChildAdapterPosition(view) == 0) outRect.top = 0;
                }
            });
        mCustomChildAdapter = new CustomChildAdapter<>(mIsSelectable, new ArrayList<I>(), mCustomChildPresenter);
        recyclerView.setAdapter(mCustomChildAdapter);
        recyclerView.setHasFixedSize(true);
    }

    @Override
    public void onStart() {
        super.onStart();
        mCustomChildPresenter.start();
    }

    @Override
    public void setCustomMainPresenter(ChildContract.CustomChildPresenter customMainPresenter) {
        mCustomChildPresenter = checkNotNull(customMainPresenter);
    }

    @Override
    public void showDetailUi(Object content, boolean isBottomShown) {
        ((BeChefActivity) mContext).showDetailUi(content, !mIsSelectable);
    }

    @Override
    public void showSelectableUi(boolean isSelectable) {
        if (mIsSelectable == isSelectable) return;
        mIsSelectable = isSelectable;
        mCustomChildAdapter.setSelectable(mIsSelectable);
        mIbtnFilter.setVisibility(mIsSelectable ? View.GONE : View.VISIBLE);
        mCustomMainFragment.showSelectable(mIsSelectable);
    }

    @Override
    public void updateItems(ArrayList<I> items) {
        mCustomChildAdapter.updateData(items);
        mTvInfoDescription.setText(String.format(getString(R.string.sum_info_msg), items.size()));
    }

    @Override
    public ArrayList<Long> getChosenItemUids() {
        return mCustomChildAdapter.getChosenItemUids();
    }
}
