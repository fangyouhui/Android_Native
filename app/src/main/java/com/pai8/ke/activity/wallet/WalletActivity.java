package com.pai8.ke.activity.wallet;

import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hjq.bar.OnTitleBarListener;
import com.pai8.ke.R;
import com.pai8.ke.activity.takeaway.api.TakeawayApi;
import com.pai8.ke.activity.wallet.data.MemberWalletResponse;
import com.pai8.ke.base.BaseActivity;
import com.pai8.ke.base.retrofit.BaseObserver;
import com.pai8.ke.base.retrofit.RxSchedulers;
import com.pai8.ke.utils.StringUtils;

import butterknife.BindView;

/**
 * Created by atian
 * on 1/9/21
 * describe: 钱包
 */


public class WalletActivity extends BaseActivity {
    @BindView(R.id.tip_invite)
    LinearLayout tipInvite;
    @BindView(R.id.tip_video)
    LinearLayout tipVideo;
    @BindView(R.id.tip_order)
    LinearLayout tipOrder;
    @BindView(R.id.rl_cash_out)
    RelativeLayout cashOut;
    @BindView(R.id.rl_in_out_detail)
    RelativeLayout inOutDetail;
    @BindView(R.id.remain_earn)
    TextView remainEarn;
    @BindView(R.id.yesterday_earn)
    TextView yesterdayEarn;
    @BindView(R.id.total_earn)
    TextView totalEarn;

    @Override
    public int getLayoutId() {
        return R.layout.activity_wallet;
    }

    @Override
    public void initData() {
        super.initData();
        walletDetail();
    }

    @Override
    public void initView() {
        setImmersionBar(R.id.base_tool_bar);
        mTitleBar.setTitle("钱包")
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
        tipInvite.setOnClickListener(v -> {
            //TODO
        });
        tipOrder.setOnClickListener(v -> {
            //TODO
        });
        tipVideo.setOnClickListener(v -> {
            //TODO
        });

        cashOut.setOnClickListener(v -> {
            Intent intent = new Intent(this, InOutDetailActivity.class);
            intent.putExtra("balance", remainEarn.getText());
            startActivity(intent);
        });
        inOutDetail.setOnClickListener(v -> {
            //TODO 收支明细
            Intent intent = new Intent(this, InOutRecordActivity.class);
//            intent.putExtra("balance",remainEarn.getText());
            startActivity(intent);
        });
    }

    private void walletDetail() {
        TakeawayApi.getInstance().memberWallet()
                .doOnSubscribe(disposable -> {
                })
                .compose(RxSchedulers.io_main())
                .subscribe(new BaseObserver<MemberWalletResponse>() {
                    @Override
                    protected void onSuccess(MemberWalletResponse data) {
                        if (data != null) {
                            totalEarn.setText(StringUtils.isEmptyDefaultValue(data.getMoney_sum(), ""));
                            yesterdayEarn.setText(StringUtils.isEmptyDefaultValue(data.getMoney(), ""));
                            remainEarn.setText(StringUtils.isEmptyDefaultValue(data.getBalance(), ""));
                        }
                    }

                    @Override
                    protected void onError(String msg, int errorCode) {
                        super.onError(msg, errorCode);
                    }
                });
    }
}
