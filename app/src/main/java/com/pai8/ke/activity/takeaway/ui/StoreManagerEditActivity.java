package com.pai8.ke.activity.takeaway.ui;

import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.bigkoo.pickerview.builder.OptionsPickerBuilder;
import com.bigkoo.pickerview.listener.OnOptionsSelectChangeListener;
import com.bigkoo.pickerview.listener.OnOptionsSelectListener;
import com.bigkoo.pickerview.view.OptionsPickerView;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.entity.LocalMedia;
import com.pai8.ke.R;
import com.pai8.ke.activity.takeaway.api.TakeawayApi;
import com.pai8.ke.activity.takeaway.entity.req.StoreInfoReq;
import com.pai8.ke.activity.takeaway.entity.resq.StoreInfo;
import com.pai8.ke.api.Api;
import com.pai8.ke.base.BaseMvpActivity;
import com.pai8.ke.base.BasePresenter;
import com.pai8.ke.base.retrofit.BaseObserver;
import com.pai8.ke.base.retrofit.RxSchedulers;
import com.pai8.ke.entity.resp.BusinessType;
import com.pai8.ke.entity.resp.City;
import com.pai8.ke.entity.resp.District;
import com.pai8.ke.entity.resp.Province;
import com.pai8.ke.manager.AccountManager;
import com.pai8.ke.utils.ChoosePicUtils;
import com.pai8.ke.utils.ImageLoadUtils;
import com.pai8.ke.widget.BottomDialog;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class StoreManagerEditActivity extends BaseMvpActivity implements View.OnClickListener {

    private final int RESULT_PICTURE = 1000;  //图片
    private final int RESULT_VIDEO = 1001;
    private TextView mTvCategory;   //分类,折扣加个
    private ImageView mIvCover;  //封面
    private TextView mTvPublish;
    private String cateId;
    private String cateName;
    private TextView tv_address;
    private EditText mEtName, et_contact, et_address_detail, et_desc, et_number;
    private BottomDialog mGoodCategoryDialog;
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
    public BasePresenter initPresenter() {
        return null;
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_good_manager_edit;
    }

    @Override
    public void initView() {
        setImmersionBar(R.id.base_tool_bar);
        findViewById(R.id.toolbar_back_all).setOnClickListener(this);
        mTvPublish = findViewById(R.id.tv_publish);
        mTvPublish.setOnClickListener(this);
        mTvCategory = findViewById(R.id.tv_category);
        mTvCategory.setOnClickListener(this);
        mIvCover = findViewById(R.id.iv_cover);
        mIvCover.setOnClickListener(this);
        mEtName = findViewById(R.id.et_name);
        et_contact = findViewById(R.id.et_contact);
        tv_address = findViewById(R.id.tv_address);
        tv_address.setOnClickListener(this);
        et_address_detail = findViewById(R.id.et_address_detail);
        et_number = findViewById(R.id.et_number);
        et_desc = findViewById(R.id.et_desc);


    }


    @Override
    public void initData() {
        super.initData();
        mData = (StoreInfo) getIntent().getSerializableExtra("data");
        ImageLoadUtils.setCircularImage(this, mData.shop_img_url, mIvCover, R.mipmap.ic_launcher);
        mEtName.setText(mData.shop_name);
        shopEditInfo();

    }


    private void setData(StoreInfo data) {
        et_contact.setText(data.mobile);
        tv_address.setText(data.province + data.city + data.district);
        et_address_detail.setText(data.address);
     //   mTvCategory.setText(data.cate_name);
        et_number.setText(data.house_number);
        et_desc.setText(data.shop_desc);

    }


    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.toolbar_back_all) {
            finish();
        } else if (v.getId() == R.id.tv_category) {
            getBusinessType();

        } else if (v.getId() == R.id.tv_address) {
            getProvince();
        } else if (v.getId() == R.id.iv_cover) {
            ChoosePicUtils.picSingle(StoreManagerEditActivity.this, 0, RESULT_PICTURE);
        } else if (v.getId() == R.id.tv_publish) {  //发布
            editShop();
        }
    }


    private void getBusinessType() {
        List<String> options1Items = new ArrayList<>();
        Api.getInstance().getBusinessType()
                .doOnSubscribe(disposable -> {
                })
                .compose(RxSchedulers.io_main())
                .subscribe(new BaseObserver<List<BusinessType>>() {
                    @Override
                    protected void onSuccess(List<BusinessType> list) {

                        for (int i = 0; i < list.size(); i++) {
                            options1Items.add(list.get(i).type_name);
                        }

                        if (mPvType == null) {
                            mPvType = new OptionsPickerBuilder(StoreManagerEditActivity.this, new OnOptionsSelectListener() {
                                @Override
                                public void onOptionsSelect(int options1, int option2, int options3, View v) {
                                    //返回的分别是三个级别的选中位置
                                    String tx = list.get(options1).type_name;
                                    mTvCategory.setText(tx);
                                    cateId = list.get(options1).id + "";
                                    mData.cate_id = cateId;

                                }
                            })
                                    .setDecorView(findViewById(R.id.rl_merchant))
                                    .build();
                        }
                        mPvType.setPicker(options1Items);
                        mPvType.show();
                    }

                    @Override
                    protected void onError(String msg, int errorCode) {
                        dismissLoadingDialog();
                        super.onError(msg, errorCode);
                    }
                });
    }


    private void getProvince() {
        Api.getInstance().getArea()
                .doOnSubscribe(disposable -> {
                })
                .compose(RxSchedulers.io_main())
                .subscribe(new BaseObserver<List<Province>>() {
                    @Override
                    protected void onSuccess(List<Province> areaResps) {
                        mProvinceList = areaResps;
                        initProvinceData(areaResps);
                        showPicker();
                    }

                    @Override
                    protected void onError(String msg, int errorCode) {
                        dismissLoadingDialog();
                        super.onError(msg, errorCode);
                    }
                });
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
                    tv_address.setText(mProvince + mCity + mDistrict);
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
            ImageLoadUtils.setRectImage(this, path, mIvCover);
            image = path;

        }

    }


    public void shopEditInfo() {
        HashMap<String, Object> map = new HashMap<>();
        map.put("shop_id", AccountManager.getInstance().getShopId());
        map.put("user_id", AccountManager.getInstance().getUid());
        TakeawayApi.getInstance().shopEditInfo(createRequestBody(map))
                .doOnSubscribe(disposable -> {
                })
                .compose(RxSchedulers.io_main())
                .subscribe(new BaseObserver<StoreInfo>() {
                    @Override
                    protected void onSuccess(StoreInfo data) {
                        mData = data;
                        setData(data);
                    }

                    @Override
                    protected void onError(String msg, int errorCode) {
                        super.onError(msg, errorCode);
                    }
                });
    }


    public void editShop() {
        StoreInfoReq storeInfo = new StoreInfoReq();
        storeInfo.shop_id = Integer.parseInt(AccountManager.getInstance().getShopId());
        storeInfo.shop_name = mEtName.getText().toString();
        storeInfo.shop_img = mData.shop_img;
        storeInfo.mobile = et_contact.getText().toString();
        storeInfo.cate_id = mData.cate_id;
        storeInfo.address = et_address_detail.getText().toString();
        storeInfo.shop_desc = et_desc.getText().toString();
        storeInfo.province = mData.province;
        storeInfo.city = mData.city;
        storeInfo.city_id = mCityId;
        storeInfo.district = mData.district;
        storeInfo.house_number = et_number.getText().toString();
        TakeawayApi.getInstance().editShop(storeInfo)
                .doOnSubscribe(disposable -> {
                })
                .compose(RxSchedulers.io_main())
                .subscribe(new BaseObserver<JSONObject>() {
                    @Override
                    protected void onSuccess(JSONObject data) {
                        com.blankj.utilcode.util.ToastUtils.showShort("编辑成功");
                        setResult(RESULT_OK);
                        finish();
                    }

                    @Override
                    protected void onError(String msg, int errorCode) {
                        super.onError(msg, errorCode);
                    }
                });
    }


}
