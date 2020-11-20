package com.pai8.ke.activity.video;

import android.app.AlertDialog;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.RelativeLayout;

import com.gh.qiniushortvideo.activity.MediaSelectActivity;
import com.gyf.immersionbar.ImmersionBar;
import com.pai8.ke.R;
import com.pai8.ke.api.Api;
import com.pai8.ke.base.BaseActivity;
import com.pai8.ke.base.BaseEvent;
import com.pai8.ke.base.retrofit.BaseObserver;
import com.pai8.ke.base.retrofit.RxSchedulers;
import com.pai8.ke.entity.PushBiz;
import com.pai8.ke.entity.resp.VideoResp;
import com.pai8.ke.fragment.chat.ChatAudioCrlFragment;
import com.pai8.ke.fragment.chat.ChatVideoCrlFragment;
import com.pai8.ke.global.EventCode;
import com.pai8.ke.global.GlobalConstants;
import com.pai8.ke.interfaces.OnChatCrlListener;
import com.pai8.ke.manager.AccountManager;
import com.pai8.ke.manager.ActivityManager;
import com.pai8.ke.manager.QNRTCManager;
import com.pai8.ke.utils.CollectionUtils;
import com.pai8.ke.utils.LogUtils;
import com.pai8.ke.utils.StringUtils;
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
import androidx.annotation.NonNull;
import androidx.core.app.ActivityOptionsCompat;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.gh.qiniushortvideo.utils.Utils.dip2px;
import static com.mob.tools.utils.ResHelper.getScreenHeight;
import static net.lucode.hackware.magicindicator.buildins.UIUtil.getScreenWidth;

public class ChatActivity extends BaseActivity implements OnChatCrlListener {

    @BindView(R.id.surface_view_local)
    QNSurfaceView mLocalWindow;
    @BindView(R.id.surface_view_remote)
    QNSurfaceView mRemoteWindow;
    @BindView(R.id.rl_local)
    RelativeLayout rlLocal;
    @BindView(R.id.rl_remote)
    RelativeLayout rlRemote;

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
    private String mRemoteId;

    private List<QNTrackInfo> mLocalAudioTracks = new ArrayList<>();
    private List<QNTrackInfo> mLocalVideoTracks = new ArrayList<>();

    private boolean mSate = false;
    private int defaultLocalMargin = dip2px(this, 28);
    private int defaultLocalWidth = dip2px(this, 120);
    private int defaultLocalHeight = dip2px(this, 180);

    public static void launch(Context context, @BizType int bizType, @IntentType int intentType,
                              String remoteId) {
        Intent intent = new Intent(context, ChatActivity.class);
        Bundle bundle = new Bundle();
        intent.putExtra("bizType", bizType);
        intent.putExtra("intentType", intentType);
        intent.putExtra("remoteId", remoteId);
        intent.putExtras(bundle);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    @Override
    protected boolean isRegisterEventBus() {
        return true;
    }

    @Override
    protected void receiveEvent(BaseEvent event) {
        super.receiveEvent(event);
        switch (event.getCode()) {
            case EventCode.EVENT_PUSH:
                //拒绝&聊天页面
                if (((PushBiz) event.getData()).getM_type().equals("3") && ActivityManager.getInstance().isChatActivity()) {
                    toast("对方已拒绝");
                    finish();
                }
                break;
        }
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
        mRemoteId = extras.getString("remoteId");

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

            zoomOpera(rlLocal, mLocalWindow, rlRemote, mRemoteWindow);

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

        // true-扬声器 false-听筒
        mEngine.setDefaultAudioRouteToSpeakerphone(false);

        if (mLocalVideoTrack != null) {
            mLocalVideoTracks.add(mLocalVideoTrack);
            mLocalVideoTracks.add(mLocalAudioTrack);
        }

        if (mLocalAudioTrack != null) {
            mLocalAudioTracks.add(mLocalAudioTrack);
        }

    }

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            toast("对方无人接听,请稍后再试！");
            if (mEngine != null) {
                mEngine.leaveRoom();
            }
            finish();
        }
    };

    @Override
    public void initData() {
        mUid = mAccountManager.getUid();
        if (mIntentType == INTENT_TYPE_CALL) {
            joinRoom(false);
            mHandler.sendEmptyMessageDelayed(0, 60 * 1000);
        }
    }

    private void push(String msgType) {
        // 拨打电话/视频 进行push通知
        Api.getInstance().notifyPush(mRemoteId, msgType, mUid)
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

    public void joinRoom(boolean isRemote) {
        String uid = isRemote ? mRemoteId : mUid;
        Api.getInstance().qnToken("5p" + uid, "5p8" + mUid)
                .doOnSubscribe(disposable -> {
                })
                .compose(RxSchedulers.io_main())
                .subscribe(new BaseObserver<String>() {
                    @Override
                    protected void onSuccess(String roomToken) {
                        mEngine.joinRoom(roomToken);
                        if (isRemote) {
                            // 我主动加入到对方
                            if (mChatAudioCrlFragment != null) mChatAudioCrlFragment.setListener();
                            if (mChatVideoCrlFragment != null) mChatVideoCrlFragment.setListener();
                        } else {
                            // 发推送邀请对方加入
                            push(String.valueOf(mBizType));
                        }

                    }

                    @Override
                    protected void onError(String msg, int errorCode) {
                        super.onError(msg, errorCode);
                    }
                });
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
                            mEngine.publishTracks(mLocalAudioTracks);
                        }
                        if (mChatVideoCrlFragment != null) {
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
                    rlRemote.setVisibility(View.VISIBLE);
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
        if (mIntentType == INTENT_TYPE_WAIT) {
            push(String.valueOf(3));
        }
        if (mEngine != null) {
            mEngine.leaveRoom();
        }
        finish();
    }

    @Override
    public void onCrlListener() { //接听
        if (mIntentType == INTENT_TYPE_WAIT) {
            // 等待接听电话/视频 先获取roomToken再加入聊天
            joinRoom(true);
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

    @OnClick({R.id.surface_view_local, R.id.surface_view_remote})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.surface_view_local:
                if (mSate) {
                    zoomOpera(rlLocal, mLocalWindow, rlRemote, mRemoteWindow);
                    mSate = false;
                }
                break;
            case R.id.surface_view_remote:
                if (!mSate) {
                    zoomOpera(rlRemote, mRemoteWindow, rlLocal, mLocalWindow);
                    mSate = true;
                }
                break;
        }
    }

    private void zoomOpera(View sourcView, QNSurfaceView beforeview,
                           View detView, QNSurfaceView afterview) {
        RelativeLayout paretview = (RelativeLayout) sourcView.getParent();
        paretview.removeView(detView);
        paretview.removeView(sourcView);

        //设置远程大视图RelativeLayout 的属性
        RelativeLayout.LayoutParams params1 =
                new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,
                        RelativeLayout.LayoutParams.MATCH_PARENT);
        params1.addRule(RelativeLayout.CENTER_IN_PARENT, RelativeLayout.TRUE);
        beforeview.setZOrderMediaOverlay(true);
        beforeview.getHolder().setFormat(PixelFormat.TRANSPARENT);
        sourcView.setLayoutParams(params1);

        //设置本地小视图RelativeLayout 的属性
        params1 = new RelativeLayout.LayoutParams(defaultLocalWidth, defaultLocalHeight);
        params1.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, RelativeLayout.TRUE);
        params1.setMargins(0, defaultLocalMargin, defaultLocalMargin, 0);
        //在调用setZOrderOnTop(true)之后调用了setZOrderMediaOverlay(true)  遮挡问题
        afterview.setZOrderOnTop(true);
        afterview.setZOrderMediaOverlay(true);
        afterview.getHolder().setFormat(PixelFormat.TRANSPARENT);
        detView.setLayoutParams(params1);

        paretview.addView(sourcView);
        paretview.addView(detView);
    }
}
