package com.pai8.ke.shop.adapter;


import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.lhs.library.base.BaseViewHolder;
import com.pai8.ke.databinding.BannerImageBinding;
import com.pai8.ke.databinding.BannerVideoBinding;
import com.pai8.ke.utils.ImageLoadUtils;
import com.youth.banner.adapter.BannerAdapter;

import java.util.List;

import cn.jzvd.Jzvd;


/**
 * 自定义布局,多个不同UI切换
 */
public class BannerMultipleTypesAdapter extends BannerAdapter<String, RecyclerView.ViewHolder> {

    private final int ITEM_VIEW_TYPE_IMG = 0;
    private final int ITEM_VIEW_TYPE_VIDEO = 1;

    public BannerMultipleTypesAdapter(List<String> mDatas) {
        //设置数据，也可以调用banner提供的方法,或者自己在adapter中实现
        super(mDatas);
    }

    @Override
    public RecyclerView.ViewHolder onCreateHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case ITEM_VIEW_TYPE_IMG:
                BannerImageBinding imageBinding = BannerImageBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
                return new ImageHolder(imageBinding);
            case ITEM_VIEW_TYPE_VIDEO:
                BannerVideoBinding videoBinding = BannerVideoBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
                return new VideoHolder(videoBinding);
            default: {
                imageBinding = BannerImageBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
                return new ImageHolder(imageBinding);
            }
        }
    }


    @Override
    public int getItemViewType(int position) {
        if (getData(getRealPosition(position)).endsWith("mp4")) {
            return ITEM_VIEW_TYPE_VIDEO;
        }
        return ITEM_VIEW_TYPE_IMG;
    }

    @Override
    public void onBindView(RecyclerView.ViewHolder holder, String data, int position, int size) {
        if (holder instanceof ImageHolder) {
            ImageHolder imageHolder = (ImageHolder) holder;
            ImageLoadUtils.loadImage(data, imageHolder.binding.image);
        } else if (holder instanceof VideoHolder) {
            VideoHolder videoHolder = (VideoHolder) holder;
            videoHolder.binding.jzvdStd.setUp(data, "", Jzvd.SCREEN_NORMAL);
            videoHolder.binding.jzvdStd.posterImageView.setScaleType(ImageView.ScaleType.CENTER_CROP);

            Glide.with(holder.itemView)
                    .setDefaultRequestOptions(new RequestOptions().frame(1000000).centerCrop())
                    .load(data)
                    .into(videoHolder.binding.jzvdStd.posterImageView);

        }
    }

    private static class ImageHolder extends BaseViewHolder<BannerImageBinding> {

        public ImageHolder(@NonNull BannerImageBinding viewBinding) {
            super(viewBinding);
        }
    }

    public static class VideoHolder extends BaseViewHolder<BannerVideoBinding> {

        public VideoHolder(@NonNull BannerVideoBinding viewBinding) {
            super(viewBinding);
        }
    }


}

