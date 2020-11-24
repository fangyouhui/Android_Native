package com.pai8.ke.activity.takeaway.order;

import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bigkoo.pickerview.builder.OptionsPickerBuilder;
import com.bigkoo.pickerview.listener.OnOptionsSelectListener;
import com.bigkoo.pickerview.view.OptionsPickerView;
import com.google.gson.Gson;
import com.pai8.ke.R;
import com.pai8.ke.activity.takeaway.adapter.ConfirmOrderAdapter;
import com.pai8.ke.activity.takeaway.contract.ConfirmContract;
import com.pai8.ke.activity.takeaway.entity.FoodGoodInfo;
import com.pai8.ke.activity.takeaway.entity.OrderGoodInfo;
import com.pai8.ke.activity.takeaway.entity.resq.StoreInfo;
import com.pai8.ke.activity.takeaway.entity.resq.WaimaiResq;
import com.pai8.ke.activity.takeaway.presenter.ConfirmOrderPresenter;
import com.pai8.ke.activity.takeaway.ui.DeliveryAddressActivity;
import com.pai8.ke.base.BaseMvpActivity;
import com.pai8.ke.fragment.pay.PayDialogFragment;
import com.pai8.ke.utils.ImageLoadUtils;

import java.util.ArrayList;
import java.util.List;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class ConfirmOrderActivity extends BaseMvpActivity<ConfirmOrderPresenter> implements View.OnClickListener, ConfirmContract.View {

    private RecyclerView mRvOrder;
    private TextView mTvPay;
    public String mName, mPhone, mAddress;
    public int mId;
    private TextView mTvName, mTvAddress;
    public static final int ACTIVITY_CONFIRM_ORDER = R.layout.activity_confirm_order;
    private View mViewHead, mViewFooter;
    private TextView mTvCoupon;
    private TextView mTvSendPrice;
    private TextView mTvPackPrice;
    private TextView mTvSendTime;


    private TextView mTvPirice;


    private OptionsPickerView pvTime;


    private ConfirmOrderAdapter mAdapter;
    private TextView mTvStoreName;
    private ImageView mIvStore;
    private StoreInfo mStoreInfo;
    private List<FoodGoodInfo> mFoodInfoList;


    @Override
    public ConfirmOrderPresenter initPresenter() {
        return new ConfirmOrderPresenter(this);
    }

    @Override
    public int getLayoutId() {
        return ACTIVITY_CONFIRM_ORDER;
    }

    @Override
    public void initView() {
        setImmersionBar(R.id.base_tool_bar);
        findViewById(R.id.toolbar_back_all).setOnClickListener(this);
        findViewById(R.id.rl_address).setOnClickListener(this);
        mTvPay = findViewById(R.id.tv_pay);
        mTvPay.setOnClickListener(this);
        mTvAddress = findViewById(R.id.tv_address);
        mTvName = findViewById(R.id.tv_name);
        mRvOrder = findViewById(R.id.rv_order_food);
        mTvSendTime = findViewById(R.id.tv_send_time);
        mTvSendTime.setOnClickListener(this);
        mRvOrder.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new ConfirmOrderAdapter(null);
        mRvOrder.setAdapter(mAdapter);
        mViewHead = getLayoutInflater().inflate(R.layout.activity_confirm_order_head, (ViewGroup) mRvOrder.getParent(), false);
        mViewFooter = getLayoutInflater().inflate(R.layout.activity_confirm_order_footer, (ViewGroup) mRvOrder.getParent(), false);
        mAdapter.addHeaderView(mViewHead);
        mAdapter.addFooterView(mViewFooter);
        mTvStoreName = mViewHead.findViewById(R.id.tv_store_name);
        mIvStore = mViewHead.findViewById(R.id.iv_store);
        mTvPackPrice = mViewFooter.findViewById(R.id.tv_pack_price);
        mTvCoupon = mViewFooter.findViewById(R.id.tv_coupon);
        mTvSendPrice = mViewFooter.findViewById(R.id.tv_send_price);
        mTvPirice = mViewFooter.findViewById(R.id.tv_price);

    }



    @Override
    public void initData() {
        super.initData();

        mStoreInfo = (StoreInfo) getIntent().getSerializableExtra("storeInfo");
        mFoodInfoList = (List<FoodGoodInfo>) getIntent().getSerializableExtra("shopCar");

        mTvStoreName.setText(mStoreInfo.shop_name);
        ImageLoadUtils.setCircularImage(this, mStoreInfo.shop_img, mIvStore, R.mipmap.ic_launcher);

        mAdapter.setNewData(mFoodInfoList);
        double price = 0;
        for(int i=0;i<mFoodInfoList.size();i++){
            if(!TextUtils.isEmpty(mFoodInfoList.get(i).packing_price)){
                price += Double.parseDouble(mFoodInfoList.get(i).packing_price.trim());
            }
        }
        mTvPackPrice.setText(price+"");
        setPrice(mFoodInfoList);

    }

    private double price;


    public void setPrice(List<FoodGoodInfo> mShopCarGoods) {
        int shopNum = 0;
        double toMoney = 0;
        boolean discount = false;
        double originalTotlMoney = 0;
        for (FoodGoodInfo pro : mShopCarGoods) {
            if (!TextUtils.isEmpty(pro.sell_price)) {
                toMoney += (Double.parseDouble(pro.sell_price) * pro.num);
            }
            shopNum = shopNum + pro.num;
        }
        price = toMoney;
        mTvPirice.setText("￥" + toMoney);

    }


    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.toolbar_back_all) {
            finish();
        } else if (v.getId() == R.id.rl_address) {
            Intent intent = new Intent(this, DeliveryAddressActivity.class);
            intent.putExtra("id", mId);
            startActivityForResult(intent, 100);
        } else if(v.getId() == R.id.tv_send_time){
            time();
        } else if (v.getId() == R.id.tv_pay) {
            Gson gson = new Gson();
            List<OrderGoodInfo> goodList = new ArrayList<>();
            for (int i = 0; i < mFoodInfoList.size(); i++) {
                OrderGoodInfo goodInfo = new OrderGoodInfo();
                goodInfo.goods_id = mFoodInfoList.get(i).id;
                goodInfo.goods_num = mFoodInfoList.get(i).num;
                goodInfo.goods_price = mFoodInfoList.get(i).sell_price;
                goodList.add(goodInfo);
            }
            String json = gson.toJson(goodList);
            mPresenter.addOrder(json, mStoreInfo.id+"", 2, mId, "10", "", "",mTvPackPrice.getText().toString());


        }
    }


    private void time(){
        List<String> list = new ArrayList<>();
        list.add("1天");
        list.add("2天");
        if (pvTime == null) {
            pvTime = new OptionsPickerBuilder(ConfirmOrderActivity.this, new OnOptionsSelectListener() {
                @Override
                public void onOptionsSelect(int options1, int option2, int options3, View v) {
                    //返回的分别是三个级别的选中位置
                    String tx = list.get(options1);
                    mTvSendTime.setText(tx);

                }
            })
                    .setDecorView(findViewById(R.id.rl_merchant))
                    .build();
        }
        pvTime.setPicker(list);
        pvTime.show();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (null != data) {
            switch (requestCode) {
                case 100:
                    mName = data.getStringExtra("name");
                    mPhone = data.getStringExtra("phone");
                    mAddress = data.getStringExtra("address");
                    mId = data.getIntExtra("id", 0);
                    mTvAddress.setText(mAddress);
                    mTvName.setVisibility(View.VISIBLE);
                    mTvName.setText(mName + "     " + mPhone);
                    mPresenter.waimaiPrice(mStoreInfo.id,mId);

                    break;
            }
        }
    }

    @Override
    public void orderSuccess(String data) {
        PayDialogFragment payDialogFragment = PayDialogFragment.newInstance(price+"", data);
        payDialogFragment.show(getSupportFragmentManager(), "pay");

    }

    @Override
    public void waimaiSuccess(WaimaiResq data) {
        mTvCoupon.setText(data.coupon);
        mTvSendPrice.setText(data.amount);
    }
}
