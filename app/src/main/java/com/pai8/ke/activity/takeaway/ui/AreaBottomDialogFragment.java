package com.pai8.ke.activity.takeaway.ui;

import com.bigkoo.pickerview.builder.OptionsPickerBuilder;
import com.bigkoo.pickerview.view.OptionsPickerView;
import com.lhs.library.base.BaseAppConstants;
import com.lhs.library.base.BaseBottomDialogFragment;
import com.pai8.ke.databinding.FragmentAreaBottomDialogFragmentBinding;
import com.pai8.ke.entity.resp.City;
import com.pai8.ke.entity.resp.District;
import com.pai8.ke.entity.resp.Province;
import com.pai8.ke.viewmodel.AreaViewModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AreaBottomDialogFragment extends BaseBottomDialogFragment<AreaViewModel, FragmentAreaBottomDialogFragmentBinding> {

    private int provinceId, cityId, districtId;

    private String provinceName, cityName, districtName;

    public static AreaBottomDialogFragment newInstance() {
        AreaBottomDialogFragment fragment = new AreaBottomDialogFragment();
        return fragment;
    }

    @Override
    public void addObserve() {
        mViewModel.getAreaData().observe(getViewLifecycleOwner(), data -> wrapProvinceData(data));
    }

    @Override
    public void initData() {
        mViewModel.getArea();
    }

    private void wrapProvinceData(List<Province> provinceList) {
        List<List<City>> mCityList = new ArrayList<>();
        List<List<List<District>>> mDistrictList = new ArrayList<>();

        List<String> mProvinceNameList = new ArrayList<>();
        List<List<String>> mCityNameList = new ArrayList<>();
        List<List<List<String>>> mDistrictNameList = new ArrayList<>();

        for (Province province : provinceList) {
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


        OptionsPickerView pvOptions = new OptionsPickerBuilder(getContext(), (options1, options2, options3, v) -> {
            Province province = provinceList.get(options1);
            provinceId = province.getId();
            provinceName = province.getName();
            if (mCityList.get(options1).size() > 0) {
                City city = mCityList.get(options1).get(options2);
                cityId = city.getId();
                cityName = city.getName();
            }
            if (mDistrictList.get(options1).get(options2).size() > 0) {
                District district = mDistrictList.get(options1).get(options2).get(options3);
                districtId = district.getId();
                districtName = district.getName();
            }
            callBack();
        }).setSubmitText("确定")//确定按钮文字
                .setCancelText("取消")//取消按钮文字
                .setSubCalSize(18)//确定和取消文字大小
                .setTitleSize(20)//标题文字大小
                .setContentTextSize(18)//滚轮文字大小
                .isCenterLabel(false) //是否只显示中间选中项的label文字，false则每项item全部都带有label。
                .setCyclic(false, false, false)//循环与否
                .setSelectOptions(0, 0, 0)  //设置默认选中项
                .setOutSideCancelable(false)//点击外部dismiss default true
                .setDecorView(mBinding.root)
                .addOnCancelClickListener(v -> dismiss())
                .build();
        pvOptions.setPicker(mProvinceNameList, mCityNameList, mDistrictNameList);//添加数据源
        pvOptions.show(false);
    }


    private void callBack() {
        Map<String, Object> map = new HashMap<>();
        map.put(BaseAppConstants.BundleConstant.ARG_PARAMS_0, provinceId);
        map.put(BaseAppConstants.BundleConstant.ARG_PARAMS_1, provinceName);
        map.put(BaseAppConstants.BundleConstant.ARG_PARAMS_2, cityId);
        map.put(BaseAppConstants.BundleConstant.ARG_PARAMS_3, cityName);
        map.put(BaseAppConstants.BundleConstant.ARG_PARAMS_4, districtId);
        map.put(BaseAppConstants.BundleConstant.ARG_PARAMS_5, districtName);
        if (mDialogListener != null) {
            mDialogListener.onConfirmClickListener(map);
        }
        dismiss();
    }

}
