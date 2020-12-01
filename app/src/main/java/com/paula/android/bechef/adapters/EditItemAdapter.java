package com.paula.android.bechef.adapters;

import android.content.Context;
import android.graphics.Rect;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.paula.android.bechef.R;
import com.paula.android.bechef.data.LoadDataCallback;
import com.paula.android.bechef.data.LoadDataTask;
import com.paula.android.bechef.data.MaterialGroup;
import com.paula.android.bechef.data.Step;
import com.paula.android.bechef.data.database.ItemDatabase;
import com.paula.android.bechef.data.entity.BaseItem;
import com.paula.android.bechef.data.entity.BookmarkItem;
import com.paula.android.bechef.data.entity.ReceiptItem;
import com.paula.android.bechef.utils.BeChefTextWatcher;
import com.paula.android.bechef.utils.EditCallback;
import com.paula.android.bechef.utils.EditTextChangeCallback;
import com.paula.android.bechef.utils.Utils;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import static com.paula.android.bechef.utils.Constants.DESCRIPTION_SIZE;
import static com.paula.android.bechef.utils.Constants.VIEW_TYPE_BODY;
import static com.paula.android.bechef.utils.Constants.VIEW_TYPE_FOOT;
import static com.paula.android.bechef.utils.Constants.VIEW_TYPE_HEAD;
import static com.paula.android.bechef.utils.Constants.VIEW_TYPE_IMAGE;

public class EditItemAdapter extends RecyclerView.Adapter implements EditTextChangeCallback {
    private ArrayList<String> mDescriptionNames
            = new ArrayList<>(Arrays.asList("標籤", "標題", "評分", "耗時", "份量", "說明"));
    private ArrayList<String> mDescriptionHints
            = new ArrayList<>(Arrays.asList("#氣炸鍋 #烤箱", "宮保雞丁，必填", "", "1 小時 30 分鐘",
            "1 ，輸入整數，單位為人份", "詳細說明"));

    private ReceiptItem mReceiptItem;
    private Context mContext;
    private EditCallback<BaseItem> mCompleteCallback;
    private String mDialogTag;
    private RecyclerView.ItemDecoration dec = new RecyclerView.ItemDecoration() {
        @Override
        public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
            super.getItemOffsets(outRect, view, parent, state);
            if (outRect.bottom == 0)
                outRect.bottom = (int) Utils.convertDpToPixel((float) 8, mContext);
        }
    };

    public EditItemAdapter(ReceiptItem receiptItem, EditCallback<BaseItem> completeCallback) {
        mReceiptItem = receiptItem;
        mCompleteCallback = completeCallback;
        mDialogTag = mCompleteCallback.getDialogTag();

        if (!mDialogTag.equals("edit_bookmark")) {
            if (receiptItem.getMaterialGroups().size() == 0) {
                mReceiptItem.getMaterialGroups().add(new MaterialGroup("準備材料", new ArrayList<String>()));
            }

            if (receiptItem.getSteps().size() == 0) {
                mReceiptItem.getSteps().add(new Step("", new ArrayList<String>()));
            }
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view;
        switch (viewType) {
            case VIEW_TYPE_IMAGE:
                return new ImageViewHolder(inflater.inflate(R.layout.item_edit_images, parent, false));
            case VIEW_TYPE_HEAD:
                view = inflater.inflate(R.layout.item_edit_descriptions, parent, false);
                return new DescriptionViewHolder(view, new BeChefTextWatcher(this));
            case VIEW_TYPE_BODY:
                view = inflater.inflate(R.layout.item_edit_materials, parent, false);
                return new MaterialsViewHolder(view, new BeChefTextWatcher(this));
            default:
                view = inflater.inflate(R.layout.item_edit_steps, parent, false);
                return new StepsViewHolder(view, new BeChefTextWatcher(this));
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        switch (holder.getItemViewType()) {
            case VIEW_TYPE_IMAGE:
                ((ImageViewHolder) holder).bindView();
                break;
            case VIEW_TYPE_HEAD:
                ((DescriptionViewHolder) holder).bindView(position);
                break;
            case VIEW_TYPE_BODY:
                ((MaterialsViewHolder) holder).bindView(position);
                break;
            default:
                ((StepsViewHolder) holder).bindView(position);
                break;
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) return VIEW_TYPE_IMAGE;
        else if (position <= DESCRIPTION_SIZE) return VIEW_TYPE_HEAD;
        else if (position <= DESCRIPTION_SIZE + mReceiptItem.getMaterialGroups().size())
            return VIEW_TYPE_BODY;
        else return VIEW_TYPE_FOOT;
    }

    @Override
    public int getItemCount() {
        if (mDialogTag.equals("edit_bookmark"))
            return 1 + DESCRIPTION_SIZE - 2;
        else
            return 1 + DESCRIPTION_SIZE
                    + mReceiptItem.getMaterialGroups().size()
                    + mReceiptItem.getSteps().size();
    }

    public void onCompleteClicked() {
        if ("".equals(mReceiptItem.getTitle())) {
            Toast.makeText(mContext, "請輸入標題", Toast.LENGTH_SHORT).show();
            return;
        }
        if (!mDialogTag.equals("edit_bookmark")) {
            if (isMaterialEmpty()) {
                Toast.makeText(mContext, "請輸入準備材料", Toast.LENGTH_SHORT).show();
                return;
            }
            if (isStepEmpty()) {
                Toast.makeText(mContext, "請輸入步驟", Toast.LENGTH_SHORT).show();
                return;
            }
            // replace empty group name and remove empty content
            ArrayList<MaterialGroup> materialGroups = mReceiptItem.getMaterialGroups();
            for (MaterialGroup group : materialGroups) {
                group.getMaterialContents().removeAll(Collections.singleton(""));
                if ("".equals(group.getGroupName())) {
                    if (group.getMaterialContents().size() == 0) materialGroups.remove(group);
                    group.setGroupName("未命名類別");
                }
            }
            mReceiptItem.setMaterialGroups(materialGroups);

            // remove image if url is empty
            ArrayList<Step> steps = mReceiptItem.getSteps();
            for (Step step : steps) {
                step.getImageUrls().removeAll(Collections.singleton(""));
                if (step.getStepDescription().isEmpty() && step.getImageUrls().size() == 0)
                    steps.remove(step);
            }
            mReceiptItem.setSteps(steps);
        }
        new LoadDataTask<>(new LoadDataCallback<ItemDatabase>() {
            private BookmarkItem mBookmarkItem;

            @Override
            public ItemDatabase getDao() {
                if (mDialogTag.equals("edit_bookmark"))
                    return ItemDatabase.getBookmarkInstance(mContext);
                else
                    return ItemDatabase.getReceiptInstance(mContext);
            }

            @Override
            public void doInBackground(ItemDatabase database) {
                switch (mDialogTag) {
                    case "edit_receipt":
                        database.receiptDao().updateItem(mReceiptItem);
                        break;
                    case "new":
                        database.receiptDao().insert(mReceiptItem);
                        break;
                    case "edit_bookmark":
                        mBookmarkItem = new BookmarkItem(mReceiptItem);
                        database.bookmarkDao().updateItem(mBookmarkItem);
                        break;
                }
            }

            @Override
            public void onCompleted() {
                // TODO: hide saving mask view
                if (mBookmarkItem != null)
                    mCompleteCallback.onSaveDataComplete(mBookmarkItem);
                else
                    mCompleteCallback.onSaveDataComplete(mReceiptItem);
            }
        }).execute();
    }

    private boolean isMaterialEmpty() {
        ArrayList<String> materialContents = new ArrayList<>(mReceiptItem.getMaterialGroups().get(0).getMaterialContents());
        materialContents.removeAll(Collections.singleton(""));
        return materialContents.size() == 0;
    }

    private boolean isStepEmpty() {
        ArrayList<Step> steps = mReceiptItem.getSteps();
        for (Step step : steps) {
            if (!step.getStepDescription().isEmpty()) return false;
        }
        return true;
    }

    @Override
    public void afterTextChanged(int position, String textContent) {
        if (position <= DESCRIPTION_SIZE) {
            mReceiptItem.setParams(position - 1, textContent);
            return;
        }
        if (position <= DESCRIPTION_SIZE + mReceiptItem.getMaterialGroups().size()) {
            mReceiptItem.getMaterialGroups().get(position - DESCRIPTION_SIZE - 1).setGroupName(textContent);
            return;
        }
        mReceiptItem.getSteps()
                .get(position - DESCRIPTION_SIZE - 1 - mReceiptItem.getMaterialGroups().size())
                .setStepDescription(textContent);
    }

    public void updateData(ReceiptItem receiptItem) {
        mReceiptItem = receiptItem;
        notifyDataSetChanged();
    }

    private void notifyAdded(int currentPosition) {
        notifyItemInserted(currentPosition + 1);
        notifyItemRangeChanged(currentPosition + 1, getItemCount() - currentPosition - 1);
        mCompleteCallback.onInsertComplete(currentPosition + 1);
    }

    private void notifyRemoved(int currentPosition) {
        notifyItemRemoved(currentPosition);
        notifyItemRangeChanged(currentPosition, getItemCount() - currentPosition);
    }

    private class ImageViewHolder extends RecyclerView.ViewHolder {
        private ImageView mIvThumbNail;

        ImageViewHolder(@NonNull View itemView) {
            super(itemView);
            mIvThumbNail = itemView.findViewById(R.id.imageview_thumbnail);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mCompleteCallback.onChooseImages(-1, -1);
                }
            });
        }

        void bindView() {
            String imageUrl = mReceiptItem.getImageUrl();
            Picasso.with(mContext)
                    .load(imageUrl.isEmpty() ? null : imageUrl)
                    .error(R.drawable.all_picture_placeholder)
                    .placeholder(R.drawable.all_picture_placeholder)
                    .into(mIvThumbNail);
        }
    }

    private class DescriptionViewHolder extends RecyclerView.ViewHolder {
        private TextView mTvDataName;
        private ImageButton mIbtnClear;
        private RatingBar mRatingBar;
        private EditText mEtDataContent;
        private BeChefTextWatcher mTextWatcher;

        DescriptionViewHolder(@NonNull View itemView, BeChefTextWatcher textWatcher) {
            super(itemView);
            mTvDataName = itemView.findViewById(R.id.textview_data_name);
            mRatingBar = itemView.findViewById(R.id.rating_bar);
            mEtDataContent = itemView.findViewById(R.id.edittext_data_content);
            mTextWatcher = textWatcher;
            mEtDataContent.addTextChangedListener(mTextWatcher);
            mIbtnClear = itemView.findViewById(R.id.imagebutton_clear);
            mIbtnClear.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if (position < 0) return;
                    mReceiptItem.setParams(position - 1, "");
                    notifyItemChanged(position);
                }
            });
        }

        void bindView(int position) {
            if (mDialogTag.equals("edit_bookmark") && position == DESCRIPTION_SIZE - 2) position += 2;
            mTvDataName.setText(mDescriptionNames.get(position - 1));
            mRatingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
                @Override
                public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                    mReceiptItem.setRating(rating);
                }
            });

            if (position - 1 == 4)
                // SetInputType for weight
                mEtDataContent.setInputType(InputType.TYPE_CLASS_NUMBER);
            else if (position - 1 == 5)
                // SetInputType for description
                mEtDataContent.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_MULTI_LINE);
            else mEtDataContent.setInputType(InputType.TYPE_CLASS_TEXT);

            if (position - 1 == 2) {
                // UI for RatingBar
                mEtDataContent.setVisibility(View.INVISIBLE);
                mIbtnClear.setVisibility(View.GONE);
                mRatingBar.setVisibility(View.VISIBLE);
                mRatingBar.setRating((float) mReceiptItem.getRating());
            } else {
                mEtDataContent.setVisibility(View.VISIBLE);
                mIbtnClear.setVisibility(View.VISIBLE);
                mRatingBar.setVisibility(View.GONE);

                mTextWatcher.bindPosition(position);
                mEtDataContent.setText(mReceiptItem.getParams(position - 1));
                mEtDataContent.setHint(mDescriptionHints.get(position - 1));
            }
        }
    }

    private class MaterialsViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private EditText mEtMaterialGroup;
        private BeChefTextWatcher mTextWatcher;
        private RecyclerView mRecyclerView;
        private ImageButton mIbtnRemove, mIbtnClear;

        MaterialsViewHolder(@NonNull View itemView, BeChefTextWatcher textWatcher) {
            super(itemView);
            itemView.findViewById(R.id.imagebutton_add).setOnClickListener(this);
            mIbtnRemove = itemView.findViewById(R.id.imagebutton_remove);
            mIbtnRemove.setOnClickListener(this);
            mIbtnClear = itemView.findViewById(R.id.imagebutton_clear);
            mIbtnClear.setOnClickListener(this);
            mRecyclerView = itemView.findViewById(R.id.recyclerview_material_content);

            mEtMaterialGroup = itemView.findViewById(R.id.edittext_material_group);
            mTextWatcher = textWatcher;
            mEtMaterialGroup.addTextChangedListener(mTextWatcher);
        }

        void bindView(int position) {
            int currentIndex = position - DESCRIPTION_SIZE - 1;
            mIbtnRemove.setImageDrawable(mContext.getResources()
                    .getDrawable(currentIndex == 0 ? R.drawable.ic_remove_gray : R.drawable.ic_remove));
            mIbtnClear.setImageDrawable(mContext.getResources()
                    .getDrawable(currentIndex == 0 ? R.drawable.ic_clear_gray : R.drawable.ic_clear));

            mEtMaterialGroup.setEnabled(currentIndex != 0);
            mTextWatcher.bindPosition(position);
            MaterialGroup currentGroup = mReceiptItem.getMaterialGroups().get(currentIndex);
            mEtMaterialGroup.setText(currentGroup.getGroupName());

            ArrayList<String> currentContents = currentGroup.getMaterialContents();
            MaterialContentAdapter materialContentAdapter = new MaterialContentAdapter(currentContents);
            mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
            mRecyclerView.setAdapter(materialContentAdapter);

            ItemTouchHelperCallback callback = new ItemTouchHelperCallback(materialContentAdapter);
            ItemTouchHelper touchHelper = new ItemTouchHelper(callback);
            touchHelper.attachToRecyclerView(mRecyclerView);
        }

        @Override
        public void onClick(View v) {
            int currentPosition = getAdapterPosition();
            if (currentPosition < 0) return;
            int currentIndex = currentPosition - DESCRIPTION_SIZE - 1;
            ArrayList<MaterialGroup> materialGroups = mReceiptItem.getMaterialGroups();
            switch (v.getId()) {
                case R.id.imagebutton_add:
                    materialGroups.add(currentIndex + 1, new MaterialGroup());
                    notifyAdded(currentPosition);
                    break;
                case R.id.imagebutton_remove:
                    if (currentIndex == 0) return;
                    materialGroups.remove(currentIndex);
                    notifyRemoved(currentPosition);
                    break;
                case R.id.imagebutton_clear:
                    if (currentIndex == 0) return;
                    materialGroups.get(currentIndex).setGroupName("");
                    notifyItemChanged(currentPosition);
                    break;
            }
        }
    }

    private class StepsViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView mTvStepNumber;
        private EditText mEtStepDescription;
        private BeChefTextWatcher mTextWatcher;
        private RecyclerView mRecyclerView;
        private ImageButton mIbtnRemove, mIbtnMoveUp, mIbtnMoveDown;

        StepsViewHolder(@NonNull View itemView, BeChefTextWatcher textWatcher) {
            super(itemView);
            mTvStepNumber = itemView.findViewById(R.id.textview_step_number);
            itemView.findViewById(R.id.imagebutton_add).setOnClickListener(this);
            mIbtnMoveUp = itemView.findViewById(R.id.imagebutton_move_up);
            mIbtnMoveUp.setOnClickListener(this);
            mIbtnMoveDown = itemView.findViewById(R.id.imagebutton_move_down);
            mIbtnMoveDown.setOnClickListener(this);
            mIbtnRemove = itemView.findViewById(R.id.imagebutton_remove);
            mIbtnRemove.setOnClickListener(this);
            itemView.findViewById(R.id.imagebutton_clear).setOnClickListener(this);
            mRecyclerView = itemView.findViewById(R.id.recyclerview_step_image);

            mEtStepDescription = itemView.findViewById(R.id.edittext_step_description);
            mTextWatcher = textWatcher;
            mEtStepDescription.addTextChangedListener(mTextWatcher);
        }

        void bindView(int position) {
            mIbtnRemove.setImageDrawable(mContext.getResources()
                    .getDrawable(mReceiptItem.getSteps().size() == 1 ? R.drawable.ic_remove_gray : R.drawable.ic_remove));

            int currentIndex = position - DESCRIPTION_SIZE - 1 - mReceiptItem.getMaterialGroups().size();
            mIbtnMoveUp.setImageDrawable(mContext.getResources()
                    .getDrawable(currentIndex == 0 ? R.drawable.ic_move_up_gray : R.drawable.ic_move_up));
            mIbtnMoveDown.setImageDrawable(mContext.getResources()
                    .getDrawable(currentIndex == getItemCount() - 1 ? R.drawable.ic_move_down_gray : R.drawable.ic_move_down));
            Step currentStep = mReceiptItem.getSteps().get(currentIndex);
            mTvStepNumber.setText(String.valueOf(currentIndex + 1));

            mTextWatcher.bindPosition(position);
            Step step = mReceiptItem.getSteps().get(currentIndex);
            mEtStepDescription.setText(step.getStepDescription());

            ArrayList<String> imageUrls = currentStep.getImageUrls();
            StepImageAdapter stepImageAdapter = new StepImageAdapter(imageUrls, currentIndex, mCompleteCallback);
            mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
            mRecyclerView.setAdapter(stepImageAdapter);
            mRecyclerView.addItemDecoration(dec);

            ItemTouchHelperCallback callback = new ItemTouchHelperCallback(stepImageAdapter);
            ItemTouchHelper touchHelper = new ItemTouchHelper(callback);
            touchHelper.attachToRecyclerView(mRecyclerView);
        }

        @Override
        public void onClick(View v) {
            int currentPosition = getAdapterPosition();
            if (currentPosition < 0) return;
            int currentIndex = currentPosition - DESCRIPTION_SIZE - 1 - mReceiptItem.getMaterialGroups().size();
            ArrayList<Step> steps = mReceiptItem.getSteps();
            switch (v.getId()) {
                case R.id.imagebutton_add:
                    steps.add(currentIndex + 1, new Step());
                    notifyAdded(currentPosition);
                    break;
                case R.id.imagebutton_remove:
                    if (mReceiptItem.getSteps().size() == 1) return;
                    steps.remove(currentIndex);
                    notifyRemoved(currentPosition);
                    break;
                case R.id.imagebutton_clear:
                    steps.get(currentIndex).setStepDescription("");
                    notifyItemChanged(currentPosition);
                    break;
                case R.id.imagebutton_move_up:
                    if (currentIndex == 0) return;
                    Collections.swap(steps, currentIndex, currentIndex - 1);
                    notifyItemRangeChanged(currentPosition - 1, 2);
                    break;
                case R.id.imagebutton_move_down:
                    if (currentIndex == mReceiptItem.getSteps().size() - 1) return;
                    Collections.swap(steps, currentIndex, currentIndex + 1);
                    notifyItemRangeChanged(currentPosition, 2);
                    break;
            }
        }
    }
}
