package com.pai8.ke.fragment.home;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.lhs.library.base.BaseFragment;
import com.pai8.ke.activity.account.LoginActivity;
import com.pai8.ke.activity.video.tiktok.TikTokActivity;
import com.pai8.ke.adapter.HomeAdapter;
import com.pai8.ke.app.MyApp;
import com.pai8.ke.base.BaseEvent;
import com.pai8.ke.databinding.FragmentTabHomeChildBinding;
import com.pai8.ke.entity.event.VideoItemRefreshEvent;
import com.pai8.ke.manager.AccountManager;
import com.pai8.ke.utils.AMapLocationUtils;
import com.pai8.ke.utils.CollectionUtils;
import com.pai8.ke.utils.EventBusUtils;
import com.pai8.ke.utils.PreferencesUtils;
import com.pai8.ke.viewmodel.VideoHomeViewModel;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.listener.OnRefreshLoadMoreListener;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import static com.pai8.ke.global.EventCode.EVENT_LOGIN_STATUS;
import static com.pai8.ke.global.EventCode.EVENT_VIDEO_ITEM;
import static com.pai8.ke.global.EventCode.EVENT_VIDEO_LIST_REFRESH;

public class TabHomeChildFragment extends BaseFragment<VideoHomeViewModel, FragmentTabHomeChildBinding> {

    private HomeAdapter mAdapter;
    private int mPosition;
    private int mPageNo = 1;


    public static TabHomeChildFragment newInstance(int position) {
        TabHomeChildFragment fragment = new TabHomeChildFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("position", position);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPosition = getArguments().getInt("position");
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
            case EVENT_VIDEO_LIST_REFRESH:
                onRefreshData();
                break;
            case EVENT_LOGIN_STATUS: {
                mBinding.recyclerView.setVisibility(View.VISIBLE);
                mBinding.ivEmpty.setVisibility(View.GONE);
                mBinding.loginView.getRoot().setVisibility(View.GONE);
                mBinding.smartRefreshLayout.setEnableRefresh(true);
                mBinding.smartRefreshLayout.setEnableLoadMore(true);
                onRefreshData();
            }
            break;
            case EVENT_VIDEO_ITEM: //刷新某一条数据
                try {
                    VideoItemRefreshEvent mRefreshEvent = (VideoItemRefreshEvent) event.getData();
                    mAdapter.getData().set(mRefreshEvent.getPosition(), mRefreshEvent.getVideo());
                    mAdapter.notifyDataSetChanged();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
        }
    }

    @Override
    public void initView(Bundle arguments) {
        mAdapter = new HomeAdapter(getContext(), null);
        mBinding.recyclerView.setAdapter(mAdapter);
        mBinding.smartRefreshLayout.setOnRefreshLoadMoreListener(new OnRefreshLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                onLoadMoreData();
            }

            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                onRefreshData();
            }
        });
        mAdapter.setListener((item, position) -> TikTokActivity.launch(getActivity(), mAdapter.getData(), mPageNo, position, mPosition));

        mBinding.loginView.btnLogin.setOnClickListener(v -> startActivity(new Intent(getContext(), LoginActivity.class)));
    }

    @Override
    public void initData() {
        if (mPosition == 2 && !AccountManager.getInstance().isLogin()) {
            mBinding.recyclerView.setVisibility(View.GONE);
            mBinding.ivEmpty.setVisibility(View.GONE);
            mBinding.loginView.getRoot().setVisibility(View.VISIBLE);
            mBinding.smartRefreshLayout.setEnableRefresh(false);
            mBinding.smartRefreshLayout.setEnableLoadMore(false);
            return;
        }
        if (mPosition == 0) {
            if (CollectionUtils.isNotEmpty(MyApp.getLngLat())) {
                onRefreshData();
            } else {
                AMapLocationUtils.getLocation(location -> {
                    PreferencesUtils.put(getActivity(), "latitude", location.getLatitude() + "");
                    PreferencesUtils.put(getActivity(), "longitude", location.getLongitude() + "");
                    PreferencesUtils.put(getActivity(), "address", location.getAddress());
                    PreferencesUtils.put(getActivity(), "city", location.getCity());
                    onRefreshData();
                }, true);
            }
            return;
        }
        onRefreshData();
    }

    @Override
    public void addObserve() {
        mViewModel.getData().observe(getViewLifecycleOwner(), data -> {
            if (mBinding.smartRefreshLayout.isLoading()) {
                if (data == null || data.getItems() == null || data.getItems().isEmpty()) {
                    mBinding.smartRefreshLayout.finishRefreshWithNoMoreData();
                } else {
                    mAdapter.addData(data.getItems());
                    mBinding.smartRefreshLayout.finishLoadMore();
                }
                return;
            }
            if (mBinding.smartRefreshLayout.isRefreshing()) {
                mBinding.smartRefreshLayout.finishRefresh();
            }

            mAdapter.setData(data.getItems());
            mBinding.loginView.getRoot().setVisibility(View.GONE);
            if (data == null || data.getItems() == null || data.getItems().isEmpty()) {
                mBinding.recyclerView.setVisibility(View.GONE);
                mBinding.ivEmpty.setVisibility(View.VISIBLE);
            } else {
                mBinding.recyclerView.setVisibility(View.VISIBLE);
                mBinding.ivEmpty.setVisibility(View.GONE);
            }
        });

    }

    private void getData() {
        if (mPosition == 0) {
            mViewModel.nearby(mPageNo);
        } else if (mPosition == 1) {
            mViewModel.flow(mPageNo);
        } else {
            mViewModel.follow(mPageNo);
        }
    }

    private void onRefreshData() {
        mPageNo = 1;
        getData();
    }

    private void onLoadMoreData() {
        mPageNo++;
        getData();
    }


}
