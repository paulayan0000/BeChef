package com.paula.android.bechef.receipt;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.paula.android.bechef.R;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class ReceiptFragment extends Fragment implements ReceiptContract.View {

    private ReceiptContract.Presenter mPresenter;

    public ReceiptFragment() {
        // Required empty public constructor
    }

    public static ReceiptFragment newInstance() {
        return new ReceiptFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_receipt, container, false);
    }

    @Override
    public void setPresenter(ReceiptContract.Presenter presenter) {

    }
}