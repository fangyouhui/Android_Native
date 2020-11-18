package com.pai8.ke.fragment.me;

import android.os.Bundle;
import android.text.SpannableStringBuilder;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.appbar.AppBarLayout;
import com.gyf.immersionbar.ImmersionBar;
import com.pai8.ke.R;
import com.pai8.ke.activity.account.LoginActivity;
import com.pai8.ke.activity.me.SettingActivity;
import com.pai8.ke.activity.takeaway.ui.MerchantSettledFirstActivity;
import com.pai8.ke.adapter.TabAdapter;
import com.pai8.ke.base.BaseFragment;
import com.pai8.ke.entity.resp.UserInfo;
import com.pai8.ke.fragment.home.TabHomeChildFragment;
import com.pai8.ke.utils.ImageLoadUtils;
import com.pai8.ke.utils.LogUtils;
import com.pai8.ke.utils.ResUtils;
import com.pai8.ke.utils.SpanUtils;
import com.pai8.ke.utils.StringUtils;
import com.pai8.ke.utils.TabCreateUtils;
import com.pai8.ke.widget.AppBarStateChangeListener;
import com.pai8.ke.widget.CircleImageView;

import net.lucode.hackware.magicindicator.MagicIndicator;

import java.util.ArrayList;
import java.util.List;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;
import butterknife.BindView;
import butterknife.OnClick;


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

        mFragments.add(TabHomeChildFragment.newInstance(0));
        mFragments.add(TabHomeChildFragment.newInstance(2));
        mFragments.add(TabHomeChildFragment.newInstance(2));

        mTabAdapter = new TabAdapter(getChildFragmentManager(), mFragments, mTitles);

        viewPager.setOffscreenPageLimit(3);
        viewPager.setAdapter(mTabAdapter);
        viewPager.setCurrentItem(0);
        TabCreateUtils.setMeTab(getActivity(), magicIndicator, viewPager, mTitles);
    }

    @Override
    protected void initData() {
        super.initData();
        setLikeCount(100);
        setFollowCount(100);
        setFansCount(200);
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
        if (mActivity.mAccountManager.isLogin()) {
            UserInfo userInfo = mActivity.mAccountManager.getUserInfo();
            if (StringUtils.isNotEmpty(userInfo.getUser_nickname())) {
                tvNickName.setText(userInfo.getUser_nickname());
            } else {
                tvNickName.setText(userInfo.getPhone());
            }
            ImageLoadUtils.loadImage(getActivity(), userInfo.getAvatar(), civAvatar, R.mipmap.img_head_def);
        } else {
            tvNickName.setText("登录/注册");
            civAvatar.setImageResource(R.mipmap.img_head_def);
        }
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

    @OnClick({R.id.civ_avatar, R.id.tv_nick_name, R.id.iv_btn_edit, R.id.iv_btn_msg, R.id.tv_like_count,
            R.id.tv_follow_count, R.id.tv_fans_count, R.id.tv_history_count, R.id.tv_apply_status,
            R.id.tv_btn_order, R.id.tv_btn_wallet, R.id.tv_btn_address, R.id.tv_btn_coupon,
            R.id.tv_btn_invite, R.id.tv_btn_feedback, R.id.tv_btn_contact_us, R.id.tv_btn_setting})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.civ_avatar:
            case R.id.tv_nick_name:
                if (!mActivity.mAccountManager.isLogin()) {
                    launch(LoginActivity.class);
                }
                break;
            case R.id.iv_btn_edit:
                break;
            case R.id.iv_btn_msg:
                break;
            case R.id.tv_like_count:
                break;
            case R.id.tv_follow_count:
                break;
            case R.id.tv_fans_count:
                break;
            case R.id.tv_history_count:
                break;
            case R.id.tv_apply_status:
                launch(MerchantSettledFirstActivity.class);
                break;
            case R.id.tv_btn_order:
                break;
            case R.id.tv_btn_wallet:
                break;
            case R.id.tv_btn_address:
                break;
            case R.id.tv_btn_coupon:
                break;
            case R.id.tv_btn_invite:
                break;
            case R.id.tv_btn_feedback:
                break;
            case R.id.tv_btn_contact_us:
                break;
            case R.id.tv_btn_setting:
                launch(SettingActivity.class);
                break;
        }
    }
}
