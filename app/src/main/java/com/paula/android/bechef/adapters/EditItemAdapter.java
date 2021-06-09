package com.paula.android.bechef.adapters;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Rect;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.paula.android.bechef.BeChef;
import com.paula.android.bechef.R;
import com.paula.android.bechef.data.MaterialGroup;
import com.paula.android.bechef.data.Step;
import com.paula.android.bechef.data.entity.RecipeItem;
import com.paula.android.bechef.utils.BeChefTextWatcher;
import com.paula.android.bechef.utils.Constants;
import com.paula.android.bechef.utils.EditCallback;
import com.paula.android.bechef.utils.EditTextChangeCallback;
import com.paula.android.bechef.utils.Utils;
import com.paula.android.bechef.viewHolders.ThumbnailImageViewHolder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public class EditItemAdapter extends RecyclerView.Adapter implements EditTextChangeCallback {
    private final ArrayList<String> mInfoNames;
    private final ArrayList<String> mInfoHints;
    private final EditCallback mEditCallback;
    private final RecipeItem mRecipeItem;
    private Context mContext;

    public EditItemAdapter(RecipeItem recipeItem, EditCallback editCallback) {
        mRecipeItem = recipeItem;
        mEditCallback = editCallback;
        mInfoNames = new ArrayList<>(Arrays.asList(BeChef.getAppContext().getResources()
                .getStringArray(R.array.info_names)));
        mInfoHints = new ArrayList<>(Arrays.asList(BeChef.getAppContext().getResources()
                .getStringArray(R.array.info_hints)));
        if (mEditCallback.isFromBookmark()) return;
        if (recipeItem.getMaterialGroups().size() == 0) {
            mRecipeItem.getMaterialGroups().add(
                    new MaterialGroup(BeChef.getAppContext().getString(R.string.default_group_name)));
        }
        if (recipeItem.getSteps().size() == 0) mRecipeItem.getSteps().add(new Step());
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view;
        switch (viewType) {
            case Constants.VIEW_TYPE_IMAGE:
                view = inflater.inflate(R.layout.item_thumbnail_image, parent, false);
                if (mEditCallback.isFromBookmark()) return new ThumbnailImageViewHolder(view);
                return new ThumbnailImageViewHolder(view, mEditCallback);
            case Constants.VIEW_TYPE_INFO:
                view = inflater.inflate(R.layout.item_edit_info, parent, false);
                return new EditInfoViewHolder(view, new BeChefTextWatcher(this));
            case Constants.VIEW_TYPE_MATERIALS:
                view = inflater.inflate(R.layout.item_edit_materials, parent, false);
                return new EditMaterialsViewHolder(view, new BeChefTextWatcher(this));
            default:
                view = inflater.inflate(R.layout.item_edit_steps, parent, false);
                return new EditStepsViewHolder(view, new BeChefTextWatcher(this));
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        switch (holder.getItemViewType()) {
            case Constants.VIEW_TYPE_IMAGE:
                ((ThumbnailImageViewHolder) holder).bindView(mRecipeItem.getImageUrl());
                return;
            case Constants.VIEW_TYPE_INFO:
                ((EditInfoViewHolder) holder).bindView(position);
                return;
            case Constants.VIEW_TYPE_MATERIALS:
                ((EditMaterialsViewHolder) holder).bindView(position);
                return;
            case Constants.VIEW_TYPE_STEPS:
                ((EditStepsViewHolder) holder).bindView(position);
                return;
            default:
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) return Constants.VIEW_TYPE_IMAGE;
        if (isInInfo(position)) return Constants.VIEW_TYPE_INFO;
        if (isInMaterial(position)) return Constants.VIEW_TYPE_MATERIALS;
        return Constants.VIEW_TYPE_STEPS;
    }

    @Override
    public int getItemCount() {
        // image + variables (tags, title, rating, description) for BookmarkItem
        if (mEditCallback.isFromBookmark()) return 1 + Constants.INFO_SIZE - 2;

        /* image + variables (tags, title, rating, duration, weight, description) for RecipeItem
         + MaterialGroups + Steps */
        return 1 + Constants.INFO_SIZE + mRecipeItem.getMaterialGroups().size()
                + mRecipeItem.getSteps().size();
    }

    public RecipeItem getRecipeItem() {
        return mRecipeItem;
    }

    @Override
    public void afterTextChanged(int position, String textContent) {
        if (isInInfo(position)) {
            mRecipeItem.setParams(position - 1, textContent);
        } else if (isInMaterial(position)) {
            mRecipeItem.getMaterialGroups().get(getMaterialPos(position)).setGroupName(textContent);
        } else {
            mRecipeItem.getSteps().get(getStepPos(position)).setStepDescription(textContent);
        }
    }


    private boolean isInInfo(int position) {
        return position <= Constants.INFO_SIZE;
    }

    private boolean isInMaterial(int position) {
        return position <= Constants.INFO_SIZE + mRecipeItem.getMaterialGroups().size();
    }

    private int getInfoPos(int position) {
        return position - 1;
    }

    private int getMaterialPos(int position) {
        return getInfoPos(position) - Constants.INFO_SIZE;
    }

    private int getStepPos(int position) {
        return getMaterialPos(position) - mRecipeItem.getMaterialGroups().size();
    }

    private void notifyAdded(int currentPosition) {
        int newPosition = currentPosition + 1;
        notifyItemInserted(newPosition);
        notifyItemRangeChanged(newPosition, getItemCount() - newPosition);
    }

    private void notifyRemoved(int currentPosition) {
        notifyItemRemoved(currentPosition);
        notifyItemRangeChanged(currentPosition, getItemCount() - currentPosition);
    }

    private class EditInfoViewHolder extends RecyclerView.ViewHolder {
        private final TextView mTvDataName;
        private final ImageButton mIbtnClear;
        private final RatingBar mRatingBar;
        private final EditText mEtDataContent;
        private final BeChefTextWatcher mTextWatcher;

        EditInfoViewHolder(@NonNull View itemView, BeChefTextWatcher textWatcher) {
            super(itemView);
            mTvDataName = itemView.findViewById(R.id.textview_param_name);
            mRatingBar = itemView.findViewById(R.id.rating_bar);
            mRatingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
                @Override
                public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                    mRecipeItem.setRating(rating);
                }
            });

            mEtDataContent = itemView.findViewById(R.id.edittext_param_content);
            mTextWatcher = textWatcher;
            mEtDataContent.addTextChangedListener(mTextWatcher);
            mIbtnClear = itemView.findViewById(R.id.imagebutton_clear);
            mIbtnClear.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if (position < 0) return;
                    if (isInfoOfBookmarkItem(position)) {
                        mRecipeItem.setParams(getInfoPos(position + 2), "");
                    } else {
                        mRecipeItem.setParams(getInfoPos(position), "");
                    }
                    notifyItemChanged(position);
                }
            });
        }

        private boolean isInfoOfBookmarkItem(int position) {
            return mEditCallback.isFromBookmark() && position == getItemCount() - 1;
        }

        void bindView(int position) {
            // drop setting duration and weight for BookmarkItem
            if (isInfoOfBookmarkItem(position)) position += 2;
            int currentParamIndex = getInfoPos(position);
            String infoName = mInfoNames.get(currentParamIndex);
            mTvDataName.setText(String.format(BeChef.getAppContext().getString(R.string.param), infoName));
            // Set UI for rating
            if (infoName.equals(BeChef.getAppContext().getString(R.string.rating))) {
                // Set UI for rating
                mEtDataContent.setVisibility(View.INVISIBLE);
                mIbtnClear.setVisibility(View.GONE);
                mRatingBar.setVisibility(View.VISIBLE);
                mRatingBar.setRating((float) mRecipeItem.getRating());
                return;
            }

            mEtDataContent.setVisibility(View.VISIBLE);
            mIbtnClear.setVisibility(View.VISIBLE);
            mRatingBar.setVisibility(View.GONE);
            mTextWatcher.bindPosition(position);
            mEtDataContent.setText(mRecipeItem.getParams(currentParamIndex));
            mEtDataContent.setHint(mInfoHints.get(currentParamIndex));

            // SetInputType for EditText
            if (infoName.equals(BeChef.getAppContext().getString(R.string.weight))) {
                // SetInputType for weight
                mEtDataContent.setInputType(InputType.TYPE_CLASS_NUMBER);
            } else if (infoName.equals(BeChef.getAppContext().getString(R.string.description))) {
                // SetInputType for description
                mEtDataContent.setInputType(InputType.TYPE_CLASS_TEXT
                        | InputType.TYPE_TEXT_FLAG_MULTI_LINE);
            } else {
                mEtDataContent.setInputType(InputType.TYPE_CLASS_TEXT);
            }
        }
    }

    private class EditMaterialsViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private final EditText mEtMaterialGroup;
        private final BeChefTextWatcher mTextWatcher;
        private final RecyclerView mRecyclerView;
        private final ImageButton mIbtnRemove, mIbtnClear;

        EditMaterialsViewHolder(@NonNull View itemView, BeChefTextWatcher textWatcher) {
            super(itemView);
            itemView.findViewById(R.id.imagebutton_add).setOnClickListener(this);
            mIbtnRemove = itemView.findViewById(R.id.imagebutton_remove);
            mIbtnRemove.setOnClickListener(this);
            mIbtnClear = itemView.findViewById(R.id.imagebutton_clear);
            mIbtnClear.setOnClickListener(this);
            mRecyclerView = itemView.findViewById(R.id.recyclerview_material_content);
            mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));

            mEtMaterialGroup = itemView.findViewById(R.id.edittext_material_group);
            mTextWatcher = textWatcher;
            mEtMaterialGroup.addTextChangedListener(mTextWatcher);
        }

        void bindView(final int position) {
            int materialPos = getMaterialPos(position);
            boolean isFirstOne = materialPos == 0;
            int removeDrawableId = getRemoveDrawableId(isFirstOne);
            setImageDrawable(mIbtnRemove, removeDrawableId);
            int clearDrawableId = getClearDrawableId(isFirstOne);
            setImageDrawable(mIbtnClear, clearDrawableId);

            MaterialGroup currentGroup = mRecipeItem.getMaterialGroups().get(materialPos);
            mEtMaterialGroup.setEnabled(!isFirstOne);
            mTextWatcher.bindPosition(position);
            mEtMaterialGroup.setText(currentGroup.getGroupName());

            ArrayList<String> currentContents = currentGroup.getMaterialContents();
            MaterialContentAdapter materialContentAdapter = new MaterialContentAdapter(currentContents);
            materialContentAdapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
                @Override
                public void onItemRangeInserted(int positionStart, int itemCount) {
                    calculateSpaceAndScroll(mRecyclerView, positionStart);
                }
            });
            mRecyclerView.setAdapter(materialContentAdapter);
            ItemTouchHelperCallback callback = new ItemTouchHelperCallback(materialContentAdapter);
            new ItemTouchHelper(callback).attachToRecyclerView(mRecyclerView);
        }

        private int getClearDrawableId(boolean isDisable) {
            return isDisable ? R.drawable.ic_clear_gray : R.drawable.ic_clear;
        }

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            if (position < 0) return;
            int materialPos = getMaterialPos(position);
            ArrayList<MaterialGroup> materialGroups = mRecipeItem.getMaterialGroups();
            int currentViewId = v.getId();
            if (currentViewId == R.id.imagebutton_add) {
                materialGroups.add(materialPos + 1, new MaterialGroup());
                notifyAdded(position);
            } else if (currentViewId == R.id.imagebutton_remove) {
                if (materialPos == 0) return;
                materialGroups.remove(materialPos);
                notifyRemoved(position);
            } else if (currentViewId == R.id.imagebutton_clear) {
                if (materialPos == 0) return;
                materialGroups.get(materialPos).setGroupName("");
                notifyItemChanged(position);
            }
        }
    }

    public class EditStepsViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private final TextView mTvStepNumber;
        private final EditText mEtStepDescription;
        private final BeChefTextWatcher mTextWatcher;
        private final RecyclerView mRecyclerView;
        private final ImageButton mIbtnRemove, mIbtnMoveUp, mIbtnMoveDown;

        EditStepsViewHolder(@NonNull View itemView, BeChefTextWatcher textWatcher) {
            super(itemView);
            mTvStepNumber = itemView.findViewById(R.id.textview_step_number);
            itemView.findViewById(R.id.imagebutton_add).setOnClickListener(this);
            itemView.findViewById(R.id.imagebutton_clear).setOnClickListener(this);
            mIbtnMoveUp = itemView.findViewById(R.id.imagebutton_move_up);
            mIbtnMoveUp.setOnClickListener(this);
            mIbtnMoveDown = itemView.findViewById(R.id.imagebutton_move_down);
            mIbtnMoveDown.setOnClickListener(this);
            mIbtnRemove = itemView.findViewById(R.id.imagebutton_remove);
            mIbtnRemove.setOnClickListener(this);
            mRecyclerView = itemView.findViewById(R.id.recyclerview_step_image);
            mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
            mRecyclerView.addItemDecoration(new RecyclerView.ItemDecoration() {
                @Override
                public void getItemOffsets(@NonNull Rect outRect,
                                           @NonNull View view,
                                           @NonNull RecyclerView parent,
                                           @NonNull RecyclerView.State state) {
                    super.getItemOffsets(outRect, view, parent, state);
                    if (outRect.bottom == 0) {
                        outRect.bottom = Utils.convertDpToPixel(Constants.NORMAL_PADDING);
                    }
                }
            });

            mEtStepDescription = itemView.findViewById(R.id.edittext_step_description);
            mTextWatcher = textWatcher;
            mEtStepDescription.addTextChangedListener(mTextWatcher);
        }

        void bindView(final int position) {
            int removeDrawableId = getRemoveDrawableId(mRecipeItem.getSteps().size() <= 1);
            setImageDrawable(mIbtnRemove, removeDrawableId);
            int stepPos = getStepPos(position);
            int moveUpDrawableId = getMoveUpDrawableId(stepPos == 0);
            setImageDrawable(mIbtnMoveUp, moveUpDrawableId);
            int moveDownDrawableId = getMoveDownDrawableId(isLastStep(stepPos));
            setImageDrawable(mIbtnMoveDown, moveDownDrawableId);

            mTextWatcher.bindPosition(position);
            mTvStepNumber.setText(String.valueOf(stepPos + 1));
            Step currentStep = mRecipeItem.getSteps().get(stepPos);
            mEtStepDescription.setText(currentStep.getStepDescription());

            ArrayList<String> imageUrls = currentStep.getImageUrls();
            final StepImageAdapter stepImageAdapter
                    = new StepImageAdapter(imageUrls, stepPos, mEditCallback);
            stepImageAdapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
                @Override
                public void onItemRangeInserted(int positionStart, int itemCount) {
                    if (position == getItemCount() - 1 &&
                            positionStart == stepImageAdapter.getItemCount() - 1) {
                        mEditCallback.scrollToBottom();
                        return;
                    }
                    calculateSpaceAndScroll(mRecyclerView, positionStart);
                }
            });
            mRecyclerView.setAdapter(stepImageAdapter);
            ItemTouchHelperCallback callback = new ItemTouchHelperCallback(stepImageAdapter);
            new ItemTouchHelper(callback).attachToRecyclerView(mRecyclerView);
        }

        public void removeSpecifiedStepImage(int imagePos) {
            StepImageAdapter stepImageAdapter = ((StepImageAdapter) mRecyclerView.getAdapter());
            if (stepImageAdapter != null) stepImageAdapter.notifyRemoved(imagePos);
        }

        public void setSpecifiedStepImage(int imagePos, String urlString) {
            StepImageAdapter stepImageAdapter = ((StepImageAdapter) mRecyclerView.getAdapter());
            if (stepImageAdapter != null) stepImageAdapter.notifyChanged(imagePos, urlString);
        }

        private int getMoveDownDrawableId(boolean isDisable) {
            return isDisable ? R.drawable.ic_move_down_gray : R.drawable.ic_move_down;
        }

        private int getMoveUpDrawableId(boolean isDisable) {
            return isDisable ? R.drawable.ic_move_up_gray : R.drawable.ic_move_up;
        }

        private boolean isLastStep(int stepPos) {
            return stepPos == mRecipeItem.getSteps().size() - 1;
        }

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            if (position < 0) return;
            int stepPos = getStepPos(position);
            ArrayList<Step> steps = mRecipeItem.getSteps();
            int currentViewId = v.getId();
            if (currentViewId == R.id.imagebutton_add) {
                steps.add(stepPos + 1, new Step());
                notifyAdded(position);
            } else if (currentViewId == R.id.imagebutton_remove) {
                if (mRecipeItem.getSteps().size() == 1) return;
                steps.remove(stepPos);
                notifyRemoved(position);
            } else if (currentViewId == R.id.imagebutton_clear) {
                steps.get(stepPos).setStepDescription("");
                notifyItemChanged(position);
            } else if (currentViewId == R.id.imagebutton_move_up) {
                if (stepPos == 0) return;
                Collections.swap(steps, stepPos, stepPos - 1);
                notifyItemRangeChanged(position - 1, 2);
            } else if (currentViewId == R.id.imagebutton_move_down) {
                if (isLastStep(stepPos)) return;
                Collections.swap(steps, stepPos, stepPos + 1);
                notifyItemRangeChanged(position, 2);
            }
        }
    }

    private void calculateSpaceAndScroll(RecyclerView recyclerView, int positionStart) {
        int height = recyclerView.getChildAt(positionStart - 1).getHeight();
        int[] prevLocations = new int[2];
        recyclerView.getChildAt(positionStart - 1).getLocationOnScreen(prevLocations);
        int prevTopToBottom = Resources.getSystem().getDisplayMetrics().heightPixels
                - prevLocations[1];
        if (prevTopToBottom >= 2 * height) return;
        mEditCallback.scrollByY(2 * height - prevTopToBottom);
    }

    private int getRemoveDrawableId(boolean isDisable) {
        return isDisable ? R.drawable.ic_remove_gray : R.drawable.ic_remove;

    }

    private void setImageDrawable(ImageButton imageButton, int drawableId) {
        imageButton.setImageDrawable(ContextCompat.getDrawable(BeChef.getAppContext(), drawableId));
    }
}