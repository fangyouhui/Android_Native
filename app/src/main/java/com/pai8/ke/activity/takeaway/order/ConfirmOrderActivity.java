package com.pai8.ke.activity.takeaway.order;

import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.pai8.ke.R;
import com.pai8.ke.activity.takeaway.adapter.ConfirmOrderAdapter;
import com.pai8.ke.activity.takeaway.contract.ConfirmContract;
import com.pai8.ke.activity.takeaway.entity.FoodGoodInfo;
import com.pai8.ke.activity.takeaway.entity.OrderGoodInfo;
import com.pai8.ke.activity.takeaway.entity.resq.StoreInfo;
import com.pai8.ke.activity.takeaway.ui.DeliveryAddressActivity;
import com.pai8.ke.base.BaseMvpActivity;
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

    private View mViewHead,mViewFooter;
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
        mRvOrder.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new ConfirmOrderAdapter(null);
        mRvOrder.setAdapter(mAdapter);
        mViewHead = getLayoutInflater().inflate(R.layout.activity_confirm_order_head, (ViewGroup) mRvOrder.getParent(), false);
        mViewFooter = getLayoutInflater().inflate(R.layout.activity_confirm_order_footer, (ViewGroup) mRvOrder.getParent(), false);
        mAdapter.addHeaderView(mViewHead);
        mAdapter.addFooterView(mViewFooter);
        mTvStoreName = mViewHead.findViewById(R.id.tv_store_name);
        mIvStore = mViewHead.findViewById(R.id.iv_store);

    }


    @Override
    public void initData() {
        super.initData();

        mStoreInfo = (StoreInfo) getIntent().getSerializableExtra("storeInfo");
        mFoodInfoList = (List<FoodGoodInfo>) getIntent().getSerializableExtra("shopCar");

        mTvStoreName.setText(mStoreInfo.shop_name);
        ImageLoadUtils.setCircularImage(this, mStoreInfo.shop_img, mIvStore, R.mipmap.ic_launcher);

        mAdapter.setNewData(mFoodInfoList);


    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.toolbar_back_all) {
            finish();
        } else if (v.getId() == R.id.rl_address) {
            Intent intent = new Intent(this,DeliveryAddressActivity.class);
            intent.putExtra("id",mId);
            startActivityForResult(intent,100);

        }else if(v.getId() == R.id.tv_pay){
            Gson gson = new Gson();
            List<OrderGoodInfo> goodList = new ArrayList<>();
            for(int i=0;i<mFoodInfoList.size();i++){
                OrderGoodInfo goodInfo = new OrderGoodInfo();
                goodInfo.goods_id = mFoodInfoList.get(i).id;
                goodInfo.goods_num = mFoodInfoList.get(i).num;
                goodInfo.goods_price = mFoodInfoList.get(i).sell_price;
                goodList.add(goodInfo);
            }
            String json = gson.toJson(goodList);
            mPresenter.addOrder(json,"1",2,mId,"10","10","10");
//            OrderPayPop pop = new OrderPayPop(this);
//            pop.showPopupWindow();


        }
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
                    mId = data.getIntExtra("id",0);
                    mTvAddress.setText(mAddress);
                    mTvName.setVisibility(View.VISIBLE);
                    mTvName.setText(mName + "     " + mPhone);

                    break;
            }
        }
    }

}
