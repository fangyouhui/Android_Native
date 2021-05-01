package com.pai8.ke.activity.takeaway.ui;

import android.os.Bundle;
import android.text.TextUtils;

import com.blankj.utilcode.util.KeyboardUtils;
import com.lhs.library.base.BaseActivity;
import com.lhs.library.base.BaseAppConstants;
import com.lhs.library.base.BaseBottomDialogFragment;
import com.pai8.ke.R;
import com.pai8.ke.activity.takeaway.entity.event.NotifyEvent;
import com.pai8.ke.activity.takeaway.entity.req.AddFoodReq;
import com.pai8.ke.activity.takeaway.entity.resq.ShopInfo;
import com.pai8.ke.activity.takeaway.widget.ChooseDiscountPricePop;
import com.pai8.ke.activity.takeaway.widget.ChooseShopCoverBottomDialogFragment;
import com.pai8.ke.databinding.ActivityEditTakeawayGoodBinding;
import com.pai8.ke.manager.AccountManager;
import com.pai8.ke.manager.UploadFileManager;
import com.pai8.ke.utils.ImageLoadUtils;
import com.pai8.ke.utils.ToastUtils;
import com.pai8.ke.viewmodel.OperateTakeawayViewModel;

import org.greenrobot.eventbus.EventBus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;

import static com.pai8.ke.activity.takeaway.Constants.EVENT_TYPE_REFRESH_SHOP_GOOD;

public class EditTakeawayGoodActivity extends BaseActivity<OperateTakeawayViewModel, ActivityEditTakeawayGoodBinding> {
    private AddFoodReq mFood;

    @Override
    public void initView(@Nullable Bundle savedInstanceState) {
        mFood = (AddFoodReq) getIntent().getSerializableExtra(BaseAppConstants.BundleConstant.ARG_PARAMS_0);
        bindViewData();
        mBinding.ivCover.setOnClickListener(v -> showSelectCoverDialog());
        mBinding.tvDiscountPrice.setOnClickListener(v -> {
            ChooseDiscountPricePop pricePop = new ChooseDiscountPricePop(this);
            pricePop.setOnSelectListener(content -> mBinding.tvDiscountPrice.setText(content));
            pricePop.showPopupWindow();
        });
        mBinding.tvCategory.setOnClickListener(v -> {
            KeyboardUtils.hideSoftInput(this);
            showCategoryBottomDialog();
        });
        mBinding.btnDel.setOnClickListener(v -> del());
        mBinding.btnPublish.setOnClickListener(v -> publish());
    }

    @Override
    public void addObserve() {
        mViewModel.getEditGoodsData().observe(this, data -> {
            EventBus.getDefault().post(new NotifyEvent(EVENT_TYPE_REFRESH_SHOP_GOOD));
            ToastUtils.showShort("编辑成功");
            finish();

        });

        mViewModel.getFoodDeleteData().observe(this, data -> {
            EventBus.getDefault().post(new NotifyEvent(EVENT_TYPE_REFRESH_SHOP_GOOD));
            ToastUtils.showShort("下架成功");
            finish();
        });
    }

    private void bindViewData() {
        mBinding.ivCover.setTag(mFood.cover);
        ImageLoadUtils.setCircularImage(this, mFood.cover, mBinding.ivCover, R.mipmap.ic_launcher);
        mBinding.etName.setText(mFood.title);
        mBinding.etPrice.setText(mFood.origin_price);
        mBinding.tvDiscountPrice.setText(mFood.discount);
        mBinding.etPackPrice.setText(mFood.packing_price);
        mBinding.tvCategory.setText(mFood.cate_name);
        mBinding.tvCategory.setTag(mFood.cate_id);
        mBinding.etDesc.setText(mFood.desc);
    }

    private void showSelectCoverDialog() {
        ChooseShopCoverBottomDialogFragment dialogFragment = new ChooseShopCoverBottomDialogFragment();
        dialogFragment.setListener(new BaseBottomDialogFragment.OnDialogListener() {
            @Override
            public void onConfirmClickListener(@NotNull Object data) {
                Map<String, String> map = (Map<String, String>) data;
                String type = map.getOrDefault(BaseAppConstants.BundleConstant.ARG_PARAMS_0, "1"); //1：图片 2：视频
                String path = map.getOrDefault(BaseAppConstants.BundleConstant.ARG_PARAMS_1, "");
                if (TextUtils.isEmpty(path)) {
                    return;
                }
                mBinding.ivCover.setTag(path);
                if ("1".equalsIgnoreCase(type)) {
                    ImageLoadUtils.setRectImage(getBaseContext(), path, mBinding.ivCover);
                } else {
                    ImageLoadUtils.loadCover(getBaseContext(), path, mBinding.ivCover);
                }

                UploadFileManager.getInstance().upload(path, new UploadFileManager.Callback() {
                    @Override
                    public void onSuccess(String url, String key) {
                        mFood.cover_qiniu_key = key;
                        mFood.cover = url;
                        mFood.type = Integer.parseInt(type);
                    }

                    @Override
                    public void onError(String msg) {
                        ToastUtils.showShort("上传失败：" + msg);
                    }
                });


            }
        });
        dialogFragment.showNow(getSupportFragmentManager(), "coverDialog");
    }


    private void showCategoryBottomDialog() {
        TakeawayProductCategoryBottomDialogFragment dialogFragment = TakeawayProductCategoryBottomDialogFragment.newInstance(null);
        dialogFragment.setListener(new BaseBottomDialogFragment.OnDialogListener() {
            @Override
            public void onConfirmClickListener(@NotNull Object data) {
                ShopInfo bean = (ShopInfo) data;
                mBinding.tvCategory.setText(bean.getName());
                mBinding.tvCategory.setTag(bean.getId() + "");
                mFood.cate_name = bean.getName();
                mFood.cate_id = bean.getId() + "";

            }
        });
        dialogFragment.show(getSupportFragmentManager(), "takeaway");
    }

    private void del() {
        mViewModel.foodDelete(mFood.id + "");
    }

    private void publish() {
        if (mBinding.ivCover.getTag() == null) {
            ToastUtils.showShort("请选择商品封面");
            return;
        }
        String shopName = mBinding.etName.getText().toString();
        if (TextUtils.isEmpty(shopName)) {
            ToastUtils.showShort("商品名称不能为空");
            return;
        }
        String sellerPrice = mBinding.etPrice.getText().toString();
        if (TextUtils.isEmpty(sellerPrice)) {
            ToastUtils.showShort("商品价格不能为空");
            return;
        }
        if (mBinding.tvCategory.getTag() == null) {
            ToastUtils.showShort("商品分类不能为空");
            return;
        }

        AddFoodReq addFoodReq = new AddFoodReq();
        addFoodReq.type = mFood.type;
        addFoodReq.shop_id = AccountManager.getInstance().getShopId();
        addFoodReq.goods_id = mFood.id;
        addFoodReq.cover = mFood.cover;
        addFoodReq.cover_qiniu_key = mFood.cover_qiniu_key;
        addFoodReq.key = mFood.cover_qiniu_key;
        addFoodReq.title = mBinding.etName.getText().toString();
        addFoodReq.sell_price = sellerPrice;  //售卖价格
        addFoodReq.discount = mBinding.tvDiscountPrice.getText().toString();
        addFoodReq.packing_price = mBinding.etPackPrice.getText().toString();
        addFoodReq.cate_id = mBinding.tvCategory.getTag().toString();

        double origin = 0;
        String price = mBinding.etPrice.getText().toString();
        if (!TextUtils.isEmpty(price)) {
            origin = Double.parseDouble(price);
        }
        addFoodReq.origin_price = String.valueOf(origin);  //原价
        addFoodReq.desc = mBinding.etDesc.getText().toString();
        mViewModel.editGoods(addFoodReq);

    }
}
