package com.pai8.ke.activity.wallet;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hjq.bar.OnTitleBarListener;
import com.pai8.ke.R;
import com.pai8.ke.activity.takeaway.api.TakeawayApi;
import com.pai8.ke.activity.wallet.data.InOutDetailBean;
import com.pai8.ke.activity.wallet.data.MemberCashRequest;
import com.pai8.ke.base.BaseActivity;
import com.pai8.ke.base.retrofit.BaseObserver;
import com.pai8.ke.base.retrofit.RxSchedulers;
import com.pai8.ke.utils.StringUtils;
import com.pai8.ke.utils.ToastUtils;
import com.pai8.ke.widget.BankAccountDialog;
import com.pai8.ke.widget.CrashOutInfoDialog;
import butterknife.BindView;

import static com.pai8.ke.activity.wallet.data.InOutDetailConstant.PAY_TYPE_BANK;
import static com.pai8.ke.activity.wallet.data.InOutDetailConstant.PAY_TYPE_WX;
import static com.pai8.ke.activity.wallet.data.InOutDetailConstant.PAY_TYPE_ZFB;

/**
 * Created by atian
 * on 1/9/21
 * describe:收入提现
 */

public class InOutDetailActivity extends BaseActivity {
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
    @BindView(R.id.ll_content)
    LinearLayout llContent;
    @BindView(R.id.ll_success)
    LinearLayout llSuccess;
    @BindView(R.id.bt_success_confirm)
    Button btSuccessConfirm;




    private BankAccountDialog accountDialog;
    private CrashOutInfoDialog crashOutInfoDialog;
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
        mTitleBar.setTitle("收入提现")
                .setRightTitle("提现记录")
                .setRightColor(getResources().getColor(R.color.black))
        .setOnTitleBarListener(new OnTitleBarListener() {
            @Override
            public void onLeftClick(View v) {
                finish();
            }

            @Override
            public void onTitleClick(View v) {

            }

            @Override
            public void onRightClick(View v) {
                 launch(OutRecordActivity.class);
            }
        });
        setImmersionBar(R.id.base_tool_bar);
        btConfirm.setOnClickListener(v -> {
            pay();
        });
        tvChooseAccount.setOnClickListener(v -> showAccountDialog());

        btSuccessConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    /**
     * 账户dialog
     */
    private void showAccountDialog() {
        if (accountDialog == null){
            accountDialog = new BankAccountDialog(InOutDetailActivity.this);
            accountDialog.setIsCanceledOnTouchOutside(true);
            accountDialog.setCallback(new BankAccountDialog.ClickCallback() {
                @Override
                public void onConfirm(BankAccountDialog.BankAccountBean bean) {
                    mPayType = bean.currentType;
                    refreshAccountView();
                }

                @Override
                public void onInfoEdit(int type) {
                    showBankEditDialog(type);
                }
            });
        }
        accountDialog.show();
    }

    /**
     * 提现信息编辑dialog
     */
    private void showBankEditDialog(int type) {
        crashOutInfoDialog = new CrashOutInfoDialog(InOutDetailActivity.this, type);
        crashOutInfoDialog.setIsCanceledOnTouchOutside(true);
        crashOutInfoDialog.setCallback(mBean -> {
            switch (type) {
                case PAY_TYPE_BANK:
                    bean.setBankCard(mBean.card);
                    break;
                case PAY_TYPE_WX:
                    bean.setWeChatAccount(mBean.card);
                    break;
                case PAY_TYPE_ZFB:
                    bean.setZfbAccount(mBean.card);
                    break;
            }
            bean.setBankName(mBean.bankName);
            bean.setBankUserName(mBean.name);
            bean.setBankAddress(mBean.address);

            if (accountDialog != null){
                accountDialog.refreshView(type,mBean.card);
            }
        });
        crashOutInfoDialog.show();
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
        request.setCash_nickname(bean.getBankUserName());
        switch (mPayType){
            case PAY_TYPE_BANK:
                request.setCash_bankname(bean.getBankName());
                request.setCash_account(bean.getBankCard());
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
                .subscribe(new BaseObserver<String>() {
                    @Override
                    protected void onSuccess(String data){
                        llContent.setVisibility(View.GONE);
                        llSuccess.setVisibility(View.VISIBLE);
                        mTitleBar.setRightTitle("");
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
