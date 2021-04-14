package com.pai8.ke.activity.takeaway.ui;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;

import com.bigkoo.pickerview.builder.OptionsPickerBuilder;
import com.bigkoo.pickerview.view.OptionsPickerView;
import com.lhs.library.base.BaseActivity;
import com.lhs.library.base.BaseBottomDialogFragment;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.entity.LocalMedia;
import com.pai8.ke.R;
import com.pai8.ke.activity.takeaway.entity.req.StoreInfoReq;
import com.pai8.ke.activity.takeaway.entity.resq.StoreInfo;
import com.pai8.ke.databinding.ActivityGoodManagerEditBinding;
import com.pai8.ke.entity.resp.BusinessType;
import com.pai8.ke.entity.resp.City;
import com.pai8.ke.entity.resp.District;
import com.pai8.ke.entity.resp.Province;
import com.pai8.ke.manager.AccountManager;
import com.pai8.ke.utils.ChoosePicUtils;
import com.pai8.ke.utils.ImageLoadUtils;
import com.pai8.ke.viewmodel.StoreManagerEditViewModel;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class StoreManagerEditActivity extends BaseActivity<StoreManagerEditViewModel, ActivityGoodManagerEditBinding> {

    private final int RESULT_PICTURE = 1000;  //图片
    private final int RESULT_VIDEO = 1001;
    private String image;
    private StoreInfo mData;
    private OptionsPickerView pvOptions;
    private List<Province> mProvinceList;
    private List<List<City>> mCityList;
    private List<List<List<District>>> mDistrictList;
    private List<String> mProvinceNameList;
    private List<List<String>> mCityNameList;
    private List<List<List<String>>> mDistrictNameList;

    @Override
    public void initView(@org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        mBinding.tvPublish.setOnClickListener(v -> editShop());
        mBinding.tvCategory.setOnClickListener(v -> showCategoryBottomDialog());
        mBinding.ivCover.setOnClickListener(v -> ChoosePicUtils.picSingle(StoreManagerEditActivity.this, 0, RESULT_PICTURE));
        mBinding.tvAddress.setOnClickListener(v -> mViewModel.area());
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

        mViewModel.getAreaData().observe(this, areaResps -> {
            mProvinceList = areaResps;
            initProvinceData(areaResps);
            showPicker();
        });

        mViewModel.getEditShopData().observe(this, data -> {
            com.blankj.utilcode.util.ToastUtils.showShort("编辑成功");
            setResult(RESULT_OK);
            finish();
        });
    }

    @Override
    public void initData() {
        mData = (StoreInfo) getIntent().getSerializableExtra("data");
        ImageLoadUtils.setCircularImage(this, mData.shop_img_url, mBinding.ivCover, R.mipmap.ic_launcher);
        mBinding.etName.setText(mData.shop_name);
        mViewModel.shopEditInfo();
    }

    private void setData(StoreInfo data) {
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

    private void initProvinceData(List<Province> provinceList) {
        mCityList = new ArrayList<>();
        mDistrictList = new ArrayList<>();
        mProvinceNameList = new ArrayList<>();
        mCityNameList = new ArrayList<>();
        mDistrictNameList = new ArrayList<>();
        for (Province province : provinceList) {
            if (province == null) {
                return;
            }
            mProvinceNameList.add(province.getName());
            List<City> cList = province.getChildren();
            mCityList.add(cList);
            List<String> sList = new ArrayList<>();
            List<List<String>> cityList = new ArrayList<>();
            List<List<District>> disList = new ArrayList<>();
            for (City city : cList) {
                if (city == null) {
                    return;
                }
                sList.add(city.getName());
                List<District> dList = city.getChildren();
                List<String> disString = new ArrayList<>();
                for (District district : dList) {
                    if (district == null) {
                        return;
                    }
                    disString.add(district.getName());
                }
                cityList.add(disString);
                disList.add(dList);
            }
            mDistrictList.add(disList);
            mDistrictNameList.add(cityList);
            mCityNameList.add(sList);
        }

    }

    private void showPicker() {
        pvOptions = new OptionsPickerBuilder(this, (options1, options2, options3, v) -> {
            Province province = mProvinceList.get(options1);
            mData.province = province.getId() + "";
            mData.province_name = province.getName();

            if (mCityList.get(options1).size() > 0) {
                City city = mCityList.get(options1).get(options2);
                mData.city_id = city.getId() + "";
                mData.city = city.getName();
            }
            if (mDistrictList.get(options1).get(options2).size() > 0) {
                District district = mDistrictList.get(options1).get(options2).get(options3);
                mData.district = district.getId() + "";
                mData.district_name = district.getName();

            }
            mBinding.tvAddress.setText(mData.province_name + mData.city + mData.district_name);
        }).setSubmitText("确定")//确定按钮文字
                .setCancelText("取消")//取消按钮文字
                .setSubCalSize(18)//确定和取消文字大小
                .setContentTextSize(18)//滚轮文字大小
                .isCenterLabel(false) //是否只显示中间选中项的label文字，false则每项item全部都带有label。
                .setCyclic(false, false, false)//循环与否
                .setSelectOptions(0, 0, 0)  //设置默认选中项
                .setOutSideCancelable(false)//点击外部dismiss default true
                .build();
        pvOptions.setPicker(mProvinceNameList, mCityNameList, mDistrictNameList);//添加数据源
        pvOptions.show();
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
        StoreInfoReq storeInfo = new StoreInfoReq();
        storeInfo.shop_id = Integer.parseInt(AccountManager.getInstance().getShopId());
        storeInfo.shop_name = mBinding.etName.getText().toString();
        storeInfo.shop_img = mData.shop_img;
        storeInfo.mobile = mBinding.etContact.getText().toString();
        storeInfo.cate_id = mData.cate_id;
        storeInfo.address = mBinding.etAddressDetail.getText().toString();
        storeInfo.shop_desc = mBinding.etDesc.getText().toString();
        storeInfo.province = mData.province;
        storeInfo.city = mData.city;
        storeInfo.city_id = mData.city_id;
        storeInfo.district = mData.district;
        storeInfo.house_number = mBinding.etNumber.getText().toString();

        mViewModel.editShop(storeInfo);

    }


}
