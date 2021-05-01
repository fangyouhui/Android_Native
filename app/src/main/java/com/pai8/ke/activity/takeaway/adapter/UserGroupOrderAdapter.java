package com.pai8.ke.activity.takeaway.adapter;

import android.content.Context;
import android.graphics.Color;
import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.lhs.library.base.BaseRecyclerViewAdapter;
import com.lhs.library.base.BaseViewHolder;
import com.pai8.ke.activity.takeaway.entity.GoodsInfo;
import com.pai8.ke.activity.takeaway.entity.OrderListResult;
import com.pai8.ke.databinding.ItemGroupOrderBinding;
import com.pai8.ke.utils.ImageLoadUtils;

import java.util.List;

public class UserGroupOrderAdapter extends BaseRecyclerViewAdapter<OrderListResult> {
    private CountDownTimer countDownTimer;

    public UserGroupOrderAdapter(Context context, List<OrderListResult> list) {
        super(context, list);
    }

    @Override
    protected RecyclerView.ViewHolder onCreateNormalViewHolder(ViewGroup parent, int viewType) {
        ItemGroupOrderBinding binding = ItemGroupOrderBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new GroupOrderViewHolder(binding);
    }

    @Override
    protected void onBindNormalViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {
        if (viewHolder instanceof GroupOrderViewHolder) {
            GroupOrderViewHolder holder = (GroupOrderViewHolder) viewHolder;
            OrderListResult bean = getItem(position);

            ImageLoadUtils.loadImage(bean.getShop_img(), holder.binding.ivShopLogo);
            holder.binding.tvShopName.setText(bean.getShop_name());
            //订单状态 0为待支付 1为已支付 2为商家已接单 7为订单制作完成 3为配送中 4为订单已完成 5为订单已申请退款 6订单被拒绝退款 8为订单已退款 9为订单已取消 -1为支付超时 -2订单拒绝接单 10为订单已评价
            int orderStatus = bean.getOrder_status();
            if (orderStatus == 0) {
                holder.binding.tvOrderStatus.setText("待付款");
                holder.binding.tvOrderStatus.setTextColor(Color.parseColor("#ffff7f47"));
                holder.binding.btnQuXiaoDingDan.setVisibility(View.VISIBLE);
                holder.binding.btnChaKan.setVisibility(View.GONE);
                holder.binding.btnZaiCiGouMai.setVisibility(View.GONE);
                holder.binding.btnLiJiFuKuan.setVisibility(View.VISIBLE);
                holder.binding.btnLiJiFuKuan.setText(timeConversion(bean.getRemain_pay_time()) + " 立即付款");
                holder.binding.btnScan.setVisibility(View.GONE);
                holder.binding.btnEvaluation.setVisibility(View.GONE);
                holder.binding.btnChongXinXiaDan.setVisibility(View.GONE);
                countDownTimer = new CountDownTimer(bean.getRemain_pay_time() * 1000, 1000) {
                    @Override
                    public void onTick(long millisUntilFinished) {
                        int mill = (int) (millisUntilFinished / 1000);
                        holder.binding.btnLiJiFuKuan.setText(timeConversion(mill) + " 立即付款");
                    }

                    @Override
                    public void onFinish() {
                        bean.setOrder_status(-1);
                    }
                };
                countDownTimer.start();
            } else if (orderStatus == -1 || orderStatus == 9) {
                holder.binding.tvOrderStatus.setText("已取消");
                holder.binding.tvOrderStatus.setTextColor(Color.parseColor("#ff999999"));
                holder.binding.btnQuXiaoDingDan.setVisibility(View.GONE);
                holder.binding.btnChaKan.setVisibility(View.GONE);
                holder.binding.btnZaiCiGouMai.setVisibility(View.GONE);
                holder.binding.btnLiJiFuKuan.setVisibility(View.GONE);
                holder.binding.btnScan.setVisibility(View.GONE);
                holder.binding.btnEvaluation.setVisibility(View.GONE);
                holder.binding.btnChongXinXiaDan.setVisibility(View.VISIBLE);
            } else if (orderStatus == 4) {
                holder.binding.tvOrderStatus.setText("已完成");
                holder.binding.tvOrderStatus.setTextColor(Color.parseColor("#ffff7f47"));
                holder.binding.btnQuXiaoDingDan.setVisibility(View.GONE);
                holder.binding.btnChaKan.setVisibility(View.GONE);
                holder.binding.btnZaiCiGouMai.setVisibility(View.VISIBLE);
                holder.binding.btnLiJiFuKuan.setVisibility(View.GONE);
                holder.binding.btnScan.setVisibility(View.GONE);
                holder.binding.btnEvaluation.setVisibility(View.VISIBLE);
                holder.binding.btnChongXinXiaDan.setVisibility(View.GONE);
            } else if (orderStatus == 10) {
                holder.binding.tvOrderStatus.setText("已完成");
                holder.binding.tvOrderStatus.setTextColor(Color.parseColor("#ffff7f47"));
                holder.binding.btnQuXiaoDingDan.setVisibility(View.GONE);
                holder.binding.btnChaKan.setVisibility(View.GONE);
                holder.binding.btnZaiCiGouMai.setVisibility(View.VISIBLE);
                holder.binding.btnLiJiFuKuan.setVisibility(View.GONE);
                holder.binding.btnScan.setVisibility(View.GONE);
                holder.binding.btnEvaluation.setVisibility(View.GONE);
                holder.binding.btnChongXinXiaDan.setVisibility(View.GONE);
            } else if (orderStatus == 1) {
                holder.binding.tvOrderStatus.setText("待使用");
                holder.binding.tvOrderStatus.setTextColor(Color.parseColor("#ffff7f47"));
                holder.binding.btnQuXiaoDingDan.setVisibility(View.GONE);
                holder.binding.btnChaKan.setVisibility(View.VISIBLE);
                holder.binding.btnZaiCiGouMai.setVisibility(View.GONE);
                holder.binding.btnLiJiFuKuan.setVisibility(View.GONE);
                holder.binding.btnScan.setVisibility(View.VISIBLE);
                holder.binding.btnEvaluation.setVisibility(View.GONE);
                holder.binding.btnChongXinXiaDan.setVisibility(View.GONE);
            }
            GoodsInfo goodInfo = bean.getGoods_info().get(0);
            ImageLoadUtils.loadImage(goodInfo.getCover().get(0), holder.binding.ivProductImg);
            holder.binding.tvProductName.setText(goodInfo.getTitle());
            holder.binding.tvDesc.setText(goodInfo.getDesc());
            holder.binding.tvCount2.setText("X" + bean.getGoods_info().size());
            holder.binding.tvSellPrice.setText("¥" + goodInfo.getSell_price());
            //    holder.binding.tvOriginPrice.setText("¥" + goodInfo.getOrigin_price());

            holder.binding.tvShiFuPrice.setText("实付：¥" + bean.getOrder_price());
            holder.binding.tvTotalPrice.setText("总价：¥" + bean.getOrder_price());
            holder.binding.tvDiscountPrice.setText("优惠：¥" + bean.getExpress_discount_price());

            holder.binding.getRoot().setOnClickListener(v -> {
                if (onItemListener != null) {
                    onItemListener.onItemClick(bean, position);
                }
            });

            holder.binding.btnChongXinXiaDan.setOnClickListener(v -> {
                if (onItemListener != null) {
                    onItemListener.onItemReOrder(bean, position);
                }
            });
            holder.binding.btnZaiCiGouMai.setOnClickListener(v -> holder.binding.btnChongXinXiaDan.callOnClick());
            holder.binding.btnEvaluation.setOnClickListener(v -> {
                if (onItemListener != null) {
                    onItemListener.onItemComment(bean, position);
                }

            });
            holder.binding.btnChaKan.setOnClickListener(v -> holder.binding.getRoot().performClick());
            holder.binding.btnScan.setOnClickListener(v -> holder.binding.getRoot().performClick());
        }
    }

    public void destroy() {
        if (countDownTimer != null) {
            countDownTimer.cancel();
            countDownTimer = null;
        }
    }

    private OnItemListener onItemListener;

    public void setOnItemListener(OnItemListener listener) {
        this.onItemListener = listener;
    }

    public interface OnItemListener extends BaseItemTouchListener<OrderListResult> {
        void onItemComment(OrderListResult item, int position);

        void onItemReOrder(OrderListResult item, int position);
    }


    private String timeConversion(int time) {
        int hour = 0;
        int minutes = 0;
        int sencond = 0;
        int temp = time % 3600;
        if (time > 3600) {
            hour = time / 3600;
            if (temp != 0) {
                if (temp > 60) {
                    minutes = temp / 60;
                    if (temp % 60 != 0) {
                        sencond = temp % 60;
                    }
                } else {
                    sencond = temp;
                }
            }
        } else {
            minutes = time / 60;
            if (time % 60 != 0) {
                sencond = time % 60;
            }
        }
        //   return (hour < 10 ? ("0" + hour) : hour) + ":" + (minutes < 10 ? ("0" + minutes) : minutes) + ":" + (sencond < 10 ? ("0" + sencond) : sencond);
        return (minutes < 10 ? ("0" + minutes) : minutes) + ":" + (sencond < 10 ? ("0" + sencond) : sencond);
    }

    class GroupOrderViewHolder extends BaseViewHolder<ItemGroupOrderBinding> {

        public GroupOrderViewHolder(@NonNull ItemGroupOrderBinding viewBinding) {
            super(viewBinding);
        }
    }
}
