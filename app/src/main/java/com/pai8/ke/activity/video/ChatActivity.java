package com.pai8.ke.activity.video;

import android.app.AlertDialog;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import com.gyf.immersionbar.ImmersionBar;
import com.pai8.ke.R;
import com.pai8.ke.base.BaseActivity;
import com.pai8.ke.fragment.chat.ChatAudioCrlFragment;
import com.pai8.ke.interfaces.OnChatCrlListener;

import androidx.annotation.IntDef;

public class ChatActivity extends BaseActivity implements OnChatCrlListener {

    //拨打
    public static final int INTENT_TYPE_CALL = 1;
    //等待接听
    public static final int INTENT_TYPE_WAIT = 2;
    //接听中
    public static final int INTENT_TYPE_LISTENER = 3;

    @IntDef({INTENT_TYPE_CALL, INTENT_TYPE_WAIT, INTENT_TYPE_LISTENER})
    public @interface IntentType {
    }

    //语音
    public static final int BIZ_TYPE_AUDIO = 1;
    //视频
    public static final int BIZ_TYPE_VIDEO = 2;

    @IntDef({BIZ_TYPE_AUDIO, BIZ_TYPE_VIDEO})
    public @interface BizType {
    }

    private ChatAudioCrlFragment mChatAudioCrlFragment;

    private int mBizType;
    private int mIntentType;

    public static void launch(Context context, @BizType int bizType, @IntentType int intentType) {
        Intent intent = new Intent(context, ChatActivity.class);
        Bundle bundle = new Bundle();
        intent.putExtra("bizType", bizType);
        intent.putExtra("intentType", intentType);
        intent.putExtras(bundle);
        context.startActivity(intent);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_chat;
    }

    @Override
    public void initView() {
        //透明状态栏，字体深色
        ImmersionBar.with(this)
                .transparentStatusBar()
                .statusBarDarkFont(false)
                .init();

        Bundle extras = getIntent().getExtras();
        mBizType = extras.getInt("bizType");
        mIntentType = extras.getInt("intentType");

        //语音面板Fragment
        mChatAudioCrlFragment = ChatAudioCrlFragment.newInstance(mIntentType);
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        getSupportFragmentManager().beginTransaction().add(R.id.fragment_container, mChatAudioCrlFragment).commitAllowingStateLoss();
        ft.commitAllowingStateLoss();
    }

    @Override
    public void initData() {

    }

    @Override
    public void onBackPressed() {
        String s = mBizType == BIZ_TYPE_VIDEO ? "您正在拨打视频电话，确定放弃？" : "您正在拨打语音电话，确定放弃？";
        if (mIntentType == INTENT_TYPE_CALL) {
            new AlertDialog.Builder(this).setTitle("提示").setMessage(s)
                    .setPositiveButton("确定", (dialogInterface, i) -> finish()).setNegativeButton("取消",
                    null).show();
            return;
        }
        if (mIntentType == INTENT_TYPE_LISTENER) {
            new AlertDialog.Builder(this).setTitle("提示").setMessage("确定放弃当前通话？")
                    .setPositiveButton("确定", (dialogInterface, i) -> finish()).setNegativeButton("取消",
                    null).show();
            return;
        }
        super.onBackPressed();
    }

    //***************************************页面控制************************************************

    @Override
    public void onCrlMic(int status) {

    }

    @Override
    public void onCrlSpeaker(int status) {

    }

    @Override
    public void onCrlHangUp(boolean isActive) {
        finish();
    }

    @Override
    public void onCrlListener() {

    }

    @Override
    public void onCrlCamera(boolean isBackCamera) {

    }

    //***************************************/页面控制***********************************************
}
