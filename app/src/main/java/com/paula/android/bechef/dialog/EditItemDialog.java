package com.paula.android.bechef.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.paula.android.bechef.adapters.EditItemAdapter;
import com.paula.android.bechef.data.entity.BaseItem;
import com.paula.android.bechef.data.entity.BookmarkItem;
import com.paula.android.bechef.data.entity.RecipeItem;
import com.paula.android.bechef.detail.DetailContract;
import com.paula.android.bechef.R;
import com.paula.android.bechef.utils.Constants;
import com.paula.android.bechef.utils.EditCallback;
import com.paula.android.bechef.utils.Utils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static android.content.pm.PackageManager.PERMISSION_GRANTED;
import static android.Manifest.permission.CAMERA;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;
import static android.provider.MediaStore.ACTION_IMAGE_CAPTURE;
import static android.provider.MediaStore.EXTRA_OUTPUT;
import static android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
import static android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS;
import static androidx.core.content.ContextCompat.checkSelfPermission;
import static androidx.core.content.FileProvider.getUriForFile;

public class EditItemDialog extends DialogFragment implements View.OnClickListener,
        AlertDialogClickCallback, EditCallback {
    private final RecipeItem mRecipeItem;
    private Context mContext;
    private DetailContract.Presenter mDetailPresenter;
    private EditItemAdapter mEditItemAdapter;
    private RecyclerView mRecyclerView;
    private String mCurrentPhotoPath = "";
    private int mStepPosition, mImagePosition = Constants.VIEW_TYPE_IMAGE;

    public EditItemDialog(BaseItem baseItem) {
        if (baseItem instanceof RecipeItem) {
            mRecipeItem = new RecipeItem((RecipeItem) baseItem);
        } else {
            mRecipeItem = new RecipeItem((BookmarkItem) baseItem);
        }
    }

    public EditItemDialog setDetailPresenter(DetailContract.Presenter detailPresenter) {
        mDetailPresenter = detailPresenter;
        return this;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NO_TITLE, R.style.FullDialogFragmentTheme);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_edit_item, container, false);
        mContext = view.getContext();
        view.findViewById(R.id.imagebutton_toolbar_back).setOnClickListener(this);
        view.findViewById(R.id.imagebutton_toolbar_complete).setOnClickListener(this);
        setRecyclerView(view);
        setCancelable(false);
        return view;
    }

    private void setRecyclerView(View view) {
        mRecyclerView = view.findViewById(R.id.recyclerview_edit_tab);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        mEditItemAdapter = new EditItemAdapter(mRecipeItem, this);
        mEditItemAdapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onItemRangeInserted(int positionStart, int itemCount) {
                mRecyclerView.smoothScrollToPosition(positionStart);
            }
        });
        mRecyclerView.setAdapter(mEditItemAdapter);
        mRecyclerView.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(@NonNull Rect outRect,
                                       @NonNull View view,
                                       @NonNull RecyclerView parent,
                                       @NonNull RecyclerView.State state) {
                super.getItemOffsets(outRect, view, parent, state);
                if (parent.getChildAdapterPosition(view) <= Constants.INFO_SIZE) {
                    outRect.top = 0;
                } else if (outRect.top == 0) {
                    outRect.top = Utils.convertDpToPixel(Constants.NORMAL_PADDING, mContext);
                }
            }
        });
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                if ((keyCode == KeyEvent.KEYCODE_BACK) && event.getAction() != KeyEvent.ACTION_DOWN) {
                    createLeaveAlertDialog();
                    return true;
                }
                return false;
            }
        });
        return dialog;
    }

    @Override
    public void onClick(View v) {
        int currentViewId = v.getId();
        if (currentViewId == R.id.imagebutton_toolbar_back) {
            createLeaveAlertDialog();
        } else if (currentViewId == R.id.imagebutton_toolbar_complete) {
            mEditItemAdapter.onCompleteClicked();
        }
    }

    private void createLeaveAlertDialog() {
        new BeChefAlertDialogBuilder(mContext).setButtons(this)
                .setTitle(getString(R.string.cancel_edit_title))
                .setMessage(R.string.cancel_edit_msg).create().show();
        // TODO: Ask if wanna save temp data here.
        //  Save temp data in onStop() and onPositiveButtonClick().
        //  Load temp data in OnStart() if specified.
    }

    @Override
    public boolean onPositiveButtonClick() {
        dismiss();
        return true;
    }

    @Override
    public boolean isFromBookmark() {
        return getTag().equals(BookmarkItem.class.getSimpleName());
    }

    @Override
    public void onSaveDataComplete(BaseItem baseItem) {
        if (mDetailPresenter != null) mDetailPresenter.refreshData(baseItem);
        dismiss();
    }

    @Override
    public void onChooseImages(final int stepPosition, final int imagePosition) {
        mStepPosition = stepPosition;
        mImagePosition = imagePosition;
        new BeChefAlertDialogBuilder(mContext).setItems(R.array.image_resource_item
                , new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                List<String> imageResources = Arrays.asList(getResources()
                        .getStringArray(R.array.image_resource_item));
                if (which == imageResources.indexOf(getString(R.string.image_resource_remove))) {
                    setImage("");
                    return;
                }
                if (which == imageResources.indexOf(getString(R.string.image_resource_from_camera))) {
                    askPermissions();
                } else {
                    Intent gallery = new Intent(Intent.ACTION_PICK, EXTERNAL_CONTENT_URI);
                    startActivityForResult(gallery, Constants.GALLERY_REQUEST_CODE);
                }
            }
        }).setTitle(R.string.image_resource_title).create().show();
    }

    @Override
    public void scrollByY(int y) {
        mRecyclerView.smoothScrollBy(0, y);
    }

    @Override
    public void scrollToBottom() {
        mRecyclerView.smoothScrollToPosition(mEditItemAdapter.getItemCount());
    }

    private void askPermissions() {
        List<String> listPermissionsNeeded = new ArrayList<>();
        addPermissionCheckResults(listPermissionsNeeded, CAMERA, WRITE_EXTERNAL_STORAGE);
        if (!listPermissionsNeeded.isEmpty()) {
            requestPermissions(listPermissionsNeeded.toArray(new String[0]),
                    Constants.REQUEST_ID_MULTIPLE_PERMISSIONS);
        } else {
            dispatchTakePictureIntent();
        }
    }

    private void addPermissionCheckResults(List<String>listPermissionsNeeded, String... permissions) {
        for (String permission : permissions) {
            if (checkSelfPermission(mContext, permission) != PERMISSION_GRANTED) {
                listPermissionsNeeded.add(permission);
            }
        }
    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(mContext.getPackageManager()) != null) {
            File photoFile;
            photoFile = createImageFile();
            Uri photoURI = getUriForFile(mContext,
                    Constants.FILE_PROVIDER_AUTHORITY,
                    photoFile);
            takePictureIntent.putExtra(EXTRA_OUTPUT, photoURI);
            startActivityForResult(takePictureIntent, Constants.CAMERA_REQUEST_CODE);
        }
    }

    private File createImageFile() {
        File storageDir = new File(Environment
                .getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "BeChef");
        if (!storageDir.exists() && !storageDir.mkdir()) {
            Toast.makeText(mContext, R.string.toast_create_folder_error, Toast.LENGTH_LONG).show();
        }

        File imageFile = null;
        try {
            imageFile = File.createTempFile(
                    String.valueOf(System.currentTimeMillis()),
                    Constants.IMAGE_FILE_SUFFIX,
                    storageDir);
            mCurrentPhotoPath = imageFile.getAbsolutePath();
        } catch (IOException e) {
            Toast.makeText(mContext, R.string.toast_create_file_error, Toast.LENGTH_LONG).show();
        }
        return imageFile;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == Constants.CAMERA_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            setImage(String.valueOf(Uri.fromFile(new File(mCurrentPhotoPath))));

//            // Show album in Camera app
//            Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
//            mediaScanIntent.setData(Uri.fromFile(new File(mCurrentPhotoPath)));
//            mContext.sendBroadcast(mediaScanIntent);
        } else if (requestCode == Constants.GALLERY_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            setImage(String.valueOf(data.getData()));
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode == Constants.REQUEST_ID_MULTIPLE_PERMISSIONS && grantResults.length > 0) {
            boolean isAllPermissionsGranted = true;
            boolean isAnyPermissionRejected = false;
            for (int i = 0; i < permissions.length; i++) {
                if (grantResults[i] != PERMISSION_GRANTED) {
                    isAllPermissionsGranted = false;
                    if (!shouldShowRequestPermissionRationale(permissions[i])) {
                        isAnyPermissionRejected = true;
                    }
                }
            }
            if (isAllPermissionsGranted) {
                dispatchTakePictureIntent();
            } else if (isAnyPermissionRejected) {
                openAppSettingsIntent();
            } else {
                Toast.makeText(mContext, getString(R.string.toast_require_camera_permission), Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void openAppSettingsIntent() {
        Intent intent = new Intent(ACTION_APPLICATION_DETAILS_SETTINGS,
                Uri.fromParts("package", mContext.getPackageName(), null));
        startActivity(intent);
    }

    private void setImage(String urlString) {
        // Set thumbnail image for RecipeItem
        if (mStepPosition == Constants.VIEW_TYPE_IMAGE) {
            mRecipeItem.setImageUrl(urlString);
            mEditItemAdapter.notifyItemChanged(0);
            return;
        }

        // Set step image for RecipeItem
        RecyclerView.ViewHolder editStepViewHolder = mRecyclerView.findViewHolderForAdapterPosition(
                1 + Constants.INFO_SIZE + mRecipeItem.getMaterialGroups().size() + mStepPosition);
        if (editStepViewHolder == null) return;
        if ("".equals(urlString)) {
            ((EditItemAdapter.EditStepsViewHolder) editStepViewHolder)
                    .removeSpecifiedStepImage(mImagePosition);
        } else {
            ((EditItemAdapter.EditStepsViewHolder) editStepViewHolder)
                    .setSpecifiedStepImage(mImagePosition, urlString);
        }
    }
}
