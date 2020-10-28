package com.paula.android.bechef.adapters;

import android.content.Context;
import android.graphics.Rect;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.paula.android.bechef.R;
import com.paula.android.bechef.data.entity.DiscoverItem;
import com.paula.android.bechef.data.MaterialGroup;
import com.paula.android.bechef.data.Step;
import com.paula.android.bechef.data.entity.BaseItem;
import com.paula.android.bechef.data.entity.BookmarkItem;
import com.paula.android.bechef.data.entity.ReceiptItem;
import com.paula.android.bechef.utils.Utils;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import static android.text.TextUtils.isEmpty;
import static com.paula.android.bechef.utils.Constants.IMAGE_ITEM_LIMIT;
import static com.paula.android.bechef.utils.Constants.VIEW_TYPE_BODY;
import static com.paula.android.bechef.utils.Constants.VIEW_TYPE_FOOT;
import static com.paula.android.bechef.utils.Constants.VIEW_TYPE_HEAD;

public class DetailAdapter extends RecyclerView.Adapter {

    private Context mContext;
    private BaseItem mBaseItem;
    private ReceiptItem mReceiptItem;
    private String mTimeAndCount;
    private int mMaterialSize;

    public DetailAdapter(BaseItem baseItem) {
        mBaseItem = baseItem;
        if (mBaseItem instanceof DiscoverItem) {
            DiscoverItem discoverItem = (DiscoverItem) baseItem;
            mTimeAndCount = getFormatDate(discoverItem.getPublishedAt()) + " • "
                    + "觀看次數 : " + getFormatCount(discoverItem.getViewCount()) + "次";
        } else {
            updateItemData(baseItem);
        }
    }

    private void updateItemData(BaseItem baseItem) {
        if (baseItem instanceof BookmarkItem) {
            BookmarkItem bookmarkItem = (BookmarkItem) baseItem;
            mTimeAndCount = bookmarkItem.getCreatedTime() + " • " + bookmarkItem.getRating() + "分";
        } else {
            mReceiptItem = (ReceiptItem) baseItem;
            String rating = mReceiptItem.getRating() == 0.0 ? "--" : String.valueOf(mReceiptItem.getRating());
            String duration = "".equals(mReceiptItem.getDuration()) ? "--" : mReceiptItem.getDuration();
            String weight = mReceiptItem.getWeight() <= 0 ? "--" : String.valueOf(mReceiptItem.getWeight());
            mTimeAndCount = mReceiptItem.getCreatedTime() + " • " + rating
                    + "分\n耗時 : " + duration + " • 份量 : "
                    + weight + "人份";
            mMaterialSize = mReceiptItem.getMaterialGroups().size();
        }
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
            case VIEW_TYPE_FOOT:
                return new StepsViewHolder(inflater.inflate(R.layout.item_detail_steps, parent, false));
            case VIEW_TYPE_BODY:
                return new MaterialsViewHolder(inflater.inflate(R.layout.item_detail_materials, parent, false));
            default:
                return new DescriptionViewHolder(inflater.inflate(R.layout.item_detail_descriptions, parent, false));
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof DescriptionViewHolder) {
            ((DescriptionViewHolder) holder).bindView();
        } else if (holder instanceof MaterialsViewHolder) {
            ((MaterialsViewHolder) holder).showDivider(position == 1, position == mMaterialSize);
            ((MaterialsViewHolder) holder).bindView(position);
        } else {
            ((StepsViewHolder) holder).bindView(position);
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) return VIEW_TYPE_HEAD;
        if (position <= mMaterialSize) return VIEW_TYPE_BODY;
        return VIEW_TYPE_FOOT;
    }

    @Override
    public int getItemCount() {
        if (mBaseItem instanceof ReceiptItem)
            return mReceiptItem.getSteps().size() + mReceiptItem.getMaterialGroups().size() + 1;
        else return 1;
    }

    public void updateData(BaseItem baseItem) {
        mBaseItem = baseItem;
        updateItemData(mBaseItem);
        notifyDataSetChanged();
    }

    private class DescriptionViewHolder extends RecyclerView.ViewHolder {
        private TextView mTvDescription, mTvTags, mTvTitle, mTvTimeCount;
        private ImageView mIvThumbnail, mIvThumbnailForeground;
        private View mViewDivider;

        private DescriptionViewHolder(@NonNull View itemView) {
            super(itemView);
            mTvTags = itemView.findViewById(R.id.textview_tags);
            mTvTitle = itemView.findViewById(R.id.textview_title);
            mTvTimeCount = itemView.findViewById(R.id.textview_time_count);
            mViewDivider = itemView.findViewById(R.id.view_divider);
            mTvDescription = itemView.findViewById(R.id.textview_info_description);
            mIvThumbnail = itemView.findViewById(R.id.imageview_thumbnail);
            mIvThumbnailForeground = itemView.findViewById(R.id.imageview_thumbnail_foreground);
            mIvThumbnailForeground.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // TODO: open fullscreen landscape YouTubePlayerView
                    Toast.makeText(mContext, mBaseItem.getVideoId(), Toast.LENGTH_SHORT).show();
                }
            });
        }

        void bindView() {
            String imageUrl = mBaseItem.getImageUrl();
            Picasso.with(mContext)
                    .load(imageUrl.isEmpty() ? null : imageUrl)
                    .error(R.drawable.all_picture_placeholder)
                    .placeholder(R.drawable.all_picture_placeholder)
                    .into(mIvThumbnail);
            setTextView(mTvTags, mBaseItem.getTags());
            mTvTitle.setText(mBaseItem.getTitle());
            mTvTimeCount.setText(mTimeAndCount);
            setDescription();
            if (mBaseItem.getVideoId().isEmpty()) mIvThumbnailForeground.setVisibility(View.GONE);
        }

        private void setTextView(TextView textView, String textContent) {
            if (textContent.equals("")) textView.setVisibility(View.GONE);
            else {
                textView.setVisibility(View.VISIBLE);
                textView.setText(textContent);
            }
        }

        private void setDescription() {
            String description = mBaseItem.getDescription();
            if (description.equals("")) mViewDivider.setVisibility(View.GONE);
            setTextView(mTvDescription, description);
        }
    }

    private class MaterialsViewHolder extends RecyclerView.ViewHolder {
        private TextView mTvType, mTvName;
        private View mVTopDivider, mVBottomDivider;

        MaterialsViewHolder(@NonNull View itemView) {
            super(itemView);
            mTvType = itemView.findViewById(R.id.textview_material_type);
            mTvName = itemView.findViewById(R.id.textview_materail_name);
            mVTopDivider = itemView.findViewById(R.id.view_top_divider);
            mVBottomDivider = itemView.findViewById(R.id.view_bottom_divider);
        }

        void showDivider(boolean showTop, boolean showBottom) {
            mVTopDivider.setVisibility(showTop ? View.VISIBLE : View.GONE);
            mVBottomDivider.setVisibility(showBottom ? View.VISIBLE : View.GONE);
        }

        void bindView(int position) {
            MaterialGroup materialGroup = mReceiptItem.getMaterialGroups().get(position - 1);
            mTvType.setText(materialGroup.getGroupName());

            StringBuilder materialName = new StringBuilder();
            ArrayList<String> contents = materialGroup.getMaterialContents();
            int contentsSize = contents.size();
            for (int i = 0; i < contentsSize; i++) {
                materialName.append(" • ").append(contents.get(i));
                if (i != contentsSize - 1) materialName.append("\n");
            }
            if (!"".contentEquals(materialName)) {
                mTvName.setText(materialName.toString());
                mTvName.setVisibility(View.VISIBLE);
            } else {
                mTvName.setVisibility(View.GONE);
            }
        }
    }

    private class StepsViewHolder extends RecyclerView.ViewHolder {
        private TextView mTvNumber, mTvDescription;
        private RecyclerView mRecyclerView;

        StepsViewHolder(@NonNull View itemView) {
            super(itemView);
            mTvNumber = itemView.findViewById(R.id.textview_step_number);
            mTvDescription = itemView.findViewById(R.id.textview_step_description);
            mRecyclerView = itemView.findViewById(R.id.recyclerview_step_image);
        }

        void bindView(int position) {
            int currentIndex = position - mMaterialSize - 1;
            Step step = mReceiptItem.getSteps().get(currentIndex);
            mTvNumber.setText(String.valueOf(currentIndex + 1));
            mTvDescription.setText(step.getStepDescription());

            int imageCount = step.getImageUrls().size();
            if (imageCount != 0) {
                mRecyclerView.setVisibility(View.VISIBLE);
                GridLayoutManager layoutManager = new GridLayoutManager(mContext,
                        Math.min(imageCount, IMAGE_ITEM_LIMIT));
                StepImageAdapter stepImageAdapter = new StepImageAdapter(step.getImageUrls());
                mRecyclerView.setLayoutManager(layoutManager);
                mRecyclerView.setAdapter(stepImageAdapter);
                mRecyclerView.addItemDecoration(dec);
            }
        }
    }

    private RecyclerView.ItemDecoration dec = new RecyclerView.ItemDecoration() {
        @Override
        public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
            super.getItemOffsets(outRect, view, parent, state);
            if (outRect.top == 0) outRect.top = (int) Utils.convertDpToPixel((float) 8, mContext);            if (outRect.top == 0) outRect.top = (int) Utils.convertDpToPixel((float) 8, mContext);
            if (outRect.right == 0) outRect.right = (int) Utils.convertDpToPixel((float) 8, mContext);
        }
    };

    private String getFormatDate(String originalTime) {
        String dateStr = "";
        if (isEmpty(originalTime)) {
            return dateStr;
        }
        String[] timeArray = originalTime.split("[-T]");
        dateStr = timeArray[0] + "年" + timeArray[1] + "月" + timeArray[2] + "日";
        return dateStr;
    }

    private String getFormatCount(String originalCount) {
        String numStr = "";
        if (isEmpty(originalCount)) {
            return numStr;
        }
        NumberFormat nf = NumberFormat.getInstance();
        try {
            DecimalFormat df = new DecimalFormat("#,###");
            numStr = df.format(nf.parse(originalCount));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return numStr;
    }
}
