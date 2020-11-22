package com.pai8.ke.activity.takeaway.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.pai8.ke.R;
import com.pai8.ke.activity.takeaway.entity.req.AddFoodReq;
import com.pai8.ke.activity.takeaway.ui.AddGoodActivity;
import com.pai8.ke.utils.ImageLoadUtils;

import java.util.ArrayList;
import java.util.List;

public class TakeawayFoodAdapter  extends RvAdapter<AddFoodReq> {

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
        TextView tvPrice;
        TextView rlItem;

        public ClassifyHolder(View itemView, int type, RvListener listener) {
            super(itemView, type, listener);
            switch (type) {
                case 0:
                    tvTitle = itemView.findViewById(R.id.tv_title);
                    break;
                case 1:

                    rlItem = itemView.findViewById(R.id.tv_edit_goods);


                    ivGoods = itemView.findViewById(R.id.item_goods_iv_goods);
                    tvTitle = itemView.findViewById(R.id.item_goods_tv_name);
                    tvPrice = itemView.findViewById(R.id.item_tv_price);
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

                    rlItem.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(mContext, AddGoodActivity.class);
                            intent.putExtra("type",1);
                            intent.putExtra("food",food);
                            mContext.startActivity(intent);
                        }
                    });

                    tvTitle.setText(food.title);
                    tvPrice.setText(food.sell_price);
                    ImageLoadUtils.setCircularImage(mContext, food.cover, ivGoods, R.mipmap.ic_launcher);


                    break;
            }

        }

    }
}
