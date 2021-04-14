package com.pai8.ke.activity.takeaway.ui;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;

import com.lhs.library.base.BaseActivity;
import com.lhs.library.base.BaseAppConstants;
import com.lhs.library.base.BaseBottomDialogFragment;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.entity.LocalMedia;
import com.pai8.ke.R;
import com.pai8.ke.activity.takeaway.entity.req.StoreInfoParam;
import com.pai8.ke.activity.takeaway.entity.resq.StoreInfoResult;
import com.pai8.ke.databinding.ActivityGoodManagerEditBinding;
import com.pai8.ke.entity.resp.BusinessType;
import com.pai8.ke.manager.AccountManager;
import com.pai8.ke.utils.ChoosePicUtils;
import com.pai8.ke.utils.ImageLoadUtils;
import com.pai8.ke.viewmodel.StoreManagerEditViewModel;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class StoreManagerEditActivity extends BaseActivity<StoreManagerEditViewModel, ActivityGoodManagerEditBinding> {

    private final int RESULT_PICTURE = 1000;  //图片
    private final int RESULT_VIDEO = 1001;
    private String image;
    private StoreInfoResult mData;

    @Override
    public void initView(@org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        mBinding.tvPublish.setOnClickListener(v -> editShop());
        mBinding.tvCategory.setOnClickListener(v -> showCategoryBottomDialog());
        mBinding.ivCover.setOnClickListener(v -> ChoosePicUtils.picSingle(StoreManagerEditActivity.this, 0, RESULT_PICTURE));
        mBinding.tvAddress.setOnClickListener(v -> showProvince());
    }

    private void showCategoryBottomDialog() {
        CategoryBottomDialogFragment categoryBottomDialogFragment = CategoryBottomDialogFragment.newInstance(new ArrayList<>());
        categoryBottomDialogFragment.setListener(new BaseBottomDialogFragment.OnDialogListener() {
            @Override
            public void onConfirmClickListener(@NotNull Object data) {
                List<BusinessType> list = (List<BusinessType>) data;
                if (list.isEmpty()) {
                    return;
                }
                StringBuilder builder = new StringBuilder();
                StringBuilder ids = new StringBuilder();
                for (BusinessType businessType : list) {
                    builder.append(businessType.type_name).append(" ");
                    ids.append(businessType.id).append(",");
                }
                ids.deleteCharAt(ids.lastIndexOf(","));
                mBinding.tvCategory.setText(builder.toString());
                mData.cate_id = ids.toString();
            }
        });
        categoryBottomDialogFragment.show(getSupportFragmentManager(), "category");
    }

    @Override
    public void addObserve() {
        mViewModel.getShopEditInfoData().observe(this, data -> {
            mData = data;
            setData(data);
        });


        mViewModel.getEditShopData().observe(this, data -> {
            com.blankj.utilcode.util.ToastUtils.showShort("编辑成功");
            setResult(RESULT_OK);
            finish();
        });
    }

    @Override
    public void initData() {
        mData = (StoreInfoResult) getIntent().getSerializableExtra("data");
        ImageLoadUtils.setCircularImage(this, mData.shop_img_url, mBinding.ivCover, R.mipmap.ic_launcher);
        mBinding.etName.setText(mData.shop_name);
        mViewModel.shopEditInfo();
    }

    private void setData(StoreInfoResult data) {
        mBinding.etContact.setText(data.mobile);
        mBinding.tvAddress.setText(data.province + data.city + data.district);
        mBinding.etAddressDetail.setText(data.address);
        StringBuilder builder = new StringBuilder();
        for (String s : data.cate_name) {
            builder.append(s).append(" ");
        }
        mBinding.tvCategory.setText(builder.toString());
        mBinding.etNumber.setText(data.house_number);
        mBinding.etDesc.setText(data.shop_desc);
    }


    private void showProvince() {
        AreaBottomDialogFragment areaBottomDialogFragment = AreaBottomDialogFragment.newInstance();
        areaBottomDialogFragment.setListener(new BaseBottomDialogFragment.OnDialogListener() {
            @Override
            public void onConfirmClickListener(@NotNull Object data) {
                Map<String, Object> map = (Map<String, Object>) data;
                int provinceId = (int) map.getOrDefault(BaseAppConstants.BundleConstant.ARG_PARAMS_0, 0);
                String provinceName = (String) map.getOrDefault(BaseAppConstants.BundleConstant.ARG_PARAMS_1, "");
                int cityId = (int) map.getOrDefault(BaseAppConstants.BundleConstant.ARG_PARAMS_2, 0);
                String cityName = (String) map.getOrDefault(BaseAppConstants.BundleConstant.ARG_PARAMS_3, "");
                int districtId = (int) map.getOrDefault(BaseAppConstants.BundleConstant.ARG_PARAMS_4, 0);
                String districtName = (String) map.getOrDefault(BaseAppConstants.BundleConstant.ARG_PARAMS_5, "");

                mData.province = provinceId + "";
                mData.province_name = provinceName;
                mData.city = cityId + "";
                mData.city_name = cityName;
                mData.district = districtId + "";
                mData.district_name = districtName;
                mBinding.tvAddress.setText(mData.province_name + mData.city_name + mData.district_name);

            }
        });
        areaBottomDialogFragment.show(getSupportFragmentManager(), "province");
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
            ImageLoadUtils.setRectImage(this, path, mBinding.ivCover);
            image = path;
        }

    }

    private void editShop() {
        StoreInfoParam storeInfo = new StoreInfoParam();
        storeInfo.shop_id = AccountManager.getInstance().getShopId();
        storeInfo.shop_name = mBinding.etName.getText().toString();
        storeInfo.shop_img = mData.shop_img;
        storeInfo.mobile = mBinding.etContact.getText().toString();
        storeInfo.cate_id = mData.cate_id;
        storeInfo.address = mBinding.etAddressDetail.getText().toString();
        storeInfo.shop_desc = mBinding.etDesc.getText().toString();
        storeInfo.province = mData.province_name;
        storeInfo.city = mData.city_name;
        storeInfo.city_id = mData.city;
        storeInfo.district = mData.district_name;
        storeInfo.house_number = mBinding.etNumber.getText().toString();
        mViewModel.editShop(storeInfo);
    }


}
