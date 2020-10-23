package com.paula.android.bechef.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.paula.android.bechef.R;
import com.paula.android.bechef.adapters.EditReceiptItemAdapter;
import com.paula.android.bechef.data.entity.BaseItem;
import com.paula.android.bechef.data.entity.ReceiptItem;
import com.paula.android.bechef.detail.DetailContract;
import com.paula.android.bechef.utils.Utils;

import java.io.File;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import static android.app.Activity.RESULT_OK;
import static com.paula.android.bechef.adapters.EditReceiptItemAdapter.DESCRIPTION_SIZE;

public class EditReceiptItemDialog extends DialogFragment implements View.OnClickListener, AlertDialogClickCallback, EditCompleteCallback {
    private static final int IMAGE_PICK = 0;
    private Context mContext;
    private ReceiptItem mReceiptItem;
    private EditReceiptItemAdapter mEditReceiptItemAdapter;
    private DetailContract.Presenter mDetailPresenter;
    private RecyclerView mRecyclerView;
    private Uri outputFileUri;
    private int mEditAdapterPosition = -1;
    private int mStepAdapterPosition = -1;

    public EditReceiptItemDialog(ReceiptItem receiptItem) {
        mReceiptItem = new ReceiptItem(receiptItem);
    }

    public EditReceiptItemDialog(BaseItem baseItem, DetailContract.Presenter detailPresenter) {
        mReceiptItem = new ReceiptItem((ReceiptItem) baseItem);
        mDetailPresenter = detailPresenter;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NO_TITLE, R.style.MyDialogTheme1);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.edit_item_dialog, container, false);
        mContext = view.getContext();
        view.findViewById(R.id.imagebutton_toolbar_back).setOnClickListener(this);
        view.findViewById(R.id.imagebutton_toolbar_complete).setOnClickListener(this);

        mRecyclerView = view.findViewById(R.id.recyclerview_edit);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        mEditReceiptItemAdapter = new EditReceiptItemAdapter(mReceiptItem, this);
        mEditReceiptItemAdapter.setDialogTag(getTag());
        mRecyclerView.addItemDecoration(dec);
        mRecyclerView.setAdapter(mEditReceiptItemAdapter);
        setCancelable(true);
        return view;
    }

    private RecyclerView.ItemDecoration dec = new RecyclerView.ItemDecoration() {
        @Override
        public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
            super.getItemOffsets(outRect, view, parent, state);
            if (outRect.top == 0) outRect.top = (int) Utils.convertDpToPixel((float) 2, mContext);

            if (parent.getChildAdapterPosition(view) < DESCRIPTION_SIZE) outRect.top = 0;
        }
    };

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
            @Override
            public boolean onKey(android.content.DialogInterface dialog, int keyCode, android.view.KeyEvent event) {
                if ((keyCode == android.view.KeyEvent.KEYCODE_BACK) && event.getAction() != KeyEvent.ACTION_DOWN) {
                    createLeaveAlertDialog();
                    return true;
                } else
                    return false;
            }
        });
        return dialog;
    }

    private void createLeaveAlertDialog() {
        BeChefAlertDialogBuilder builder = new BeChefAlertDialogBuilder(mContext);
        builder.setButtons(this).setTitle("取消編輯").setMessage("是否要取消編輯？").create().show();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.imagebutton_toolbar_back:
                createLeaveAlertDialog();
                break;
            case R.id.imagebutton_toolbar_complete:
                mEditReceiptItemAdapter.onCompleteClicked();
                break;
        }
    }

    @Override
    public void onPositiveButtonClick() {
        dismiss();
    }

    @Override
    public void onEditComplete(ReceiptItem receiptItem) {
        if (mDetailPresenter != null) mDetailPresenter.refreshData(receiptItem);
        dismiss();
    }

    @Override
    public void onInsertComplete(int position) {
        mRecyclerView.smoothScrollToPosition(position);
    }

    @Override
    public void onChooseImages(int editAdapterPosition, int stepAdapterPosition) {
        mEditAdapterPosition = editAdapterPosition;
        mStepAdapterPosition = stepAdapterPosition;
        BeChefAlertDialogBuilder builder = new BeChefAlertDialogBuilder(mContext);
        builder.setCustomItems(new AlertDialogItemsCallback() {
            @Override
            public String[] getItems() {
                return new String[]{"從相機", "從相簿", "移除"};
            }

            @Override
            public DialogInterface.OnClickListener getItemOnClickListener() {
                return new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (which == 2) {
                            setImage("");
                            mEditReceiptItemAdapter.updateData(mReceiptItem);
                            return;
                        }
                        Intent intent;
                        if (which == 0) {
                            intent = new Intent(
                                    android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                            File tmpFile = new File(
                                    Environment.getExternalStorageDirectory(), System.currentTimeMillis() + ".jpg");
                            outputFileUri = Uri.fromFile(tmpFile);
                            intent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);
                        } else {
                            intent = new Intent(Intent.ACTION_PICK, null);
                            intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
                        }
                        startActivityForResult(intent, IMAGE_PICK);
                    }
                };
            }
        }).setTitle("請選擇相片來源").create().show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK) return;
        if (data != null && data.getData() != null) outputFileUri = data.getData();
        setImage(outputFileUri.toString());
        mEditReceiptItemAdapter.updateData(mReceiptItem);
    }

    private void setImage(String urlString) {
        if (mEditAdapterPosition == -1) {
            mReceiptItem.setImageUrl(urlString);
        } else if ("".equals(urlString)) {
            mReceiptItem.getSteps().get(mEditAdapterPosition).getImageUrls().remove(mStepAdapterPosition);
        } else {
            mReceiptItem.getSteps().get(mEditAdapterPosition).getImageUrls()
                    .set(mStepAdapterPosition, urlString);
        }
    }
}
