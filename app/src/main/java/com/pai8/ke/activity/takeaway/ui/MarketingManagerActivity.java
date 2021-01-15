package com.pai8.ke.activity.takeaway.ui;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.RelativeLayout;
import com.hjq.bar.OnTitleBarListener;
import com.hjq.bar.TitleBar;
import com.pai8.ke.R;
import com.pai8.ke.base.BaseActivity;
import butterknife.BindView;


/**
 * 营销管理
 */
public class MarketingManagerActivity extends BaseActivity {

    @BindView(R.id.title_bar)
    TitleBar titleBar;
    @BindView(R.id.rl_1)
    RelativeLayout rl1;
    @BindView(R.id.rl_2)
    RelativeLayout rl2;
    @BindView(R.id.rl_3)
    RelativeLayout rl3;
    @BindView(R.id.rl_4)
    RelativeLayout rl4;

    public static void launch(Context context) {
        Intent intent = new Intent(context, MarketingManagerActivity.class);
        context.startActivity(intent);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_marketing_manager;
    }

    @Override
    public void initView() {

    }

    @Override
    public void initData() {
        mTitleBar.setTitle("营销管理");

    }

    @Override
    public void initListener() {
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
        //优惠券
        rl1.setOnClickListener(v -> CouponActivity.launch(MarketingManagerActivity.this,CouponActivity.INTENT_TYPE_COUPON));
        //拍客返点
        rl2.setOnClickListener(v -> {
           launch(MarketingRebateActivity.class);
        });
        //活动报名
        rl3.setOnClickListener(v -> {
           //TODO
        });
        //关注送好礼
        rl4.setOnClickListener(v -> {

        });
    }

    @Override
    protected boolean isRegisterEventBus() {
        return true;
    }

}
