package com.pai8.ke.activity.wallet;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.pai8.ke.R;
import com.pai8.ke.activity.takeaway.api.TakeawayApi;
import com.pai8.ke.activity.wallet.data.MemberCashRequest;
import com.pai8.ke.activity.wallet.data.MemberCashResponse;
import com.pai8.ke.base.BaseActivity;
import com.pai8.ke.base.retrofit.BaseObserver;
import com.pai8.ke.base.retrofit.RxSchedulers;
import com.pai8.ke.utils.StringUtils;
import com.pai8.ke.utils.ToastUtils;
import com.pai8.ke.widget.BottomDialog;

import butterknife.BindView;

/**
 * Created by atian
 * on 1/9/21
 * describe:收入提现
 */

public class InOutDetailActivity extends BaseActivity {
    final int PAY_TYPE_BANK = 3;
    final int PAY_TYPE_WX = 2;
    final int PAY_TYPE_ZFB = 1;

    int mPayType = PAY_TYPE_BANK;

    @BindView(R.id.bt_confirm)
    Button btConfirm;
    @BindView(R.id.tv_remain)
    TextView tvRemain;


    private BottomDialog mContactBottomDialog;
    private String balance = "";//当前收益余额
    private String bankUserName = "";
    private String bankAddress = "";
    private String bankCard = "";
    private String weChatAccount = "";
    private String zfbAccount = "";


    @Override
    public int getLayoutId() {
        return R.layout.activity_in_out_detail;
    }

    @Override
    public void initData() {
        super.initData();
        balance = getIntent().getStringExtra("balance");

        tvRemain.setText(StringUtils.isEmptyDefaultValue(balance,""));
    }

    @Override
    public void initView() {
        setImmersionBar(R.id.base_tool_bar);
        btConfirm.setOnClickListener(v -> {
            showContactBottomDialog();
        });
    }

    /**
     * 提现dialog
     */
    private void showContactBottomDialog() {
        View view = View.inflate(this, R.layout.view_dialog_wallet_pay, null);
        ImageButton itnClose = view.findViewById(R.id.itn_close);
        TextView tvBtnPay = view.findViewById(R.id.tv_btn_pay);
        RelativeLayout zfbPay = view.findViewById(R.id.rl_zfb_pay);
        RelativeLayout bankPay = view.findViewById(R.id.rl_bank_pay);
        RelativeLayout wxPay = view.findViewById(R.id.rl_wx_pay);
        ImageView ivCbZfb = view.findViewById(R.id.iv_cb_zfb);
        ImageView ivCbWx = view.findViewById(R.id.iv_cb_wx);
        ImageView ivCbBank = view.findViewById(R.id.iv_cb_bank);

        itnClose.setOnClickListener(v -> mContactBottomDialog.dismiss());
        zfbPay.setOnClickListener(v -> {
            mPayType = PAY_TYPE_ZFB;
            ivCbWx.setImageResource(R.mipmap.ic_cb_n);
            ivCbBank.setImageResource(R.mipmap.ic_cb_n);
            ivCbZfb.setImageResource(R.mipmap.ic_cb_s);
        });
        bankPay.setOnClickListener(v -> {
            mPayType = PAY_TYPE_BANK;
            ivCbBank.setImageResource(R.mipmap.ic_cb_s);
            ivCbWx.setImageResource(R.mipmap.ic_cb_n);
            ivCbZfb.setImageResource(R.mipmap.ic_cb_n);
        });
        wxPay.setOnClickListener(v -> {
            mPayType = PAY_TYPE_WX;
            ivCbWx.setImageResource(R.mipmap.ic_cb_s);
            ivCbBank.setImageResource(R.mipmap.ic_cb_n);
            ivCbZfb.setImageResource(R.mipmap.ic_cb_n);
        });
        tvBtnPay.setOnClickListener(v -> pay());


        if (mContactBottomDialog == null) {
            mContactBottomDialog = new BottomDialog(this, view);
        }
        mContactBottomDialog.setIsCanceledOnTouchOutside(true);
        mContactBottomDialog.show();
    }

    /**
     * 银行卡dialog
     */
    private void showBankEditDialog() {
        View view = View.inflate(this, R.layout.view_dialog_wallet_bank_edit, null);
        ImageButton itnClose = view.findViewById(R.id.itn_close);
        TextView btConfirm = view.findViewById(R.id.tv_btn_confirm);
        EditText etName = view.findViewById(R.id.et_name);
        EditText etCard = view.findViewById(R.id.et_card);
        EditText etAddress = view.findViewById(R.id.et_address);
        itnClose.setOnClickListener(v -> mContactBottomDialog.dismiss());
        btConfirm.setOnClickListener(v -> {
            if (StringUtils.isEmpty(etName.getText().toString())
            || StringUtils.isEmpty(etAddress.getText().toString())
            ||StringUtils.isEmpty(etCard.getText().toString())){
                ToastUtils.show(InOutDetailActivity.this,"请输入相关信息",0);
                return;
            }

            bankUserName = etName.getText().toString();
            bankAddress = etAddress.getText().toString();
            bankCard = etCard.getText().toString();
            mContactBottomDialog.dismiss();
        });

        if (mContactBottomDialog == null) {
            mContactBottomDialog = new BottomDialog(this, view);
        }
        mContactBottomDialog.setIsCanceledOnTouchOutside(true);
        mContactBottomDialog.show();
    }





    private void pay(){
        if (StringUtils.isEmpty(tvRemain.getText().toString())){
            ToastUtils.show(InOutDetailActivity.this,"请输入金额",0);
            return;
        }

        String balance = tvRemain.getText().toString();

        MemberCashRequest request = new MemberCashRequest();
        request.setCash_type(String.valueOf(mPayType));
        request.setBalance(balance);

        switch (mPayType){
            case PAY_TYPE_BANK:
                request.setCash_nickname(bankUserName);
                request.setCash_account(bankCard);
                request.setCash_bankname("中国银行");
                request.setCash_bankadd(bankAddress);
                break;
            case PAY_TYPE_WX:
                request.setCash_account(weChatAccount);
            case PAY_TYPE_ZFB:
                request.setCash_account(zfbAccount);
                break;
        }


        TakeawayApi.getInstance().userMemberCash(request)
                .doOnSubscribe(disposable -> {
                })
                .compose(RxSchedulers.io_main())
                .subscribe(new BaseObserver<MemberCashResponse>() {
                    @Override
                    protected void onSuccess(MemberCashResponse data){
                        ToastUtils.show(InOutDetailActivity.this,data.getMsg(),0);
                    }

                    @Override
                    protected void onError(String msg, int errorCode) {
                        super.onError(msg, errorCode);
                        ToastUtils.show(InOutDetailActivity.this,msg,0);
                    }
                });
    }
}
