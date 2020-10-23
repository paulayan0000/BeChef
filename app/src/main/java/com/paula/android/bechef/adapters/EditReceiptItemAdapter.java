package com.paula.android.bechef.adapters;

import android.content.Context;
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
import com.paula.android.bechef.data.dao.ReceiptItemDao;
import com.paula.android.bechef.data.database.ItemDatabase;
import com.paula.android.bechef.data.entity.ReceiptItem;
import com.paula.android.bechef.dialog.BeChefTextWatcher;
import com.paula.android.bechef.dialog.EditCompleteCallback;
import com.paula.android.bechef.dialog.EditTextChangeCallback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class EditReceiptItemAdapter extends RecyclerView.Adapter implements EditTextChangeCallback{
    private static final int IMAGE = -1;
    private static final int HEAD = 0;
    private static final int BODY = 1;
    private static final int FOOT = 2;

    private static final ArrayList<String> DESCRIPTION_NAMES
            = new ArrayList<>(Arrays.asList("標籤", "標題", "評分", "耗時", "份量", "說明"));
    private static final ArrayList<String> DESCRIPTION_HINTS
            = new ArrayList<>(Arrays.asList("#氣炸鍋 #烤箱", "宮保雞丁，必填", "", "1 小時 30 分鐘",
            "1 ，輸入整數，單位為人份", "詳細說明"));
    public static final int DESCRIPTION_SIZE = DESCRIPTION_NAMES.size();

    private ReceiptItem mReceiptItem;
    private Context mContext;
    private EditCompleteCallback mCompleteCallback;
    private String mDialogTag;

    public EditReceiptItemAdapter(ReceiptItem receiptItem, EditCompleteCallback completeCallback) {
//        mReceiptItem = new ReceiptItem(receiptItem);
        mReceiptItem = receiptItem;
        mCompleteCallback = completeCallback;

        if (receiptItem.getMaterialGroups().size() == 0) {
            mReceiptItem.getMaterialGroups().add(new MaterialGroup("準備材料", new ArrayList<String>()));
        }

        if (receiptItem.getSteps().size() == 0) {
            mReceiptItem.getSteps().add(new Step("", new ArrayList<String>()));
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view;
        switch (viewType) {
            case IMAGE:
                return new ImageViewHolder(inflater.inflate(R.layout.item_edit_images, parent, false));
            case HEAD:
                view = inflater.inflate(R.layout.item_edit_descriptions, parent, false);
                return new DescriptionViewHolder(view, new BeChefTextWatcher(this));
            case BODY:
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
            case IMAGE:
                ((ImageViewHolder) holder).bindView();
                break;
            case HEAD:
                ((DescriptionViewHolder) holder).bindView(position);
                break;
            case BODY:
                ((MaterialsViewHolder) holder).bindView(position);
                break;
            default:
                ((StepsViewHolder) holder).bindView(position);
                break;
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) return IMAGE;
        else if (position <= DESCRIPTION_SIZE) return HEAD;
        else if (position <= DESCRIPTION_SIZE + mReceiptItem.getMaterialGroups().size()) return BODY;
        else return FOOT;
    }

    @Override
    public int getItemCount() {
        return 1 + DESCRIPTION_SIZE
                + mReceiptItem.getMaterialGroups().size()
                + mReceiptItem.getSteps().size();
    }

    public void onCompleteClicked() {
        if ("".equals(mReceiptItem.getTitle())) {
            Toast.makeText(mContext, "請輸入標題", Toast.LENGTH_SHORT).show();
            return;
        }
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
        }
        mReceiptItem.setSteps(steps);

        new LoadDataTask<>(new LoadDataCallback<ReceiptItemDao>() {
            @Override
            public ReceiptItemDao getDao() {
                return ItemDatabase.getReceiptInstance(mContext).receiptDao();
            }

            @Override
            public void doInBackground(ReceiptItemDao dao) {
                if (mDialogTag.equals("edit"))
                    dao.updateItem(mReceiptItem);
                else
                    dao.insert(mReceiptItem);
            }

            @Override
            public void onCompleted() {
                Toast.makeText(mContext, "complete", Toast.LENGTH_SHORT).show();
                mCompleteCallback.onEditComplete(mReceiptItem);
                // TODO: hide saving mask view
            }
        }).execute();
    }

    private boolean isMaterialEmpty() {
        String materialContent = mReceiptItem.getMaterialGroups().get(0).getMaterialContents().get(0);
        return mReceiptItem.getMaterialGroups().size() == 1 && (materialContent == null || materialContent.isEmpty());
    }

    private boolean isStepEmpty() {
        return mReceiptItem.getSteps().size() == 1 && mReceiptItem.getSteps().get(0).getStepDescription().isEmpty();
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

    private class ImageViewHolder extends RecyclerView.ViewHolder {
        private ImageView mIvThumbNail, mIvForeground;

        public ImageViewHolder(@NonNull View itemView) {
            super(itemView);
            mIvThumbNail = itemView.findViewById(R.id.imageview_thumbnail);
            mIvForeground = itemView.findViewById(R.id.imageview_thumbnail_foreground);
        }

        public void bindView() {
            mIvForeground.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mCompleteCallback.onChooseImages(-1, -1);
                }
            });
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

        public DescriptionViewHolder(@NonNull View itemView, BeChefTextWatcher textWatcher) {
            super(itemView);
            mTvDataName = itemView.findViewById(R.id.textview_data_name);
            mIbtnClear = itemView.findViewById(R.id.imagebutton_clear);
            mRatingBar = itemView.findViewById(R.id.rating_bar);
            mEtDataContent = itemView.findViewById(R.id.edittext_data_content);
            mTextWatcher = textWatcher;
            mEtDataContent.addTextChangedListener(mTextWatcher);
        }

        public void bindView(final int position) {
            mTvDataName.setText(DESCRIPTION_NAMES.get(position - 1));
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
                mEtDataContent.setHint(DESCRIPTION_HINTS.get(position - 1));
                mIbtnClear.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mReceiptItem.setParams(position - 1, "");
                        notifyItemChanged(position);
                    }
                });
            }
        }
    }

    private class MaterialsViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private EditText mEtMaterialGroup;
        private BeChefTextWatcher mTextWatcher;
        private RecyclerView mRecyclerView;
        private ImageButton mIbtnRemove, mIbtnClear;

        public MaterialsViewHolder(@NonNull View itemView, BeChefTextWatcher textWatcher) {
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

        public void bindView(int position) {
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
        }

        @Override
        public void onClick(View v) {
            int currentPosition = getAdapterPosition();
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
        private ImageButton mIbtnRemove, mIbtnClear;

        public StepsViewHolder(@NonNull View itemView, BeChefTextWatcher textWatcher) {
            super(itemView);
            mTvStepNumber = itemView.findViewById(R.id.textview_step_number);
            itemView.findViewById(R.id.imagebutton_add).setOnClickListener(this);
            mIbtnRemove = itemView.findViewById(R.id.imagebutton_remove);
            mIbtnRemove.setOnClickListener(this);
            mIbtnClear = itemView.findViewById(R.id.imagebutton_clear);
            mIbtnClear.setOnClickListener(this);
            mRecyclerView = itemView.findViewById(R.id.recyclerview_step_image);

            mEtStepDescription = itemView.findViewById(R.id.edittext_step_description);
            mTextWatcher = textWatcher;
            mEtStepDescription.addTextChangedListener(mTextWatcher);
        }

        public void bindView(int position) {
            mIbtnRemove.setImageDrawable(mContext.getResources()
                    .getDrawable(mReceiptItem.getSteps().size() == 1 ? R.drawable.ic_remove_gray : R.drawable.ic_remove));

            int currentIndex = position - DESCRIPTION_SIZE - 1 - mReceiptItem.getMaterialGroups().size();
            Step currentStep = mReceiptItem.getSteps().get(currentIndex);
            mTvStepNumber.setText(String.valueOf(currentIndex + 1));

            mTextWatcher.bindPosition(position);
            Step step = mReceiptItem.getSteps().get(currentIndex);
            mEtStepDescription.setText(step.getStepDescription());

            ArrayList<String> imageUrls = currentStep.getImageUrls();
            StepImageAdapter stepImageAdapter = new StepImageAdapter(imageUrls, currentIndex, mCompleteCallback);
            mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
            mRecyclerView.setAdapter(stepImageAdapter);
        }

        @Override
        public void onClick(View v) {
            int currentPosition = getAdapterPosition();
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
            }
        }
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

    public void setDialogTag(String dialogTag) {
        mDialogTag = dialogTag;
    }
}
