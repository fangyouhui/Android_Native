package com.pai8.ke.activity.takeaway.ui;

import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;
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
        bankNo = intent.getStringExtra("bankNo");

    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.toolbar_back_all) {
            finish();
        } else if (v.getId() == R.id.tv_next) {
            getToken();
        } else if (v.getId() == R.id.iv_business_license) {

            choosePicture(RESULT_LICENSE);

        } else if (v.getId() == R.id.iv_health_license) {

            choosePicture(RESULT_HEALTH_LICENSE);

        } else if (v.getId() == R.id.iv_card_front) {

            choosePicture(RESULT_CARD_FRONT);

        } else if (v.getId() == R.id.iv_card_back) {

            choosePicture(RESULT_CARD_BACK);
        } else if (v.getId() == R.id.iv_store_front) {

            choosePicture(RESULT_STORE_FRONT);

        }
    }


    private void getToken() {
//        showLoadingDialog("");
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
        TakeawayApi.getInstance().merchantSettled(red)
                .doOnSubscribe(disposable -> {
                })
                .compose(RxSchedulers.io_main())
                .subscribe(new BaseObserver<String>() {
                    @Override
                    protected void onSuccess(String token) {
                        finish();
                        startActivity(new Intent(MerchantSettledSecondActivity.this, MerchantSettledThreeActivity.class));

                    }

                    @Override
                    protected void onError(String msg, int errorCode) {
                        dismissLoadingDialog();
                        super.onError(msg, errorCode);
                    }
                });

    }


    /**
     * 选择相册
     */
    public void choosePicture(int requestCode) {
        //单选相册
        // 进入相册 以下是例子：不需要的api可以不写
        PictureSelector.create(MerchantSettledSecondActivity.this)
                .openGallery(PictureMimeType.ofImage())// 全部.PictureMimeType.ofAll()、图片.ofImage()、视频.ofVideo()、音频.ofAudio()
//                    .theme(themeId)// 主题样式设置 具体参考 values/styles   用法：R.style.picture.white.style
                .maxSelectNum(1)// 最大图片选择数量
                .minSelectNum(1)// 最小选择数量
                .imageSpanCount(4)// 每行显示个数
                .selectionMode(PictureConfig.SINGLE)// 多选 or 单选
                .previewImage(false)// 是否可预览图片
                .previewVideo(false)// 是否可预览视频
                .enablePreviewAudio(false) // 是否可播放音频
                .isCamera(true)// 是否显示拍照按钮
                .isZoomAnim(false)// 图片列表点击 缩放效果 默认true
//                .imageFormat(PictureMimeType.PNG)// 拍照保存图片格式后缀,默认jpeg
                //.setOutputCameraPath("/CustomPath")// 自定义拍照保存路径
                .enableCrop(false)// 是否裁剪
                .compress(true)// 是否压缩
                .synOrAsy(false)//同步true或异步false 压缩 默认同步
                //.compressSavePath(getPath())//压缩图片保存地址
                //.sizeMultiplier(0.5f)// glide 加载图片大小 0~1之间 如设置 .glideOverride()无效
//                    .glideOverride(160, 160)// glide 加载宽高，越小图片列表越流畅，但会影响列表图片浏览的清晰度
//                .withAspectRatio(1, 1)// 裁剪比例 如16:9 3:2 3:4 1:1 可自定义
//                    .hideBottomControls(false)// 是否显示uCrop工具栏，默认不显示
//                    .isGif(false)// 是否显示gif图片
//                    .freeStyleCropEnabled(true)// 裁剪框是否可拖拽
//                    .circleDimmedLayer(false)// 是否圆形裁剪
                .showCropFrame(true)// 是否显示裁剪矩形边框 圆形裁剪时建议设为false
//                    .showCropGrid(false)// 是否显示裁剪矩形网格 圆形裁剪时建议设为false
                .openClickSound(true)// 是否开启点击声音
                // .selectionMedia(selectList)// 是否传入已选图片
                //.isDragFrame(false)// 是否可拖动裁剪框(固定)
//                        .videoMaxSecond(15)
//                        .videoMinSecond(10)
                //.previewEggs(false)// 预览图片时 是否增强左右滑动图片体验(图片滑动一半即可看到上一张是否选中)
                .cropCompressQuality(100)// 裁剪压缩质量 默认100
                .minimumCompressSize(100)// 小于100kb的图片不压缩
                //.cropWH()// 裁剪宽高比，设置如果大于图片本身宽高则无效
                .rotateEnabled(true) // 裁剪是否可旋转图片
                .scaleEnabled(true)// 裁剪是否可放大缩小图片
                //.videoQuality()// 视频录制质量 0 or 1
                //.videoSecond()//显示多少秒以内的视频or音频也可适用
                //.recordVideoSecond()//录制视频秒数 默认60s
                .forResult(requestCode);//结果回调onActivityResult code
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
        }else{
            mTvNext.setBackgroundResource(R.drawable.shape_orgin_gradient);
            mTvNext.setEnabled(true);
        }

    }


}
