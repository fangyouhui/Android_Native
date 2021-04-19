package com.pai8.ke.activity.video.tiktok;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.constraintlayout.widget.Group;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.viewpager2.widget.ViewPager2;

import com.blankj.utilcode.util.ClipboardUtils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.FutureTarget;
import com.bumptech.glide.request.target.Target;
import com.dueeeke.videoplayer.player.VideoView;
import com.dueeeke.videoplayer.util.L;
import com.gyf.immersionbar.ImmersionBar;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;
import com.pai8.ke.R;
import com.pai8.ke.activity.account.LoginActivity;
import com.pai8.ke.activity.takeaway.ui.StoreActivity;
import com.pai8.ke.activity.video.ChatActivity;
import com.pai8.ke.activity.video.ReportActivity;
import com.pai8.ke.activity.video.adapter.CommentAdapter;
import com.pai8.ke.activity.video.adapter.TikTokAdapter;
import com.pai8.ke.activity.video.contract.ReportContract;
import com.pai8.ke.activity.video.contract.VideoContract;
import com.pai8.ke.activity.video.fragment.InputCommentDialogFragment;
import com.pai8.ke.activity.video.presenter.ReportPresenter;
import com.pai8.ke.activity.video.presenter.VideoPresenter;
import com.pai8.ke.app.MyApp;
import com.pai8.ke.base.BaseEvent;
import com.pai8.ke.base.BaseMvpActivity;
import com.pai8.ke.entity.Shop;
import com.pai8.ke.entity.Video;
import com.pai8.ke.entity.event.VideoItemRefreshEvent;
import com.pai8.ke.entity.resp.CommentResp;
import com.pai8.ke.entity.resp.Comments;
import com.pai8.ke.entity.resp.ShareMiniResp;
import com.pai8.ke.global.EventCode;
import com.pai8.ke.global.GlobalConstants;
import com.pai8.ke.interfaces.OnVideoControllerListener;
import com.pai8.ke.interfaces.contract.ShareContract;
import com.pai8.ke.interfaces.contract.VideoDetailContract;
import com.pai8.ke.interfaces.contract.VideoHomeContract;
import com.pai8.ke.manager.UploadFileManager;
import com.pai8.ke.presenter.SharePresenter;
import com.pai8.ke.presenter.VideoHomePresenter;
import com.pai8.ke.utils.ChoosePicUtils;
import com.pai8.ke.utils.CollectionUtils;
import com.pai8.ke.utils.DKPlayerUtils;
import com.pai8.ke.utils.EventBusUtils;
import com.pai8.ke.utils.GlideEngine;
import com.pai8.ke.utils.ImageLoadUtils;
import com.pai8.ke.utils.LogUtils;
import com.pai8.ke.utils.StringUtils;
import com.pai8.ke.utils.ToastUtils;
import com.pai8.ke.utils.WxShareUtils;
import com.pai8.ke.utils.cache.PreloadManager;
import com.pai8.ke.widget.BottomDialog;
import com.pai8.ke.widget.CircleImageView;
import com.pai8.ke.widget.EditTextCountView;
import com.pai8.ke.widget.VerticalViewPager;
import com.theartofdev.edmodo.cropper.CropImage;

import java.io.File;
import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ExecutionException;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.wechat.moments.WechatMoments;
import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.FlowableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

import static com.pai8.ke.global.EventCode.EVENT_REPORT;
import static com.pai8.ke.global.GlobalConstants.LOADMORE;
import static com.pai8.ke.global.GlobalConstants.REFRESH;
import static com.pai8.ke.utils.AppUtils.isWeChatClientValid;

/**
 * 视频详情
 * Created by gh on 2020/11/3.
 */
public class TikTokActivity extends BaseMvpActivity<VideoContract.Presenter> implements VideoContract.View,
        SwipeRefreshLayout.OnRefreshListener, ReportContract.View, VideoDetailContract.View,
        VideoHomeContract.View, ShareContract.View {

    private ViewPager2 mViewPager;

    private int mCurPlayPos;
    private int mPageNo = 1;
    private int mPosition;
    private int mType;
    private String mKeyWords = "";
    private String mShareImgUrl = "";
    private String mShareDescription = "";

    private TikTokAdapter mTikTokAdapter;
    private PreloadManager mPreloadManager;
    private TikTokController mController;
    private RecyclerView mViewPagerImpl;

    private BottomDialog mShareBottomDialog;
    private BottomDialog mMoreBottomDialog;
    private BottomDialog mContactBottomDialog;
    private BottomDialog mShareModifyBottomDialog;
    private BottomDialog mChatBottomDialog;
    private BottomDialog mCommentsBottomDialog;

    private CircleImageView mCivShareCover;
    private CommentAdapter mCommentAdapter;

    private ReportPresenter mReportPresenter;
    private VideoHomePresenter mVideoHomePresenter;
    private SharePresenter mSharePresenter;
    private TextView mTvCommentsTitle;
    private VideoView mVideoView;
    private BottomDialog mChooseBottomDialog;

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
                    String path = imgs.get(0).getRealPath();
                    ImageLoadUtils.loadImage(TikTokActivity.this, path, mCivShareCover,
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
                case CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE:
                    //裁剪后跳转分享框
                    CropImage.ActivityResult result = CropImage.getActivityResult(data);
                    Uri resultUri = result.getUri();
                    shareUrl(getCurVideo().getShare_url(), resultUri.toString());
                    break;
            }
        }
    }

    public static void launch(Context context, List<Video> videos, int pageNo, int position,
                              int type) {
        Intent intent = new Intent(context, TikTokActivity.class);
        Bundle bundle = new Bundle();
        intent.putExtra("videos", (Serializable) videos);
        intent.putExtra("pageNo", pageNo);
        intent.putExtra("position", position);
        intent.putExtra("type", type);
        intent.putExtras(bundle);
        context.startActivity(intent);
    }

    public static void launchSearch(Context context, List<Video> videos, String keywords, int pageNo,
                                    int position) {
        Intent intent = new Intent(context, TikTokActivity.class);
        Bundle bundle = new Bundle();
        intent.putExtra("videos", (Serializable) videos);
        intent.putExtra("pageNo", pageNo);
        intent.putExtra("position", position);
        intent.putExtra("keywords", keywords);
        intent.putExtra("type", 5);
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

        //init VideoView
        mVideoView = new VideoView(this);
        mVideoView.setLooping(true);

    }

    private void initViewPager() {
        mViewPager = findViewById(R.id.vp2);
        mViewPager.setOffscreenPageLimit(4);
        mTikTokAdapter = new TikTokAdapter(this);
        mViewPager.setAdapter(mTikTokAdapter);
        mViewPager.setOverScrollMode(View.OVER_SCROLL_NEVER);
        mViewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            private int mCurItem;
            private boolean mIsReverseScroll;//VerticalViewPager是否反向滑动

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                super.onPageScrolled(position, positionOffset, positionOffsetPixels);
                if (position == mCurItem) {
                    return;
                }
                mIsReverseScroll = position < mCurItem;
            }

            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                if (position == mCurPlayPos) return;
                if (position == mTikTokAdapter.getDataList().size() - 1) {
                    mPageNo++;
                    switch (mType) {
                        case 0:
                            mVideoHomePresenter.nearby(mPageNo, LOADMORE);
                            break;
                        case 1:
                            mVideoHomePresenter.flow(mPageNo, LOADMORE);
                            break;
                        case 2:
                            mVideoHomePresenter.follow(mPageNo, LOADMORE);
                            break;
                        case 3:
                            mVideoHomePresenter.myVideo(mPageNo, LOADMORE);
                            break;
                        case 4:
                            mVideoHomePresenter.myLike(mPageNo, LOADMORE);
                        case 5: //搜索
                            mVideoHomePresenter.search(mKeyWords, mPageNo, LOADMORE);
                        case 6://关联我/我关联
                            mVideoHomePresenter.myLink(mPageNo, LOADMORE);
                    }
                }
                mViewPager.post(() -> startPlay(position));
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                super.onPageScrollStateChanged(state);
                if (state == VerticalViewPager.SCROLL_STATE_DRAGGING) {
                    mCurItem = mViewPager.getCurrentItem();
                }
                if (state == ViewPager2.SCROLL_STATE_IDLE) {
                    mPreloadManager.resumePreload(mCurPlayPos, mIsReverseScroll);
                } else {
                    mPreloadManager.pausePreload(mCurPlayPos, mIsReverseScroll);
                }
            }
        });

        //ViewPage2内部是通过RecyclerView去实现的，它位于ViewPager2的第0个位置
        mViewPagerImpl = (RecyclerView) mViewPager.getChildAt(0);
    }

    @Override
    public void initData() {
        mVideoHomePresenter = new VideoHomePresenter(this);
        mReportPresenter = new ReportPresenter(this);
        mSharePresenter = new SharePresenter(this);

        Bundle extras = getIntent().getExtras();
        List<Video> videos = (List<Video>) extras.getSerializable("videos");
        mKeyWords = extras.getString("keywords");
        mPageNo = extras.getInt("pageNo", 0);
        mPosition = extras.getInt("position");
        mType = extras.getInt("type");

        initViewPager();

        mVideoView.setScreenScaleType(VideoView.SCREEN_SCALE_DEFAULT);
        mController = new TikTokController(this);
        mVideoView.setVideoController(mController);

        mPreloadManager = PreloadManager.getInstance(this);

        if (CollectionUtils.isNotEmpty(videos)) {
            mTikTokAdapter.setDataList(videos);
        }

        mViewPager.postDelayed(() -> {
            if (mPosition == 0) {
                startPlay(0);
            } else {
                mViewPager.setCurrentItem(mPosition, false);
            }
        }, 50);

    }

    @Override
    public void initListener() {
        mTikTokAdapter.setVideoControllerListener(new OnVideoControllerListener() {
            @Override
            public void onAvatarClick() {
            }

            @Override
            public void onFollowClick() {
                mPresenter.follow(getCurVideo());
            }

            @Override
            public void onHomeClick() {
                finish();
                EventBusUtils.sendEvent(new BaseEvent(EventCode.EVENT_HOME_TAB, 0));
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
                mPresenter.like(getCurVideo());
            }

            @Override
            public void onCommentClick() {
                mPresenter.getComments(getCurVideo());
            }

            @Override
            public void onShareClick() {
                cropImage(getCurVideo().getCover_path());
            }

            @Override
            public void onContactUsClick() {
                showContactBottomDialog();
            }

            @Override
            public void onGoSee() {
                Shop shop = getCurVideo().getShop();
                if (shop == null) return;
                StoreActivity.launch(TikTokActivity.this, shop.getId());
            }
        });
    }

    private void startPlay(int position) {
        int count = mViewPagerImpl.getChildCount();
        for (int i = 0; i < count; i++) {
            View itemView = mViewPagerImpl.getChildAt(i);
            TikTokAdapter.ViewHolder viewHolder = (TikTokAdapter.ViewHolder) itemView.getTag();
            L.i("startPlay: " + "viewHolder.mPosition: " + viewHolder.mPosition + "  position: " + position);
            if (viewHolder.mPosition == position) {
                mVideoView.release();
                DKPlayerUtils.removeViewFormParent(mVideoView);
                Video video = mTikTokAdapter.getDataList().get(position);
                String playUrl = mPreloadManager.getPlayUrl(video.getVideo_path());
                L.i("startPlay: " + "position: " + position + "  url: " + playUrl);
                mVideoView.setUrl(playUrl);
                mController.addControlComponent(viewHolder.mTikTokView, true);
                viewHolder.mPlayerContainer.addView(mVideoView, 0);
                mVideoView.start();
                mCurPlayPos = position;
                mPresenter.look(getCurVideo());
                break;
            }
        }
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
//            share(Wechat.NAME, url, name);
            mSharePresenter.shareVideo(getCurVideo().getId());
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
        Group group = view.findViewById(R.id.group);
        TextView tvBtnCancel = view.findViewById(R.id.tv_btn_cancel);
        TextView tvBtnDel = view.findViewById(R.id.tv_btn_del);
        TextView tvBtnJB = view.findViewById(R.id.tv_btn_jubao);
        TextView tvBtnTS = view.findViewById(R.id.tv_btn_tousu);
        TextView tvBtnLH = view.findViewById(R.id.tv_btn_lahei);
        if (getCurVideo().isSelf()) {
            group.setVisibility(View.GONE);
            tvBtnDel.setVisibility(View.VISIBLE);
        } else {
            group.setVisibility(View.VISIBLE);
            tvBtnDel.setVisibility(View.GONE);
        }
        itnClose.setOnClickListener(view1 -> {
            mMoreBottomDialog.dismiss();
        });
        tvBtnDel.setOnClickListener(view1 -> {//删除
            new AlertDialog.Builder(this)
                    .setTitle("提示")
                    .setMessage("确定删除该视频？")
                    .setPositiveButton("确定", (dialogInterface, i) -> {
                        mVideoHomePresenter.deleteVideo(getCurVideo().getId());
                    }).setNegativeButton("取消", null)
                    .show();

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
        tvBtnPhone.setOnClickListener(view1 -> { //电话
            mContactBottomDialog.dismiss();
            Shop shop = getCurVideo().getShop();
            PhoneBottomDialogFragment dialogFragment = PhoneBottomDialogFragment.newInstance(getCurVideo().getUser().getPhone(), shop.getName());
            dialogFragment.show(getSupportFragmentManager(), "phone");

//            String[] options = {"呼叫", "复制号码", "添加至手机通讯录"};
//            String mobile = getCurVideo().getUser().getPhone();
//            new AlertDialog.Builder(this)
//                    .setCancelable(false)
//                    .setTitle("这是电话号码，你可以")
//                    .setItems(options, (dialogInterface, which) -> {
//                        switch (which) {
//                            case 0:
//                                AppUtils.intentCallPhone(TikTokActivity.this, mobile);
//                                break;
//                            case 1:
//                                AppUtils.copyText(mobile);
//                                toast("复制成功");
//                                break;
//                            case 2:
//                                Shop shop = getCurVideo().getShop();
//                                if (shop != null) {
//                                    AppUtils.intentContactAdd(TikTokActivity.this, shop.getName(),
//                                            shop.getName(), mobile);
//                                } else {
//                                    AppUtils.intentContactAdd(TikTokActivity.this, "", "", mobile);
//                                }
//                                break;
//                        }
//                    }).show();
        });
        tvBtnSms.setOnClickListener(view1 -> {//私信
            if (getCurVideo().getUser() == null || StringUtils.isEmpty(getCurVideo().getUser().getPhone())) {
                toast("暂时没有联系方式~");
                return;
            }
            new AlertDialog.Builder(this)
                    .setCancelable(false)
                    .setTitle("私信")
                    .setMessage("我的联系方式是：" + getCurVideo().getUser().getPhone())
                    .setPositiveButton("确认", null)
                    .show();
        });
        tvBtnWechat.setOnClickListener(view1 -> {//微信
            if (getCurVideo().getUser() == null || StringUtils.isEmpty(getCurVideo().getUser().getWechat())) {
                toast("暂时没有联系方式~");
                return;
            }
            new AlertDialog.Builder(this)
                    .setCancelable(false)
                    .setTitle("微信")
                    .setMessage("我的微信号是：" + getCurVideo().getUser().getWechat())
                    .setPositiveButton("确认", (dialog, which) -> {
                        ClipboardUtils.copyText(getCurVideo().getUser().getWechat());
                        ToastUtils.showShort("已复制到粘贴板");
                    })
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
                    getCurVideo().getUser().getId());
        });
        tvBtnVideo.setOnClickListener(view1 -> {
            mChatBottomDialog.dismiss();
            ChatActivity.launch(this, ChatActivity.BIZ_TYPE_VIDEO, ChatActivity.INTENT_TYPE_CALL,
                    getCurVideo().getUser().getId());
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
        mTvCommentsTitle = view.findViewById(R.id.tv_comment_title);
        TextView tvBtnComment = view.findViewById(R.id.tv_btn_comment);
        RecyclerView rvComment = view.findViewById(R.id.rv_comments);
        mTvCommentsTitle.setText("评论(" + getCurVideo().getComment_counts() + ")");
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
        if (!mAccountManager.isLogin()) {
            launch(LoginActivity.class);
            return;
        }
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
                    mPresenter.commentVideo(getCurVideo(), txt);
                    break;
                case 1:
                    mPresenter.comment(getCurVideo(), comment.getCommentId(), txt);
                    break;
                case 2:
                    mPresenter.comment(getCurVideo(), comments.getCommentId(), txt);
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
        if (mShareImgUrl != null && mShareImgUrl.length() > 0) {
            sp.setImageUrl(mShareImgUrl);
        } else {
            sp.setImageUrl(url);
        }
        sp.setShareType(Platform.SHARE_WEBPAGE);
        Platform pform = ShareSDK.getPlatform(platform);
        pform.setPlatformActionListener(new PlatformActionListener() {
            @Override
            public void onComplete(Platform platform, int i, HashMap<String, Object> hashMap) {
                runOnUiThread(() -> {
                    if (mShareBottomDialog != null && mShareBottomDialog.isShowing())
                        mShareBottomDialog.dismiss();
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
        mCurPlayPos = -1;
        mPageNo = 1;
        MyApp.getMyAppHandler().postDelayed(() -> {
            switch (mType) {
                case 0: //附近
                    mVideoHomePresenter.nearby(mPageNo, REFRESH);
                    break;
                case 1: //推荐
                    mVideoHomePresenter.flow(mPageNo, REFRESH);
                    break;
                case 2: // 关注
                    mVideoHomePresenter.follow(mPageNo, REFRESH);
                    break;
                case 3: //我的
                    mVideoHomePresenter.myVideo(mPageNo, REFRESH);
                    break;
                case 4: //喜欢
                    mVideoHomePresenter.myLike(mPageNo, REFRESH);
                    break;
                case 5: //搜索
                    mVideoHomePresenter.search(mKeyWords, mPageNo, REFRESH);
                    break;
                case 6://我关联/关联我
                    mVideoHomePresenter.myLink(mPageNo, REFRESH);
                    break;


            }
        }, 200);
    }

    @Override
    public void refreshComplete() {

    }

    @Override
    public void setNoMore() {

    }

    @Override
    public void videoList(List<Video> data, int tag) {
        if (tag == GlobalConstants.REFRESH) {
            mTikTokAdapter.setDataList(data);
        } else if (tag == GlobalConstants.LOADMORE) {
            mTikTokAdapter.addAll(data);
        }
    }

    @Override
    public void deleteVideo(String videoId) {
        finish();
    }

    @Override
    public void loginView() {

    }

    @Override
    public void login() {
        launch(LoginActivity.class);
    }

    @Override
    public void getComments(List<CommentResp> data) {
        showCommentsDialog(data);
    }

    @Override
    public void newVideo(Video video, boolean refreshPage) {
        LogUtils.d("video:" + video.toString());
        if (refreshPage) {
            mTikTokAdapter.notifyItemChanged(mCurPlayPos, video);
            if (mTvCommentsTitle != null)
                mTvCommentsTitle.setText("评论(" + video.getComment_counts() + ")");
        }
        //通知首页视频列表局部刷新
        EventBusUtils.sendEvent(new BaseEvent(EventCode.EVENT_VIDEO_ITEM,
                new VideoItemRefreshEvent(mCurPlayPos, video)));
    }

    public void shareUrl(String url, String localUrl) {
        View view = View.inflate(this, R.layout.view_dialog_share_modify, null);
        ImageButton itnClose = view.findViewById(R.id.itn_close);
        TextView tvBtnShare = view.findViewById(R.id.tv_btn_share);
        mCivShareCover = view.findViewById(R.id.civ_cover);
        EditTextCountView etCv = view.findViewById(R.id.et_cv);
        etCv.setLength(50);
        etCv.setEtText(StringUtils.strSafe(getCurVideo().getVideo_desc()));
        ImageLoadUtils.loadImage(this, localUrl, mCivShareCover, R.mipmap.img_share_cover);
        mCivShareCover.setOnClickListener(view1 -> {
            View popView = View.inflate(TikTokActivity.this, R.layout.view_dialog_choose_qnvideo,
                    null);
            TextView tvBtnGalley = popView.findViewById(R.id.tv_btn_galley);
            TextView tvBtnTakePhoto = popView.findViewById(R.id.tv_btn_take_photo);
            ImageButton tnClose = popView.findViewById(R.id.itn_close);
            tvBtnGalley.setOnClickListener(view12 -> {
                ChoosePicUtils.picSingle(TikTokActivity.this, 1);
                mChooseBottomDialog.dismiss();
            });
            tvBtnTakePhoto.setOnClickListener(view13 -> {
                PictureSelector.create(this)
                        .openCamera(PictureMimeType.ofImage())
                        .loadImageEngine(GlideEngine.createGlideEngine())
                        .forResult(1);
                mChooseBottomDialog.dismiss();
            });

            tnClose.setOnClickListener(v -> mChooseBottomDialog.dismiss());

            mChooseBottomDialog = new BottomDialog(TikTokActivity.this, popView);
            mChooseBottomDialog.setIsCanceledOnTouchOutside(true);
            mChooseBottomDialog.show();
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
            mShareDescription = shareContent;
            mShareModifyBottomDialog.dismiss();
            showShareBottomDialog(url, shareContent);
        });
        if (mShareModifyBottomDialog == null) {
            mShareModifyBottomDialog = new BottomDialog(this, view);
        }
        mShareModifyBottomDialog.setIsCanceledOnTouchOutside(true);
        mShareModifyBottomDialog.show();
    }

    private Video getCurVideo() {
        return mTikTokAdapter.getDataList().get(mCurPlayPos);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mVideoView != null) {
            mVideoView.resume();
        }
    }


    @Override
    protected void onPause() {
        super.onPause();
        if (mVideoView != null) {
            mVideoView.pause();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mVideoView != null) {
            mVideoView.release();
        }
    }

    @Override
    public void onBackPressed() {
        if (mVideoView == null || !mVideoView.onBackPressed()) {
            super.onBackPressed();
        }
    }

    @Override
    public void shareMini(ShareMiniResp resp) {
        if (mShareImgUrl != null && mShareImgUrl.length() > 0) {
            resp.setThumb(mShareImgUrl);
        }
        if (mShareDescription != null && mShareDescription.length() > 0) {
            resp.setDescription(mShareDescription);
        }
        WxShareUtils.shareToWeChat(resp, new PlatformActionListener() {
            @Override
            public void onComplete(Platform platform, int i, HashMap<String, Object> hashMap) {
                runOnUiThread(() -> {
                    if (mShareBottomDialog != null && mShareBottomDialog.isShowing())
                        mShareBottomDialog.dismiss();
                });
            }

            @Override
            public void onError(Platform platform, int i, Throwable throwable) {
                runOnUiThread(() -> {
                    toast("分享失败:" + throwable.getMessage() + i);
                });
            }

            @Override
            public void onCancel(Platform platform, int i) {

            }
        });
    }


    /**
     * 裁剪图片
     */
    @SuppressLint("CheckResult")
    private void cropImage(String url) {
        Flowable.create((FlowableOnSubscribe<File>) emitter -> {
            try {
                FutureTarget<File> target = Glide.with(TikTokActivity.this)
                        .downloadOnly()
                        .load(TextUtils.isEmpty(url) ? "" : url)
                        .submit(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL);
                File imageFile = target.get();
                emitter.onNext(imageFile);
            } catch (ExecutionException e) {
                e.printStackTrace();
                emitter.onComplete();
            } catch (InterruptedException e) {
                e.printStackTrace();
                emitter.onComplete();
            }
        }, BackpressureStrategy.BUFFER)
                .subscribeOn(Schedulers.newThread())//指定在子线程中执行下载/取缓存图片路径
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(file -> {
                    Uri localUri = Uri.fromFile(file);
                    CropImage.activity(localUri)
                            .start(TikTokActivity.this);
                });
    }

}