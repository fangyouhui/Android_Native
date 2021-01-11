package com.pai8.ke.widget;

import android.content.Context;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.pai8.ke.R;
import com.pai8.ke.activity.wallet.data.InOutDetailConstant;

import java.io.Serializable;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.pai8.ke.activity.wallet.data.InOutDetailConstant.PAY_TYPE_BANK;
import static com.pai8.ke.activity.wallet.data.InOutDetailConstant.PAY_TYPE_WX;
import static com.pai8.ke.activity.wallet.data.InOutDetailConstant.PAY_TYPE_ZFB;

/**
 * Created by atian
 * on 1/11/21
 * describe: 银行账户Dialog
 */

public class BankAccountDialog extends BottomDialog {
    @BindView(R.id.itn_close)
    ImageButton itnClose;
    @BindView(R.id.tv_btn_pay)
    TextView tvBtnPay;
    @BindView(R.id.iv_cb_zfb)
    ImageView ivCbZfb;
    @BindView(R.id.iv_cb_wx)
    ImageView ivCbWx;
    @BindView(R.id.iv_cb_bank)
    ImageView ivCbBank;
    @BindView(R.id.tv_bank)
    TextView tvBank;
    @BindView(R.id.tv_wx)
    TextView tvWx;
    @BindView(R.id.tv_zfb)
    TextView tvZfb;


    private BankAccountDialog.ClickCallback callback;
    private int currentType = 0;
    private Context context;

    /**
     * 默认view
     *
     * @param context
     */
    public BankAccountDialog(Context context) {
        super(context, null);
        this.context = context;
    }

    @Override
    public View getView() {
        View view = View.inflate(context, R.layout.view_dialog_wallet_pay, null);
        ButterKnife.bind(this,view);
        itnClose.setOnClickListener(v -> dismiss());
        tvBank.setOnClickListener(v -> {
            if (callback !=null){
                callback.onInfoEdit(InOutDetailConstant.PAY_TYPE_BANK);
            }
        });
        tvWx.setOnClickListener(v -> {
            if (callback !=null){
                callback.onInfoEdit(InOutDetailConstant.PAY_TYPE_WX);
            }
        });
        tvZfb.setOnClickListener(v -> {
            if (callback !=null){
                callback.onInfoEdit(InOutDetailConstant.PAY_TYPE_ZFB);
            }
        });
        ivCbZfb.setOnClickListener(v -> {
            currentType = InOutDetailConstant.PAY_TYPE_ZFB;
            ivCbWx.setImageResource(R.mipmap.ic_cb_n);
            ivCbBank.setImageResource(R.mipmap.ic_cb_n);
            ivCbZfb.setImageResource(R.mipmap.ic_cb_s);
        });
        ivCbBank.setOnClickListener(v -> {
            currentType = InOutDetailConstant.PAY_TYPE_BANK;
            ivCbBank.setImageResource(R.mipmap.ic_cb_s);
            ivCbWx.setImageResource(R.mipmap.ic_cb_n);
            ivCbZfb.setImageResource(R.mipmap.ic_cb_n);
        });
        ivCbWx.setOnClickListener(v -> {
            currentType = InOutDetailConstant.PAY_TYPE_WX;
            ivCbWx.setImageResource(R.mipmap.ic_cb_s);
            ivCbBank.setImageResource(R.mipmap.ic_cb_n);
            ivCbZfb.setImageResource(R.mipmap.ic_cb_n);
        });
        tvBtnPay.setOnClickListener(v -> {
            if (callback != null){
                BankAccountBean bean = new BankAccountBean();
                bean.currentType = currentType;
                callback.onConfirm(bean);
            }
            dismiss();
        });

        return view;
    }

    /**
     * 更新数据
     * @param type
     * @param msg
     */
    public void refreshView(int type,String msg){
        switch (type) {
            case PAY_TYPE_BANK:
                tvBank.setText(msg);
                break;
            case PAY_TYPE_WX:
                tvWx.setText(msg);
                break;
            case PAY_TYPE_ZFB:
                tvZfb.setText(msg);
                break;
        }
    }

    public void setCallback(BankAccountDialog.ClickCallback callback) {
        this.callback = callback;
    }

    public interface ClickCallback {
        void onConfirm(BankAccountBean bean);
        void onInfoEdit(int type);
    }

    public class BankAccountBean implements Serializable {
        public int currentType;
    }
}
