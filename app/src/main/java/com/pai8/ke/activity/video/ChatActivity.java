package com.pai8.ke.activity.video;

import android.app.AlertDialog;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.WindowManager;

import com.gyf.immersionbar.ImmersionBar;
import com.pai8.ke.R;
import com.pai8.ke.api.Api;
import com.pai8.ke.base.BaseActivity;
import com.pai8.ke.base.retrofit.BaseObserver;
import com.pai8.ke.base.retrofit.RxSchedulers;
import com.pai8.ke.entity.resp.VideoResp;
import com.pai8.ke.fragment.chat.ChatAudioCrlFragment;
import com.pai8.ke.fragment.chat.ChatVideoCrlFragment;
import com.pai8.ke.global.GlobalConstants;
import com.pai8.ke.interfaces.OnChatCrlListener;
import com.pai8.ke.manager.AccountManager;
import com.pai8.ke.manager.QNRTCManager;
import com.pai8.ke.utils.CollectionUtils;
import com.pai8.ke.utils.LogUtils;
import com.qiniu.droid.rtc.QNCustomMessage;
import com.qiniu.droid.rtc.QNRTCEngine;
import com.qiniu.droid.rtc.QNRTCEngineEventListener;
import com.qiniu.droid.rtc.QNRoomState;
import com.qiniu.droid.rtc.QNSourceType;
import com.qiniu.droid.rtc.QNStatisticsReport;
import com.qiniu.droid.rtc.QNSurfaceView;
import com.qiniu.droid.rtc.QNTrackInfo;
import com.qiniu.droid.rtc.QNTrackKind;
import com.qiniu.droid.rtc.model.QNAudioDevice;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import androidx.annotation.IntDef;
import butterknife.BindView;
import butterknife.ButterKnife;

public class ChatActivity extends BaseActivity implements OnChatCrlListener {

    @BindView(R.id.surface_view_local)
    QNSurfaceView mLocalWindow;
    @BindView(R.id.surface_view_remote)
    QNSurfaceView mRemoteWindow;

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
    private ChatVideoCrlFragment mChatVideoCrlFragment;

    private QNRTCEngine mEngine;
    private QNTrackInfo mLocalVideoTrack;
    private QNTrackInfo mLocalAudioTrack;

    private int mBizType;
    private int mIntentType;

    private String mUid;
    private String mRemoteUid;

    private List<QNTrackInfo> mLocalAudioTracks = new ArrayList<>();
    private List<QNTrackInfo> mLocalVideoTracks = new ArrayList<>();

    public static void launch(Context context, @BizType int bizType, @IntentType int intentType) {
        Intent intent = new Intent(context, ChatActivity.class);
        Bundle bundle = new Bundle();
        intent.putExtra("bizType", bizType);
        intent.putExtra("intentType", intentType);
        intent.putExtras(bundle);
        context.startActivity(intent);
    }

    public static void launch(Context context, @BizType int bizType, @IntentType int intentType,
                              String remoteUid) {
        Intent intent = new Intent(context, ChatActivity.class);
        Bundle bundle = new Bundle();
        intent.putExtra("bizType", bizType);
        intent.putExtra("intentType", intentType);
        intent.putExtra("remoteUid", remoteUid);
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
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        Bundle extras = getIntent().getExtras();
        mBizType = extras.getInt("bizType");
        mIntentType = extras.getInt("intentType");
        mRemoteUid = extras.getString("remoteUid");

        mEngine = QNRTCManager.getInstance().getQNRTCEngine();

        if (mBizType == BIZ_TYPE_VIDEO) {
            // 视频面板Fragment
            mChatVideoCrlFragment = ChatVideoCrlFragment.newInstance(mIntentType);
            FragmentTransaction ft = getFragmentManager().beginTransaction();
            getSupportFragmentManager().beginTransaction().add(R.id.fragment_container,
                    mChatVideoCrlFragment).commitAllowingStateLoss();
            ft.commitAllowingStateLoss();

            // 视频Track
            mLocalVideoTrack = mEngine.createTrackInfoBuilder()
                    .setSourceType(QNSourceType.VIDEO_CAMERA)
                    .setMaster(true)
                    .create();
            mEngine.setRenderWindow(mLocalVideoTrack, mLocalWindow);

        } else if (mBizType == BIZ_TYPE_AUDIO) {
            // 语音面板Fragment
            mChatAudioCrlFragment = ChatAudioCrlFragment.newInstance(mIntentType);
            FragmentTransaction ft = getFragmentManager().beginTransaction();
            getSupportFragmentManager().beginTransaction().add(R.id.fragment_container,
                    mChatAudioCrlFragment).commitAllowingStateLoss();
            ft.commitAllowingStateLoss();
        }

        // 音频Track
        mLocalAudioTrack = mEngine.createTrackInfoBuilder()
                .setSourceType(QNSourceType.AUDIO)
                .setMaster(true)
                .create();

        //设置初始化或音频设备状态改变时使用的默认音频路由，true-扬声器 false-听筒
        mEngine.setDefaultAudioRouteToSpeakerphone(false);

        if (mLocalVideoTrack != null) {
            mLocalVideoTracks.add(mLocalVideoTrack);
            mLocalVideoTracks.add(mLocalAudioTrack);
        }

        if (mLocalAudioTrack != null) {
            mLocalAudioTracks.add(mLocalAudioTrack);
        }

    }

    @Override
    public void initData() {
        mUid = mAccountManager.getUid();

        if (mIntentType == INTENT_TYPE_CALL) {
            // 拨打电话/视频 进行push通知
            Api.getInstance().notifyPush(mRemoteUid, String.valueOf(mBizType))
                    .doOnSubscribe(disposable -> {
                    })
                    .compose(RxSchedulers.io_main())
                    .subscribe(new BaseObserver<Object>() {
                        @Override
                        protected void onSuccess(Object o) {

                        }

                        @Override
                        protected void onError(String msg, int errorCode) {
                            super.onError(msg, errorCode);
                        }
                    });
        }
    }

    @Override
    public void initListener() {
        mEngine.setEventListener(new QNRTCEngineEventListener() {
            @Override
            public void onRoomStateChanged(QNRoomState qnRoomState) {
                LogUtils.d("qu_chat onRoomStateChanged:" + qnRoomState);
                // 当房间状态改变时会触发此回调
                switch (qnRoomState) {
                    case IDLE:
                        break;
                    case RECONNECTING:
                        if (mChatAudioCrlFragment != null) mChatAudioCrlFragment.stopTimer();
                        if (mChatVideoCrlFragment != null) mChatVideoCrlFragment.stopTimer();
                        break;
                    case CONNECTED:
                        // 加入房间后可以进行tracks的发布
                        if (mChatAudioCrlFragment != null) {
                            mChatAudioCrlFragment.startTimer();
                            mEngine.publishTracks(mLocalAudioTracks);
                        }
                        if (mChatVideoCrlFragment != null) {
                            mChatVideoCrlFragment.startTimer();
                            mEngine.publishTracks(mLocalVideoTracks);
                        }
                        break;
                    case RECONNECTED:
                        if (mChatAudioCrlFragment != null) mChatAudioCrlFragment.startTimer();
                        if (mChatVideoCrlFragment != null) mChatVideoCrlFragment.startTimer();
                        break;
                    case CONNECTING:
                        break;
                }
            }

            @Override
            public void onRoomLeft() {
                // 当退出房间执行完毕后触发该回调
                LogUtils.d("qu_chat onRoomLeft");
            }

            @Override
            public void onRemoteUserJoined(String remoteUserId, String userData) {
                LogUtils.d("qu_chat onRemoteUserJoined remoteUserId:" + remoteUserId);

                // 当远端用户加入房间时会触发此回调
                if (mChatAudioCrlFragment != null) {
                    mChatAudioCrlFragment.setListener();
                    mChatAudioCrlFragment.startTimer();
                }
                if (mChatVideoCrlFragment != null) {
                    mChatVideoCrlFragment.setListener();
                    mChatVideoCrlFragment.startTimer();
                }
            }

            @Override
            public void onRemoteUserReconnecting(String remoteUserId) {
                // 当远端用户重连时会触发此回调
                LogUtils.d("qu_chat onRemoteUserReconnecting:" + remoteUserId);
            }

            @Override
            public void onRemoteUserReconnected(String remoteUserId) {
                // 当远端用户重连成功时会触发此回调
                LogUtils.d("qu_chat onRemoteUserReconnected:" + remoteUserId);
            }

            @Override
            public void onRemoteUserLeft(String remoteUserId) {
                // 当远端用户离开房间时会触发此回调
                LogUtils.d("qu_chat onRemoteUserLeft:" + remoteUserId);
                toast("对方已经挂断！");
                finish();
            }

            @Override
            public void onLocalPublished(List<QNTrackInfo> list) {
                // 当本地Track发布时会触发此回调
                LogUtils.d("qu_chat onLocalPublished QNTrackInfo:" + list.size());
            }

            @Override
            public void onRemotePublished(String remoteUserId, List<QNTrackInfo> list) {
                // 当远端Track发布时会触发此回调
                LogUtils.d("qu_chat onRemotePublished remoteUserId:" + remoteUserId + "-list" + list.size());
            }

            @Override
            public void onRemoteUnpublished(String remoteUserId, List<QNTrackInfo> list) {
                // 当远端Track取消发布时会触发此回调
            }

            @Override
            public void onRemoteUserMuted(String remoteUserId, List<QNTrackInfo> list) {
                // 当远端用户mute Track时会触发此回调
            }

            @Override
            public void onSubscribed(String remoteUserId, List<QNTrackInfo> list) {
                LogUtils.d("qu_chat onSubscribed:" + remoteUserId);
                // 当订阅Track成功时会触发此回调
                for (QNTrackInfo track : list) {
                    if (track.getTrackKind().equals(QNTrackKind.VIDEO)) {
                        mEngine.setRenderWindow(track, mRemoteWindow);
                    }
                }
            }

            @Override
            public void onSubscribedProfileChanged(String remoteUserId, List<QNTrackInfo> list) {
                // 当订阅Track配置变化时触发此回调
            }

            @Override
            public void onKickedOut(String userId) {
                // 当自己被踢出时会触发此回调
            }

            @Override
            public void onStatisticsUpdated(QNStatisticsReport qnStatisticsReport) {

            }

            @Override
            public void onRemoteStatisticsUpdated(List<QNStatisticsReport> list) {

            }

            @Override
            public void onAudioRouteChanged(QNAudioDevice qnAudioDevice) {
                // 当音频路由发生变化时会回调此方法
            }

            @Override
            public void onCreateMergeJobSuccess(String s) {

            }

            @Override
            public void onCreateForwardJobSuccess(String s) {

            }

            @Override
            public void onError(int i, String s) {

            }

            @Override
            public void onMessageReceived(QNCustomMessage qnCustomMessage) {

            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mEngine != null) {
            mEngine.destroy();
        }
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
    public void onCrlMic(boolean isSilent) {
        if (isSilent) {
            toast("已打开静音");
        } else {
            toast("已开启声音");
        }
        mLocalAudioTrack.setMuted(isSilent);
    }

    @Override
    public void onCrlSpeaker(boolean isSpeaker) {
        if (isSpeaker) {
            toast("已切换成扬声器");
        } else {
            toast("已切换成听筒");
        }
        mEngine.setSpeakerphoneOn(isSpeaker);
    }

    @Override
    public void onCrlHangUp(boolean isActive) { //挂断
        if (mEngine != null) {
            mEngine.leaveRoom();
        }
        finish();
    }

    @Override
    public void onCrlListener() { //接听
        if (mIntentType == INTENT_TYPE_WAIT) {
            // 等待接听电话/视频 先获取roomToken再加入聊天
            Api.getInstance().qnToken("5p" + mUid, "5p8" + mUid)
                    .doOnSubscribe(disposable -> {
                    })
                    .compose(RxSchedulers.io_main())
                    .subscribe(new BaseObserver<String>() {
                        @Override
                        protected void onSuccess(String roomToken) {
                            LogUtils.d("chat roomToken:" + roomToken);
                            mEngine.joinRoom(roomToken);
                            if (mChatAudioCrlFragment != null) mChatAudioCrlFragment.setListener();
                            if (mChatVideoCrlFragment != null) mChatVideoCrlFragment.setListener();
                        }

                        @Override
                        protected void onError(String msg, int errorCode) {
                            super.onError(msg, errorCode);
                        }
                    });
        }
    }

    @Override
    public void onCrlCamera(boolean isBackCamera) {
        mEngine.switchCamera(null);
        if (isBackCamera) {
            toast("已切换成后置摄像头");
        } else {
            toast("已切换成前置摄像头");
        }
    }

    //***************************************/页面控制***********************************************

}
