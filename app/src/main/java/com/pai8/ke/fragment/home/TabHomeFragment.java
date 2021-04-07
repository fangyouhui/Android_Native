package com.pai8.ke.fragment.home;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.viewpager.widget.ViewPager;

import com.blankj.utilcode.util.ToastUtils;
import com.lhs.library.base.BaseAppConstants;
import com.lhs.library.base.BaseFragment;
import com.lhs.library.base.NoViewModel;
import com.pai8.ke.activity.account.LoginActivity;
import com.pai8.ke.activity.home.SearchVideoActivity;
import com.pai8.ke.activity.takeaway.ui.ScanItActivity;
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
    private ActivityResultLauncher activityResultLauncher;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
            int resultCode = result.getResultCode();
            if (resultCode == Activity.RESULT_CANCELED) { //用户取消
                return;
            }
            if (resultCode == Activity.RESULT_OK) {
                String data = result.getData().getStringExtra(BaseAppConstants.BundleConstant.ARG_PARAMS_0);
                ToastUtils.showShort("扫描结果:" + data);
            } else {
                ToastUtils.showShort("未识别出二维码");
            }
        });
    }

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
        mBinding.viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                mBinding.tab.setVisibility(position == 1 ? View.VISIBLE : View.GONE);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

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
            activityResultLauncher.launch(new Intent(getContext(), ScanItActivity.class));

        });
        mBinding.bnWaiMai.setOnClickListener(v -> startActivity(new Intent(getContext(), TakeawayActivity.class)));
        mBinding.bnTuanGou.setOnClickListener(v -> startActivity(new Intent(getContext(), GroupBuyMainActivity.class)));
    }

}
