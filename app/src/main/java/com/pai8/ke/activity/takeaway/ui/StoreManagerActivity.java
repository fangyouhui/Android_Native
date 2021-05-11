package com.pai8.ke.activity.takeaway.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;

import com.blankj.utilcode.util.ToastUtils;
import com.lhs.library.base.BaseAppConstants;
import com.pai8.ke.R;
import com.pai8.ke.activity.takeaway.contract.StoreManagerContract;
import com.pai8.ke.activity.takeaway.entity.resq.StoreInfoResult;
import com.pai8.ke.activity.takeaway.order.WriteOffOrderDetailActivity;
import com.pai8.ke.activity.takeaway.presenter.StoreManagerPresenter;
import com.pai8.ke.activity.takeaway.widget.ShopStatusPop;
import com.pai8.ke.api.Api;
import com.pai8.ke.base.BaseMvpActivity;
import com.pai8.ke.base.retrofit.BaseObserver;
import com.pai8.ke.base.retrofit.RxSchedulers;
import com.pai8.ke.manager.AccountManager;
import com.pai8.ke.utils.ImageLoadUtils;
import com.pai8.ke.widget.BottomDialog;

import org.json.JSONObject;

import java.util.List;

public class StoreManagerActivity extends BaseMvpActivity<StoreManagerPresenter> implements View.OnClickListener, StoreManagerContract.View {

    private BottomDialog mStoreStatusDialog;
    private TextView mTvStatus, mTvIncome, mTvSaleNum;
    private StoreInfoResult mData;

    private ImageView mIvStore;

    private TextView mTvShopName;
    private ActivityResultLauncher activityResultLauncher;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
            if (result.getResultCode() == Activity.RESULT_OK) {
                String orderNo = result.getData().getStringExtra(BaseAppConstants.BundleConstant.ARG_PARAMS_0);
                Api.getInstance().verifyOrder(orderNo, AccountManager.getInstance().getShopId())
                        .doOnSubscribe(disposable -> {

                        })
                        .compose(RxSchedulers.io_main())
                        .subscribe(new BaseObserver<List<String>>() {
                            @Override
                            protected void onSuccess(List<String> shareMiniResp) {
                                Intent intent = new Intent(getBaseContext(), WriteOffOrderDetailActivity.class)
                                        .putExtra(BaseAppConstants.BundleConstant.ARG_PARAMS_0, orderNo);
                                startActivity(intent);
                            }

                            @Override
                            protected void onError(String msg, int errorCode) {
                                //   startActivity(new Intent(getBaseContext(), WriteOffErrorActivity.class));
                                ToastUtils.showShort("核销失败");
                            }
                        });
            }
        });
    }

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
        findViewById(R.id.tvIncomeWithdrawal).setOnClickListener(this);
        findViewById(R.id.tvShopQrCode).setOnClickListener(this::onClick);

        findViewById(R.id.tv_second_manager).setOnClickListener(this);
        findViewById(R.id.tv_marketing_manager).setOnClickListener(this);
        findViewById(R.id.rl_num_rank).setOnClickListener(this);
        findViewById(R.id.rl_income_rank).setOnClickListener(this);
        mTvIncome = findViewById(R.id.tv_income);
        mTvSaleNum = findViewById(R.id.tv_sale_num);
        mTvStatus = findViewById(R.id.tv_status);
        mTvShopName = findViewById(R.id.tv_shop_name);
        mIvStore = findViewById(R.id.iv_store);
        mIvStore.setOnClickListener(this);
        mTvStatus.setOnClickListener(this);
        mPresenter.shopIndex();

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_group_hedui:
//                new IntentIntegrator(this)
//                        .setDesiredBarcodeFormats(IntentIntegrator.ALL_CODE_TYPES)
//                        .setPrompt("请对准二维码进行扫描")
//                        .setOrientationLocked(false)
//                        .setCameraId(0)// 选择摄像头
//                        .setBeepEnabled(true)// 是否开启声音
//                        .setCaptureActivity(ScanActivity.class)
//                        .initiateScan();
                activityResultLauncher.launch(new Intent(this, ScanItActivity.class));
                break;
            case R.id.toolbar_back_all:
                finish();
                break;
            case R.id.rl_income:
                startActivity(new Intent(this, IncomeDetailActivity.class));
                break;
            case R.id.tv_status:
                showBottomDialog();
                break;
            case R.id.tv_good_manager:
                startActivity(new Intent(this, GoodManagerActivity.class));
                break;
            case R.id.tv_order_manager:
                startActivity(new Intent(this, ShopOrderActivity.class));
                break;
            case R.id.iv_store:
            case R.id.toolbar_iv_menu:
                if (mData == null)
                    return;
                Intent intent = new Intent(this, StoreManagerEditActivity.class);
                intent.putExtra("data", mData);
                startActivityForResult(intent, 1000);

                break;
            case R.id.tv_second_manager:
                startActivity(new Intent(this, SecondAdminManagerActivity.class));
                break;
            case R.id.tv_marketing_manager:
                MarketingManagerActivity.launch(this);
                break;
            case R.id.rl_num_rank:  //月销售数量排行
                startActivity(new Intent(this, ShopRankActivity.class)
                        .putExtra("type", 0));
                break;
            case R.id.rl_income_rank:   //月销售收入排行
                startActivity(new Intent(this, ShopRankActivity.class)
                        .putExtra("type", 1));
                break;
            case R.id.tvIncomeWithdrawal: { //收入提现
                startActivity(new Intent(this, ShopWithDrawActivity.class));
                break;
            }

            case R.id.tvShopQrCode: { //店铺二维码
                startActivity(new Intent(this, ShopQrCodeActivity.class));
                break;
            }
        }

    }


    private void showBottomDialog() {
        View view = View.inflate(this, R.layout.dialog_store_status, null);
        ImageButton itnClose = view.findViewById(R.id.itn_close);
        LinearLayout llBus = view.findViewById(R.id.ll_busniess);
        LinearLayout ll_rest = view.findViewById(R.id.ll_rest);

        if (mData != null) {
            if (mData.is_open == 1) {
                llBus.setBackgroundResource(R.drawable.shape_store_business);
                ll_rest.setBackgroundResource(R.drawable.shape_store_rest);
            } else {
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


    public void showStatus(int status) {
        ShopStatusPop shopStatusPop = new ShopStatusPop(this, status);
        shopStatusPop.setOnSelectListener(new ShopStatusPop.OnSelectListener() {
            @Override
            public void onSelect(int status) {
                mPresenter.shopStatus(status + "");
            }
        });
        shopStatusPop.showPopupWindow();
    }


    @Override
    public void getStoreInfo(StoreInfoResult data) {
        mData = data;
        ImageLoadUtils.setCircularImage(this, data.shop_img_url, mIvStore, R.mipmap.ic_launcher);
        mTvShopName.setText(data.shop_name);
        mTvSaleNum.setText(data.month_sale_count);
        mTvIncome.setText(data.month_sale_price);
        if (data.is_open == 1) {
            mTvStatus.setText("营业中");
        } else {
            mTvStatus.setText("未营业");
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1000 && resultCode == RESULT_OK) {
            mPresenter.shopIndex();
        }
    }

    @Override
    public void getStatusSuccess(JSONObject data) {
        mPresenter.shopIndex();
    }
}
