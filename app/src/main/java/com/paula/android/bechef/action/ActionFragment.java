package com.paula.android.bechef.action;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.paula.android.bechef.dialog.AlertDialogClickCallback;
import com.paula.android.bechef.dialog.BeChefAlertDialogBuilder;
import com.paula.android.bechef.dialog.MoveToDialogBuilder;
import com.paula.android.bechef.R;

import static com.google.android.gms.common.internal.Preconditions.checkNotNull;

public class ActionFragment extends Fragment implements ActionContract.View, View.OnClickListener {
    private ActionContract.Presenter mPresenter;
    private Context mContext;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_action, container, false);
        mContext = view.getContext();
        view.findViewById(R.id.textview_action_cancel).setOnClickListener(this);
        view.findViewById(R.id.textview_action_move).setOnClickListener(this);
        view.findViewById(R.id.textview_action_delete).setOnClickListener(this);
        return view;
    }

    @Override
    public void onClick(View v) {
        int currentViewId = v.getId();
        int chosenItemsCount = mPresenter.getChosenItemsCount();

        // Show hint or nothing (CANCEL clicked) if click with nothing chosen.
        if (chosenItemsCount == 0) {
            if (currentViewId == R.id.textview_action_cancel) mPresenter.leaveChooseMode();
            else Toast.makeText(getContext(), R.string.toast_action_nothing_chosen, Toast.LENGTH_SHORT).show();
            return;
        }

        // Show AlertDialog if click with something chosen.
        if (currentViewId == R.id.textview_action_move) {
            new MoveToDialogBuilder(mPresenter).setTabs(mPresenter.getOtherTabs())
                    .create().show();
            return;
        }

        String title = ((TextView) v).getText().toString();
        String message = String.format(getString(R.string.msg_action), chosenItemsCount, title);
        AlertDialogClickCallback clickCallback;
        if (currentViewId == R.id.textview_action_cancel) {
            clickCallback = new AlertDialogClickCallback() {
                @Override
                public boolean onPositiveButtonClick() {
                    mPresenter.leaveChooseMode();
                    return true;
                }
            };
        } else if (currentViewId == R.id.textview_action_delete) {
            clickCallback = new AlertDialogClickCallback() {
                @Override
                public boolean onPositiveButtonClick() {
                    mPresenter.deleteData();
                    return true;
                }
            };
        } else {
            return;
        }
        new BeChefAlertDialogBuilder(mContext).setButtons(clickCallback)
                .setMessage(message)
                .setTitle(title)
                .create()
                .show();
    }

    @Override
    public void setCustomMainPresenter(ActionContract.Presenter customMainPresenter) {
        mPresenter = checkNotNull(customMainPresenter);
    }
}
