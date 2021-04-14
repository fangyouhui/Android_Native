package com.pai8.ke.activity.takeaway.ui;

import android.content.Intent;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.bigkoo.pickerview.builder.OptionsPickerBuilder;
import com.bigkoo.pickerview.listener.OnOptionsSelectChangeListener;
import com.bigkoo.pickerview.listener.OnOptionsSelectListener;
import com.bigkoo.pickerview.view.OptionsPickerView;
import com.lhs.library.base.BaseBottomDialogFragment;
import com.pai8.ke.R;
import com.pai8.ke.activity.takeaway.Constants;
import com.pai8.ke.activity.takeaway.entity.event.NotifyEvent;
import com.pai8.ke.api.Api;
import com.pai8.ke.base.BaseEvent;
import com.pai8.ke.base.BaseMvpActivity;
import com.pai8.ke.base.BasePresenter;
import com.pai8.ke.base.retrofit.BaseObserver;
import com.pai8.ke.base.retrofit.RxSchedulers;
import com.pai8.ke.entity.Address;
import com.pai8.ke.entity.resp.BusinessType;
import com.pai8.ke.entity.resp.City;
import com.pai8.ke.entity.resp.District;
import com.pai8.ke.entity.resp.Province;
import com.pai8.ke.global.EventCode;
import com.pai8.ke.utils.EventBusUtils;
import com.pai8.ke.utils.StringUtils;
import com.pai8.ke.widget.BottomDialog;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import razerdp.util.KeyboardUtils;

public class MerchantSettledFirstActivity extends BaseMvpActivity implements View.OnClickListener, TextWatcher {

    private EditText mEtStoreName, mEtPhone, mEtEmail;
    private TextView mTvCate, mTvAddress, mTvCollectionAccount;
    private TextView mEtAddressDetail, mTvNext;
    private RelativeLayout mRlCollectionAccount;
    private EditText mEtAddressNumber;

    private String mCate;

    private String mProvince, mCity = "", mDistrict;

    private OptionsPickerView pvOptions, mPvType;
    private List<Province> mProvinceList;
    private List<List<City>> mCityList;
    private List<List<List<District>>> mDistrictList;
    private List<String> mProvinceNameList;
    private List<List<String>> mCityNameList;
    private List<List<List<String>>> mDistrictNameList;
    private BottomDialog mBottomDialog;
    private String mCardAddress;
    private String mCardNo;
    private int mPayType = -1;

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
        mRlCollectionAccount = findViewById(R.id.rl_collection_account);
        mRlCollectionAccount.setOnClickListener(this);
        mTvCollectionAccount = findViewById(R.id.tv_collection_account);
        mTvAddress = findViewById(R.id.tv_address);
        mTvAddress.setOnClickListener(this);
        mTvCate = findViewById(R.id.tv_cate);
        mTvCate.setOnClickListener(this);
        mEtAddressNumber = findViewById(R.id.et_address_number);
        editListener();
    }


    @Override
    public void initListener() {
        super.initListener();
        mEtStoreName.addTextChangedListener(this);
        mEtPhone.addTextChangedListener(this);
        mEtEmail.addTextChangedListener(this);
        mEtAddressDetail.addTextChangedListener(this);
//        mEtBankNo.addTextChangedListener(this);
//        mEtBankAddress.addTextChangedListener(this);
        mTvCate.addTextChangedListener(this);
        mTvAddress.addTextChangedListener(this);

    }

    private void editListener() {
        if (TextUtils.isEmpty(mEtStoreName.getText().toString()) || TextUtils.isEmpty(mEtPhone.getText().toString())
                || TextUtils.isEmpty(mEtEmail.getText().toString()) || TextUtils.isEmpty(mEtAddressDetail.getText().toString())
                || TextUtils.isEmpty(mTvCollectionAccount.getText().toString())
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
    protected void receiveEvent(BaseEvent event) {
        super.receiveEvent(event);
        if (event.getCode() == EventCode.EVENT_CHOOSE_ADDRESS) {
            mAddress = (Address) event.getData();
            mEtAddressDetail.setText(mAddress.getAddress());
        }
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.rl_collection_account) {
            showShareBottomDialog();
        } else if (v.getId() == R.id.toolbar_back_all) {
            finish();
        } else if (v.getId() == R.id.tv_cate) {
            KeyboardUtils.close(this);
            getBusinessType();
        } else if (v.getId() == R.id.tv_address) {
            KeyboardUtils.close(this);
            getProvince();
        } else if (v.getId() == R.id.et_address_detail) {
            startActivityForResult(new Intent(MerchantSettledFirstActivity.this
                    , MapAddressChooseActivity.class).putExtra("ADDRESS", mCity), 100);

        } else if (v.getId() == R.id.tv_next) {

            String storeName = mEtStoreName.getText().toString();
            String phone = mEtPhone.getText().toString();
            String email = mEtEmail.getText().toString();
            String cate = mTvAddress.getText().toString();
            String addressDetail = mEtAddressDetail.getText().toString();
            String addressNumber = mEtAddressNumber.getText().toString();

            Intent intent = new Intent(this, MerchantSettledSecondActivity.class);
            intent.putExtra("storeName", storeName);
            intent.putExtra("phone", phone);
            intent.putExtra("email", email);
            intent.putExtra("cate", mCate);
            intent.putExtra("province", mProvince);
            intent.putExtra("city", mCity);
            intent.putExtra("district", mDistrict);
            intent.putExtra("addressDetail", addressDetail);
            intent.putExtra("bankAddress", mCardAddress);
            intent.putExtra("bankNo", mCardNo);
            intent.putExtra("payType", mPayType);
            intent.putExtra("payAccount", mCardNo);
            intent.putExtra("address", mAddress);
            intent.putExtra("addressNumber", addressNumber);
            startActivity(intent);
        }
    }

    private void showShareBottomDialog() {
        View view = View.inflate(this, R.layout.view_dialog_collection_account, null);
        ViewHolder holder = new ViewHolder(view);
        holder.itnClose.setOnClickListener(view1 -> {
            mBottomDialog.dismiss();
        });
        holder.btnConfirm.setOnClickListener(view1 -> {
            if (holder.cbAlipay.isChecked()) {
                String cardNo = holder.etAlipayAccount.getText().toString().trim();
                if (StringUtils.isEmpty(cardNo)) {
                    toast("请输入支付宝账号");
                    return;
                }
                mCardAddress = "支付宝账户";
                mCardNo = cardNo;
                mPayType = 1;
                mTvCollectionAccount.setText(String.format("%s：%s", mCardAddress, mCardNo));
            } else if (holder.cbWechat.isChecked()) {
                String cardNo = holder.etWechatAccount.getText().toString().trim();
                if (StringUtils.isEmpty(cardNo)) {
                    toast("请输入微信账号");
                    return;
                }
                mCardAddress = "微信账户";
                mCardNo = cardNo;
                mPayType = 2;
                mTvCollectionAccount.setText(String.format("%s：%s", mCardAddress, mCardNo));
            } else if (holder.cbBank.isChecked()) {
                String cardAddress = holder.etBankAddress.getText().toString().trim();
                String cardNo = holder.etBankNo.getText().toString().trim();
                if (StringUtils.isEmpty(cardAddress) || StringUtils.isEmpty(cardNo)) {
                    toast("请输入银行卡号/开户行地址");
                    return;
                }
                mCardAddress = cardAddress;
                mCardNo = cardNo;
                mPayType = 3;
                mTvCollectionAccount.setText(String.format("银行卡号：%s\n开户行地址：%s", mCardNo, mCardAddress));
            } else if (holder.cbOther.isChecked()) {
                String cardNo = holder.etOtherAccount.getText().toString().trim();
                if (StringUtils.isEmpty(cardNo)) {
                    toast("请输入账户号码");
                    return;
                }
                mCardAddress = "指定账户";
                mCardNo = cardNo;
                mPayType = 4;
                mTvCollectionAccount.setText(String.format("%s：%s", mCardAddress, mCardNo));
            } else {
                toast("请选择收款账户");
                return;
            }
            editListener();
            mBottomDialog.dismiss();
        });
        holder.llChooseAlipayAccount.setOnClickListener(view1 -> showView(holder, true, false, false, false));
        holder.llChooseWechatAccount.setOnClickListener(view1 -> showView(holder, false, true, false, false));
        holder.llChooseBankAccount.setOnClickListener(view1 -> showView(holder, false, false, true, false));
        holder.llChooseOtherAccount.setOnClickListener(view1 -> showView(holder, false, false, false, true));
        if (mBottomDialog == null) {
            mBottomDialog = new BottomDialog(this, view);
        }
        mBottomDialog.setIsCanceledOnTouchOutside(true);
        mBottomDialog.show();
    }

    private void showView(ViewHolder holder, boolean ali, boolean wechat, boolean bank, boolean other) {
        holder.cbAlipay.setChecked(ali);
        holder.cbWechat.setChecked(wechat);
        holder.cbBank.setChecked(bank);
        holder.cbOther.setChecked(other);
        holder.llAlipayAccount.setVisibility(ali ? View.VISIBLE : View.GONE);
        holder.llWechatAccount.setVisibility(wechat ? View.VISIBLE : View.GONE);
        holder.llBankAccount.setVisibility(bank ? View.VISIBLE : View.GONE);
        holder.llOtherAccount.setVisibility(other ? View.VISIBLE : View.GONE);
    }

    private void getBusinessType() {

        CategoryBottomDialogFragment categoryBottomDialogFragment = CategoryBottomDialogFragment.newInstance(null);
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
                mTvCate.setText(builder.toString());
                mCate = ids.toString();
            }
        });
        categoryBottomDialogFragment.show(getSupportFragmentManager(), "category");

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

    class ViewHolder {
        @BindView(R.id.itn_close)
        ImageButton itnClose;
        @BindView(R.id.cb_wechat)
        CheckBox cbWechat;
        @BindView(R.id.ll_choose_wechat_account)
        LinearLayout llChooseWechatAccount;
        @BindView(R.id.et_wechat_account)
        EditText etWechatAccount;
        @BindView(R.id.ll_wechat_account)
        LinearLayout llWechatAccount;
        @BindView(R.id.cb_alipay)
        CheckBox cbAlipay;
        @BindView(R.id.ll_choose_alipay_account)
        LinearLayout llChooseAlipayAccount;
        @BindView(R.id.et_alipay_account)
        EditText etAlipayAccount;
        @BindView(R.id.ll_alipay_account)
        LinearLayout llAlipayAccount;
        @BindView(R.id.cb_bank)
        CheckBox cbBank;
        @BindView(R.id.ll_choose_bank_account)
        LinearLayout llChooseBankAccount;
        @BindView(R.id.tv_bank_no)
        TextView tvBankNo;
        @BindView(R.id.et_bank_no)
        EditText etBankNo;
        @BindView(R.id.tv_bank_address)
        TextView tvBankAddress;
        @BindView(R.id.et_bank_address)
        EditText etBankAddress;
        @BindView(R.id.ll_bank_account)
        LinearLayout llBankAccount;
        @BindView(R.id.cb_other)
        CheckBox cbOther;
        @BindView(R.id.ll_choose_other_account)
        LinearLayout llChooseOtherAccount;
        @BindView(R.id.et_other_account)
        EditText etOtherAccount;
        @BindView(R.id.ll_other_account)
        LinearLayout llOtherAccount;
        @BindView(R.id.btn_confirm)
        Button btnConfirm;
        @BindView(R.id.ll_bottom)
        LinearLayout llBottom;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
