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
import com.pai8.ke.activity.me.AddressChooseActivity;
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
import com.pai8.ke.utils.ChoosePicUtils;
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
                    ChoosePicUtils.picSingle(AddGoodActivity.this, type, type == 0 ? RESULT_PICTURE :
                            RESULT_VIDEO);
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
