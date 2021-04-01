package com.pai8.ke.shop.ui;

import android.os.Bundle;

import com.lhs.library.base.BaseAppConstants;
import com.lhs.library.base.BaseFragment;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.style.PictureSelectorUIStyle;
import com.pai8.ke.R;
import com.pai8.ke.databinding.FragmentBusinessVideoListBinding;
import com.pai8.ke.shop.adapter.BusinessVideoAdapter;
import com.pai8.ke.shop.viewmodel.BusinessHomeViewModel;

import org.jetbrains.annotations.Nullable;

public class BusinessVideoListFragment extends BaseFragment<BusinessHomeViewModel, FragmentBusinessVideoListBinding> {
    private String shopId;
    private int page = 1;
    private BusinessVideoAdapter adapter;

    public static BusinessVideoListFragment newInstance(String shopId) {
        BusinessVideoListFragment fragment = new BusinessVideoListFragment();
        Bundle bundle = new Bundle();
        bundle.putString(BaseAppConstants.BundleConstant.ARG_PARAMS_0, shopId);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(@androidx.annotation.Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        shopId = getArguments().getString(BaseAppConstants.BundleConstant.ARG_PARAMS_0, "");
    }

    @Override
    public void initView(@Nullable Bundle savedInstanceState) {
        mBinding.recyclerView.setAdapter(adapter = new BusinessVideoAdapter(getContext(), null, false));
        adapter.setListener((item, position) -> {
            PictureSelector.create(this)
                    .themeStyle(R.style.picture_default_style)
                    .setPictureUIStyle(PictureSelectorUIStyle.ofDefaultStyle())// 动态自定义相册主题
                    .externalPictureVideo(item.getVideo_path());
        });
    }

    @Override
    public void addObserve() {
        mViewModel.getShopVideoListData().observe(getViewLifecycleOwner(), data -> {
            BusinessHomeActivity businessHomeActivity = (BusinessHomeActivity) getActivity();
            if (page == 1) {
                adapter.setData(data);
                businessHomeActivity.finishRefresh();
            } else {
                if (data == null || data.isEmpty()) {
                    businessHomeActivity.finishLoadMore(true);
                } else {
                    adapter.addData(data);
                    businessHomeActivity.finishLoadMore(false);
                }
            }

        });
    }

    @Override
    public void initData() {
        mViewModel.getShopVideoList(shopId, page + "");
    }

    public void onRefresh() {
        page = 1;
        initData();
    }

    public void onLoadMore() {
        page++;
        initData();
    }
}
