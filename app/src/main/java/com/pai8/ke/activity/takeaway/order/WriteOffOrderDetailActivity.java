package com.pai8.ke.activity.takeaway.order;

import android.os.Bundle;

import com.blankj.utilcode.util.TimeUtils;
import com.lhs.library.base.BaseActivity;
import com.lhs.library.base.BaseAppConstants;
import com.pai8.ke.activity.takeaway.entity.GoodsInfo;
import com.pai8.ke.activity.takeaway.entity.OrderDetailResult;
import com.pai8.ke.databinding.ActivityWriteOffOrderDetailBinding;
import com.pai8.ke.groupBuy.viewmodel.OrderDetailViewModel;
import com.pai8.ke.utils.ImageLoadUtils;
import com.pai8.ke.utils.TimeUtil;

import org.jetbrains.annotations.Nullable;

public class WriteOffOrderDetailActivity extends BaseActivity<OrderDetailViewModel, ActivityWriteOffOrderDetailBinding> {

    private String orderNo;


    @Override
    public void initView(@Nullable Bundle savedInstanceState) {
        orderNo = getIntent().getStringExtra(BaseAppConstants.BundleConstant.ARG_PARAMS_0);
    }

    @Override
    public void addObserve() {
        mViewModel.getOrderDetailData().observe(this, data -> bindViewData(data));
    }

    @Override
    public void initData() {
        mViewModel.orderList(orderNo);
    }

    private void bindViewData(OrderDetailResult bean) {
        GoodsInfo info = bean.getGoods_info().get(0);
        if (info != null) {
            ImageLoadUtils.loadImage(info.getGoods_img().get(0), mBinding.ivProductImg);
            mBinding.tvProductName.setText(info.getGoods_title());
            mBinding.tvDesc.setText(info.getDetails());
            mBinding.tvCount2.setText("X" + bean.getGoods_info().size());
            mBinding.tvGroupBuyPrice.setText("¥" + info.getGoods_sell_price());
            //    mBinding.tvOriginPrice.setText("¥" + info.getGoods_origin_price());
        }

        mBinding.tvProductPrice.setText("¥" + bean.getOrder_price());
        mBinding.tvFullDiscountPrice.setText("- ¥ " + bean.getOrder_discount_price());
        mBinding.tvDiscountPrice.setText("优惠 ¥" + bean.getOrder_discount_price());
        mBinding.tvTotalPrice.setText("¥" + bean.getOrder_price());
        String startTime = "", endTime = "";
        if (info.getTerm() != null) {
            String[] term = info.getTerm().split("-");
            startTime = TimeUtil.timeStampToString(term[0]);
            endTime = TimeUtil.timeStampToString(term[1]);
        }
        mBinding.tvTermTime.setText(String.format("%s至%s （周末、法定节假日通用）", startTime, endTime));
        mBinding.tvMatter.setText(info.getMatter());
        mBinding.tvContent.setText(bean.getBuyer_name());
        mBinding.tvPhone2.setText(bean.getBuyer_phone());
        mBinding.tvOrderNo.setText(bean.getOrder_no());
        long mill = bean.getAdd_time() * 1000L;
        mBinding.tvOrderTime.setText(TimeUtils.millis2String(mill));

    }


}
