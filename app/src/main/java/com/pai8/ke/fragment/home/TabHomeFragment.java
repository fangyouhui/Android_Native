package com.pai8.ke.fragment.home;

import android.content.Intent;
import android.os.Bundle;

import com.google.zxing.integration.android.IntentIntegrator;
import com.lhs.library.base.BaseFragment;
import com.lhs.library.base.NoViewModel;
import com.pai8.ke.activity.account.LoginActivity;
import com.pai8.ke.activity.common.ScanActivity;
import com.pai8.ke.activity.home.SearchVideoActivity;
import com.pai8.ke.activity.takeaway.ui.TakeawayActivity;
import com.pai8.ke.app.MyApp;
import com.pai8.ke.databinding.FragmentTabHomeBinding;
import com.pai8.ke.fragment.CouponGetDialogFragment;
import com.pai8.ke.groupBuy.adapter.ViewPagerAdapter;
import com.pai8.ke.groupBuy.ui.GroupBuyMainActivity;
import com.pai8.ke.manager.AccountManager;
import com.pai8.ke.utils.PreferencesUtils;
import com.pai8.ke.utils.TabCreateUtils;

public class TabHomeFragment extends BaseFragment<NoViewModel, FragmentTabHomeBinding> {

    @Override
    public void initView(Bundle arguments) {
        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getChildFragmentManager());
        viewPagerAdapter.addFragment(TabHomeChildFragment.newInstance(0), "附近");
        viewPagerAdapter.addFragment(TabHomeChildFragment.newInstance(1), "推荐");
        viewPagerAdapter.addFragment(TabHomeChildFragment.newInstance(2), "关注");

        mBinding.viewPager.setOffscreenPageLimit(3);
        mBinding.viewPager.setAdapter(viewPagerAdapter);
        mBinding.viewPager.setCurrentItem(1);
        TabCreateUtils.setHomeTab(getActivity(), mBinding.magicIndicator, mBinding.viewPager, viewPagerAdapter.getTitleList());

        mBinding.tvSearch.setOnClickListener(v -> startActivity(new Intent(getContext(), SearchVideoActivity.class)));
        mBinding.ivLiwu.setOnClickListener(v -> {
            if (!AccountManager.getInstance().isLogin()) {
                startActivity(new Intent(getContext(), LoginActivity.class));
                return;
            }
            String shopId = (String) PreferencesUtils.get(MyApp.getMyApp(), "shop_id", "");
            CouponGetDialogFragment newInstance = CouponGetDialogFragment.newInstance(shopId);
            newInstance.show(getChildFragmentManager(), "CouponGetDialog");
        });

        mBinding.ivScan.setOnClickListener(v -> {
            if (!AccountManager.getInstance().isLogin()) {
                startActivity(new Intent(getContext(), LoginActivity.class));
                return;
            }
            new IntentIntegrator(getActivity())
                    .setDesiredBarcodeFormats(IntentIntegrator.ALL_CODE_TYPES)
                    .setPrompt("请对准二维码进行扫描")
                    .setOrientationLocked(false)
                    .setCameraId(0)// 选择摄像头
                    .setBeepEnabled(true)// 是否开启声音
                    .setCaptureActivity(ScanActivity.class)
                    .initiateScan();


        });
        mBinding.bnWaiMai.setOnClickListener(v -> startActivity(new Intent(getContext(), TakeawayActivity.class)));
        mBinding.bnTuanGou.setOnClickListener(v -> startActivity(new Intent(getContext(), GroupBuyMainActivity.class)));
    }

}
