package com.pai8.ke.activity.takeaway.ui;

import android.annotation.SuppressLint;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.hjq.bar.OnTitleBarListener;
import com.hjq.bar.TitleBar;
import com.pai8.ke.R;
import com.pai8.ke.activity.takeaway.api.TakeawayApi;
import com.pai8.ke.activity.takeaway.entity.req.RebateReq;
import com.pai8.ke.base.BaseActivity;
import com.pai8.ke.base.retrofit.BaseObserver;
import com.pai8.ke.base.retrofit.RxSchedulers;
import com.pai8.ke.manager.AccountManager;
import com.pai8.ke.utils.StringUtils;
import com.pai8.ke.utils.ToastUtils;
import butterknife.BindView;

/**
 * Created by atian
 * on 1/14/21
 * describe:拍客返点
 */

public class MarketingRebateActivity extends BaseActivity {
    @BindView(R.id.title_bar)
    TitleBar titleBar;
    @BindView(R.id.bt_confirm)
    Button btConfirm;
    @BindView(R.id.et_rebate)
    EditText etRebate;

    private int rateNum = 0;


    @Override
    public int getLayoutId() {
        return R.layout.activity_market_rebate;
    }

    @Override
    public void initView() {
        mTitleBar.setTitle("拍客返点");
        titleBar.setOnTitleBarListener(new OnTitleBarListener() {
            @Override
            public void onLeftClick(View v) {
                finish();
            }

            @Override
            public void onTitleClick(View v) {

            }

            @Override
            public void onRightClick(View v) {

            }
        });

        btConfirm.setOnClickListener(v -> {
           setUpRate();
        });

        etRebate.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @SuppressLint("SetTextI18n")
            @Override
            public void afterTextChanged(Editable s) {
                String msg = s.toString();
                if (StringUtils.isEmpty(msg)) return;
                int value;
                if (msg.contains("%")){
                    value = Integer.parseInt(msg.split("%")[0]);
                }else{
                    value = Integer.parseInt(msg);
                }

                if (value > 100 || value < 0){
                    ToastUtils.show(MarketingRebateActivity.this,"请输入正确比例",0);
                }
                rateNum = value;
                //s.setText(value + "%");
            }
        });
    }


    //设置返现比例
    private void setUpRate() {
        if (StringUtils.isEmpty(etRebate.toString())){
            ToastUtils.showShort("请输入正确比例");
            return;
        }
        RebateReq req = new RebateReq();
        req.setBeat_rebate(String.valueOf(rateNum));
        req.setId(AccountManager.getInstance().getShopId());
        TakeawayApi.getInstance().setupRebate(req)
                .doOnSubscribe(disposable -> {
                })
                .compose(RxSchedulers.io_main())
                .subscribe(new BaseObserver<String>() {
                    @Override
                    protected void onSuccess(String data) {
                        ToastUtils.showShort(data);
                        finish();
                    }

                    @Override
                    protected void onError(String msg, int errorCode) {
                        super.onError(msg, errorCode);
                    }
                });
    }
}
