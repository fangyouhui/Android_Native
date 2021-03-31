package com.pai8.ke.activity.takeaway.ui;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.zxing.integration.android.IntentIntegrator;
import com.pai8.ke.R;
import com.pai8.ke.activity.common.ScanActivity;
import com.pai8.ke.activity.takeaway.contract.StoreManagerContract;
import com.pai8.ke.activity.takeaway.entity.resq.StoreInfo;
import com.pai8.ke.activity.takeaway.presenter.StoreManagerPresenter;
import com.pai8.ke.activity.takeaway.widget.ShopStatusPop;
import com.pai8.ke.base.BaseMvpActivity;
import com.pai8.ke.utils.ImageLoadUtils;
import com.pai8.ke.widget.BottomDialog;

import androidx.annotation.Nullable;

public class StoreManagerActivity extends BaseMvpActivity<StoreManagerPresenter> implements View.OnClickListener, StoreManagerContract.View {

    private BottomDialog mStoreStatusDialog;
    private TextView mTvStatus,mTvIncome,mTvSaleNum;
    private StoreInfo mData;

    private ImageView mIvStore;

    private TextView mTvShopName;


    @Override
    public StoreManagerPresenter initPresenter() {
        return new StoreManagerPresenter(this);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_store_manager;
    }

    @Override
    public void initView() {

        setImmersionBar(R.id.iv_top);
        findViewById(R.id.rl_income).setOnClickListener(this);
        findViewById(R.id.toolbar_back_all).setOnClickListener(this);
        findViewById(R.id.tv_order_manager).setOnClickListener(this);
        findViewById(R.id.tv_good_manager).setOnClickListener(this);
        findViewById(R.id.toolbar_iv_menu).setOnClickListener(this);
        findViewById(R.id.tv_group_hedui).setOnClickListener(this);

        findViewById(R.id.tv_second_manager).setOnClickListener(this);
        findViewById(R.id.tv_marketing_manager).setOnClickListener(this);
        findViewById(R.id.rl_num_rank).setOnClickListener(this);
        findViewById(R.id.rl_income_rank).setOnClickListener(this);
        mTvIncome  = findViewById(R.id.tv_income);
        mTvSaleNum  = findViewById(R.id.tv_sale_num);
        mTvStatus = findViewById(R.id.tv_status);
        mTvShopName = findViewById(R.id.tv_shop_name);
        mIvStore = findViewById(R.id.iv_store);
        mIvStore.setOnClickListener(this);
        mTvStatus.setOnClickListener(this);
        mPresenter.shopIndex();

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){

            case R.id.tv_group_hedui:
                new IntentIntegrator(this)
                        .setDesiredBarcodeFormats(IntentIntegrator.ALL_CODE_TYPES)
                        .setPrompt("请对准二维码进行扫描")
                        .setOrientationLocked(false)
                        .setCameraId(0)// 选择摄像头
                        .setBeepEnabled(true)// 是否开启声音
                        .setCaptureActivity(ScanActivity.class)
                        .initiateScan();
                break;

            case R.id.toolbar_back_all:
                finish();
                break;
            case R.id.rl_income:
                startActivity(new Intent(this,IncomeDetailActivity.class));
                break;
            case R.id.tv_status:
                showBottomDialog();
                break;
            case R.id.tv_good_manager:
                startActivity(new Intent(this,GoodManagerActivity.class));
                break;
            case R.id.tv_order_manager:
                startActivity(new Intent(this,OrderProcessingActivity.class));
                break;
            case R.id.iv_store:
            case R.id.toolbar_iv_menu:
                if(mData==null)
                    return;
                Intent intent = new Intent(this,StoreManagerEditActivity.class);
                intent.putExtra("data",mData);
                startActivityForResult(intent,1000);

                break;
            case R.id.tv_second_manager:
                startActivity(new Intent(this,SecondAdminManagerActivity.class));
                break;
            case R.id.tv_marketing_manager:
                MarketingManagerActivity.launch(this);
                break;
            case R.id.rl_num_rank:  //月销售数量排行
                startActivity(new Intent(this,ShopRankActivity.class)
                .putExtra("type",0));
                break;
            case R.id.rl_income_rank:   //月销售收入排行
                startActivity(new Intent(this,ShopRankActivity.class)
                        .putExtra("type",1));
                break;
        }

    }


    private void showBottomDialog() {
        View view = View.inflate(this, R.layout.dialog_store_status, null);
        ImageButton itnClose = view.findViewById(R.id.itn_close);
        LinearLayout llBus  = view.findViewById(R.id.ll_busniess);
        LinearLayout ll_rest = view.findViewById(R.id.ll_rest);

        if(mData!=null){
            if(mData.is_open == 1){
                llBus.setBackgroundResource(R.drawable.shape_store_business);
                ll_rest.setBackgroundResource(R.drawable.shape_store_rest);
            }else{
                llBus.setBackgroundResource(R.drawable.shape_store_rest);
                ll_rest.setBackgroundResource(R.drawable.shape_store_business);
            }
        }


        itnClose.setOnClickListener(view1 -> {
            mStoreStatusDialog.dismiss();
        });

        ll_rest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showStatus(2);
                mStoreStatusDialog.dismiss();
            }
        });
        llBus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showStatus(1);
                mStoreStatusDialog.dismiss();
            }
        });

        if (mStoreStatusDialog == null) {
            mStoreStatusDialog = new BottomDialog(this, view);
        }
        mStoreStatusDialog.setIsCanceledOnTouchOutside(true);
        mStoreStatusDialog.show();
    }



    public void showStatus(int status){
        ShopStatusPop shopStatusPop = new ShopStatusPop(this,status);
        shopStatusPop.setOnSelectListener(new ShopStatusPop.OnSelectListener() {
            @Override
            public void onSelect(int status) {
                mPresenter.shopStatus(status+"");
            }
        });
        shopStatusPop.showPopupWindow();
    }



    @Override
    public void getStoreInfo(StoreInfo data) {
        mData = data;
        ImageLoadUtils.setCircularImage(this,data.shop_img_url,mIvStore,R.mipmap.ic_launcher);
        mTvShopName.setText(data.shop_name);
        mTvSaleNum.setText(data.month_sale_count);
        mTvIncome.setText(data.month_sale_price);
        if(data.is_open == 1){
            mTvStatus.setText("营业中");
        }else{
            mTvStatus.setText("未营业");
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1000 && resultCode == RESULT_OK){
            mPresenter.shopIndex();
        }
    }

    @Override
    public void getStatusSuccess(String data) {
        mPresenter.shopIndex();
    }
}
