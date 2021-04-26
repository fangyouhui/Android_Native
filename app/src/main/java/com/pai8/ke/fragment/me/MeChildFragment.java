package com.pai8.ke.fragment.me;

import android.os.Bundle;

import androidx.annotation.Nullable;

import com.lhs.library.base.BaseAppConstants;
import com.lhs.library.base.BaseFragment;
import com.pai8.ke.activity.video.tiktok.TikTokActivity;
import com.pai8.ke.adapter.HomeAdapter;
import com.pai8.ke.databinding.FragmentMeChildBinding;
import com.pai8.ke.viewmodel.MeChildViewModel;

public class MeChildFragment extends BaseFragment<MeChildViewModel, FragmentMeChildBinding> {
    private int pos = 0;
    private int mPageNo = 1;
    private HomeAdapter mAdapter;

    public static MeChildFragment newInstance(int position) {
        MeChildFragment fragment = new MeChildFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(BaseAppConstants.BundleConstant.ARG_PARAMS_0, position);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        pos = getArguments().getInt(BaseAppConstants.BundleConstant.ARG_PARAMS_0, 0);
    }

    @Override
    public void initView(@org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        mAdapter = new HomeAdapter(getContext(), null);
        mBinding.recyclerView.setAdapter(mAdapter);
        mAdapter.setListener((item, position) -> TikTokActivity.launch(getActivity(), mAdapter.getData(), mPageNo, position, position));
    }

    @Override
    public void initData() {
        if (pos == 0) {
            mViewModel.myVideo(mPageNo);
        } else if (pos == 1) {
            mViewModel.follow(mPageNo);
        } else if (pos == 2) {
            mViewModel.myLike(mPageNo);
        } else {
            mViewModel.myLink(mPageNo);
        }
    }

    @Override
    public void addObserve() {
        mViewModel.getData().observe(this, data -> {
            mAdapter.setData(data.getItems());
        });
    }
}
