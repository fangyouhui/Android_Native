package com.pai8.ke.activity.takeaway.adapter;

import com.google.gson.Gson;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.pai8.ke.R;
import com.pai8.ke.activity.takeaway.Constants;
import com.pai8.ke.activity.takeaway.api.TakeawayApi;
import com.pai8.ke.activity.takeaway.entity.FoodGoodInfo;
import com.pai8.ke.activity.takeaway.entity.event.CartNumEvent;
import com.pai8.ke.base.retrofit.BaseObserver;
import com.pai8.ke.base.retrofit.RxSchedulers;
import com.pai8.ke.manager.AccountManager;
import com.pai8.ke.utils.ImageLoadUtils;

import org.greenrobot.eventbus.EventBus;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import okhttp3.MediaType;
import okhttp3.RequestBody;

public class ShopCarAdapter extends BaseQuickAdapter<FoodGoodInfo, BaseViewHolder> {

    int shopId;

    public ShopCarAdapter(@Nullable List<FoodGoodInfo> data, int shopId) {
        super(R.layout.item_shop_car, data);
        this.shopId = shopId;
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, FoodGoodInfo item) {
        ImageLoadUtils.setCircularImage(mContext, item.cover, helper.getView(R.id.iv_food), R.mipmap.ic_launcher);

        helper.setText(R.id.tv_food_name, item.title);
        helper.setText(R.id.tv_num, "*" + item.goods_num);
        helper.setText(R.id.tv_price, "" + item.sell_price);
        helper.setText(R.id.tv_add_num, "" + item.goods_num);

        helper.getView(R.id.tv_add_goods).setOnClickListener(v -> {
            updateCartNum(1, item.id, helper.getLayoutPosition());
        });

        helper.getView(R.id.tv_reduce_goods).setOnClickListener(v -> {
            int num = item.goods_num;
            updateCartNum(2, item.id, helper.getLayoutPosition());
        });
    }

    private void updateCartNum(int type, int goods_id, int postion) {
        HashMap<String, Object> map = new HashMap<>();
        //1 增加  2减少
        map.put("type", type + "");
        map.put("goods_id", goods_id + "");
        map.put("buyer_id", AccountManager.getInstance().getUid());
        map.put("shop_id", shopId + "");
        map.put("num", 1);
        TakeawayApi.getInstance().updateCartNum(createRequestBody(map))
                .doOnSubscribe(disposable -> {
                })
                .compose(RxSchedulers.io_main())
                .subscribe(new BaseObserver<String>() {
                    @Override
                    protected void onSuccess(String data) {
                        if (type == 1) {
                            mData.get(postion).goods_num ++ ;
                            EventBus.getDefault().post(new CartNumEvent(
                                    Constants.EVENT_TYPE_ADD_CAR, mData.get(postion).goods_num ++, goods_id));
                        } else {
                            if(-- mData.get(postion).goods_num == 0){
                                mData.remove(postion);
                                EventBus.getDefault().post(new CartNumEvent(
                                        Constants.EVENT_TYPE_DELETE_CAR,0, goods_id));
                            }else{
                                EventBus.getDefault().post(new CartNumEvent(
                                        Constants.EVENT_TYPE_DELETE_CAR, mData.get(postion).goods_num, goods_id));
                            }
                        }
                        notifyDataSetChanged();
                    }

                    @Override
                    protected void onError(String msg, int errorCode) {
                        super.onError(msg, errorCode);
                    }
                });
    }

    public RequestBody createRequestBody(Map map) {
        String json = new Gson().toJson(map);
        return RequestBody.create(json, MediaType.parse("application/json"));
    }
}
