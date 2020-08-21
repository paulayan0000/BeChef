package com.paula.android.bechef.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.paula.android.bechef.R;
import com.paula.android.bechef.data.DiscoverItem;
import com.paula.android.bechef.data.Material;
import com.paula.android.bechef.data.Step;
import com.paula.android.bechef.data.entity.BaseItem;
import com.paula.android.bechef.data.entity.BookmarkItem;
import com.paula.android.bechef.data.entity.ReceiptItem;
import com.paula.android.bechef.detail.DetailContract;
import com.squareup.picasso.Picasso;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import static android.text.TextUtils.isEmpty;

public class DetailAdapter extends RecyclerView.Adapter {
    private static final int HEAD = 0;
    private static final int BODY = 1;
    private static final int FOOT = 2;

    private DetailContract.Presenter mDetailPresenter;
    private Context mContext;
    private BaseItem mBaseItem;
    private ReceiptItem mReceiptItem;
    private String mTimeAndCount;
    private int mMaterialSize;

    public DetailAdapter(BaseItem baseItem, DetailContract.Presenter presenter) {
        mBaseItem = baseItem;
        mDetailPresenter = presenter;
        if (mBaseItem instanceof DiscoverItem) {
            DiscoverItem discoverItem = (DiscoverItem) baseItem;
            mTimeAndCount = getFormatDate(discoverItem.getPublishedAt())+ " • "
                    + "觀看次數 : " + getFormatCount(discoverItem.getViewCount()) + "次";
        }
        else if (mBaseItem instanceof BookmarkItem) {
            BookmarkItem bookmarkItem = (BookmarkItem) baseItem;
            mTimeAndCount = bookmarkItem.getPublishedTime() + " • " + bookmarkItem.getRating() + "分";
        }
        else {
            mReceiptItem = (ReceiptItem) baseItem;
            mTimeAndCount = "耗時 : " + mReceiptItem.getDuration() + " • 份量 : "
                    + mReceiptItem.getWeight() + "人份 • " + mReceiptItem.getRating() + "分";
            mMaterialSize = mReceiptItem.getMaterials().size();
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        View view;
        switch (viewType) {
            case FOOT:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_detail_steps, parent, false);
                return new StepsViewHolder(view);
            case BODY:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_detail_materials, parent, false);
                return new MaterialsViewHolder(view);
            default:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_detail_description, parent, false);
                return new DescriptionViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof DescriptionViewHolder) {
            ((DescriptionViewHolder) holder).bindView();
        } else if (holder instanceof MaterialsViewHolder) {
            ((MaterialsViewHolder) holder).showDivider(position == 1, position == mMaterialSize);
            ((MaterialsViewHolder) holder).bindView(mReceiptItem.getMaterials().get(position - 1));
        } else {
            ((StepsViewHolder) holder).bindView(mReceiptItem.getSteps().get(position - mMaterialSize - 1));
        }
        // TODO: StepsViewHolder and MaterialsViewHolder bindView.
        //  Material : If p == 1: show solid upper divider; else if position == size : show lower solid divider
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) return HEAD;
        if (position <= mMaterialSize) return BODY;
        return FOOT;
    }

    @Override
    public int getItemCount() {
        if (mBaseItem instanceof ReceiptItem)
            return mReceiptItem.getSteps().size() + mReceiptItem.getMaterials().size() + 1;
        else return 1;
    }

    private class DescriptionViewHolder extends RecyclerView.ViewHolder {
        private TextView mTvDescription, mTvTags, mTvTitle, mTvTimeCount;
        private ImageView mIvThumbnail, mIvThumbnailForeground;
        private View mViewDivider;

        private DescriptionViewHolder(@NonNull View itemView) {
            super(itemView);
            mIvThumbnail = itemView.findViewById(R.id.imageview_thumbnail);
            mIvThumbnailForeground = itemView.findViewById(R.id.imageview_thumbnail_foreground);
            mTvTags = itemView.findViewById(R.id.textview_tags);
            mTvTitle = itemView.findViewById(R.id.textview_title);
            mTvTimeCount = itemView.findViewById(R.id.textview_time_count);
            mViewDivider = itemView.findViewById(R.id.view_divider);
            mTvDescription = itemView.findViewById(R.id.textview_info_description);
        }

        void bindView() {
            Picasso.with(mContext)
                    .load(mBaseItem.getImageUrl())
                    .error(R.drawable.all_picture_placeholder)
                    .placeholder(R.drawable.all_picture_placeholder)
                    .into(mIvThumbnail);
            setVideo(mBaseItem.getVideoId());
            setTextView(mTvTags, mBaseItem.getTags());
            mTvTitle.setText(mBaseItem.getTitle());
            mTvTimeCount.setText(mTimeAndCount);
            setDescription();
        }

        private void setVideo(final String videoId) {
            if (videoId.equals("")) {
                mIvThumbnailForeground.setVisibility(View.GONE);
            } else {
                mIvThumbnailForeground.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // TODO: open fullscreen landscape YouTubePlayerView
                        Toast.makeText(mContext, videoId, Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }

        private void setTextView(TextView textView, String textContent) {
            if (textContent.equals("")) textView.setVisibility(View.GONE);
            else textView.setText(textContent);
        }

        private void setDescription() {
            String description = mBaseItem.getDescription();
            if (description.equals("")) mViewDivider.setVisibility(View.GONE);
            setTextView(mTvDescription, description);
        }
    }

    private class StepsViewHolder extends RecyclerView.ViewHolder {
        private TextView mTvNumber, mTvDescription;

        StepsViewHolder(@NonNull View itemView) {
            super(itemView);
            mTvNumber = itemView.findViewById(R.id.textview_step_number);
            mTvDescription = itemView.findViewById(R.id.textview_step_description);
        }

        public void bindView(Step step) {
            mTvNumber.setText(String.valueOf(step.getStepNumber()));
            mTvDescription.setText(step.getStepDescription());
        }
    }

    private class MaterialsViewHolder extends RecyclerView.ViewHolder {
        private TextView mTvType, mTvName, mTvAmount;

        MaterialsViewHolder(@NonNull View itemView) {
            super(itemView);
            mTvType = itemView.findViewById(R.id.textview_material_type);
            mTvName = itemView.findViewById(R.id.textview_materail_name);
            mTvAmount = itemView.findViewById(R.id.textview_material_amount);
        }

        public void showDivider(boolean showTop, boolean showBottom) {
            if (showTop) itemView.findViewById(R.id.view_top_divider).setVisibility(View.VISIBLE);
            if (showBottom) itemView.findViewById(R.id.view_bottom_divider).setVisibility(View.VISIBLE);
        }

        public void bindView(Material material) {
            if (material.getMaterialIndex() == 0) mTvType.setVisibility(View.VISIBLE);
            String materialType = material.getMaterialType();
            mTvType.setText(materialType.equals("") ? "預備食材" : materialType);
            mTvName.setText(" • " + material.getMaterialName());
            mTvAmount.setText(material.getMaterialAmount());
        }
    }

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
