package com.pai8.ke.activity.video;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.gyf.immersionbar.ImmersionBar;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.entity.LocalMedia;
import com.pai8.ke.R;
import com.pai8.ke.activity.account.LoginActivity;
import com.pai8.ke.activity.common.VideoViewActivity;
import com.pai8.ke.activity.takeaway.ui.StoreActivity;
import com.pai8.ke.activity.video.adapter.CommentAdapter;
import com.pai8.ke.activity.video.adapter.VideoDetailAdapter;
import com.pai8.ke.activity.video.contract.ReportContract;
import com.pai8.ke.activity.video.contract.VideoContract;
import com.pai8.ke.activity.video.fragment.InputCommentDialogFragment;
import com.pai8.ke.activity.video.presenter.ReportPresenter;
import com.pai8.ke.activity.video.presenter.VideoPresenter;
import com.pai8.ke.app.MyApp;
import com.pai8.ke.base.BaseEvent;
import com.pai8.ke.base.BaseMvpActivity;
import com.pai8.ke.entity.resp.CommentResp;
import com.pai8.ke.entity.resp.Comments;
import com.pai8.ke.entity.resp.VideoResp;
import com.pai8.ke.global.GlobalConstants;
import com.pai8.ke.interfaces.OnVideoControllerListener;
import com.pai8.ke.interfaces.OnViewPagerListener;
import com.pai8.ke.manager.AccountManager;
import com.pai8.ke.manager.ActivityManager;
import com.pai8.ke.manager.UploadFileManager;
import com.pai8.ke.manager.ViewPagerLayoutManager;
import com.pai8.ke.utils.AppUtils;
import com.pai8.ke.utils.ChoosePicUtils;
import com.pai8.ke.utils.CollectionUtils;
import com.pai8.ke.utils.ImageLoadUtils;
import com.pai8.ke.utils.LogUtils;
import com.pai8.ke.utils.ResUtils;
import com.pai8.ke.utils.StringUtils;
import com.pai8.ke.utils.ToastUtils;
import com.pai8.ke.widget.BottomDialog;
import com.pai8.ke.widget.CircleImageView;
import com.pai8.ke.widget.CustomVideoView;
import com.pai8.ke.widget.EditTextCountView;
import com.pai8.ke.widget.LikeView;

import java.util.HashMap;
import java.util.List;

import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.LinearLayoutManager;
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
public class VideoDetailActivity extends BaseMvpActivity<VideoContract.Presenter> implements VideoContract.View, SwipeRefreshLayout.OnRefreshListener, ReportContract.View {

    @BindView(R.id.rv)
    RecyclerView mLuRv;
    @BindView(R.id.sr_layout)
    SwipeRefreshLayout mRefreshLayout;
    private CustomVideoView mVideoView;

    private ImageView mIvCurCover;
    private ImageView mIvPlay;

    private VideoDetailAdapter mVideoAdapter;
    private ViewPagerLayoutManager mViewPagerLayoutManager;

    private int mCurPlayPos = -1;
    private int mPageNo = 1;
    private String video_id = "";
    private String mShareImgUrl = "";

    private BottomDialog mShareBottomDialog;
    private BottomDialog mMoreBottomDialog;
    private BottomDialog mContactBottomDialog;
    private BottomDialog mShareModifyBottomDialog;
    private BottomDialog mChatBottomDialog;
    private BottomDialog mCommentsBottomDialog;

    private VideoControllerView mVideoControllerView;
    private CircleImageView mCivShareCover;
    private CommentAdapter mCommentAdapter;

    private ReportPresenter mReportPresenter;

    @Override
    protected boolean isRegisterEventBus() {
        return true;
    }

    @Override
    protected void receiveEvent(BaseEvent event) {
        switch (event.getCode()) {
            case EVENT_REPORT://举报/投诉/拉黑 成功
                if (mMoreBottomDialog != null && mMoreBottomDialog.isShowing()) {
                    mMoreBottomDialog.dismiss();
                }
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case 1:
                    List<LocalMedia> imgs = PictureSelector.obtainMultipleResult(data);
                    if (CollectionUtils.isEmpty(imgs) || mCivShareCover == null) return;
                    String path = imgs.get(0).getPath();
                    ImageLoadUtils.loadImage(VideoDetailActivity.this, path, mCivShareCover,
                            R.mipmap.img_share_cover);
                    UploadFileManager.getInstance().upload(path, new UploadFileManager.Callback() {
                        @Override
                        public void onSuccess(String url, String key) {
                            mShareImgUrl = url;
                        }

                        @Override
                        public void onError(String msg) {
                            ToastUtils.showShort(msg);
                        }
                    });
                    break;

            }
        }
    }

    public static void launch(Context context, String video_id) {
        Intent intent = new Intent(context, VideoDetailActivity.class);
        Bundle bundle = new Bundle();
        intent.putExtra("video_id", video_id);
        intent.putExtras(bundle);
        context.startActivity(intent);
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
                .statusBarDarkFont(false)
                .init();
        mRefreshLayout.setColorSchemeResources(R.color.colorPrimary);

        View view = LayoutInflater.from(this).inflate(R.layout.view_video, findViewById(android.R.id.content),
                false);
        mVideoView = view.findViewById(R.id.video_view);
        mVideoView.setGravityType(CustomVideoView.CENTER);

        mVideoAdapter = new VideoDetailAdapter(this);
        mLuRv.setAdapter(mVideoAdapter);
        setViewPagerLayoutManager();
    }

    @Override
    public void initData() {
        Bundle extras = getIntent().getExtras();
        video_id = extras.getString("video_id");
        mReportPresenter = new ReportPresenter(this);
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
        if (mVideoView != null) {
            mVideoView.start();
        }
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
                playVideo(3);
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
                if (mIvCurCover != null && mVideoView.isPlaying()) {
                    mIvCurCover.setVisibility(View.GONE);
                }
                playVideo(position);
                if (isBottom) {//加载更多
                    mPageNo++;
                    mPresenter.contentList(video_id, mPageNo, GlobalConstants.LOADMORE);
                }
            }
        });
    }

    private void playVideo(int position) {
        if (position == mCurPlayPos) return;

        View itemView = mViewPagerLayoutManager.findViewByPosition(position);

        if (itemView == null) return;

        ViewGroup rootView = itemView.findViewById(R.id.rl_container);
        mVideoControllerView = rootView.findViewById(R.id.video_controller_view);
        LikeView likeView = rootView.findViewById(R.id.like_view);
        mIvPlay = rootView.findViewById(R.id.iv_play);
        ImageView ivCover = rootView.findViewById(R.id.iv_cover);
        mIvPlay.setAlpha(0.4f);

        onVideoControllerClick(mVideoControllerView);

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
        mVideoView.setVideoPath(videoEntity.getProxyUrl());
        mVideoView.start();
        mVideoView.setOnPreparedListener(mp -> {
            mp.setLooping(true);
            mp.setOnInfoListener((mp1, what, extra) -> {
                if (what == MediaPlayer.MEDIA_INFO_VIDEO_RENDERING_START) {
                    // 延迟取消封面，避免加载视频黑屏
                    MyApp.getMyAppHandler().postDelayed(() -> {
                        ivCover.setVisibility(View.INVISIBLE);
                        mIvCurCover = ivCover;
                    }, 150);
                }
                return true;
            });
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
                mPresenter.follow(getCurVideo().getUser_id(), getCurVideo().getFollow_status());
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
                mPresenter.like(getCurVideo().getId(), getCurVideo().getLike_status());
            }

            @Override
            public void onCommentClick() {
                mPresenter.getComments(getCurVideo().getId());
            }

            @Override
            public void onShareClick() {
                mPresenter.share(getCurVideo().getId());
            }

            @Override
            public void onContactUsClick() {
                showContactBottomDialog();
            }

            @Override
            public void onGoSee() {
                String shop_id = getCurVideo().getShop_id();
                StoreActivity.launch(VideoDetailActivity.this, shop_id);
            }
        });
    }

    /**
     * 第三方分享Dialog
     */
    private void showShareBottomDialog(String url, String name) {
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
            share(Wechat.NAME, url, name);
        });
        tvBtnWechatMoments.setOnClickListener(view1 -> {
            if (!isWeChatClientValid()) return;
            share(WechatMoments.NAME, url, name);
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
            new AlertDialog.Builder(this)
                    .setTitle("提示")
                    .setMessage("确定拉黑该用户？")
                    .setPositiveButton("确定", (dialogInterface, i) -> {
                        mReportPresenter.report(getCurVideo().getId(), null, 0);
                    }).setNegativeButton("取消", null)
                    .show();
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
            String[] options = {"呼叫", "复制号码", "添加至手机通讯录"};
            String mobile = getCurVideo().getMobile();
            String shopName = getCurVideo().getShop_name();
            new AlertDialog.Builder(this)
                    .setCancelable(false)
                    .setTitle("这是电话号码，你可以")
                    .setItems(options, (dialogInterface, which) -> {
                        switch (which) {
                            case 0:
                                AppUtils.intentCallPhone(VideoDetailActivity.this, mobile);
                                break;
                            case 1:
                                AppUtils.copyText(mobile);
                                toast("复制成功");
                                break;
                            case 2:
                                AppUtils.intentContactAdd(VideoDetailActivity.this, shopName, shopName,
                                        mobile);
                                break;
                        }
                    }).show();
        });
        tvBtnSms.setOnClickListener(view1 -> {//私信
            new AlertDialog.Builder(this)
                    .setCancelable(false)
                    .setTitle("私信")
                    .setMessage("我的联系方式是：" + getCurVideo().getMobile())
                    .setPositiveButton("确认", null)
                    .show();
        });
        tvBtnWechat.setOnClickListener(view1 -> {//微信
            new AlertDialog.Builder(this)
                    .setCancelable(false)
                    .setTitle("微信")
                    .setMessage("我的微信号是：" + getCurVideo().getWechat())
                    .setPositiveButton("确认", null)
                    .show();
        });
        tvBtnPtp.setOnClickListener(view1 -> {//一对一聊天
            mContactBottomDialog.dismiss();
            showChatBottomDialog();
        });
        if (mContactBottomDialog == null) {
            mContactBottomDialog = new BottomDialog(this, view);
        }
        mContactBottomDialog.setIsCanceledOnTouchOutside(true);
        mContactBottomDialog.show();
    }

    /**
     * 一对一聊天对话框
     */
    private void showChatBottomDialog() {
        View view = View.inflate(this, R.layout.view_dialog_ptp_chat, null);
        ImageButton itnClose = view.findViewById(R.id.itn_close);
        TextView tvBtnAudio = view.findViewById(R.id.tv_btn_audio);
        TextView tvBtnVideo = view.findViewById(R.id.tv_btn_video);
        itnClose.setOnClickListener(view1 -> {
            mChatBottomDialog.dismiss();
        });
        tvBtnAudio.setOnClickListener(view1 -> {
            mChatBottomDialog.dismiss();
            ChatActivity.launch(this, ChatActivity.BIZ_TYPE_AUDIO, ChatActivity.INTENT_TYPE_CALL,
                    getCurVideo().getUser_id());
        });
        tvBtnVideo.setOnClickListener(view1 -> {
            mChatBottomDialog.dismiss();
            ChatActivity.launch(this, ChatActivity.BIZ_TYPE_VIDEO, ChatActivity.INTENT_TYPE_CALL,
                    getCurVideo().getUser_id());
        });
        if (mChatBottomDialog == null) {
            mChatBottomDialog = new BottomDialog(this, view);
        }
        mChatBottomDialog.setIsCanceledOnTouchOutside(true);
        mChatBottomDialog.show();
    }

    private void showCommentsDialog(List<CommentResp> data) {
        if (mCommentsBottomDialog != null && mCommentsBottomDialog.isShowing()) {
            mCommentAdapter.setDataList(data);
            return;
        }
        View view = View.inflate(this, R.layout.view_dialog_comments, null);
        ImageButton itnClose = view.findViewById(R.id.itn_close);
        TextView tvTitle = view.findViewById(R.id.tv_comment_title);
        TextView tvBtnComment = view.findViewById(R.id.tv_btn_comment);
        RecyclerView rvComment = view.findViewById(R.id.rv_comments);
        tvTitle.setText("评论(" + data.size() + ")");
        if (mCommentAdapter == null) {
            mCommentAdapter = new CommentAdapter(this);
            mCommentAdapter.setClick(new CommentAdapter.Click() {
                @Override
                public void comment(CommentResp comment) {
                    showCommentInputDialog(1, comment, null);
                }

                @Override
                public void commentChild(CommentResp comment, Comments comments) {
                    showCommentInputDialog(2, comment, comments);
                }
            });
            rvComment.setLayoutManager(new LinearLayoutManager(this));
            rvComment.setAdapter(mCommentAdapter);
        }
        mCommentAdapter.setDataList(data);

        tvBtnComment.setOnClickListener(view1 -> {
            showCommentInputDialog(0, null, null);
        });
        itnClose.setOnClickListener(view1 -> {
            mCommentsBottomDialog.dismiss();
        });
        if (mCommentsBottomDialog == null) {
            mCommentsBottomDialog = new BottomDialog(this, view);
            mCommentsBottomDialog.setIsCanceledOnTouchOutside(true);
        }
        mCommentsBottomDialog.show();
    }

    /**
     * @param type 0 1 2
     */
    private void showCommentInputDialog(int type, CommentResp comment, Comments comments) {
        String hint = "";
        if (type == 1) {
            hint = comment.getUser_nickname();
        } else if (type == 2) {
            hint = comments.getFrom_nickname();
        }
        InputCommentDialogFragment inputCommentDialogFragment = InputCommentDialogFragment.newInstance(hint);
        inputCommentDialogFragment.show(getSupportFragmentManager(), "1");
        inputCommentDialogFragment.setInputCallback(txt -> {

            switch (type) {
                case 0:
                    mPresenter.comment(getCurVideo().getId(), txt, getCurVideo().getUser_id());
                    break;
                case 1:
                    mPresenter.comment1(getCurVideo().getId(), comment.getCommentId(), txt,
                            comment.getFrom_user_id());
                    break;
                case 2:
                    mPresenter.comment2(getCurVideo().getId(), comment.getCommentId(),
                            comments.getCommentId(), txt, comments.getFrom_user_id());
                    break;
            }

        });
    }

    /**
     * 第三方分享
     *
     * @param platform
     */
    private void share(String platform, String url, String name) {
        Platform.ShareParams sp = new Platform.ShareParams();
        sp.setTitle(name);
        sp.setTitleUrl(url);
        sp.setText(name);
        sp.setUrl(url);
        sp.setImageUrl(mShareImgUrl);
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
        mCurPlayPos = -1;
        MyApp.getMyAppHandler().postDelayed(() -> {
            mPresenter.contentList(video_id, mPageNo, GlobalConstants.REFRESH);
        }, 200);
    }

    @Override
    public void refreshComplete() {
        if (mRefreshLayout != null) {
            mRefreshLayout.setRefreshing(false);
        }
    }

    @Override
    public void contentList(List<VideoResp> data, int tag) {
        if (tag == GlobalConstants.REFRESH) {
            mVideoAdapter.setDataList(data);
        } else if (tag == GlobalConstants.LOADMORE) {
            mVideoAdapter.addAll(data);
        }
    }

    @Override
    public void getComments(List<CommentResp> data) {
        showCommentsDialog(data);
    }

    @Override
    public void follow(int followStatus) {
        getCurVideo().setFollow_status(followStatus);
        mVideoControllerView.follow(followStatus);
    }

    @Override
    public void like(int likeStatus) {
        int like = mVideoControllerView.like(likeStatus);
        getCurVideo().setLike_status(likeStatus);
        getCurVideo().setLike_counts(like);
    }

    @Override
    public void shareUrl(String url) {
        View view = View.inflate(this, R.layout.view_dialog_share_modify, null);
        ImageButton itnClose = view.findViewById(R.id.itn_close);
        TextView tvBtnShare = view.findViewById(R.id.tv_btn_share);
        mCivShareCover = view.findViewById(R.id.civ_cover);
        EditTextCountView etCv = view.findViewById(R.id.et_cv);
        etCv.setLength(50);
        etCv.setEtText(StringUtils.strSafe(getCurVideo().getVideo_desc()));
        ImageLoadUtils.loadImage(this, mShareImgUrl = getCurVideo().getCover_path(), mCivShareCover,
                R.mipmap.img_share_cover);
        mCivShareCover.setOnClickListener(view1 -> {
            ChoosePicUtils.picSingle(VideoDetailActivity.this, 1);
        });
        itnClose.setOnClickListener(view1 -> {
            mShareModifyBottomDialog.dismiss();
        });
        tvBtnShare.setOnClickListener(view1 -> {
            String shareContent = etCv.getText();
            if (StringUtils.isEmpty(url)) {
                toast("分享的url为空");
                return;
            }
            if (StringUtils.isEmpty(shareContent)) {
                toast("请输入分享的内容");
                return;
            }
            mShareModifyBottomDialog.dismiss();
            showShareBottomDialog(url, shareContent);
        });
        if (mShareModifyBottomDialog == null) {
            mShareModifyBottomDialog = new BottomDialog(this, view);
        }
        mShareModifyBottomDialog.setIsCanceledOnTouchOutside(true);
        mShareModifyBottomDialog.show();
    }

    private VideoResp getCurVideo() {
        return mVideoAdapter.getDataList().get(mCurPlayPos);
    }

}