package com.pai8.ke.fragment.me;

import android.content.Intent;
import android.os.Bundle;
import android.text.SpannableStringBuilder;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.lhs.library.base.BaseFragment;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.entity.LocalMedia;
import com.pai8.ke.R;
import com.pai8.ke.activity.account.LoginActivity;
import com.pai8.ke.activity.me.CouponListActivity;
import com.pai8.ke.activity.me.SettingActivity;
import com.pai8.ke.activity.me.ui.AttentionMineActivity;
import com.pai8.ke.activity.me.ui.EditPersonalInfoActivity;
import com.pai8.ke.activity.me.ui.FansActivity;
import com.pai8.ke.activity.me.ui.HistoryWatchActivity;
import com.pai8.ke.activity.me.ui.ReceiveLikesActivity;
import com.pai8.ke.activity.takeaway.order.UserOrderActivity;
import com.pai8.ke.activity.takeaway.ui.DeliveryAddressActivity;
import com.pai8.ke.activity.takeaway.ui.MerchantSettledFirstActivity;
import com.pai8.ke.activity.takeaway.ui.StoreManagerActivity;
import com.pai8.ke.activity.video.ReportActivity;
import com.pai8.ke.activity.wallet.WalletActivity;
import com.pai8.ke.adapter.TabAdapter;
import com.pai8.ke.base.BaseEvent;
import com.pai8.ke.databinding.FragmentTabMeCopyBinding;
import com.pai8.ke.entity.UserInfo;
import com.pai8.ke.global.EventCode;
import com.pai8.ke.manager.AccountManager;
import com.pai8.ke.manager.UploadFileManager;
import com.pai8.ke.utils.AppUtils;
import com.pai8.ke.utils.ChoosePicUtils;
import com.pai8.ke.utils.CollectionUtils;
import com.pai8.ke.utils.EventBusUtils;
import com.pai8.ke.utils.ImageLoadUtils;
import com.pai8.ke.utils.ResUtils;
import com.pai8.ke.utils.SpanUtils;
import com.pai8.ke.utils.StringUtils;
import com.pai8.ke.utils.TabCreateUtils;
import com.pai8.ke.utils.ToastUtils;
import com.pai8.ke.viewmodel.TabMeViewModel;
import com.pai8.ke.widget.BottomDialog;
import com.pai8.ke.widget.CircleImageView;
import com.pai8.ke.widget.EditTextCountView;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.wechat.friends.Wechat;
import cn.sharesdk.wechat.moments.WechatMoments;

import static android.app.Activity.RESULT_OK;
import static com.pai8.ke.utils.AppUtils.isWeChatClientValid;

public class TabMeFragment extends BaseFragment<TabMeViewModel, FragmentTabMeCopyBinding> {

    private int mStatus;
    private CircleImageView mCivShareCover;
    private BottomDialog mShareModifyBottomDialog;
    private BottomDialog mShareBottomDialog;
    private String mShareImgUrl;
    private ActivityResultLauncher activityResultLauncher;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), data -> {
            if (data.getResultCode() == RESULT_OK) {
                initUserInfo();
            }
        });
        EventBusUtils.register(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBusUtils.unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void receiveEvent(BaseEvent event) {
        switch (event.getCode()) {
            case EventCode.EVENT_LOGIN_STATUS:
                initUserInfo();
                break;
        }
    }


    @Override
    public void initView(Bundle arguments) {
        initTab();
        initEvent();
    }

    private void initTab() {
        if (!AccountManager.getInstance().isLogin()) {
            mBinding.magicIndicator.setVisibility(View.GONE);
            mBinding.viewPager.setVisibility(View.GONE);
            return;
        }
        mBinding.magicIndicator.setVisibility(View.VISIBLE);
        mBinding.viewPager.setVisibility(View.VISIBLE);

        List<Fragment> mFragments = new ArrayList<>();
        List<String> mTitles = new ArrayList<>();

        mTitles.add("作品");
        mTitles.add("收藏");
        mTitles.add("喜欢");
        mTitles.add("关联我的");

        mFragments.add(MeChildFragment.newInstance(0));
        mFragments.add(MeChildFragment.newInstance(1));
        mFragments.add(MeChildFragment.newInstance(2));
        mFragments.add(MeChildFragment.newInstance(3));

        TabAdapter tabAdapter = new TabAdapter(getChildFragmentManager(), mFragments, mTitles);
        mBinding.viewPager.setOffscreenPageLimit(4);
        mBinding.viewPager.setAdapter(tabAdapter);

        TabCreateUtils.setMeTab(getActivity(), mBinding.magicIndicator, mBinding.viewPager, mTitles);
    }

    @Override
    public void initData() {
        setLikeCount(0);
        setFollowCount(0);
        setFansCount(0);
        setHistoryCount(0);

        initUserInfo();
        initTab();
    }

    @Override
    public void addObserve() {
        mViewModel.getMyInfoData().observe(getViewLifecycleOwner(), data -> {
            setLikeCount(data.getMy_likes());
            setFansCount(data.getMy_fans());
            setFollowCount(data.getMy_follows());
            setHistoryCount(data.getMy_history());
            initVerifyStatus(data.getVerify_status());
            addLinkFragment();
        });

        mViewModel.getInfoByUidData().observe(getViewLifecycleOwner(), user -> {
            UserInfo userInfo = AccountManager.getInstance().getUserInfo();
            userInfo.setAvatar(user.getAvatar());
            userInfo.setUser_nickname(user.getUser_nickname());
            AccountManager.getInstance().saveUserInfo(userInfo);
            mBinding.tvNickName.setText(user.getUser_nickname());
            ImageLoadUtils.loadImage(getActivity(), user.getAvatar(), mBinding.civAvatar, R.mipmap.img_head_def);
        });

    }

    private void initUserInfo() {
        if (!AccountManager.getInstance().isLogin()) {
            mBinding.tvNickName.setText("登录/注册");
            mBinding.civAvatar.setImageResource(R.mipmap.img_head_def);
            setLikeCount(0);
            setFansCount(0);
            setFollowCount(0);
            setHistoryCount(0);
            return;
        }
        mViewModel.getMyInfo();
        mViewModel.getInfoByUid();
    }

    private void setLikeCount(int likeCount) {
        SpannableStringBuilder span = SpanUtils.getBuilder("")
                .append(getActivity(), String.valueOf(likeCount))
                .setForegroundColor(ResUtils.getColor(R.color.color_dark_font))
                .setProportion(1.3f)
                .setBold()
                .append(getActivity(), "\n获赞")
                .create(getActivity());
        mBinding.tvLikeCount.setText(span);
    }

    private void setFollowCount(int followCount) {
        SpannableStringBuilder span = SpanUtils.getBuilder("")
                .append(getActivity(), String.valueOf(followCount))
                .setForegroundColor(ResUtils.getColor(R.color.color_dark_font))
                .setProportion(1.3f)
                .setBold()
                .append(getActivity(), "\n关注")
                .create(getActivity());
        mBinding.tvFollowCount.setText(span);
    }

    private void setFansCount(int fansCount) {
        SpannableStringBuilder span = SpanUtils.getBuilder("")
                .append(getActivity(), String.valueOf(fansCount))
                .setForegroundColor(ResUtils.getColor(R.color.color_dark_font))
                .setProportion(1.3f)
                .setBold()
                .append(getActivity(), "\n粉丝")
                .create(getActivity());
        mBinding.tvFansCount.setText(span);
    }

    private void setHistoryCount(int historyCount) {
        SpannableStringBuilder span = SpanUtils.getBuilder("")
                .append(getActivity(), String.valueOf(historyCount))
                .setForegroundColor(ResUtils.getColor(R.color.color_dark_font))
                .setProportion(1.3f)
                .setBold()
                .append(getActivity(), "\n足迹")
                .create(getActivity());
        mBinding.tvHistoryCount.setText(span);
    }

    /**
     * 0:未申请 - 申请商家入驻
     * 1:请求审核 - 正在审核中
     * 2:审核通过 - 店铺管理
     * 3:审核驳回 - 申请商家入驻
     */
    private void initVerifyStatus(int status) {
        mStatus = status;
        switch (status) {
            case 0:
            case 3:
                mBinding.tvApplyStatus.setEnabled(true);
                mBinding.tvApplyStatus.setText("申请商家入驻");
                break;
            case 1:
                mBinding.tvApplyStatus.setEnabled(false);
                mBinding.tvApplyStatus.setText("正在审核中...");
                break;
            case 2:
                mBinding.tvApplyStatus.setEnabled(true);
                mBinding.tvApplyStatus.setText("店铺管理");
                break;
            default:
                break;
        }
    }


    private void initEvent() {
        mBinding.civAvatar.setOnClickListener(v -> {
            if (!AccountManager.getInstance().isLogin()) {
                startActivity(new Intent(getContext(), LoginActivity.class));
                return;
            }
            mBinding.ivBtnEdit.performClick();

        });
        mBinding.tvNickName.setOnClickListener(v -> mBinding.civAvatar.callOnClick());

        mBinding.ivBtnEdit.setOnClickListener(v -> {
            if (!AccountManager.getInstance().isLogin()) {
                startActivity(new Intent(getContext(), LoginActivity.class));
                return;
            }

            activityResultLauncher.launch(new Intent(getContext(), EditPersonalInfoActivity.class));
        });

        mBinding.ivBtnMsg.setOnClickListener(v -> {
            if (!AccountManager.getInstance().isLogin()) {
                startActivity(new Intent(getContext(), LoginActivity.class));
                return;
            }
            EventBusUtils.sendEvent(new BaseEvent(EventCode.EVENT_HOME_TAB, 3));
        });

        mBinding.llLikeCount.setOnClickListener(v -> {
            launchInterceptLogin(ReceiveLikesActivity.class);
        });
        mBinding.llFollowCount.setOnClickListener(v -> launchInterceptLogin(AttentionMineActivity.class));

        mBinding.llFansCount.setOnClickListener(v -> launchInterceptLogin(FansActivity.class));
        mBinding.llHistoryCount.setOnClickListener(v -> launchInterceptLogin(HistoryWatchActivity.class));

        mBinding.tvApplyStatus.setOnClickListener(v -> {
            if (mStatus == 0 || mStatus == 3) {
                launchInterceptLogin(MerchantSettledFirstActivity.class);
            } else if (mStatus == 2) {
                //店铺管理
                launchInterceptLogin(StoreManagerActivity.class);
            }
        });
        mBinding.tvBtnOrder.setOnClickListener(v -> launchInterceptLogin(UserOrderActivity.class));
        mBinding.tvBtnWallet.setOnClickListener(v -> launchInterceptLogin(WalletActivity.class));
        mBinding.tvBtnAddress.setOnClickListener(v -> launchInterceptLogin(DeliveryAddressActivity.class));

        mBinding.tvBtnCoupon.setOnClickListener(v -> {
            if (!AccountManager.getInstance().isLogin()) {
                startActivity(new Intent(getContext(), LoginActivity.class));
                return;
            }
            //优惠券
            CouponListActivity.launch(getActivity(), CouponListActivity.INTENT_TYPE_CAN_USE);
        });

        mBinding.tvBtnInvite.setOnClickListener(v -> {
            if (!AccountManager.getInstance().isLogin()) {
                startActivity(new Intent(getContext(), LoginActivity.class));
                return;
            }
            share();
        });

        mBinding.tvBtnFeedback.setOnClickListener(v -> {
            if (!AccountManager.getInstance().isLogin()) {
                startActivity(new Intent(getContext(), LoginActivity.class));
                return;
            }
            ReportActivity.launchFeedBack(getActivity());
        });

        mBinding.tvBtnContactUs.setOnClickListener(v -> {
            if (!AccountManager.getInstance().isLogin()) {
                startActivity(new Intent(getContext(), LoginActivity.class));
                return;
            }
            AppUtils.intentCallPhone(getActivity(), "18068446996");
        });

        mBinding.tvBtnSetting.setOnClickListener(v -> launchInterceptLogin(SettingActivity.class));
    }

    private void launchInterceptLogin(Class clazz) {
        if (AccountManager.getInstance().isLogin()) {
            Intent intent = new Intent(getContext(), clazz);
            startActivity(intent);
        } else {
            startActivity(new Intent(getContext(), LoginActivity.class));
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
                com.blankj.utilcode.util.ToastUtils.showShort("请输入分享的内容");
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
    private void addLinkFragment() {
        String title = mStatus == 3 ? "关联我的" : "我关联的";
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
                    com.blankj.utilcode.util.ToastUtils.showShort("分享失败");
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
                    ImageLoadUtils.loadImage(getActivity(), path, mCivShareCover, R.mipmap.img_share_cover);
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
}
