package com.pai8.ke.fragment.chat;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.pai8.ke.R;
import com.pai8.ke.activity.video.ChatActivity;
import com.pai8.ke.base.BaseFragment;
import com.pai8.ke.interfaces.OnChatCrlListener;
import com.pai8.ke.widget.CircleImageView;

import butterknife.BindView;
import butterknife.OnClick;

import static com.pai8.ke.activity.video.ChatActivity.INTENT_TYPE_CALL;
import static com.pai8.ke.activity.video.ChatActivity.INTENT_TYPE_LISTENER;
import static com.pai8.ke.activity.video.ChatActivity.INTENT_TYPE_WAIT;

/**
 * 聊天音频控制面板
 */
public class ChatAudioCrlFragment extends BaseFragment {

    @BindView(R.id.civ_avatar)
    CircleImageView civAvatar;
    @BindView(R.id.tv_name)
    TextView tvName;
    @BindView(R.id.tv_status)
    TextView tvStatus;
    @BindView(R.id.tv_btn_mic)
    TextView tvBtnMic;
    @BindView(R.id.tv_btn_speaker)
    TextView tvBtnSpeaker;
    @BindView(R.id.tv_btn_hang_up)
    TextView tvBtnHangUp;
    @BindView(R.id.tv_btn_hang_up2)
    TextView tvBtnHangUp2;
    @BindView(R.id.tv_btn_listener)
    TextView tvBtnListener;
    @BindView(R.id.tv_timer)
    TextView tvTimer;
    @BindView(R.id.tv_tip)
    TextView tvTip;
    private int mIntentType;

    private OnChatCrlListener mOnChatCrlListener;

    public static ChatAudioCrlFragment newInstance(@ChatActivity.IntentType int intentType) {
        ChatAudioCrlFragment fragment = new ChatAudioCrlFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("intentType", intentType);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_chat_audio_crl;
    }

    @Override
    protected void initView(Bundle arguments) {
        mIntentType = arguments.getInt("intentType");
    }

    @Override
    protected void initData() {
        if (mIntentType == INTENT_TYPE_CALL) {
            tvBtnHangUp2.setVisibility(View.GONE);
            tvBtnListener.setVisibility(View.GONE);
            tvTimer.setVisibility(View.GONE);
            tvTip.setVisibility(View.GONE);
        } else if (mIntentType == INTENT_TYPE_WAIT) {
            tvBtnMic.setVisibility(View.GONE);
            tvBtnSpeaker.setVisibility(View.GONE);
            tvBtnHangUp.setVisibility(View.GONE);
            tvTimer.setVisibility(View.GONE);
            tvTip.setVisibility(View.GONE);
        } else if (mIntentType == INTENT_TYPE_LISTENER) {
            tvStatus.setVisibility(View.GONE);
            tvBtnHangUp2.setVisibility(View.GONE);
            tvBtnListener.setVisibility(View.GONE);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mOnChatCrlListener = (OnChatCrlListener) context;
    }

    @OnClick({R.id.tv_btn_mic, R.id.tv_btn_speaker, R.id.tv_btn_hang_up, R.id.tv_btn_hang_up2,
            R.id.tv_btn_listener})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_btn_mic:
                mOnChatCrlListener.onCrlMic(1);
                break;
            case R.id.tv_btn_speaker:
                mOnChatCrlListener.onCrlSpeaker(1);
                break;
            case R.id.tv_btn_hang_up:
                mOnChatCrlListener.onCrlHangUp(false);
                break;
            case R.id.tv_btn_hang_up2:
                mOnChatCrlListener.onCrlHangUp(true);
                break;
            case R.id.tv_btn_listener:
                mOnChatCrlListener.onCrlListener();
                break;
        }
    }
}
