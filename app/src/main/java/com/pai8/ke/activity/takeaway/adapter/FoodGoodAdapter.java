package com.pai8.ke.activity.takeaway.adapter;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.pai8.ke.R;
import com.pai8.ke.activity.takeaway.Constants;
import com.pai8.ke.activity.takeaway.entity.FoodGoodInfo;
import com.pai8.ke.activity.takeaway.entity.event.AddGoodEvent;
import com.pai8.ke.utils.ImageLoadUtils;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

public class FoodGoodAdapter extends RvAdapter<FoodGoodInfo> {

    List<FoodGoodInfo> goodInfoList = new ArrayList<>();

    public FoodGoodAdapter(Context context, List<FoodGoodInfo> list, RvListener listener) {
        super(context, list, listener);
    }


    @Override
    protected int getLayoutId(int viewType) {
        return viewType == 0 ? R.layout.item_food_title : R.layout.item_store_goods;
    }

    @Override
    public int getItemViewType(int position) {
        return list.get(position).isTitle() ? 0 : 1;
    }

    @Override
    protected RvHolder getHolder(View view, int viewType) {
        return new ClassifyHolder(view, viewType, listener);
    }

    public class ClassifyHolder extends RvHolder<FoodGoodInfo> {
        TextView tvTitle;

        TextView tvAddGoods;
        TextView tvReduce;
        TextView tvNum;
        ImageView ivGoods;
        TextView tvPrice;

        public ClassifyHolder(View itemView, int type, RvListener listener) {
            super(itemView, type, listener);
            switch (type) {
                case 0:
                    tvTitle = itemView.findViewById(R.id.tv_title);
                    break;
                case 1:
                    tvAddGoods = itemView.findViewById(R.id.tv_add_goods);
                    tvReduce = itemView.findViewById(R.id.tv_reduce_goods);
                    tvNum = itemView.findViewById(R.id.tv_num);
                    ivGoods = itemView.findViewById(R.id.item_goods_iv_goods);
                    tvTitle = itemView.findViewById(R.id.item_goods_tv_name);
                    tvPrice = itemView.findViewById(R.id.item_tv_price);
                    break;
            }

        }

        @Override
        public void bindHolder(FoodGoodInfo food, int position) {
            int itemViewType = FoodGoodAdapter.this.getItemViewType(position);
            List<FoodGoodInfo> currentList = new ArrayList<>();

            switch (itemViewType) {
                case 0:
                    tvTitle.setText(food.name);
                    break;
                case 1:
                    tvTitle.setText(food.title);
                    tvPrice.setText(food.sell_price);
                    ImageLoadUtils.setCircularImage(mContext,food.cover,ivGoods,R.mipmap.ic_launcher);
                    tvAddGoods.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            final int[] endXY = new int[2];
                            v.getLocationInWindow(endXY);
                            boolean isAdd = false;//不存在相同ID
                            if (goodInfoList.size() > 0) {
                                for (int i = 0; i < goodInfoList.size(); i++) {
                                    if (goodInfoList.get(i).id == food.id) {
                                        isAdd = true;
                                    }
                                }
                            }
                            int num = 0;
                            if (!isAdd) {
                                num = num + 1;
                                food.num = num;
                                tvNum.setText(String.valueOf(num));
                                tvReduce.setVisibility(View.VISIBLE);
                                tvNum.setVisibility(View.VISIBLE);
                                goodInfoList.add(food);
                                EventBus.getDefault().post(new AddGoodEvent(
                                        Constants.EVENT_TYPE_ADD_CAR,endXY[0],endXY[1],num,goodInfoList));
                            } else {
                                int num_add = food.num;
//                                for (int i = 0; i < goodInfoList.size(); i++) {
//                                    num_add += goodInfoList.get(i).num;
//                                }
                                num = num_add + 1;
                                food.num = num;
                                tvNum.setText(String.valueOf(num));
                                tvReduce.setVisibility(View.VISIBLE);
                                tvNum.setVisibility(View.VISIBLE);
                                EventBus.getDefault().post(new AddGoodEvent(
                                        Constants.EVENT_TYPE_ADD_CAR,endXY[0],endXY[1],num,goodInfoList));

                            }
                        }
                    });

                    tvReduce.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            int num = food.num;
                            int position = 0;
//                            for (int i = 0; i < currentList.size(); i++) {
//                                num += currentList.get(i).num;
//                                position = i;
//                            }
                            if (num <= 1) {
                                //删除当前的商品
                                tvNum.setVisibility(View.INVISIBLE);
                                tvReduce.setVisibility(View.INVISIBLE);
                                tvNum.setText("");
                                food.num = 0;
                                goodInfoList.remove(food);
                                EventBus.getDefault().post(new AddGoodEvent(
                                        Constants.EVENT_TYPE_DELETE_CAR,goodInfoList.size(),goodInfoList));
                            } else {
                                num -= 1;
                                food.num = num;
                                tvNum.setVisibility(View.VISIBLE);
                                tvReduce.setVisibility(View.VISIBLE);
                                tvNum.setText(String.valueOf(num));
                                updateSellerGoods(food,num);
                                EventBus.getDefault().post(new AddGoodEvent(
                                        Constants.EVENT_TYPE_DELETE_CAR,goodInfoList.size(),goodInfoList));                             }
                        }

                    });

                    break;
            }

        }

        public  void deleteSellerGoods(List<FoodGoodInfo> sellerGoods) {
            if (sellerGoods != null && sellerGoods.size() > 0) {
                for (int i = 0; i < sellerGoods.size(); i++) {
                    for(int j=0;j<goodInfoList.size();j++){
                        if(sellerGoods.get(i).id == goodInfoList.get(j).id){
                            goodInfoList.remove(j);
                        }
                    }
                }
            }
        }


        public  void updateSellerGoods(FoodGoodInfo food,int num) {
            if (food != null) {
                for (int i = 0; i < goodInfoList.size(); i++) {
                    if(food.id == goodInfoList.get(i).id){
                        goodInfoList.get(i).num = num;
                    }
                }
            }
        }
    }




}
