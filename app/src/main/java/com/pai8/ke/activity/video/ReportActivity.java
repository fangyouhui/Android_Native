package com.pai8.ke.activity.video;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.hjq.bar.OnTitleBarListener;
import com.pai8.ke.R;
import com.pai8.ke.activity.video.contract.ReportContract;
import com.pai8.ke.activity.video.presenter.ReportPresenter;
import com.pai8.ke.base.BaseEvent;
import com.pai8.ke.base.BaseMvpActivity;
import com.pai8.ke.utils.EventBusUtils;
import com.pai8.ke.widget.EditTextCountView;

import androidx.annotation.IntDef;
import butterknife.BindView;
import butterknife.OnClick;

import static com.pai8.ke.global.EventCode.EVENT_REPORT;

/**
 * 举报/投诉
 */
public class ReportActivity extends BaseMvpActivity<ReportContract.Presenter> implements ReportContract.View {
    @BindView(R.id.etcv_content)
    EditTextCountView etContent;
    @BindView(R.id.btn_submit)
    Button btnSubmit;

    //举报
    public static final int INTENT_TYPE_1 = 1;
    //投诉
    public static final int INTENT_TYPE_2 = 2;
    //意见反馈
    public static final int INTENT_TYPE_3 = 3;

    private int mIntentType;
    private String mVideoId;

    @IntDef({INTENT_TYPE_1, INTENT_TYPE_2})
    public @interface IntentType {

    }

    public static void launch(Context context, String video_id, @IntentType int intentType) {
        Intent intent = new Intent(context, ReportActivity.class);
        Bundle bundle = new Bundle();
        intent.putExtra("video_id", video_id);
        intent.putExtra("intentType", intentType);
        intent.putExtras(bundle);
        context.startActivity(intent);
    }

    public static void launchFeedBack(Context context) {
        Intent intent = new Intent(context, ReportActivity.class);
        Bundle bundle = new Bundle();
        intent.putExtra("intentType", INTENT_TYPE_3);
        intent.putExtras(bundle);
        context.startActivity(intent);
    }

    @Override
    protected boolean isRegisterEventBus() {
        return true;
    }

    @Override
    protected void receiveEvent(BaseEvent event) {
        switch (event.getCode()) {
            case EVENT_REPORT://举报/投诉/拉黑 成功
                finish();
                break;
        }
    }

    @Override
    public ReportContract.Presenter initPresenter() {
        return new ReportPresenter(this);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_report;
    }

    @Override
    public void initView() {
        Bundle extras = getIntent().getExtras();
        mIntentType = extras.getInt("intentType");
        mVideoId = extras.getString("video_id");
        if (mIntentType == INTENT_TYPE_1) {
            mTitleBar.setTitle("举报");
            btnSubmit.setText("提交举报");
        } else if (mIntentType == INTENT_TYPE_2) {
            mTitleBar.setTitle("投诉");
            btnSubmit.setText("提交投诉");
        } else if (mIntentType == INTENT_TYPE_3) {
            mTitleBar.setTitle("意见反馈");
            btnSubmit.setText("提交");
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
        if (mIntentType == INTENT_TYPE_3) {
            finish();
            toast("感谢您的反馈！");
        } else {
            mPresenter.report(mVideoId, etContent.getText(), mIntentType);
        }
    }

}
