package com.pai8.ke.groupBuy.adapter;

import com.pai8.ke.entity.GetGroupShopListResult;
import com.pai8.ke.utils.ImageLoadUtils;
import com.youth.banner.holder.BannerImageHolder;

import java.util.List;

public class GroupBuyBannerAdapter extends com.youth.banner.adapter.BannerImageAdapter<GetGroupShopListResult.Banner> {
    public GroupBuyBannerAdapter(List<GetGroupShopListResult.Banner> mData) {
        super(mData);
    }

    @Override
    public void onBindView(BannerImageHolder holder, GetGroupShopListResult.Banner data, int position, int size) {
        holder.imageView.setTag(data);
        ImageLoadUtils.loadImage(data.getImgurl(), holder.imageView);
    }
}
