package com.pai8.ke.fragment.chat;

import android.content.Context;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.View;
import android.widget.Chronometer;
import android.widget.TextView;

import com.pai8.ke.R;
import com.pai8.ke.activity.video.ChatActivity;
import com.pai8.ke.api.Api;
import com.pai8.ke.base.BaseFragment;
import com.pai8.ke.base.retrofit.BaseObserver;
import com.pai8.ke.base.retrofit.RxSchedulers;
import com.pai8.ke.entity.UserInfo;
import com.pai8.ke.interfaces.OnChatCrlListener;
import com.pai8.ke.utils.ImageLoadUtils;
import com.pai8.ke.utils.TextViewUtils;
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
    @BindView(R.id.timer)
    Chronometer mTimer;
    @BindView(R.id.tv_tip)
    TextView tvTip;
    private int mIntentType;

    private OnChatCrlListener mOnChatCrlListener;
    private String mRemoteId;

    public static ChatAudioCrlFragment newInstance(@ChatActivity.IntentType int intentType, String remoteId) {
        ChatAudioCrlFragment fragment = new ChatAudioCrlFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("intentType", intentType);
        bundle.putString("remoteId", remoteId);
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
        mRemoteId = arguments.getString("remoteId");
    }

    @Override
    protected void initData() {
        if (mIntentType == INTENT_TYPE_CALL) {
            tvBtnHangUp2.setVisibility(View.GONE);
            tvBtnListener.setVisibility(View.GONE);
            mTimer.setVisibility(View.GONE);
            tvTip.setVisibility(View.GONE);
            tvStatus.setText("正在打给对方，请等待接听…");
        } else if (mIntentType == INTENT_TYPE_WAIT) {
            tvBtnMic.setVisibility(View.GONE);
            tvBtnSpeaker.setVisibility(View.GONE);
            tvBtnHangUp.setVisibility(View.GONE);
            mTimer.setVisibility(View.GONE);
            tvTip.setVisibility(View.GONE);
            tvStatus.setText("邀请你进行语音通话…");
        } else if (mIntentType == INTENT_TYPE_LISTENER) {
            tvStatus.setVisibility(View.GONE);
            tvBtnHangUp2.setVisibility(View.GONE);
            tvBtnListener.setVisibility(View.GONE);
        }
        Api.getInstance().getUserInfoById(mRemoteId)
                .doOnSubscribe(disposable -> {
                })
                .compose(RxSchedulers.io_main())
                .subscribe(new BaseObserver<UserInfo>() {
                    @Override
                    protected void onSuccess(UserInfo userInfo) {
                        tvName.setText(userInfo.getUser_nickname());
                        ImageLoadUtils.loadImage(getActivity(), userInfo.getAvatar(), civAvatar, 0);
                    }

                    @Override
                    protected void onError(String msg, int errorCode) {
                        super.onError(msg, errorCode);
                    }
                });
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mOnChatCrlListener = (OnChatCrlListener) context;
    }

    private boolean isSilent;//是否静音
    private boolean isSpeaker;//是否扬声器

    @OnClick({R.id.tv_btn_mic, R.id.tv_btn_speaker, R.id.tv_btn_hang_up, R.id.tv_btn_hang_up2,
            R.id.tv_btn_listener})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_btn_mic:
                if (!isSilent) {
                    tvBtnMic.setText("点击开麦");
                    TextViewUtils.drawableTop(tvBtnMic, R.mipmap.img_audio_mic_on);
                    isSilent = true;
                } else {
                    tvBtnMic.setText("点击闭麦");
                    TextViewUtils.drawableTop(tvBtnMic, R.mipmap.img_audio_mic_off);
                    isSilent = false;
                }
                mOnChatCrlListener.onCrlMic(isSilent);
                break;
            case R.id.tv_btn_speaker:
                if (!isSpeaker) {
                    TextViewUtils.drawableTop(tvBtnSpeaker, R.mipmap.img_audio_speaker_on);
                    isSpeaker = true;
                } else {
                    TextViewUtils.drawableTop(tvBtnSpeaker, R.mipmap.img_audio_speaker_off);
                    isSpeaker = false;
                }
                mOnChatCrlListener.onCrlSpeaker(isSpeaker);
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

    /**
     * 接听
     */
    public void setListener() {
        tvStatus.setVisibility(View.GONE);
        tvBtnHangUp2.setVisibility(View.GONE);
        tvBtnListener.setVisibility(View.GONE);

        tvBtnMic.setVisibility(View.VISIBLE);
        tvBtnSpeaker.setVisibility(View.VISIBLE);
        tvBtnHangUp.setVisibility(View.VISIBLE);
        mTimer.setVisibility(View.VISIBLE);
        tvTip.setVisibility(View.VISIBLE);
    }

    public void startTimer() {
        mTimer.setBase(SystemClock.elapsedRealtime());
        mTimer.start();
    }

    public void stopTimer() {
        mTimer.stop();
    }
}
