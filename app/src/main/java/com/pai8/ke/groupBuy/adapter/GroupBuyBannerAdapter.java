package com.pai8.ke.groupBuy.adapter;

import com.pai8.ke.entity.BannerResult;
import com.pai8.ke.entity.GetGroupShopListResult;
import com.pai8.ke.utils.ImageLoadUtils;
import com.youth.banner.holder.BannerImageHolder;

import java.util.List;

public class GroupBuyBannerAdapter extends com.youth.banner.adapter.BannerImageAdapter<BannerResult> {
    public GroupBuyBannerAdapter(List<BannerResult> mData) {
        super(mData);
    }

    @Override
    public void onBindView(BannerImageHolder holder, BannerResult data, int position, int size) {
        holder.imageView.setTag(data);
        ImageLoadUtils.loadImage(data.getImgurl(), holder.imageView);
    }
}
