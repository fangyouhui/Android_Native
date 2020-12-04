package com.pai8.ke.activity.takeaway.adapter;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.pai8.ke.R;
import com.pai8.ke.activity.takeaway.Constants;
import com.pai8.ke.activity.takeaway.api.TakeawayApi;
import com.pai8.ke.activity.takeaway.entity.FoodGoodInfo;
import com.pai8.ke.activity.takeaway.entity.OrderGoodInfo;
import com.pai8.ke.activity.takeaway.entity.event.AddGoodEvent;
import com.pai8.ke.base.retrofit.BaseObserver;
import com.pai8.ke.base.retrofit.RxSchedulers;
import com.pai8.ke.manager.AccountManager;
import com.pai8.ke.utils.ImageLoadUtils;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.RequestBody;

public class FoodGoodAdapter extends RvAdapter<FoodGoodInfo> {

    List<FoodGoodInfo> goodInfoList = new ArrayList<>();
    int shopId;

    public FoodGoodAdapter(Context context, List<FoodGoodInfo> list,int shopId,RvListener listener) {
        super(context, list, listener);
        this.shopId = shopId;
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
                    if (goodInfoList != null && goodInfoList.size() > 0) {
                        int num = 0;
                        for (int i = 0; i < goodInfoList.size(); i++) {
                            if (food.id == goodInfoList.get(i).id) {
                                num += goodInfoList.get(i).goods_num;
                            }
                        }
                        tvNum.setText(String.valueOf(num));
                        tvNum.setVisibility(View.VISIBLE);
                        tvReduce.setVisibility(View.VISIBLE);
                    } else {
                        tvNum.setVisibility(View.INVISIBLE);
                        tvReduce.setVisibility(View.INVISIBLE);
                    }



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
                                food.goods_num = num;
                                tvNum.setText(String.valueOf(num));
                                tvReduce.setVisibility(View.VISIBLE);
                                tvNum.setVisibility(View.VISIBLE);
                                goodInfoList.add(food);
                                EventBus.getDefault().post(new AddGoodEvent(
                                        Constants.EVENT_TYPE_ADD_CAR,endXY[0],endXY[1],num,goodInfoList));


                                // net
                                Gson gson = new Gson();
                                List<OrderGoodInfo> goodList = new ArrayList<>();
                                OrderGoodInfo orderGoodInfo = new OrderGoodInfo();
                                orderGoodInfo.goods_price = food.sell_price;
                                orderGoodInfo.goods_num = food.goods_num;
                                orderGoodInfo.packing_price = food.packing_price;
                                orderGoodInfo.goods_id  = food.id;
                                goodList.add(orderGoodInfo);
                                String json = gson.toJson(goodList);
                                addCart(json);

                            } else {
                                int num_add = food.goods_num;
//                                for (int i = 0; i < goodInfoList.size(); i++) {
//                                    num_add += goodInfoList.get(i).num;
//                                }
                                num = num_add + 1;
                                food.goods_num = num;
                                tvNum.setText(String.valueOf(num));
                                tvReduce.setVisibility(View.VISIBLE);
                                tvNum.setVisibility(View.VISIBLE);
                                EventBus.getDefault().post(new AddGoodEvent(
                                        Constants.EVENT_TYPE_ADD_CAR,endXY[0],endXY[1],num,goodInfoList));
                                updateCartNum(1 ,food.id+"",num);
                            }
                        }
                    });

                    tvReduce.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            int num = food.goods_num;
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
                                food.goods_num = 0;
                                goodInfoList.remove(food);
                                updateCartNum(2 ,food.id+"", food.goods_num);
                                EventBus.getDefault().post(new AddGoodEvent(
                                        Constants.EVENT_TYPE_DELETE_CAR,goodInfoList.size(),goodInfoList));
                            } else {
                                num -= 1;
                                food.goods_num = num;
                                tvNum.setVisibility(View.VISIBLE);
                                tvReduce.setVisibility(View.VISIBLE);
                                tvNum.setText(String.valueOf(num));
                                updateSellerGoods(food,num);
                                updateCartNum(2 ,food.id+"",num);
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
                        goodInfoList.get(i).goods_num = num;
                    }
                }
            }
        }
    }


    public void addCart(String goods_info){
        HashMap<String, Object> map = new HashMap<>();
        map.put("goods_info",goods_info);
        map.put("buyer_id", AccountManager.getInstance().getUid());
        map.put("shop_id",shopId+"");
        TakeawayApi.getInstance().addCart(createRequestBody(map))
                .doOnSubscribe(disposable -> {
                })
                .compose(RxSchedulers.io_main())
                .subscribe(new BaseObserver<String>() {
                    @Override
                    protected void onSuccess(String data){

                    }
                    @Override
                    protected void onError(String msg, int errorCode) {
                        super.onError(msg, errorCode);
                    }
                });
    }



    public void updateCartNum(int type ,String goods_id,int num){
        HashMap<String, Object> map = new HashMap<>();
        map.put("type",type+"");   //1 增加 2减少
        map.put("goods_id",goods_id);
        map.put("buyer_id", AccountManager.getInstance().getUid());
        map.put("shop_id",shopId+"");
        map.put("num",num);
        TakeawayApi.getInstance().updateCartNum(createRequestBody(map))
                .doOnSubscribe(disposable -> {
                })
                .compose(RxSchedulers.io_main())
                .subscribe(new BaseObserver<String>() {
                    @Override
                    protected void onSuccess(String data){

                    }
                    @Override
                    protected void onError(String msg, int errorCode) {
                        super.onError(msg, errorCode);
                    }
                });
    }




    public RequestBody createRequestBody(Map map) {
        String json = new Gson().toJson(map);
        RequestBody requestBody = RequestBody.create(json, MediaType.parse("application/json"));
        return requestBody;
    }


}
