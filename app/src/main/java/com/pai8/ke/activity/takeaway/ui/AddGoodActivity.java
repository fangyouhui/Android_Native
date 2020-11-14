package com.pai8.ke.activity.takeaway.ui;

import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;
import com.pai8.ke.R;
import com.pai8.ke.activity.takeaway.adapter.GoodCategoryAdapter;
import com.pai8.ke.activity.takeaway.api.TakeawayApi;
import com.pai8.ke.activity.takeaway.contract.AddGoodContract;
import com.pai8.ke.activity.takeaway.entity.req.AddFoodReq;
import com.pai8.ke.activity.takeaway.entity.req.UpCategoryReq;
import com.pai8.ke.activity.takeaway.entity.resq.ShopInfo;
import com.pai8.ke.activity.takeaway.presenter.AddGoodPresenter;
import com.pai8.ke.activity.takeaway.utils.SoftHideKeyBoardUtil;
import com.pai8.ke.activity.takeaway.widget.ChooseDiscountPricePop;
import com.pai8.ke.activity.takeaway.widget.ChooseShopCoverPop;
import com.pai8.ke.activity.takeaway.widget.ShopCarPop;
import com.pai8.ke.base.BaseMvpActivity;
import com.pai8.ke.base.BasePresenter;
import com.pai8.ke.base.retrofit.BaseObserver;
import com.pai8.ke.base.retrofit.RxSchedulers;
import com.pai8.ke.manager.UploadFileManager;
import com.pai8.ke.utils.ImageLoadUtils;
import com.pai8.ke.utils.ToastUtils;
import com.pai8.ke.widget.BottomDialog;

import java.util.List;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class AddGoodActivity extends BaseMvpActivity implements View.OnClickListener, AddGoodContract.View {

    private AddGoodPresenter p;
    private final int RESULT_PICTURE = 1000;  //图片
    private final int RESULT_VIDEO = 1001;
    private TextView mTvCategory, mTvDiscountPrice;   //分类,折扣加个
    private ImageView mIvCover;  //封面
    private TextView mTvPublish;
    private int cateId;
    private String cateName;
    private EditText mEtName,mEtPrice,mEtDesc;
    private AddFoodReq addFoodReq;
    private int mType;     //3:编辑团购商品


    private TextView mTvTile;
    private BottomDialog mGoodCategoryDialog;


    @Override
    public BasePresenter initPresenter() {
        return new AddGoodPresenter(this);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_add_good;
    }

    @Override
    public void initView() {

        SoftHideKeyBoardUtil.assistActivity(this);
        setImmersionBar(R.id.base_tool_bar);
        findViewById(R.id.toolbar_back_all).setOnClickListener(this);
        mTvPublish = findViewById(R.id.tv_publish);
        mTvPublish.setOnClickListener(this);
        mTvTile = findViewById(R.id.toolbar_title);
        mTvCategory = findViewById(R.id.tv_category);
        mTvCategory.setOnClickListener(this);
        mIvCover = findViewById(R.id.iv_cover);
        mIvCover.setOnClickListener(this);
        mTvDiscountPrice = findViewById(R.id.tv_discount_price);
        mTvDiscountPrice.setOnClickListener(this);
        mEtName = findViewById(R.id.et_name);
        mEtPrice = findViewById(R.id.et_price);
        mEtDesc  = findViewById(R.id.et_desc);

    }


    @Override
    public void initData() {
        super.initData();
        p = new AddGoodPresenter(this);
        addFoodReq = new AddFoodReq();
        mType = getIntent().getIntExtra("type", 0);
        if (mType == 0) {
            mTvTile.setText("添加外卖商品");
        } else if (mType == 1) {
            mTvTile.setText("编辑外卖商品");
        } else if (mType == 2) {
            mTvTile.setText("添加团购商品");
        } else if (mType == 3) {
            mTvTile.setText("编辑团购商品");
        }
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.toolbar_back_all) {
            finish();
        } else if (v.getId() == R.id.tv_category) {
            getCategoryList();

        } else if (v.getId() == R.id.iv_cover) {
            ChooseShopCoverPop pop = new ChooseShopCoverPop(this);
            pop.setOnSelectListener(new ChooseShopCoverPop.OnSelectListener() {
                @Override
                public void onSelect(int type) {
                    choosePicture(type, type == 0 ? RESULT_PICTURE : RESULT_VIDEO);
                }
            });
            pop.showPopupWindow();
        } else if (v.getId() == R.id.tv_discount_price) {
            ChooseDiscountPricePop pricePop = new ChooseDiscountPricePop(this);
            pricePop.setOnSelectListener(new ShopCarPop.OnSelectListener() {
                @Override
                public void onSelect(String content) {
                    mTvDiscountPrice.setText(content);
                }

            });
            pricePop.showPopupWindow();

        } else if(v.getId() == R.id.tv_publish){  //发布

            if(!TextUtils.isEmpty(mFoodPath)){
                UploadFileManager.getInstance().upload(mFoodPath, new UploadFileManager.Callback() {
                    @Override
                    public void onSuccess(String url, String key) {
                        addFoodReq.cover = key;
                        addFoodReq.shop_id = "1";
                        addFoodReq.title = mEtName.getText().toString();  //名称
                        addFoodReq.sell_price = mEtPrice.getText().toString();  //售卖价格
                        addFoodReq.discount = mTvDiscountPrice.getText().toString();
                        addFoodReq.origin_price = "100";  //原价
                        addFoodReq.desc = mEtDesc.getText().toString();
                        addFoodReq.cate_id = cateId+"";
                        p.addGood(addFoodReq);
                    }

                    @Override
                    public void onError(String msg) {
                        ToastUtils.showShort(msg);
                    }
                });
            }



        }
    }


    private void getCategoryList(){
        UpCategoryReq upCategoryReq = new UpCategoryReq();
        upCategoryReq.shop_id = "1";
        TakeawayApi.getInstance().getCategoryList(upCategoryReq)
                .doOnSubscribe(disposable -> {
                })
                .compose(RxSchedulers.io_main())
                .subscribe(new BaseObserver<List<ShopInfo>>() {
                    @Override
                    protected void onSuccess(List<ShopInfo> data) {
                        showBottomDialog(data);
                    }

                    @Override
                    protected void onError(String msg, int errorCode) {
                        super.onError(msg, errorCode);
                    }
                });

    }


    /**
     * 选择相册
     */
    public void choosePicture(int type, int requestCode) {
        //单选相册
        // 进入相册 以下是例子：不需要的api可以不写
        PictureSelector.create(AddGoodActivity.this)
                .openGallery(type == 0 ? PictureMimeType.ofImage() : PictureMimeType.ofVideo())// 全部.PictureMimeType.ofAll()、图片.ofImage()、视频.ofVideo()、音频.ofAudio()
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


    private void showBottomDialog(List<ShopInfo> shopInfoList) {
        View view = View.inflate(this, R.layout.dialog_good_category, null);
        ImageButton itnClose = view.findViewById(R.id.itn_close);
        TextView tvSure = view.findViewById(R.id.tv_next);
        RecyclerView rvGoodCategory = view.findViewById(R.id.rv_good_category);
        GridLayoutManager layoutManager = new GridLayoutManager(this, 3);
        rvGoodCategory.setLayoutManager(layoutManager);


        GoodCategoryAdapter adapter = new GoodCategoryAdapter(shopInfoList);
        rvGoodCategory.setAdapter(adapter);
        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter1, View view, int position) {
                adapter.singleChoose(position);
                cateName = adapter.getData().get(position).name;
                cateId = adapter.getData().get(position).id;
            }
        });

        tvSure.setOnClickListener(v -> {
            mTvCategory.setText(cateName);
            mGoodCategoryDialog.dismiss();
        });

        itnClose.setOnClickListener(view1 -> {
            mGoodCategoryDialog.dismiss();
        });

        if (mGoodCategoryDialog == null) {
            mGoodCategoryDialog = new BottomDialog(this, view);
        }
        mGoodCategoryDialog.setIsCanceledOnTouchOutside(true);
        mGoodCategoryDialog.show();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case RESULT_PICTURE:
                    setPic(data, 0);
                    break;
                case RESULT_VIDEO:
                    setPic(data, 1);
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
        if (type == 0) {  //图片
            ImageLoadUtils.setRectImage(this, path, mIvCover);
            mFoodPath = path;

        } else if (type == 1) {
        }

    }


    private String mFoodPath;

    @Override
    public void addGoodSuccess(String data) {

        ToastUtils.showShort("发布成功");
        finish();
    }
}
