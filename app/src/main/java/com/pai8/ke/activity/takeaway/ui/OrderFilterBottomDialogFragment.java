package com.pai8.ke.activity.takeaway.ui;

import android.os.Bundle;

import com.lhs.library.base.BaseBottomDialogFragment;
import com.lhs.library.base.NoViewModel;
import com.pai8.ke.activity.takeaway.adapter.OrderStatusAdapter;
import com.pai8.ke.activity.takeaway.entity.req.OrderStatusInfo;
import com.pai8.ke.databinding.DialogOrderFilterBinding;

import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class OrderFilterBottomDialogFragment extends BaseBottomDialogFragment<NoViewModel, DialogOrderFilterBinding> {
    @Override
    public void initView(@Nullable Bundle savedInstanceState) {
        mBinding.itnClose.setOnClickListener(v -> dismiss());
        OrderStatusAdapter adapter = new OrderStatusAdapter(getContext(), wrapData());
        mBinding.recyclerView.setAdapter(adapter);
        mBinding.tvNext.setOnClickListener(v -> {
            List<OrderStatusInfo> statusInfos = new ArrayList<>();
            for (OrderStatusInfo datum : adapter.getData()) {
                if (datum.isSelect) {
                    statusInfos.add(datum);
                }
            }
            if (!statusInfos.isEmpty()) {
                mDialogListener.onConfirmClickListener(statusInfos);
            }
            dismiss();
        });
    }

    private List<OrderStatusInfo> wrapData() {
        //0为待支付 1为已支付 2为商家已接单 7为订单制作完成 3为配送中 4为订单已完成 5为订单已申请退款 6订单被拒绝退款 8为订单已退款 9为订单已取消 -1为支付超时 -2订单拒绝接单
        List<OrderStatusInfo> statusInfos = new ArrayList<>();
        OrderStatusInfo statusInfo = new OrderStatusInfo();
        statusInfo.name = "待接单";
        statusInfo.status = "1";
        statusInfos.add(statusInfo);
        OrderStatusInfo statusInfo1 = new OrderStatusInfo();
        statusInfo1.name = "已接单";
        statusInfo1.status = "2";
        statusInfos.add(statusInfo1);
        OrderStatusInfo statusInfo2 = new OrderStatusInfo();
        statusInfo2.name = "待配送";
        statusInfo2.status = "7";
        statusInfos.add(statusInfo2);
        OrderStatusInfo statusInfo3 = new OrderStatusInfo();
        statusInfo3.name = "配送中";
        statusInfo3.status = "3";
        statusInfos.add(statusInfo3);
        OrderStatusInfo statusInfo4 = new OrderStatusInfo();
        statusInfo4.name = "已完成";
        statusInfo4.status = "4";
        statusInfos.add(statusInfo4);
        OrderStatusInfo statusInfo5 = new OrderStatusInfo();
        statusInfo5.name = "申请退款";
        statusInfo5.status = "5";
        statusInfos.add(statusInfo5);
        return statusInfos;
    }
}
