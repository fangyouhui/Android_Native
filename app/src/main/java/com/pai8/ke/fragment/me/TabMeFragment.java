package com.pai8.ke.fragment.me;

import android.content.Intent;
import android.os.Bundle;
import android.text.SpannableStringBuilder;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.appbar.AppBarLayout;
import com.gyf.immersionbar.ImmersionBar;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.entity.LocalMedia;
import com.pai8.ke.R;
import com.pai8.ke.activity.account.LoginActivity;
import com.pai8.ke.activity.me.CouponListActivity;
import com.pai8.ke.activity.me.SettingActivity;
import com.pai8.ke.activity.me.ui.AttentionMineActivity;
import com.pai8.ke.activity.me.ui.EditPersonalInfoActivity;
import com.pai8.ke.activity.me.ui.FansActivity;
import com.pai8.ke.activity.me.ui.ReceiveLikesActivity;
import com.pai8.ke.activity.takeaway.order.OrderActivity;
import com.pai8.ke.activity.takeaway.ui.MerchantSettledFirstActivity;
import com.pai8.ke.activity.takeaway.ui.StoreManagerActivity;
import com.pai8.ke.activity.video.ReportActivity;
import com.pai8.ke.activity.wallet.WalletActivity;
import com.pai8.ke.adapter.TabAdapter;
import com.pai8.ke.api.Api;
import com.pai8.ke.base.BaseEvent;
import com.pai8.ke.base.BaseFragment;
import com.pai8.ke.base.retrofit.BaseObserver;
import com.pai8.ke.base.retrofit.RxSchedulers;
import com.pai8.ke.entity.UserInfo;
import com.pai8.ke.entity.resp.MyInfoResp;
import com.pai8.ke.fragment.home.TabHomeChildFragment;
import com.pai8.ke.global.EventCode;
import com.pai8.ke.activity.me.ui.HistoryWatchActivity;
import com.pai8.ke.activity.takeaway.ui.DeliveryAddressActivity;
import com.pai8.ke.manager.UploadFileManager;
import com.pai8.ke.utils.AppUtils;
import com.pai8.ke.utils.ChoosePicUtils;
import com.pai8.ke.utils.CollectionUtils;
import com.pai8.ke.utils.EventBusUtils;
import com.pai8.ke.utils.ImageLoadUtils;
import com.pai8.ke.utils.LogUtils;
import com.pai8.ke.utils.ResUtils;
import com.pai8.ke.utils.SpanUtils;
import com.pai8.ke.utils.StringUtils;
import com.pai8.ke.utils.TabCreateUtils;
import com.pai8.ke.utils.ToastUtils;
import com.pai8.ke.widget.AppBarStateChangeListener;
import com.pai8.ke.widget.BottomDialog;
import com.pai8.ke.widget.CircleImageView;
import com.pai8.ke.widget.EditTextCountView;

import net.lucode.hackware.magicindicator.MagicIndicator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;
import butterknife.BindView;
import butterknife.OnClick;
import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.wechat.friends.Wechat;
import cn.sharesdk.wechat.moments.WechatMoments;

import static android.app.Activity.RESULT_OK;
import static com.pai8.ke.utils.AppUtils.isWeChatClientValid;


public class TabMeFragment extends BaseFragment {

    @BindView(R.id.iv_bg_blur)
    ImageView ivBgBlur;
    @BindView(R.id.civ_avatar)
    CircleImageView civAvatar;
    @BindView(R.id.tv_nick_name)
    TextView tvNickName;
    @BindView(R.id.tv_like_count)
    TextView tvLikeCount;
    @BindView(R.id.tv_follow_count)
    TextView tvFollowCount;
    @BindView(R.id.tv_fans_count)
    TextView tvFansCount;
    @BindView(R.id.tv_history_count)
    TextView tvHistoryCount;
    @BindView(R.id.tv_apply_status)
    TextView tvApplyStatus;
    @BindView(R.id.magic_indicator)
    MagicIndicator magicIndicator;
    @BindView(R.id.view_pager)
    ViewPager viewPager;
    @BindView(R.id.app_bar)
    AppBarLayout mAppBarLayout;

    private List<Fragment> mFragments;
    private List<String> mTitles;
    private TabAdapter mTabAdapter;
    private int mStatus;
    private CircleImageView mCivShareCover;
    private BottomDialog mShareModifyBottomDialog;
    private BottomDialog mShareBottomDialog;
    private String mShareImgUrl;

    @Override
    protected boolean isRegisterEventBus() {
        return true;
    }

    @Override
    protected void receiveEvent(BaseEvent event) {
        super.receiveEvent(event);
        switch (event.getCode()) {
            case EventCode.EVENT_LOGIN_STATUS:
                initUserInfo();
                break;
        }
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_tab_me;
    }

    @Override
    protected void initView(Bundle arguments) {
        //透明状态栏，字体深色
        ImmersionBar.with(this)
                .transparentStatusBar()
                .statusBarDarkFont(true)
                .init();

        mTitles = new ArrayList<>();
        mFragments = new ArrayList<>();

        mTitles.add("作品");
        mTitles.add("收藏");
        mTitles.add("喜欢");
        mTitles.add("我关联的");

        mFragments.add(TabHomeChildFragment.newInstance(3));
        mFragments.add(TabHomeChildFragment.newInstance(2));
        mFragments.add(TabHomeChildFragment.newInstance(4));
        mFragments.add(TabHomeChildFragment.newInstance(6));

        mTabAdapter = new TabAdapter(getChildFragmentManager(), mFragments, mTitles);

        viewPager.setOffscreenPageLimit(4);
        viewPager.setAdapter(mTabAdapter);
        viewPager.setCurrentItem(0);
        TabCreateUtils.setMeTab(getActivity(), magicIndicator, viewPager, mTitles);
    }

    @Override
    protected void initData() {
        super.initData();
        setLikeCount(0);
        setFollowCount(0);
        setFansCount(0);
        setHistoryCount(0);
    }

    @Override
    protected void initListener() {
        super.initListener();
        mAppBarLayout.addOnOffsetChangedListener(new AppBarStateChangeListener() {
            @Override
            public void onStateChanged(AppBarLayout appBarLayout, State state) {
                if (state == State.EXPANDED) {//展开状态
                    LogUtils.d("mAppBarLayout 展开");

                } else if (state == State.COLLAPSED) {//折叠状态
                    LogUtils.d("mAppBarLayout 折叠");
                } else {//中间状态
                    LogUtils.d("mAppBarLayout 中间");

                }
            }
        });
    }

    @Override
    protected void onLazyLoad() {
        initUserInfo();
    }

    private void initUserInfo() {
        if (!mActivity.mAccountManager.isLogin()) {
            tvNickName.setText("登录/注册");
            civAvatar.setImageResource(R.mipmap.img_head_def);
            setLikeCount(0);
            setFansCount(0);
            setFollowCount(0);
            setHistoryCount(0);
            return;
        }
        Api.getInstance().getMyInfo()
                .doOnSubscribe(disposable -> {
                })
                .compose(RxSchedulers.io_main())
                .subscribe(new BaseObserver<MyInfoResp>() {
                    @Override
                    protected void onSuccess(MyInfoResp myInfoResp) {
                        setLikeCount(myInfoResp.getMy_likes());
                        setFansCount(myInfoResp.getMy_fans());
                        setFollowCount(myInfoResp.getMy_fans());
                        setHistoryCount(myInfoResp.getMy_history());
                        initVerifyStatus(myInfoResp.getVerify_status() == null ? 0 :
                                myInfoResp.getVerify_status());
                        addLinkFragment();
                    }

                    @Override
                    protected void onError(String msg, int errorCode) {
                        setLikeCount(0);
                        setFansCount(0);
                        setFollowCount(0);
                        setHistoryCount(0);
                    }
                });
        Api.getInstance().getUserInfoById(mActivity.mAccountManager.getUid())
                .doOnSubscribe(disposable -> {
                })
                .compose(RxSchedulers.io_main())
                .subscribe(new BaseObserver<UserInfo>() {
                    @Override
                    protected void onSuccess(UserInfo user) {
                        UserInfo userInfo = mActivity.mAccountManager.getUserInfo();
                        userInfo.setAvatar(user.getAvatar());
                        userInfo.setUser_nickname(user.getUser_nickname());
                        mActivity.mAccountManager.saveUserInfo(userInfo);
                        tvNickName.setText(user.getUser_nickname());
                        ImageLoadUtils.loadImage(getActivity(), user.getAvatar(), civAvatar,
                                R.mipmap.img_head_def);
                    }

                    @Override
                    protected void onError(String msg, int errorCode) {

                    }
                });
    }

    private void setLikeCount(int likeCount) {
        SpannableStringBuilder span = SpanUtils.getBuilder("")
                .append(getActivity(), String.valueOf(likeCount))
                .setForegroundColor(ResUtils.getColor(R.color.color_dark_font))
                .setProportion(1.3f)
                .setBold()
                .append(getActivity(), "\n获赞")
                .create(getActivity());
        tvLikeCount.setText(span);
    }

    private void setFollowCount(int followCount) {
        SpannableStringBuilder span = SpanUtils.getBuilder("")
                .append(getActivity(), String.valueOf(followCount))
                .setForegroundColor(ResUtils.getColor(R.color.color_dark_font))
                .setProportion(1.3f)
                .setBold()
                .append(getActivity(), "\n关注")
                .create(getActivity());
        tvFollowCount.setText(span);
    }

    private void setFansCount(int fansCount) {
        SpannableStringBuilder span = SpanUtils.getBuilder("")
                .append(getActivity(), String.valueOf(fansCount))
                .setForegroundColor(ResUtils.getColor(R.color.color_dark_font))
                .setProportion(1.3f)
                .setBold()
                .append(getActivity(), "\n粉丝")
                .create(getActivity());
        tvFansCount.setText(span);
    }

    private void setHistoryCount(int historyCount) {
        SpannableStringBuilder span = SpanUtils.getBuilder("")
                .append(getActivity(), String.valueOf(historyCount))
                .setForegroundColor(ResUtils.getColor(R.color.color_dark_font))
                .setProportion(1.3f)
                .setBold()
                .append(getActivity(), "\n足迹")
                .create(getActivity());
        tvHistoryCount.setText(span);
    }

    /**
     * 0:未申请 - 申请商家入驻
     * 1:请求审核 - 正在审核中
     * 2:审核通过 - 店铺管理
     * 3:审核驳回 - 申请商家入驻
     */
    private void initVerifyStatus(int status) {
        mStatus = status;
        mStatus = 2;
        switch (status) {
            case 0:
            case 3:
                tvApplyStatus.setEnabled(true);
                tvApplyStatus.setText("申请商家入驻");
                break;
            case 1:
                tvApplyStatus.setEnabled(false);
                tvApplyStatus.setText("正在审核中...");
                break;
            case 2:
                tvApplyStatus.setEnabled(true);
                tvApplyStatus.setText("店铺管理");
                break;
            default:
                break;
        }
    }

    @OnClick({R.id.civ_avatar, R.id.tv_nick_name, R.id.iv_btn_edit, R.id.iv_btn_msg, R.id.ll_like_count,
            R.id.ll_follow_count, R.id.ll_fans_count, R.id.ll_history_count, R.id.tv_apply_status,
            R.id.tv_btn_order, R.id.tv_btn_wallet, R.id.tv_btn_address, R.id.tv_btn_coupon,
            R.id.tv_btn_invite, R.id.tv_btn_feedback, R.id.tv_btn_contact_us, R.id.tv_btn_setting})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.civ_avatar:
            case R.id.tv_nick_name:
                if (!mActivity.mAccountManager.isLogin()) {
                    launch(LoginActivity.class);
                    return;
                }
                break;
            case R.id.iv_btn_edit:
                if (!mActivity.mAccountManager.isLogin()) {
                    launch(LoginActivity.class);
                    return;
                }
                startActivityForResult(new Intent(mActivity, EditPersonalInfoActivity.class), 100);
                break;
            case R.id.iv_btn_msg:
                if (!mActivity.mAccountManager.isLogin()) {
                    launch(LoginActivity.class);
                    return;
                }
                EventBusUtils.sendEvent(new BaseEvent(EventCode.EVENT_HOME_TAB, 3));
                break;
            case R.id.ll_like_count:
                launchInterceptLogin(ReceiveLikesActivity.class);
                break;
            case R.id.ll_follow_count:
                launchInterceptLogin(AttentionMineActivity.class);
                break;
            case R.id.ll_fans_count:
                launchInterceptLogin(FansActivity.class);
                break;
            case R.id.ll_history_count:
                launchInterceptLogin(HistoryWatchActivity.class);
                break;
            case R.id.tv_apply_status:
                //申请商家入驻
                if (mStatus == 0 || mStatus == 3) {
                    launchInterceptLogin(MerchantSettledFirstActivity.class);
                } else if (mStatus == 2) {
                    //店铺管理
                    launchInterceptLogin(StoreManagerActivity.class);
                }
                break;
            case R.id.tv_btn_order:
                launchInterceptLogin(OrderActivity.class);
                break;
            case R.id.tv_btn_wallet:
                launch(WalletActivity.class);
                break;
            case R.id.tv_btn_address:
                launchInterceptLogin(DeliveryAddressActivity.class);
                break;
            case R.id.tv_btn_coupon:
                if (!mActivity.mAccountManager.isLogin()) {
                    launch(LoginActivity.class);
                    return;
                }
                //优惠券
                CouponListActivity.launch(getActivity(), CouponListActivity.INTENT_TYPE_CAN_USE);
                break;
            case R.id.tv_btn_invite:
                if (!mActivity.mAccountManager.isLogin()) {
                    launch(LoginActivity.class);
                    return;
                }
                share();
                break;
            case R.id.tv_btn_feedback:
                if (!mActivity.mAccountManager.isLogin()) {
                    launch(LoginActivity.class);
                    return;
                }
                ReportActivity.launchFeedBack(getActivity());
                break;
            case R.id.tv_btn_contact_us:
                if (!mActivity.mAccountManager.isLogin()) {
                    launch(LoginActivity.class);
                    return;
                }
                AppUtils.intentCallPhone(getActivity(), "18068446996");
                break;
            case R.id.tv_btn_setting:
                launchInterceptLogin(SettingActivity.class);
                break;
            default:
                break;
        }
    }

    private void share() {
        View view = View.inflate(getActivity(), R.layout.view_dialog_share_modify, null);
        ImageButton itnClose = view.findViewById(R.id.itn_close);
        TextView tvBtnShare = view.findViewById(R.id.tv_btn_share);
        mCivShareCover = view.findViewById(R.id.civ_cover);
        EditTextCountView etCv = view.findViewById(R.id.et_cv);
        mCivShareCover.setImageResource(R.mipmap.ic_launcher);
        etCv.setLength(50);
        etCv.setEtText(StringUtils.strSafe("快来一起使用5拍8~"));
        mCivShareCover.setOnClickListener(view1 -> {
            ChoosePicUtils.picSingle(getActivity(), 1);
        });
        itnClose.setOnClickListener(view1 -> {
            mShareModifyBottomDialog.dismiss();
        });
        tvBtnShare.setOnClickListener(view1 -> {
            String shareContent = etCv.getText();

            if (StringUtils.isEmpty(shareContent)) {
                toast("请输入分享的内容");
                return;
            }
            showShareBottomDialog("http://www.baidu.com", shareContent);
            mShareModifyBottomDialog.dismiss();
        });
        if (mShareModifyBottomDialog == null) {
            mShareModifyBottomDialog = new BottomDialog(getActivity(), view);
        }
        mShareModifyBottomDialog.setIsCanceledOnTouchOutside(true);
        mShareModifyBottomDialog.show();
    }

    /**
     * 第三方分享Dialog
     */
    private void showShareBottomDialog(String url, String name) {
        View view = View.inflate(getActivity(), R.layout.view_dialog_share, null);
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
            mShareBottomDialog = new BottomDialog(getActivity(), view);
        }
        mShareBottomDialog.setIsCanceledOnTouchOutside(true);
        mShareBottomDialog.show();
    }

    //添加关联页
    private void addLinkFragment(){
        String title = mStatus == 3 ?"关联我的" : "我关联的";

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
                getActivity().runOnUiThread(() -> {
                    if (mShareBottomDialog.isShowing()) mShareBottomDialog.dismiss();
                });
            }

            @Override
            public void onError(Platform platform, int i, Throwable throwable) {
                getActivity().runOnUiThread(() -> {
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
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case 1:
                    List<LocalMedia> imgs = PictureSelector.obtainMultipleResult(data);
                    if (CollectionUtils.isEmpty(imgs) || mCivShareCover == null) return;
                    String path = imgs.get(0).getPath();
                    ImageLoadUtils.loadImage(getActivity(), path, mCivShareCover,
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
                case 100:
                    initUserInfo();
                    break;
                default:
                    break;
            }
        }
    }
}
