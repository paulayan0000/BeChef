package com.paula.android.bechef.adapters;

import android.content.Context;
import android.graphics.Rect;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.paula.android.bechef.BeChef;
import com.paula.android.bechef.R;
import com.paula.android.bechef.data.entity.DiscoverItem;
import com.paula.android.bechef.data.MaterialGroup;
import com.paula.android.bechef.data.Step;
import com.paula.android.bechef.data.entity.BaseItem;
import com.paula.android.bechef.data.entity.BookmarkItem;
import com.paula.android.bechef.data.entity.RecipeItem;
import com.paula.android.bechef.utils.Constants;
import com.paula.android.bechef.utils.Utils;
import com.paula.android.bechef.viewHolders.ThumbnailImageViewHolder;
import com.paula.android.bechef.viewHolders.NoResultViewHolder;

import java.util.ArrayList;

public class DetailAdapter extends RecyclerView.Adapter {
    private Context mContext;
    private BaseItem mBaseItem;
    private RecipeItem mRecipeItem;
    private int mMaterialSize;
    private boolean mIsLoading;
    private String mErrorMsg = "";
    // TODO: Add a common RecycledViewPool for nested recyclerViews for this class and EditItemAdapter

    public DetailAdapter(BaseItem baseItem) {
        mBaseItem = baseItem;
        setForReceipt();
    }

    private void setForReceipt() {
        if (!(mBaseItem instanceof RecipeItem)) return;
        mRecipeItem = (RecipeItem) mBaseItem;
        mMaterialSize = mRecipeItem.getMaterialGroups().size();
    }

    public BaseItem getBaseItem() {
        return mBaseItem;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(mContext);
        switch (viewType) {
            case Constants.VIEW_TYPE_NO_RESULT:
                return new NoResultViewHolder(inflater
                        .inflate(R.layout.item_search_no_result, parent, false), mErrorMsg);
            case Constants.VIEW_TYPE_LOADING:
                return new RecyclerView.ViewHolder(inflater
                        .inflate(R.layout.item_loading, parent, false)) {
                };
            case Constants.VIEW_TYPE_INFO:
                return new InfoViewHolder(inflater
                        .inflate(R.layout.item_detail_info, parent, false));
            case Constants.VIEW_TYPE_MATERIALS:
                return new MaterialsViewHolder(inflater
                        .inflate(R.layout.item_detail_materials, parent, false));
            case Constants.VIEW_TYPE_STEPS:
                return new StepsViewHolder(inflater
                        .inflate(R.layout.item_detail_steps, parent, false));
            default:
                return new ThumbnailImageViewHolder(inflater
                        .inflate(R.layout.item_thumbnail_image, parent, false));
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        switch (holder.getItemViewType()) {
            case Constants.VIEW_TYPE_INFO:
                ((InfoViewHolder) holder).bindView();
                return;
            case Constants.VIEW_TYPE_MATERIALS:
                ((MaterialsViewHolder) holder).bindView(position - 2);
                return;
            case Constants.VIEW_TYPE_STEPS:
                ((StepsViewHolder) holder).bindView(position - mMaterialSize - 2);
                return;
            case Constants.VIEW_TYPE_IMAGE:
                ((ThumbnailImageViewHolder) holder).bindView(mBaseItem.getImageUrl());
                return;
            default:
        }
    }

    public void setLoading(boolean loading) {
        mIsLoading = loading;
        notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int position) {
        if (mIsLoading) return Constants.VIEW_TYPE_LOADING;
        if (mBaseItem == null) return Constants.VIEW_TYPE_NO_RESULT;
        if (mRecipeItem != null && position == 0) return Constants.VIEW_TYPE_IMAGE;
        if (position <= 1) return Constants.VIEW_TYPE_INFO;
        if (position <= mMaterialSize + 1) return Constants.VIEW_TYPE_MATERIALS;
        return Constants.VIEW_TYPE_STEPS;
    }

    @Override
    public int getItemCount() {
        /* image + variables (tags, title, rating, duration, weight, description) for RecipeItem
         + MaterialGroups + Steps */
        if (mRecipeItem != null) {
            return 1 + 1 + mRecipeItem.getSteps().size() + mRecipeItem.getMaterialGroups().size();
        }
        return 1;
    }

    public void updateError(String errorMsg) {
        mBaseItem = null;
        mErrorMsg = errorMsg;
        mIsLoading = false;
        notifyDataSetChanged();
    }

    public void updateData(BaseItem baseItem) {
        mBaseItem = baseItem;
        setForReceipt();
        mIsLoading = false;
        notifyDataSetChanged();
    }

    private class InfoViewHolder extends RecyclerView.ViewHolder {
        private final TextView mTvDescription, mTvTags, mTvTitle, mTvTimeAndCount;
        private final View mViewDivider;

        private InfoViewHolder(@NonNull View itemView) {
            super(itemView);
            mTvDescription = itemView.findViewById(R.id.textview_description);
            mTvTags = itemView.findViewById(R.id.textview_tags);
            mTvTitle = itemView.findViewById(R.id.textview_title);
            mTvTimeAndCount = itemView.findViewById(R.id.textview_time_and_rating);
            mViewDivider = itemView.findViewById(R.id.view_divider);
        }

        void bindView() {
            setTextView(mTvTags, mBaseItem.getTags());
            mTvTitle.setText(mBaseItem.getTitle());
            mTvTimeAndCount.setText(getTimAndCount());
            String description = mBaseItem.getDescription();
            setTextView(mTvDescription, description);
            if (description.equals("")) {
                mViewDivider.setVisibility(View.GONE);
            }
        }

        private String getTimAndCount() {
            String timeAndCount;
            if (mBaseItem instanceof DiscoverItem) {
                DiscoverItem discoverItem = (DiscoverItem) mBaseItem;
                timeAndCount = String.format(BeChef.getAppContext().getString(R.string.time_and_count),
                        getFormatDate(discoverItem.getPublishedAt()),
                        getFormatCount(discoverItem.getViewCount()));
                if (!discoverItem.getVideoId().isEmpty()) return timeAndCount;
                return String.format(BeChef.getAppContext().getString(R.string.subscribe_count),
                        getFormatCount(discoverItem.getSubscribeCount())) + "\n" + timeAndCount;
            } else if (mBaseItem instanceof BookmarkItem) {
                BookmarkItem bookmarkItem = (BookmarkItem) mBaseItem;
                return String.format(BeChef.getAppContext().getString(R.string.time_and_rating),
                        bookmarkItem.getCreatedTime(),
                        bookmarkItem.getRating());
            } else {
                String duration = "".equals(mRecipeItem.getDuration()) ? "--" : mRecipeItem.getDuration();
                String durationAndWeight = String.format(
                        BeChef.getAppContext().getString(R.string.duration_and_weight), duration, mRecipeItem.getWeight());
                return String.format(BeChef.getAppContext().getString(R.string.time_and_rating),
                        mRecipeItem.getCreatedTime(), mRecipeItem.getRating()) + "\n" + durationAndWeight;
            }
        }

        private String getFormatDate(String originalTime) {
            String dateStr = "";
            if (originalTime.isEmpty()) return dateStr;
            String[] timeArray = originalTime.split(Constants.API_TIME_REGEX);
            dateStr = String.format(BeChef.getAppContext().getString(R.string.date_format_for_discover),
                    timeArray[0], timeArray[1], timeArray[2]);
            return dateStr;
        }

        private int getFormatCount(String originalCount) {
            try {
                return Integer.parseInt(originalCount);
            } catch (NumberFormatException e) {
                return 0;
            }
        }
    }

    private class MaterialsViewHolder extends RecyclerView.ViewHolder {
        private final TextView mTvType, mTvName;
        private final View mVTopDivider, mVBottomDivider;

        MaterialsViewHolder(@NonNull View itemView) {
            super(itemView);
            mTvType = itemView.findViewById(R.id.textview_material_type);
            mTvName = itemView.findViewById(R.id.textview_material_name);
            mVTopDivider = itemView.findViewById(R.id.view_top_divider);
            mVBottomDivider = itemView.findViewById(R.id.view_bottom_divider);
        }

        void bindView(int materialPos) {
            MaterialGroup materialGroup = mRecipeItem.getMaterialGroups().get(materialPos);
            mTvType.setText(materialGroup.getGroupName());

            StringBuilder materialContent = new StringBuilder();
            ArrayList<String> contents = materialGroup.getMaterialContents();
            String contentFormat = BeChef.getAppContext().getString(R.string.material_contents_detail);
            if (materialPos == mMaterialSize - 1) {
                // Set material content and divider for the last one
                mVBottomDivider.setVisibility(View.VISIBLE);
                int contentsSize = contents.size();
                for (int i = 0; i < contentsSize; i++) {
                    materialContent.append(" • ").append(contents.get(i));
                    if (i != contentsSize - 1) materialContent.append("\n");
                }
            } else {
                mVBottomDivider.setVisibility(View.GONE);
                for (String content : contents) {
                    materialContent.append(String.format(contentFormat, content));
                }
            }
            // Set visibility of top divider
            if (materialPos == 0) {
                mVTopDivider.setVisibility(View.VISIBLE);
            } else {
                mVTopDivider.setVisibility(View.GONE);
            }
            setTextView(mTvName, materialContent.toString());
        }
    }

    private class StepsViewHolder extends RecyclerView.ViewHolder {
        private final TextView mTvNumber, mTvDescription;
        private final RecyclerView mRecyclerView;

        StepsViewHolder(@NonNull View itemView) {
            super(itemView);
            mTvNumber = itemView.findViewById(R.id.textview_step_number);
            mTvDescription = itemView.findViewById(R.id.textview_step_description);
            mRecyclerView = itemView.findViewById(R.id.recyclerview_step_image);
            mRecyclerView.addItemDecoration(new RecyclerView.ItemDecoration() {
                @Override
                public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
                    super.getItemOffsets(outRect, view, parent, state);
                    if (outRect.top == 0) {
                        outRect.top = Utils.convertDpToPixel(Constants.NORMAL_PADDING);
                    }
                    if (outRect.right == 0) {
                        outRect.right = Utils.convertDpToPixel(Constants.NORMAL_PADDING);
                    }
                }
            });
        }

        void bindView(int stepPos) {
            mTvNumber.setText(String.valueOf(stepPos + 1));
            Step step = mRecipeItem.getSteps().get(stepPos);
            setTextView(mTvDescription, step.getStepDescription());

            int imageCount = step.getImageUrls().size();
            if (imageCount > 0) {
                GridLayoutManager layoutManager = new GridLayoutManager(mContext,
                        Math.min(imageCount, Constants.IMAGE_ITEM_LIMIT));
                mRecyclerView.setLayoutManager(layoutManager);
                StepImageAdapter stepImageAdapter = new StepImageAdapter(step.getImageUrls());
                mRecyclerView.setAdapter(stepImageAdapter);
            }
        }
    }

    private void setTextView(TextView textView, String textContent) {
        if (textContent.isEmpty()) {
            textView.setVisibility(View.GONE);
        } else {
            textView.setVisibility(View.VISIBLE);
            textView.setText(textContent);
        }
    }
}