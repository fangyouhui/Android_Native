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
import com.luck.picture.lib.entity.LocalMedia;
import com.pai8.ke.R;
import com.pai8.ke.activity.takeaway.adapter.GoodCategoryAdapter;
import com.pai8.ke.activity.takeaway.api.TakeawayApi;
import com.pai8.ke.activity.takeaway.contract.AddGoodContract;
import com.pai8.ke.activity.takeaway.entity.event.NotifyEvent;
import com.pai8.ke.activity.takeaway.entity.req.AddFoodReq;
import com.pai8.ke.activity.takeaway.entity.req.UpCategoryReq;
import com.pai8.ke.activity.takeaway.entity.resq.ShopInfo;
import com.pai8.ke.activity.takeaway.presenter.AddGoodPresenter;
import com.pai8.ke.activity.takeaway.utils.SoftHideKeyBoardUtil;
import com.pai8.ke.activity.takeaway.widget.ChooseDiscountPricePop;
import com.pai8.ke.activity.takeaway.widget.ChooseShopCoverPop;
import com.pai8.ke.activity.takeaway.widget.ShopCarPop;
import com.pai8.ke.base.BaseMvpActivity;
import com.pai8.ke.base.retrofit.BaseObserver;
import com.pai8.ke.base.retrofit.RxSchedulers;
import com.pai8.ke.manager.AccountManager;
import com.pai8.ke.manager.UploadFileManager;
import com.pai8.ke.utils.ChoosePicUtils;
import com.pai8.ke.utils.ImageLoadUtils;
import com.pai8.ke.utils.ToastUtils;
import com.pai8.ke.widget.BottomDialog;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import static com.pai8.ke.activity.takeaway.Constants.EVENT_TYPE_REFRESH_SHOP_GOOD;

public class AddGoodActivity extends BaseMvpActivity<AddGoodPresenter> implements View.OnClickListener, AddGoodContract.View {

    private final int RESULT_PICTURE = 1000;  //图片
    private final int RESULT_VIDEO = 1001;
    private TextView mTvCategory, mTvDiscountPrice;   //分类,折扣加个
    private ImageView mIvCover;  //封面
    private TextView mTvPublish;
    private String cateId;
    private String cateName;
    private EditText mEtName, mEtPrice, mEtDesc, mEtPackPrice;
    private AddFoodReq addFoodReq;
    private int mType;     //3:编辑团购商品

    private AddFoodReq mFood;

    private TextView mTvDel;

    private TextView mTvTile;
    private BottomDialog mGoodCategoryDialog;


    @Override
    public AddGoodPresenter initPresenter() {
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
        mTvDel = findViewById(R.id.tv_del);
        mTvDel.setOnClickListener(this);
        mTvTile = findViewById(R.id.toolbar_title);
        mTvCategory = findViewById(R.id.tv_category);
        mTvCategory.setOnClickListener(this);
        mIvCover = findViewById(R.id.iv_cover);
        mIvCover.setOnClickListener(this);
        mTvDiscountPrice = findViewById(R.id.tv_discount_price);
        mTvDiscountPrice.setOnClickListener(this);
        mEtName = findViewById(R.id.et_name);
        mEtPrice = findViewById(R.id.et_price);
        mEtDesc = findViewById(R.id.et_desc);
        mEtPackPrice = findViewById(R.id.et_pack_price);

    }


    @Override
    public void initData() {
        super.initData();
        addFoodReq = new AddFoodReq();
        mType = getIntent().getIntExtra("type", 0);
        if (mType == 0) {
            mTvTile.setText("添加外卖商品");
        } else if (mType == 1) {
            mTvDel.setVisibility(View.VISIBLE);
            mTvPublish.setText("确定");
            mTvTile.setText("编辑外卖商品");
            mFood = (AddFoodReq) getIntent().getSerializableExtra("food");
            mTvCategory.setText(mFood.cateName);
            ImageLoadUtils.setCircularImage(this, mFood.cover, mIvCover, R.mipmap.ic_launcher);
            mTvDiscountPrice.setText(mFood.discount);
            mEtName.setText(mFood.title);
            cateId = mFood.cate_id+"";
            mEtPrice.setText(mFood.sell_price);
            mEtDesc.setText(mFood.desc);
            mEtPackPrice.setText(mFood.packing_price);
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

        } else if (v.getId() == R.id.tv_publish) {  //发布
            if (TextUtils.isEmpty(mFoodPath) && mType == 0) {
                ToastUtils.showShort("图片不能为空");
                return;
            }
            showLoadingDialog("");
            if (mType == 0) {
                upload(0);
            } else if (mType == 1) {
                if (TextUtils.isEmpty(mFood.cover)) {
                    upload(1);
                } else {
                    double origin = 0;
                    String price = mEtPrice.getText().toString();
                    String discount = mTvDiscountPrice.getText().toString();
                    if (!TextUtils.isEmpty(price) && !TextUtils.isEmpty(discount)) {
                        origin = Double.parseDouble(price) + Double.parseDouble(discount);
                    }
                    addFoodReq.goods_id = mFood.id;
                    addFoodReq.cover = mFood.cover;
                    addFoodReq.shop_id = AccountManager.getInstance().getShopId();
                    addFoodReq.title = mEtName.getText().toString();  //名称
                    addFoodReq.sell_price = mEtPrice.getText().toString();  //售卖价格
                    addFoodReq.discount = mTvDiscountPrice.getText().toString();
                    addFoodReq.origin_price = String.valueOf(origin);  //原价
                    addFoodReq.desc = mEtDesc.getText().toString();
                    addFoodReq.cate_id = cateId + "";
                    addFoodReq.packing_price = mEtPackPrice.getText().toString();
                    mPresenter.editGoods(addFoodReq);
                }
            }


        } else if (v.getId() == R.id.tv_del) {


            mPresenter.foodDelete(mFood.id + "");


        }
    }


    private void upload(int type) {
        UploadFileManager.getInstance().upload(mFoodPath, new UploadFileManager.Callback() {
            @Override
            public void onSuccess(String url, String key) {
                double origin = 0;
                String price = mEtPrice.getText().toString();
                String discount = mTvDiscountPrice.getText().toString();
                if (!TextUtils.isEmpty(price) && !TextUtils.isEmpty(discount)) {
                    origin = Double.parseDouble(price) + Double.parseDouble(discount);
                }
                addFoodReq.shop_id = AccountManager.getInstance().getShopId();
                addFoodReq.cover = key;
                addFoodReq.title = mEtName.getText().toString();  //名称
                addFoodReq.sell_price = mEtPrice.getText().toString();  //售卖价格
                addFoodReq.discount = mTvDiscountPrice.getText().toString();
                addFoodReq.origin_price = String.valueOf(origin);  //原价
                addFoodReq.desc = mEtDesc.getText().toString();
                addFoodReq.cate_id = cateId + "";
                addFoodReq.packing_price = mEtPackPrice.getText().toString();
                if (type == 0) {

                    mPresenter.addGood(addFoodReq);
                } else if (type == 1) {
                    addFoodReq.goods_id = mFood.id;
                    mPresenter.editGoods(addFoodReq);
                }
                dismissLoadingDialog();
            }

            @Override
            public void onError(String msg) {
                ToastUtils.showShort(msg);
            }
        });
    }


    private void getCategoryList() {
        UpCategoryReq upCategoryReq = new UpCategoryReq();
        upCategoryReq.shop_id = AccountManager.getInstance().getShopId();
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
                cateId = adapter.getData().get(position).id+"";
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
        EventBus.getDefault().post(new NotifyEvent(EVENT_TYPE_REFRESH_SHOP_GOOD));
        ToastUtils.showShort("发布成功");
        finish();
    }

    @Override
    public void editGoodSuccess(String data) {
        EventBus.getDefault().post(new NotifyEvent(EVENT_TYPE_REFRESH_SHOP_GOOD));
        ToastUtils.showShort("编辑成功");
        dismissLoadingDialog();
        finish();
    }

    @Override
    public void deleteGoodSuccess(String data) {
        EventBus.getDefault().post(new NotifyEvent(EVENT_TYPE_REFRESH_SHOP_GOOD));
        ToastUtils.showShort("下架成功");
        dismissLoadingDialog();
        finish();
    }

    @Override
    public void fail() {
        dismissLoadingDialog();
    }
}
