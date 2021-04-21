package com.pai8.ke.activity.takeaway.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;

import com.lhs.library.base.BaseAppConstants;
import com.lhs.library.base.BaseFragment;
import com.lhs.library.base.NoViewModel;
import com.pai8.ke.activity.takeaway.entity.OrderDetailResult;
import com.pai8.ke.activity.takeaway.order.UserTakeawayOrderDetailActivity;
import com.pai8.ke.databinding.FragmentOrdetDetailHeadBinding;
import com.pai8.ke.shop.ui.CommentActivity;
import com.pai8.ke.shop.ui.LookCommentActivity;
import com.pai8.ke.shop.ui.PayBottomDialogFragment;
import com.pai8.ke.shop.ui.ShopProductDetailActivity;

import org.jetbrains.annotations.Nullable;

public class OrderDetailHeadFragment extends BaseFragment<NoViewModel, FragmentOrdetDetailHeadBinding> {

    private OrderDetailResult bean;
    private ActivityResultLauncher activityResultLauncher;

    public static OrderDetailHeadFragment newInstance(OrderDetailResult bean) {
        OrderDetailHeadFragment fragment = new OrderDetailHeadFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable(BaseAppConstants.BundleConstant.ARG_PARAMS_0, bean);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(@androidx.annotation.Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bean = (OrderDetailResult) getArguments().getSerializable(BaseAppConstants.BundleConstant.ARG_PARAMS_0);
        activityResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
            if (result.getResultCode() == Activity.RESULT_OK) {
                if (getActivity() instanceof UserTakeawayOrderDetailActivity) {
                    ((UserTakeawayOrderDetailActivity) getActivity()).initData();
                }
            }
        });
    }

    @Override
    public void initView(@Nullable Bundle savedInstanceState) {
        super.initView(savedInstanceState);
        bindViewData(bean);
    }

    private void bindViewData(OrderDetailResult bean) {
        mBinding.btnRight.setVisibility(View.GONE);
        mBinding.btnLeft.setVisibility(View.GONE);
        if (bean.getOrder_status() == 0) {
            mBinding.tvOrderStatus.setText("待支付");
            mBinding.tvStatusName.setText("请在29:59s内进行付款，否则订单讲自动取消");
            mBinding.btnLeft.setVisibility(View.GONE);
            mBinding.btnRight.setText("立即支付");
            mBinding.btnRight.setVisibility(View.VISIBLE);
        } else if (bean.getOrder_status() == 1) {
            mBinding.tvOrderStatus.setText("待商家接单");
            mBinding.tvStatusName.setText("支付成功，请等待商家接单");
        } else if (bean.getOrder_status() == 2) {
            mBinding.tvOrderStatus.setText("商品准备中");
        } else if (bean.getOrder_status() == 3) {
            mBinding.tvOrderStatus.setText("商品配送中");
        } else if (bean.getOrder_status() == 4) {
            mBinding.tvOrderStatus.setText("已完成");
            mBinding.tvStatusName.setText("您已成功使用本次团购服务，请给个评价吧");
            mBinding.btnLeft.setText("再次购买");
            mBinding.btnRight.setText("立即评价");
            mBinding.btnRight.setVisibility(View.VISIBLE);
            mBinding.btnLeft.setVisibility(View.VISIBLE);
        } else if (bean.getOrder_status() == 5) {
            mBinding.tvOrderStatus.setText("退款申请中");
            mBinding.tvStatusName.setText("你发起的退款正在申请审批中，审批成功将为您发起退款");
        } else if (bean.getOrder_status() == 6) {
            mBinding.tvOrderStatus.setText("拒绝退款");
            mBinding.tvStatusName.setText("您发起的退款申请审批未通过，请与商家进行联系");
        } else if (bean.getOrder_status() == 7) {
            mBinding.tvOrderStatus.setText("商品已完成,等待骑手取货");
            mBinding.tvStatusName.setText("商品已完成,骑手正快马加鞭赶往商家取货");
        } else if (bean.getOrder_status() == 8) {
            mBinding.tvOrderStatus.setText("退款成功");
            mBinding.tvStatusName.setText("退款完成，在5~7个工作日内欠款将原路退回到你支付时的账户");
        } else if (bean.getOrder_status() == 9) {
            mBinding.tvOrderStatus.setText("已取消");
            mBinding.tvStatusName.setText("您的订单已经取消，可重新选购下单");
            mBinding.btnLeft.setVisibility(View.GONE);
            mBinding.btnRight.setText("重新下单");
            mBinding.btnRight.setVisibility(View.VISIBLE);
        } else if (bean.getOrder_status() == -1) {
            mBinding.tvOrderStatus.setText("订单超时");
            mBinding.btnLeft.setVisibility(View.GONE);
            mBinding.btnRight.setText("重新下单");
            mBinding.btnRight.setVisibility(View.VISIBLE);
        } else if (bean.getOrder_status() == -2) {
            mBinding.tvOrderStatus.setText("商家拒绝接单");
            mBinding.tvStatusName.setText("商家可能暂停营业或缺货，可主动与商家联系，支付金额将会原路退回到你支付的账户");
        } else if (bean.getOrder_status() == 10) {
            mBinding.tvOrderStatus.setText("已完成");
            mBinding.tvStatusName.setText("你的订单已完成");
            mBinding.btnLeft.setText("重新下单");
            mBinding.btnRight.setText("查看评价");
            mBinding.btnRight.setVisibility(View.VISIBLE);
            mBinding.btnLeft.setVisibility(View.VISIBLE);
        }


        mBinding.btnRight.setOnClickListener(v -> {
            if (bean.getOrder_status() == 9 || -1 == bean.getOrder_status()) {
                Intent intent = new Intent(getContext(), ShopProductDetailActivity.class);
                intent.putExtra(BaseAppConstants.BundleConstant.ARG_PARAMS_0, bean.getShop_id() + "");
                intent.putExtra(BaseAppConstants.BundleConstant.ARG_PARAMS_1, bean.getGoods_info().get(0).getGoods_id() + "");
                startActivity(intent);
            } else if (4 == bean.getOrder_status()) { //评论
                Intent intent = new Intent(getContext(), CommentActivity.class);
                intent.putExtra(BaseAppConstants.BundleConstant.ARG_PARAMS_0, bean.getOrder_no());
                intent.putExtra(BaseAppConstants.BundleConstant.ARG_PARAMS_1, bean.getShop_id() + "");
                intent.putExtra(BaseAppConstants.BundleConstant.ARG_PARAMS_2, bean.getGoods_info().size() + "");
                intent.putExtra(BaseAppConstants.BundleConstant.ARG_PARAMS_3, bean.getGoods_info().get(0));
                activityResultLauncher.launch(intent);
            } else if (0 == bean.getOrder_status()) {//待支付
                PayBottomDialogFragment paySelectBottomDialog = PayBottomDialogFragment.newInstance(bean.getOrder_price(), bean.getOrder_no());
                paySelectBottomDialog.showNow(getChildFragmentManager(), "payWay");
            }
        });

        mBinding.btnLeft.setOnClickListener(v -> {
            if (4 == bean.getOrder_status() || 10 == bean.getOrder_status()) { //再次购买
                mBinding.btnRight.callOnClick();
            }
        });

        mBinding.btnRight.setOnClickListener(v -> {
            if (10 == bean.getOrder_status()) { //查看评价
                Intent intent = new Intent(getContext(), LookCommentActivity.class);
                intent.putExtra(BaseAppConstants.BundleConstant.ARG_PARAMS_0, bean);
                startActivity(intent);
            }
        });

    }

}
