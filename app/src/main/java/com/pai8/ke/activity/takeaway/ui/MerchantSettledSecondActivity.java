package com.pai8.ke.activity.takeaway.ui;

import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.entity.LocalMedia;
import com.pai8.ke.R;
import com.pai8.ke.activity.takeaway.api.TakeawayApi;
import com.pai8.ke.activity.takeaway.entity.req.MerchantSettledReq;
import com.pai8.ke.api.Api;
import com.pai8.ke.app.MyApp;
import com.pai8.ke.base.BaseMvpActivity;
import com.pai8.ke.base.BasePresenter;
import com.pai8.ke.base.retrofit.BaseObserver;
import com.pai8.ke.base.retrofit.RxSchedulers;
import com.pai8.ke.entity.Address;
import com.pai8.ke.utils.ChoosePicUtils;
import com.pai8.ke.utils.ImageLoadUtils;
import com.qiniu.android.http.ResponseInfo;
import com.qiniu.android.storage.UpCompletionHandler;

import org.json.JSONObject;

import java.util.List;

public class MerchantSettledSecondActivity extends BaseMvpActivity implements View.OnClickListener {

    private final int RESULT_LICENSE = 1;  //营业执照
    private final int RESULT_HEALTH_LICENSE = 2;  //卫生许可证
    private final int RESULT_CARD_FRONT = 3;  //身份证正面
    private final int RESULT_CARD_BACK = 4;  //身份证反面
    private final int RESULT_STORE_FRONT = 5;  //店铺正面

    private String mToken;

    private String licensePath, healthLicensePath, cardFrontPath, cardBackPath, storeFrontPath;

    private String storeName;
    private String phone;
    private String email;
    private String cate;
    private String province;
    private String city;
    private String district;
    private String addressDetail;
    private String bankAddress;
    private String bankNo;
    private String addressNumber;
    private Address mAddress;
    private ImageView mIvBusinessLicense;  //营业执照
    private ImageView mIvHealthLicense;  //卫生许可证
    private ImageView mIvCardFrond;  //身份证正面
    private ImageView mIvCardBack;  //身份证反面
    private ImageView mIvStoreFront;  //店铺正面


    private TextView mTvNext;

    @Override
    public BasePresenter initPresenter() {
        return null;
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_merchant_settled_second;
    }

    @Override
    public void initView() {

        setImmersionBar(R.id.base_tool_bar);
        findViewById(R.id.toolbar_back_all).setOnClickListener(this);
        mTvNext = findViewById(R.id.tv_next);
        mTvNext.setOnClickListener(this);
        mIvBusinessLicense = findViewById(R.id.iv_business_license);
        mIvBusinessLicense.setOnClickListener(this);
        mIvHealthLicense = findViewById(R.id.iv_health_license);
        mIvHealthLicense.setOnClickListener(this);
        mIvCardFrond = findViewById(R.id.iv_card_front);
        mIvCardFrond.setOnClickListener(this);
        mIvCardBack = findViewById(R.id.iv_card_back);
        mIvCardBack.setOnClickListener(this);
        mIvStoreFront = findViewById(R.id.iv_store_front);
        mIvStoreFront.setOnClickListener(this);

    }


    @Override
    public void initData() {
        super.initData();
        Intent intent = getIntent();
        storeName = intent.getStringExtra("storeName");
        phone = intent.getStringExtra("phone");
        email = intent.getStringExtra("email");
        cate = intent.getStringExtra("storeName");
        province = intent.getStringExtra("province");
        city = intent.getStringExtra("city");
        district = intent.getStringExtra("district");
        addressDetail = intent.getStringExtra("addressDetail");
        bankAddress = intent.getStringExtra("bankAddress");
        addressNumber = intent.getStringExtra("addressNumber");
        bankNo = intent.getStringExtra("bankNo");
        mAddress = (Address) intent.getSerializableExtra("address");

    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.toolbar_back_all) {
            finish();
        } else if (v.getId() == R.id.tv_next) {
            getToken();
        } else if (v.getId() == R.id.iv_business_license) {

            ChoosePicUtils.picSingle(this, RESULT_LICENSE);

        } else if (v.getId() == R.id.iv_health_license) {

            ChoosePicUtils.picSingle(this, RESULT_HEALTH_LICENSE);

        } else if (v.getId() == R.id.iv_card_front) {

            ChoosePicUtils.picSingle(this, RESULT_CARD_FRONT);

        } else if (v.getId() == R.id.iv_card_back) {

            ChoosePicUtils.picSingle(this, RESULT_CARD_BACK);
        } else if (v.getId() == R.id.iv_store_front) {

            ChoosePicUtils.picSingle(this, RESULT_STORE_FRONT);

        }
    }


    private void getToken() {
        showLoadingDialog("");
        Api.getInstance().getQNToken()
                .doOnSubscribe(disposable -> {
                })
                .compose(RxSchedulers.io_main())
                .subscribe(new BaseObserver<String>() {
                    @Override
                    protected void onSuccess(String token) {
                        mToken = token;
                        MyApp.getMyApp().getUploadManager().put(licensePath, null,
                                mToken, mUpCompletionHandler, null);
                    }

                    @Override
                    protected void onError(String msg, int errorCode) {
                        dismissLoadingDialog();
                        super.onError(msg, errorCode);
                    }
                });
    }


    private UpCompletionHandler mUpCompletionHandler = new UpCompletionHandler() {
        @Override
        public void complete(String key, ResponseInfo info, JSONObject res) {

            if (info.isOK()) {
                try {
                    licensePath = res.getString("key");
                    MyApp.getMyApp().getUploadManager().put(healthLicensePath, null,
                            mToken, mUpCompletionHandler1, null);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                dismissLoadingDialog();
            }
        }


    };

    private UpCompletionHandler mUpCompletionHandler1 = new UpCompletionHandler() {
        @Override
        public void complete(String key, ResponseInfo info, JSONObject res) {
            if (info.isOK()) {
                try {
                    healthLicensePath = res.getString("key");
                    MyApp.getMyApp().getUploadManager().put(cardFrontPath, null,
                            mToken, mUpCompletionHandler2, null);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                dismissLoadingDialog();
            }
        }
    };


    private final UpCompletionHandler mUpCompletionHandler2 = new UpCompletionHandler() {
        @Override
        public void complete(String key, ResponseInfo info, JSONObject res) {

            if (info.isOK()) {
                try {
                    cardFrontPath = res.getString("key");
                    MyApp.getMyApp().getUploadManager().put(cardBackPath, null,
                            mToken, mUpCompletionHandler3, null);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                dismissLoadingDialog();
            }
        }
    };


    private UpCompletionHandler mUpCompletionHandler3 = new UpCompletionHandler() {
        @Override
        public void complete(String key, ResponseInfo info, JSONObject res) {

            if (info.isOK()) {
                try {
                    cardBackPath = res.getString("key");
                    MyApp.getMyApp().getUploadManager().put(storeFrontPath, null,
                            mToken, new UpCompletionHandler() {
                                @Override
                                public void complete(String key, ResponseInfo info, JSONObject res) {

                                    if (info.isOK()) {
                                        try {
                                            storeFrontPath = res.getString("key");
                                            merchantSettled();
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }
                                    }
                                }
                            }, null);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                dismissLoadingDialog();
            }
        }
    };


    private void merchantSettled() {
        MerchantSettledReq red = new MerchantSettledReq();
        red.shop_name = storeName;
        red.mobile = phone;
        red.shop_email = email;
        red.cate_id = cate;
        red.province = province;
        red.city = city;
        red.district = district;
        red.address = addressDetail;
        red.bank_card = bankNo;
        red.bank_address = bankAddress;
        red.business_license = licensePath;
        red.health_permit = healthLicensePath;
        red.idcard_front = cardFrontPath;
        red.idcard_back = cardBackPath;
        red.shop_img = storeFrontPath;
        red.latitude = mAddress.getLat() + "";
        red.longitude = mAddress.getLon() + "";
        red.shop_video = storeFrontPath;
        red.house_number = addressNumber;
        TakeawayApi.getInstance().merchantSettled(red)
                .doOnSubscribe(disposable -> {
                })
                .compose(RxSchedulers.io_main())
                .subscribe(new BaseObserver<String>() {
                    @Override
                    protected void onSuccess(String token) {
                        dismissLoadingDialog();
                        finish();
                        startActivity(new Intent(MerchantSettledSecondActivity.this,
                                MerchantSettledThreeActivity.class));

                    }

                    @Override
                    protected void onError(String msg, int errorCode) {
                        dismissLoadingDialog();
                        super.onError(msg, errorCode);
                    }
                });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {

                case RESULT_LICENSE:   //营业执照
                    setPic(data, 0);

                    break;
                case RESULT_HEALTH_LICENSE:  //卫生许可证
                    setPic(data, 1);

                    break;
                case RESULT_CARD_FRONT:  //身份证正面
                    setPic(data, 2);
                    break;
                case RESULT_CARD_BACK: //身份证反面
                    setPic(data, 3);
                    break;
                case RESULT_STORE_FRONT:  //店铺正面
                    setPic(data, 4);
                    break;

            }
        }
    }

    private void setPic(Intent data, int type) {
        String path = "";
        List<LocalMedia> selectList = PictureSelector.obtainMultipleResult(data);
        if (null != selectList && selectList.size() > 0) {
            if (selectList.get(0).isCompressed()) {
                path = selectList.get(0).getCompressPath();
            } else {
                path = selectList.get(0).getPath();
            }
        }
        if (type == 0) {
            licensePath = path;
            ImageLoadUtils.setRectImage(this, path, mIvBusinessLicense);
        } else if (type == 1) {
            healthLicensePath = path;
            ImageLoadUtils.setRectImage(this, path, mIvHealthLicense);
        } else if (type == 2) {
            cardFrontPath = path;
            ImageLoadUtils.setRectImage(this, path, mIvCardFrond);
        } else if (type == 3) {
            cardBackPath = path;
            ImageLoadUtils.setRectImage(this, path, mIvCardBack);
        } else if (type == 4) {
            storeFrontPath = path;
            ImageLoadUtils.setRectImage(this, path, mIvStoreFront);
        }
        if (TextUtils.isEmpty(licensePath) || TextUtils.isEmpty(healthLicensePath)
                || TextUtils.isEmpty(cardFrontPath) || TextUtils.isEmpty(cardBackPath) || TextUtils.isEmpty(storeFrontPath)) {
            mTvNext.setBackgroundResource(R.drawable.shape_orgin_gradient_gray);
            mTvNext.setEnabled(false);
        } else {
            mTvNext.setBackgroundResource(R.drawable.shape_orgin_gradient);
            mTvNext.setEnabled(true);
        }

    }


}
