package com.pai8.ke.activity.takeaway.ui;

import android.content.Intent;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.bigkoo.pickerview.builder.OptionsPickerBuilder;
import com.bigkoo.pickerview.listener.OnOptionsSelectChangeListener;
import com.bigkoo.pickerview.listener.OnOptionsSelectListener;
import com.bigkoo.pickerview.view.OptionsPickerView;
import com.pai8.ke.R;
import com.pai8.ke.activity.takeaway.Constants;
import com.pai8.ke.activity.takeaway.entity.event.NotifyEvent;
import com.pai8.ke.api.Api;
import com.pai8.ke.base.BaseMvpActivity;
import com.pai8.ke.base.BasePresenter;
import com.pai8.ke.base.retrofit.BaseObserver;
import com.pai8.ke.base.retrofit.RxSchedulers;
import com.pai8.ke.entity.Address;
import com.pai8.ke.entity.resp.BusinessType;
import com.pai8.ke.entity.resp.City;
import com.pai8.ke.entity.resp.District;
import com.pai8.ke.entity.resp.Province;
import com.pai8.ke.utils.EventBusUtils;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.Nullable;

public class MerchantSettledFirstActivity extends BaseMvpActivity implements View.OnClickListener, TextWatcher {

    private EditText mEtStoreName, mEtPhone, mEtEmail, mEtBankNo, mEtBankAddress;
    private TextView mTvCate, mTvAddress;
    private TextView mEtAddressDetail, mTvNext;


    private String mCate;

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
        return R.layout.activity_merchant_settled_first;
    }

    @Override
    public void initView() {
        EventBusUtils.register(this);
        setImmersionBar(R.id.base_tool_bar);
        findViewById(R.id.toolbar_back_all).setOnClickListener(this);
        mTvNext = findViewById(R.id.tv_next);
        mTvNext.setOnClickListener(this);
        mEtStoreName = findViewById(R.id.et_store_name);
        mEtEmail = findViewById(R.id.et_email);
        mEtPhone = findViewById(R.id.et_phone);
        mEtAddressDetail = findViewById(R.id.et_address_detail);
        mEtAddressDetail.setOnClickListener(this);
        mEtBankAddress = findViewById(R.id.et_bank_address);
        mEtBankNo = findViewById(R.id.et_bank_no);
        mTvAddress = findViewById(R.id.tv_address);
        mTvAddress.setOnClickListener(this);
        mTvCate = findViewById(R.id.tv_cate);
        mTvCate.setOnClickListener(this);
        editListener();
    }


    @Override
    public void initListener() {
        super.initListener();
        mEtStoreName.addTextChangedListener(this);
        mEtPhone.addTextChangedListener(this);
        mEtEmail.addTextChangedListener(this);
        mEtAddressDetail.addTextChangedListener(this);
        mEtBankNo.addTextChangedListener(this);
        mEtBankAddress.addTextChangedListener(this);
        mTvCate.addTextChangedListener(this);
        mTvAddress.addTextChangedListener(this);

    }

    private void editListener() {
        if (TextUtils.isEmpty(mEtStoreName.getText().toString()) || TextUtils.isEmpty(mEtPhone.getText().toString())
                || TextUtils.isEmpty(mEtEmail.getText().toString()) || TextUtils.isEmpty(mEtAddressDetail.getText().toString())
                || TextUtils.isEmpty(mEtBankNo.getText().toString()) || TextUtils.isEmpty(mEtBankAddress.getText().toString())
                || TextUtils.isEmpty(mTvCate.getText().toString()) || TextUtils.isEmpty(mTvAddress.getText().toString())) {
            mTvNext.setBackgroundResource(R.drawable.shape_orgin_gradient_gray);
            mTvNext.setEnabled(false);
        } else {
            mTvNext.setBackgroundResource(R.drawable.shape_orgin_gradient);
            mTvNext.setEnabled(true);
        }
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(NotifyEvent event) {
        if (event.type == Constants.EVENT_TYPE_MERCHANT_SETTLED) {
            finish();
        }

    }



    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.toolbar_back_all) {
            finish();
        } else if (v.getId() == R.id.tv_cate) {
            getBusinessType();
        } else if (v.getId() == R.id.tv_address) {
            getProvince();
        } else if (v.getId() == R.id.et_address_detail) {
            startActivityForResult(new Intent(MerchantSettledFirstActivity.this
                    ,MapAddressChooseActivity.class),100);

        } else if (v.getId() == R.id.tv_next) {

            String storeName = mEtStoreName.getText().toString();
            String phone = mEtPhone.getText().toString();
            String email = mEtEmail.getText().toString();
            String cate = mTvAddress.getText().toString();
            String addressDetail = mEtAddressDetail.getText().toString();
            String bankAddress = mEtBankAddress.getText().toString();
            String bankNo = mEtBankNo.getText().toString();

            Intent intent = new Intent(this, MerchantSettledSecondActivity.class);
            intent.putExtra("storeName", storeName);
            intent.putExtra("phone", phone);
            intent.putExtra("email", email);
            intent.putExtra("cate", mCate);
            intent.putExtra("province", mProvince);
            intent.putExtra("city", mCity);
            intent.putExtra("district", mDistrict);
            intent.putExtra("addressDetail", addressDetail);
            intent.putExtra("bankAddress", bankAddress);
            intent.putExtra("bankNo", bankNo);
            intent.putExtra("address",mAddress);
            startActivity(intent);
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
                            mPvType = new OptionsPickerBuilder(MerchantSettledFirstActivity.this, new OnOptionsSelectListener() {
                                @Override
                                public void onOptionsSelect(int options1, int option2, int options3, View v) {
                                    //返回的分别是三个级别的选中位置
                                    String tx = list.get(options1).type_name;
                                    mTvCate.setText(tx);
                                    mCate = list.get(options1).id + "";

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
                Province province = mProvinceList.get(options1);
                int mProvinceId = province.getId();
                mProvince = province.getName();
                if (mCityList.get(options1).size() > 0) {
                    City city = mCityList.get(options1).get(options2);
                    int mCityId = city.getId();
                    mCity = city.getName();
                }
                if (mDistrictList.get(options1).get(options2).size() > 0) {
                    District district = mDistrictList.get(options1).get(options2).get(options3);
                    mDistrict = district.getName();
                }
                mTvAddress.setText(mProvince + mCity + mDistrict);
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
    protected void onDestroy() {
        super.onDestroy();
        EventBusUtils.unregister(this);
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        editListener();
    }

    @Override
    public void afterTextChanged(Editable s) {

    }


    Address mAddress;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (null != data) {
            switch (requestCode) {
                case 100:
                    mAddress = (Address) data.getSerializableExtra("address");
                    mEtAddressDetail.setText(mAddress.getAddress());
                    break;
            }
        }

    }
}
