package com.pai8.ke.activity.video;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.hjq.bar.OnTitleBarListener;
import com.pai8.ke.R;
import com.pai8.ke.base.BaseActivity;
import com.pai8.ke.widget.EditTextCountView;

import androidx.annotation.IntDef;
import butterknife.BindView;
import butterknife.OnClick;

/**
 * 举报/投诉
 */
public class ReportActivity extends BaseActivity {
    @BindView(R.id.etcv_content)
    EditTextCountView etContent;
    @BindView(R.id.btn_submit)
    Button btnSubmit;

    //举报
    public static final int INTENT_TYPE_1 = 1;
    //投诉
    public static final int INTENT_TYPE_2 = 2;
    private int mIntentType;

    @IntDef({INTENT_TYPE_1, INTENT_TYPE_2})
    public @interface IntentType {

    }

    public static void launch(Context context, @IntentType int intentType) {
        Intent intent = new Intent(context, ReportActivity.class);
        Bundle bundle = new Bundle();
        intent.putExtra("intentType", intentType);
        intent.putExtras(bundle);
        context.startActivity(intent);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_report;
    }

    @Override
    public void initView() {
        Bundle extras = getIntent().getExtras();
        mIntentType = extras.getInt("intentType");
        if (mIntentType == INTENT_TYPE_1) {
            mTitleBar.setTitle("举报");
            btnSubmit.setText("提交举报");
        } else {
            mTitleBar.setTitle("投诉");
            btnSubmit.setText("提交投诉");
        }
    }

    @Override
    public void initListener() {
        mTitleBar.setOnTitleBarListener(new OnTitleBarListener() {
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
    }

    @OnClick(R.id.btn_submit)
    public void onClick() {
    }
}
