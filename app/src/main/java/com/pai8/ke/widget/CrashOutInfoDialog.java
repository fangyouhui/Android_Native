package com.pai8.ke.widget;

import android.content.Context;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.pai8.ke.R;
import com.pai8.ke.activity.wallet.data.InOutDetailConstant;
import com.pai8.ke.utils.StringUtils;
import com.pai8.ke.utils.ToastUtils;

import java.io.Serializable;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by atian
 * on 1/11/21
 * describe: 提现信息编辑Dialog
 */

public class CrashOutInfoDialog extends BottomDialog {
    @BindView(R.id.itn_close)
    ImageButton itnClose;
    @BindView(R.id.tv_btn_confirm)
    TextView btConfirm;
    @BindView(R.id.et_name)
    EditText etName;
    @BindView(R.id.et_card)
    EditText etCard;
    @BindView(R.id.et_address)
    EditText etAddress;
    @BindView(R.id.et_bank_name)
    EditText etBankName;
    @BindView(R.id.ll_address)
    LinearLayout llAddress;
    @BindView(R.id.ll_bank_name)
    LinearLayout llBankName;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tv_card)
    TextView tvCard;



    private CrashOutInfoDialog.ClickCallback callback;
    private int type;
    private Context context;

    /**
     * 默认view
     *
     * @param context
     */
    public CrashOutInfoDialog(Context context, int type) {
        super(context, null);
        this.type = type;
        this.context = context;
    }

    @Override
    public View getView() {
        View view = View.inflate(context, R.layout.view_dialog_wallet_bank_edit, null);
        ButterKnife.bind(this,view);

        switch (type){
            case InOutDetailConstant.PAY_TYPE_BANK:
                tvTitle.setText("提现到银行卡");
                tvCard.setText("银行卡号");
                etCard.setHint("请输入银行卡号");
                llAddress.setVisibility(View.VISIBLE);
                llBankName.setVisibility(View.VISIBLE);
                break;
            case InOutDetailConstant.PAY_TYPE_WX:
                tvTitle.setText("提现到微信");
                tvCard.setText("微信账号");
                etCard.setHint("请输入微信账号");
                llAddress.setVisibility(View.GONE);
                llBankName.setVisibility(View.GONE);
                break;
            case InOutDetailConstant.PAY_TYPE_ZFB:
                tvTitle.setText("提现到支付宝");
                tvCard.setText("支付宝账号");
                etCard.setHint("请输入支付宝账号");
                llAddress.setVisibility(View.GONE);
                llBankName.setVisibility(View.GONE);
                break;
        }

        itnClose.setOnClickListener(v -> {
            dismiss();
        });
        btConfirm.setOnClickListener(v -> {
            switch (type){
                case InOutDetailConstant.PAY_TYPE_BANK:
                    if (StringUtils.isEmpty(etName.getText().toString())
                            || StringUtils.isEmpty(etAddress.getText().toString())
                            || StringUtils.isEmpty(etCard.getText().toString())
                            || StringUtils.isEmpty(etBankName.getText().toString())) {
                        ToastUtils.show(context, "请输入相关信息", 0);
                        return;
                    }
                    break;
                case InOutDetailConstant.PAY_TYPE_WX:
                case InOutDetailConstant.PAY_TYPE_ZFB:
                    if (StringUtils.isEmpty(etName.getText().toString())
                            || StringUtils.isEmpty(etCard.getText().toString())) {
                        ToastUtils.show(context, "请输入相关信息", 0);
                        return;
                    }
                    break;
            }


            if (callback != null) {
                CrashOutInfoBean bean = new CrashOutInfoBean();
                bean.bankName = etBankName.getText().toString();
                bean.name = etName.getText().toString();
                bean.address = etAddress.getText().toString();
                bean.card = etCard.getText().toString();
                callback.onConfirm(bean);
                dismiss();
            }
        });
        return view;
    }

    public void setCallback(CrashOutInfoDialog.ClickCallback callback) {
        this.callback = callback;
    }

    public interface ClickCallback {
        void onConfirm(CrashOutInfoBean bean);
    }


    public class CrashOutInfoBean implements Serializable {
        public String bankName = "";
        public String name = "";
        public String address = "";
        public String card = "";
    }
}
