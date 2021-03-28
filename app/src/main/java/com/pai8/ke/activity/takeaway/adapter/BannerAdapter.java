package com.pai8.ke.activity.takeaway.adapter;

import android.widget.ImageView;

import com.pai8.ke.utils.ImageLoadUtils;
import com.youth.banner.holder.BannerImageHolder;

import java.util.List;

public class BannerAdapter extends com.youth.banner.adapter.BannerImageAdapter<String> {

    public BannerAdapter(List<String> mData) {
        super(mData);
    }

    @Override
    public void onBindView(BannerImageHolder holder, String data, int position, int size) {
        holder.imageView.setScaleType(ImageView.ScaleType.FIT_XY);
        ImageLoadUtils.setRectImage(holder.imageView.getContext(), data, holder.imageView);

    }
}
