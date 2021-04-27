package com.pai8.ke.activity.takeaway.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.lhs.library.base.BaseAppConstants;
import com.pai8.ke.R;
import com.pai8.ke.activity.takeaway.entity.req.AddFoodReq;
import com.pai8.ke.activity.takeaway.ui.AddGoodActivity;
import com.pai8.ke.activity.takeaway.ui.EditTakeawayGoodActivity;
import com.pai8.ke.utils.ImageLoadUtils;

import java.util.ArrayList;
import java.util.List;

public class TakeawayFoodAdapter extends RvAdapter<AddFoodReq> {

    List<AddFoodReq> goodInfoList = new ArrayList<>();

    public TakeawayFoodAdapter(Context context, List<AddFoodReq> list, RvListener listener) {
        super(context, list, listener);
    }


    @Override
    protected int getLayoutId(int viewType) {
        return viewType == 0 ? R.layout.item_food_title : R.layout.item_takeway_manager_goods;
    }

    @Override
    public int getItemViewType(int position) {
        return list.get(position).isTitle() ? 0 : 1;
    }

    @Override
    protected RvHolder getHolder(View view, int viewType) {
        return new TakeawayFoodAdapter.ClassifyHolder(view, viewType, listener);
    }

    public class ClassifyHolder extends RvHolder<AddFoodReq> {
        TextView tvTitle;

        ImageView ivGoods;
        ImageView ivBtnPlayer;
        TextView tvPrice;
        TextView rlItem;
        TextView tvPriceDiscount;  //折扣价

        public ClassifyHolder(View itemView, int type, RvListener listener) {
            super(itemView, type, listener);
            switch (type) {
                case 0:
                    tvTitle = itemView.findViewById(R.id.tv_title);
                    break;
                case 1:
                    rlItem = itemView.findViewById(R.id.tv_edit_goods);
                    ivGoods = itemView.findViewById(R.id.item_goods_iv_goods);
                    ivBtnPlayer = itemView.findViewById(R.id.iv_btn_player);
                    tvTitle = itemView.findViewById(R.id.item_goods_tv_name);
                    tvPrice = itemView.findViewById(R.id.item_tv_price);
                    tvPriceDiscount = itemView.findViewById(R.id.item_tv_price_discount);
                    break;
            }

        }

        @Override
        public void bindHolder(AddFoodReq food, int position) {
            int itemViewType = TakeawayFoodAdapter.this.getItemViewType(position);

            switch (itemViewType) {
                case 0:
                    tvTitle.setText(food.title);
                    break;
                case 1:

                    if (food.type == 2) {
                        ivBtnPlayer.setVisibility(View.VISIBLE);
                    } else {
                        ivBtnPlayer.setVisibility(View.INVISIBLE);
                    }
                    ivGoods.setOnClickListener(v -> {
                        listener.onItemClick(v.getId(), getAdapterPosition());
                    });
                    //编辑
                    rlItem.setOnClickListener(v -> {
//                        Intent intent = new Intent(mContext, AddGoodActivity.class);
//                        intent.putExtra("type",1);
//                        intent.putExtra("food",food);
//                        mContext.startActivity(intent);

                            Intent intent = new Intent(mContext, EditTakeawayGoodActivity.class);
                            intent.putExtra(BaseAppConstants.BundleConstant.ARG_PARAMS_0, food);
                            mContext.startActivity(intent);
                    });

                    tvTitle.setText(food.title);
                    tvPrice.setText(food.sell_price);


                    if (TextUtils.isEmpty(food.discount) || TextUtils.equals(food.discount, "0")) {
                        tvPriceDiscount.setVisibility(View.GONE);
                    } else {
                        tvPriceDiscount.setVisibility(View.VISIBLE);
                        tvPriceDiscount.setText(food.discount);
                        tvPriceDiscount.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG); //中划线
                    }


                    ImageLoadUtils.setCircularImage(mContext, food.cover, ivGoods, R.mipmap.ic_launcher);


                    break;
            }

        }

    }
}
