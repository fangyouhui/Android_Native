package com.pai8.ke.activity.takeaway.widget;

import android.content.Context;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.pai8.ke.R;
import com.pai8.ke.activity.takeaway.entity.resq.AddressInfo;

import razerdp.basepopup.BasePopupWindow;

public class AddAddressPop extends BasePopupWindow implements View.OnClickListener,TextWatcher {



    private EditText mEtName,mEtPhone,mEtAddress;
    private TextView mTvTitle,mTvDelete;
    private TextView mTvNext;
    public AddressInfo addressInfo;


    public AddAddressPop(Context context,AddressInfo addressInfo) {
        super(context);
        this.addressInfo = addressInfo;
        setPopupGravity(Gravity.BOTTOM);
        setOutSideDismiss(true);
        initUI();
    }

    private void initUI() {
        findViewById(R.id.iv_close).setOnClickListener(this);
        mEtName = findViewById(R.id.et_name);
        mEtPhone = findViewById(R.id.et_phone);
        mEtAddress = findViewById(R.id.et_address);
        mTvNext = findViewById(R.id.tv_next);
        mTvDelete = findViewById(R.id.tv_delete);
        mTvTitle = findViewById(R.id.tv_title);
        mTvNext.setOnClickListener(this);
        mTvDelete.setOnClickListener(this);
        mEtName.addTextChangedListener(this);
        mEtPhone.addTextChangedListener(this);
        mEtAddress.addTextChangedListener(this);
        editListener();


        if(addressInfo!=null){
            mEtName.setText(addressInfo.linkman);
            mEtPhone.setText(addressInfo.phone);
            mEtAddress.setText(addressInfo.address);
            mTvDelete.setVisibility(View.VISIBLE);
            mTvTitle.setText("编辑地址");
        }else{
            mTvDelete.setVisibility(View.GONE);
            mTvTitle.setText("新增地址");
        }

    }

    private void editListener() {
        if (TextUtils.isEmpty(mEtName.getText().toString()) || TextUtils.isEmpty(mEtPhone.getText().toString())
                || TextUtils.isEmpty(mEtAddress.getText().toString())) {
            mTvNext.setBackgroundResource(R.drawable.shape_orgin_gradient_gray);
            mTvNext.setEnabled(false);
        } else {
            mTvNext.setBackgroundResource(R.drawable.shape_orgin_gradient);
            mTvNext.setEnabled(true);
        }
    }




    @Override
    public View onCreateContentView() {
        View v = createPopupById(R.layout.pop_add_address);
        return v;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.iv_close) {
            dismiss();
        }else if(id == R.id.tv_next){
            String name = mEtName.getText().toString();
            String phone = mEtPhone.getText().toString();
            String address = mEtAddress.getText().toString();
            if(onSelectListener!=null)
                onSelectListener.onSelect(name,phone,address);
            dismiss();
        }else if(id == R.id.tv_delete){
            if(onSelectListener!=null)
                onSelectListener.delete(addressInfo.id);
            dismiss();

        }
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


    public interface OnSelectListener {
        void onSelect(String name,String phone,String address);

        void delete(int id);


    }

    private OnSelectListener onSelectListener;

    public void setOnSelectListener(OnSelectListener listener) {
        this.onSelectListener = listener;
    }
}
