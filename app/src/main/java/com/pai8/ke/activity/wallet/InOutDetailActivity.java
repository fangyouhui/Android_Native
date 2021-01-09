package com.pai8.ke.activity.wallet;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.pai8.ke.R;
import com.pai8.ke.activity.takeaway.api.TakeawayApi;
import com.pai8.ke.activity.wallet.data.InOutDetailBean;
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
    @BindView(R.id.tv_choose_account)
    TextView tvChooseAccount;
    @BindView(R.id.tv_account)
    TextView tvAccount;
    @BindView(R.id.et_money)
    EditText etMoney;





    private BottomDialog accountDialog;
    private BottomDialog bankEditDialog;
    private String balance = "";//当前收益余额
    private InOutDetailBean bean = new InOutDetailBean();


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
            pay();
        });
        tvChooseAccount.setOnClickListener(v -> showAccountDialog());
    }

    /**
     * 账户dialog
     */
    private void showAccountDialog() {
        View view = View.inflate(this, R.layout.view_dialog_wallet_pay, null);
        ImageButton itnClose = view.findViewById(R.id.itn_close);
        TextView tvBtnPay = view.findViewById(R.id.tv_btn_pay);
        ImageView ivCbZfb = view.findViewById(R.id.iv_cb_zfb);
        ImageView ivCbWx = view.findViewById(R.id.iv_cb_wx);
        ImageView ivCbBank = view.findViewById(R.id.iv_cb_bank);
        TextView tvBank = view.findViewById(R.id.tv_bank);
        EditText etWx = view.findViewById(R.id.et_wx);
        EditText etZfb = view.findViewById(R.id.et_zfb);

        itnClose.setOnClickListener(v -> accountDialog.dismiss());
        tvBank.setOnClickListener(v -> showBankEditDialog());
        ivCbZfb.setOnClickListener(v -> {
            mPayType = PAY_TYPE_ZFB;
            ivCbWx.setImageResource(R.mipmap.ic_cb_n);
            ivCbBank.setImageResource(R.mipmap.ic_cb_n);
            ivCbZfb.setImageResource(R.mipmap.ic_cb_s);
        });
        ivCbBank.setOnClickListener(v -> {
            mPayType = PAY_TYPE_BANK;
            ivCbBank.setImageResource(R.mipmap.ic_cb_s);
            ivCbWx.setImageResource(R.mipmap.ic_cb_n);
            ivCbZfb.setImageResource(R.mipmap.ic_cb_n);
        });
        ivCbWx.setOnClickListener(v -> {
            mPayType = PAY_TYPE_WX;
            ivCbWx.setImageResource(R.mipmap.ic_cb_s);
            ivCbBank.setImageResource(R.mipmap.ic_cb_n);
            ivCbZfb.setImageResource(R.mipmap.ic_cb_n);
        });
        tvBtnPay.setOnClickListener(v -> {
            bean.setWeChatAccount(etWx.getText().toString());
            bean.setZfbAccount(etZfb.getText().toString());
            refreshAccountView();
            accountDialog.dismiss();
        });


        if (accountDialog == null) {
            accountDialog = new BottomDialog(this, view);
        }
        accountDialog.setIsCanceledOnTouchOutside(true);
        accountDialog.show();
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
        EditText etBankName = view.findViewById(R.id.et_bank_name);


        itnClose.setOnClickListener(v -> bankEditDialog.dismiss());
        btConfirm.setOnClickListener(v -> {
            if (StringUtils.isEmpty(etName.getText().toString())
            || StringUtils.isEmpty(etAddress.getText().toString())
            ||StringUtils.isEmpty(etCard.getText().toString())
            ||StringUtils.isEmpty(etBankName.getText().toString())){
                ToastUtils.show(InOutDetailActivity.this,"请输入相关信息",0);
                return;
            }

            bean.setBankName(etBankName.getText().toString());
            bean.setBankUserName(etName.getText().toString());
            bean.setBankAddress(etAddress.getText().toString());
            bean.setBankCard(etCard.getText().toString());
            bankEditDialog.dismiss();
        });

        if (bankEditDialog == null) {
            bankEditDialog = new BottomDialog(this, view);
        }
        bankEditDialog.setIsCanceledOnTouchOutside(true);
        bankEditDialog.show();
    }

    private void pay(){
        if (StringUtils.isEmpty(etMoney.getText().toString())){
            ToastUtils.show(InOutDetailActivity.this,"请输入金额",0);
            return;
        }

        String balance = etMoney.getText().toString();

        MemberCashRequest request = new MemberCashRequest();
        request.setCash_type(String.valueOf(mPayType));
        request.setBalance(balance);

        switch (mPayType){
            case PAY_TYPE_BANK:
                request.setCash_nickname(bean.getBankUserName());
                request.setCash_account(bean.getBankCard());
                request.setCash_bankname(bean.getBankName());
                request.setCash_bankadd(bean.getBankAddress());
                break;
            case PAY_TYPE_WX:
                request.setCash_account(bean.getWeChatAccount());
                break;
            case PAY_TYPE_ZFB:
                request.setCash_account(bean.getZfbAccount());
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


    /**
     * 刷新账户信息
     */
    private void refreshAccountView(){
        String account = "";
        switch (mPayType){
            case PAY_TYPE_BANK:
                account = bean.getBankName() + "(" + bean.getBankCard() + ")";
                break;
            case PAY_TYPE_WX:
                account = "微信" + "(" + bean.getWeChatAccount() + ")";
                break;
            case PAY_TYPE_ZFB:
                account = "支付宝" + "(" + bean.getZfbAccount()+ ")";
                break;
        }

        tvAccount.setText(account);
    }
}
