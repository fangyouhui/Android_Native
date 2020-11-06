package com.pai8.ke.activity.video;

import android.net.Uri;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.gyf.immersionbar.ImmersionBar;
import com.pai8.ke.R;
import com.pai8.ke.activity.video.adapter.VideoDetailAdapter;
import com.pai8.ke.activity.video.contract.VideoContract;
import com.pai8.ke.activity.video.presenter.VideoPresenter;
import com.pai8.ke.app.MyApp;
import com.pai8.ke.base.BaseActivity;
import com.pai8.ke.base.BaseEvent;
import com.pai8.ke.base.BaseMvpActivity;
import com.pai8.ke.entity.resp.VideoResp;
import com.pai8.ke.global.GlobalConstants;
import com.pai8.ke.global.MockData;
import com.pai8.ke.interfaces.OnVideoControllerListener;
import com.pai8.ke.interfaces.OnViewPagerListener;
import com.pai8.ke.manager.ViewPagerLayoutManager;
import com.pai8.ke.utils.LogUtils;
import com.pai8.ke.widget.BottomDialog;
import com.pai8.ke.widget.FullScreenVideoView;
import com.pai8.ke.widget.LikeView;

import java.util.HashMap;
import java.util.List;

import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import butterknife.BindView;
import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.wechat.friends.Wechat;
import cn.sharesdk.wechat.moments.WechatMoments;

import static com.pai8.ke.global.EventCode.EVENT_REPORT;
import static com.pai8.ke.utils.AppUtils.isWeChatClientValid;

/**
 * 视频详情
 * Created by gh on 2020/11/3.
 */
public class VideoDetailActivity extends BaseMvpActivity<VideoContract.Presenter> implements VideoContract.View, SwipeRefreshLayout.OnRefreshListener {

    @BindView(R.id.rv)
    RecyclerView mLuRv;
    @BindView(R.id.sr_layout)
    SwipeRefreshLayout mRefreshLayout;

    private ImageView mIvCurCover;
    private ImageView mIvPlay;
    private FullScreenVideoView mVideoView;

    private VideoDetailAdapter mVideoAdapter;
    private ViewPagerLayoutManager mViewPagerLayoutManager;

    private int mCurPlayPos = -1;
    private int mPageNo = 1;
    private String keywords;

    private BottomDialog mShareBottomDialog;
    private BottomDialog mMoreBottomDialog;
    private BottomDialog mContactBottomDialog;

    @Override
    protected boolean isRegisterEventBus() {
        return true;
    }

    @Override
    protected void receiveEvent(BaseEvent event) {
        switch (event.getCode()) {
            case EVENT_REPORT://举报投诉成功
                if (mMoreBottomDialog != null && mMoreBottomDialog.isShowing()) {
                    mMoreBottomDialog.dismiss();
                }
                break;
        }
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_video_detail;
    }

    @Override
    public void initView() {
        //透明状态栏，字体深色
        ImmersionBar.with(this)
                .transparentStatusBar()
                .statusBarDarkFont(true)
                .init();
        mRefreshLayout.setColorSchemeResources(R.color.colorPrimary);

        mVideoView = new FullScreenVideoView(this);

        mVideoAdapter = new VideoDetailAdapter(this);
        mLuRv.setAdapter(mVideoAdapter);
        setViewPagerLayoutManager();
    }

    @Override
    public void initData() {
        onRefresh();
    }

    @Override
    public void initListener() {
        mRefreshLayout.setOnRefreshListener(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (mIvPlay != null) {
            mIvPlay.setVisibility(View.GONE);
        }
        mVideoView.start();
    }

    @Override
    public void onPause() {
        super.onPause();
        mVideoView.pause();
    }

    @Override
    public void onStop() {
        super.onStop();
        mVideoView.stopPlayback();
    }

    private void setViewPagerLayoutManager() {
        mViewPagerLayoutManager = new ViewPagerLayoutManager(this);
        mLuRv.setLayoutManager(mViewPagerLayoutManager);
        mLuRv.scrollToPosition(0);
        mViewPagerLayoutManager.setOnViewPagerListener(new OnViewPagerListener() {
            @Override
            public void onInitComplete() {
                playVideo(0);
            }

            @Override
            public void onPageRelease(boolean isNext, int position) {
                LogUtils.d("onPageRelease:" + isNext + "-position:" + position);
                if (mIvCurCover != null) {
                    mIvCurCover.setVisibility(View.VISIBLE);
                }
                if (mIvPlay != null) {
                    mIvPlay.setVisibility(View.GONE);
                }
            }

            @Override
            public void onPageSelected(int position, boolean isBottom) {
                playVideo(position);
                if (isBottom) {//加载更多
                    mPageNo++;
                    mPresenter.getVideoList(keywords, mPageNo, GlobalConstants.LOADMORE);
                }
            }
        });
    }

    private void playVideo(int position) {
        if (position == mCurPlayPos) return;

        View itemView = mViewPagerLayoutManager.findViewByPosition(position);

        if (itemView == null) return;

        ViewGroup rootView = itemView.findViewById(R.id.rl_container);
        VideoControllerView videoControllerView = rootView.findViewById(R.id.video_controller_view);
        LikeView likeView = rootView.findViewById(R.id.like_view);
        mIvPlay = rootView.findViewById(R.id.iv_play);
        ImageView ivCover = rootView.findViewById(R.id.iv_cover);
        mIvPlay.setAlpha(0.4f);

        onVideoControllerClick(videoControllerView);

        likeView.setOnPlayPauseListener(() -> {
            if (mVideoView.isPlaying()) {
                mVideoView.pause();
                mIvPlay.setVisibility(View.VISIBLE);
            } else {
                mVideoView.start();
                mIvPlay.setVisibility(View.GONE);
            }
        });

        mCurPlayPos = position;

        // 添加VideoView到当前需要播放的item中，添加进item之前，保证VideoView没有父view。
        ViewGroup parent = (ViewGroup) mVideoView.getParent();
        if (parent != null) {
            parent.removeView(mVideoView);
        }
        rootView.addView(mVideoView, 0);

        VideoResp videoEntity = mVideoAdapter.getDataList().get(position);
        // VideoView设置并播放
        mVideoView.setVideoURI(Uri.parse(videoEntity.getVideo_path()));
        mVideoView.start();
        mVideoView.setOnPreparedListener(mp -> {
            mp.setLooping(true);
            // 延迟取消封面，避免加载视频黑屏
            MyApp.getMyAppHandler().postDelayed(() -> {
                ivCover.setVisibility(View.GONE);
                mIvCurCover = ivCover;
            }, 150);
        });
    }

    /**
     * 视频外部控制器点击事件
     *
     * @param videoControllerView
     */
    private void onVideoControllerClick(VideoControllerView videoControllerView) {
        videoControllerView.setVideoControllerListener(new OnVideoControllerListener() {
            @Override
            public void onAvatarClick() {
                toast(mCurPlayPos + "");
            }

            @Override
            public void onFollowClick() {
            }

            @Override
            public void onHomeClick() {
            }

            @Override
            public void onMoreClick() {
                showMoreBottomDialog();
            }

            @Override
            public void onCloseClick() {
                finish();
            }

            @Override
            public void onLikeClick() {
            }

            @Override
            public void onCommentClick() {
            }

            @Override
            public void onShareClick() {
                showShareBottomDialog();
            }

            @Override
            public void onContactUsClick() {
                showContactBottomDialog();
            }

            @Override
            public void onGoSee() {
            }
        });
    }

    /**
     * 第三方分享Dialog
     */
    private void showShareBottomDialog() {
        View view = View.inflate(this, R.layout.view_dialog_share, null);
        ImageButton itnClose = view.findViewById(R.id.itn_close);
        TextView tvBtnCancel = view.findViewById(R.id.tv_btn_cancel);
        TextView tvBtnWechatFriend = view.findViewById(R.id.tv_btn_wechat_friend);
        TextView tvBtnWechatMoments = view.findViewById(R.id.tv_btn_wechat_moments);
        itnClose.setOnClickListener(view1 -> {
            mShareBottomDialog.dismiss();
        });
        tvBtnCancel.setOnClickListener(view1 -> {
            mShareBottomDialog.dismiss();
        });
        tvBtnWechatFriend.setOnClickListener(view1 -> {
            if (!isWeChatClientValid()) return;
            mPresenter.share(Wechat.NAME, getCurVideo().getId());
        });
        tvBtnWechatMoments.setOnClickListener(view1 -> {
            if (!isWeChatClientValid()) return;
            mPresenter.share(WechatMoments.NAME, getCurVideo().getId());
        });
        if (mShareBottomDialog == null) {
            mShareBottomDialog = new BottomDialog(this, view);
        }
        mShareBottomDialog.setIsCanceledOnTouchOutside(true);
        mShareBottomDialog.show();
    }

    /**
     * 更多Dialog
     */
    private void showMoreBottomDialog() {
        View view = View.inflate(this, R.layout.view_dialog_more, null);
        ImageButton itnClose = view.findViewById(R.id.itn_close);
        TextView tvBtnCancel = view.findViewById(R.id.tv_btn_cancel);
        TextView tvBtnJB = view.findViewById(R.id.tv_btn_jubao);
        TextView tvBtnTS = view.findViewById(R.id.tv_btn_tousu);
        TextView tvBtnLH = view.findViewById(R.id.tv_btn_lahei);
        itnClose.setOnClickListener(view1 -> {
            mMoreBottomDialog.dismiss();
        });
        tvBtnCancel.setOnClickListener(view1 -> {
            mMoreBottomDialog.dismiss();
        });
        tvBtnJB.setOnClickListener(view1 -> {//举报
            ReportActivity.launch(this, getCurVideo().getId(), ReportActivity.INTENT_TYPE_1);
        });
        tvBtnTS.setOnClickListener(view1 -> {//投诉
            ReportActivity.launch(this, getCurVideo().getId(), ReportActivity.INTENT_TYPE_2);

        });
        tvBtnLH.setOnClickListener(view1 -> {//拉黑

        });
        if (mMoreBottomDialog == null) {
            mMoreBottomDialog = new BottomDialog(this, view);
        }
        mMoreBottomDialog.setIsCanceledOnTouchOutside(true);
        mMoreBottomDialog.show();
    }

    /**
     * 联系我Dialog
     */
    private void showContactBottomDialog() {
        View view = View.inflate(this, R.layout.view_dialog_contact, null);
        ImageButton itnClose = view.findViewById(R.id.itn_close);
        TextView tvBtnCancel = view.findViewById(R.id.tv_btn_cancel);
        TextView tvBtnPhone = view.findViewById(R.id.tv_btn_contact_phone);
        TextView tvBtnSms = view.findViewById(R.id.tv_btn_contact_sms);
        TextView tvBtnWechat = view.findViewById(R.id.tv_btn_contact_wechat);
        TextView tvBtnPtp = view.findViewById(R.id.tv_btn_contact_ptp);
        itnClose.setOnClickListener(view1 -> {
            mContactBottomDialog.dismiss();
        });
        tvBtnCancel.setOnClickListener(view1 -> {
            mContactBottomDialog.dismiss();
        });
        tvBtnPhone.setOnClickListener(view1 -> {//电话

        });
        tvBtnSms.setOnClickListener(view1 -> {//私信

        });
        tvBtnWechat.setOnClickListener(view1 -> {//微信

        });
        tvBtnPtp.setOnClickListener(view1 -> {//一对一聊天

        });
        if (mContactBottomDialog == null) {
            mContactBottomDialog = new BottomDialog(this, view);
        }
        mContactBottomDialog.setIsCanceledOnTouchOutside(true);
        mContactBottomDialog.show();
    }

    /**
     * 第三方分享
     *
     * @param platform
     */
    private void share(String platform) {
        Platform.ShareParams sp = new Platform.ShareParams();
        sp.setTitle("");
        sp.setTitleUrl("");
        sp.setText("");
        sp.setUrl("");
        sp.setImageUrl("");
        sp.setShareType(Platform.SHARE_WEBPAGE);
        Platform pform = ShareSDK.getPlatform(platform);
        pform.setPlatformActionListener(new PlatformActionListener() {
            @Override
            public void onComplete(Platform platform, int i, HashMap<String, Object> hashMap) {
                runOnUiThread(() -> {
                    if (mShareBottomDialog.isShowing()) mShareBottomDialog.dismiss();
                });
            }

            @Override
            public void onError(Platform platform, int i, Throwable throwable) {
                runOnUiThread(() -> {
                    toast("分享失败");
                });
            }

            @Override
            public void onCancel(Platform platform, int i) {

            }
        });
        pform.share(sp);
    }

    @Override
    public VideoContract.Presenter initPresenter() {
        return new VideoPresenter(this);
    }

    @Override
    public void onRefresh() {
        mPageNo = 1;
        MyApp.getMyAppHandler().postDelayed(() -> {
            mPresenter.getVideoList(keywords, mPageNo, GlobalConstants.REFRESH);
        }, 200);
    }

    @Override
    public void refreshComplete() {
        if (mRefreshLayout != null) {
            mRefreshLayout.setRefreshing(false);
        }
    }

    @Override
    public void getVideoList(List<VideoResp> data, int tag) {
        if (tag == GlobalConstants.REFRESH) {
            mVideoAdapter.setDataList(data);
        } else if (tag == GlobalConstants.REFRESH) {
            mVideoAdapter.addAll(data);
        }
    }

    @Override
    public void follow(int followStatus) {
        getCurVideo().setFollow_status(followStatus);
        if (followStatus == 1) {

        } else {

        }
    }

    @Override
    public void like(int likeStatus) {
        getCurVideo().setLike_status(likeStatus);
        if (likeStatus == 1) {

        } else {

        }
    }

    @Override
    public void share(String platform, String url) {

    }


    private VideoResp getCurVideo() {
        return mVideoAdapter.getDataList().get(mCurPlayPos);
    }

}