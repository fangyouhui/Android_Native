package com.pai8.ke.activity.takeaway.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;

import com.bigkoo.pickerview.builder.OptionsPickerBuilder;
import com.bigkoo.pickerview.listener.OnOptionsSelectChangeListener;
import com.bigkoo.pickerview.listener.OnOptionsSelectListener;
import com.bigkoo.pickerview.view.OptionsPickerView;
import com.lhs.library.base.BaseActivity;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.entity.LocalMedia;
import com.pai8.ke.R;
import com.pai8.ke.activity.takeaway.entity.req.StoreInfoReq;
import com.pai8.ke.activity.takeaway.entity.resq.StoreInfo;
import com.pai8.ke.databinding.ActivityGoodManagerEditBinding;
import com.pai8.ke.entity.resp.City;
import com.pai8.ke.entity.resp.District;
import com.pai8.ke.entity.resp.Province;
import com.pai8.ke.manager.AccountManager;
import com.pai8.ke.utils.ChoosePicUtils;
import com.pai8.ke.utils.ImageLoadUtils;
import com.pai8.ke.viewmodel.StoreManagerEditViewModel;

import java.util.ArrayList;
import java.util.List;

public class StoreManagerEditActivity extends BaseActivity<StoreManagerEditViewModel, ActivityGoodManagerEditBinding> {

    private final int RESULT_PICTURE = 1000;  //图片
    private final int RESULT_VIDEO = 1001;
    private String cateId;
    private String image;
    private StoreInfo mData;
    private String mCityId;

    private String mProvince, mCity, mDistrict;

    private OptionsPickerView pvOptions, mPvType;
    private List<Province> mProvinceList;
    private List<List<City>> mCityList;
    private List<List<List<District>>> mDistrictList;
    private List<String> mProvinceNameList;
    private List<List<String>> mCityNameList;
    private List<List<List<String>>> mDistrictNameList;

    @Override
    public void initView(@org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        mBinding.tvPublish.setOnClickListener(v -> editShop());
        mBinding.tvCategory.setOnClickListener(v -> mViewModel.businessType());
        mBinding.ivCover.setOnClickListener(v -> ChoosePicUtils.picSingle(StoreManagerEditActivity.this, 0, RESULT_PICTURE));
        mBinding.tvAddress.setOnClickListener(v -> mViewModel.area());
    }

    @Override
    public void addObserve() {
        mViewModel.getShopEditInfoData().observe(this, data -> {
            mData = data;
            setData(data);
        });

        mViewModel.getBusinessTypeData().observe(this, list -> {
            List<String> options1Items = new ArrayList<>();
            for (int i = 0; i < list.size(); i++) {
                options1Items.add(list.get(i).type_name);
            }

            if (mPvType == null) {
                mPvType = new OptionsPickerBuilder(StoreManagerEditActivity.this, (options1, option2, options3, v) -> {
                    //返回的分别是三个级别的选中位置
                    String tx = list.get(options1).type_name;
                    mBinding.tvCategory.setText(tx);
                    cateId = list.get(options1).id + "";
                    mData.cate_id = cateId;

                })
                        .setDecorView(findViewById(R.id.rl_merchant))
                        .build();
            }
            mPvType.setPicker(options1Items);
            mPvType.show();
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
        mBinding.tvCategory.setText(data.cate_name.toString());
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
            List<List<District>> disList = new ArrayList<List<District>>();
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
        pvOptions = new OptionsPickerBuilder(this, new OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int options2, int options3, View v) {
                try {
                    Province province = mProvinceList.get(options1);
                    int mProvinceId = province.getId();
                    mProvince = province.getName();
                    if (mCityList.get(options1).size() > 0) {
                        City city = mCityList.get(options1).get(options2);
                        mCityId = city.getId() + "";
                        mCity = city.getName();
                    }
                    if (mDistrictList.get(options1).get(options2).size() > 0) {
                        District district = mDistrictList.get(options1).get(options2).get(options3);
                        mDistrict = district.getName();
                    }
                    mBinding.tvAddress.setText(mProvince + mCity + mDistrict);
                } catch (Exception e) {

                }

            }
        }).setOptionsSelectChangeListener(new OnOptionsSelectChangeListener() {
            @Override
            public void onOptionsSelectChanged(int options1, int options2, int options3) {

            }
        }).setSubmitText("确定")//确定按钮文字
                .setCancelText("取消")//取消按钮文字
                .setSubCalSize(18)//确定和取消文字大小
                .setTitleSize(20)//标题文字大小
                .setContentTextSize(18)//滚轮文字大小
                .isCenterLabel(false) //是否只显示中间选中项的label文字，false则每项item全部都带有label。
                .setCyclic(false, false, false)//循环与否
                .setSelectOptions(0, 0, 0)  //设置默认选中项
                .setOutSideCancelable(false)//点击外部dismiss default true
                .setDecorView((ViewGroup) findViewById(R.id.rl_merchant))
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


    public void editShop() {
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
        storeInfo.city_id = mCityId;
        storeInfo.district = mData.district;
        storeInfo.house_number = mBinding.etNumber.getText().toString();

        mViewModel.editShop(storeInfo);

    }


}
