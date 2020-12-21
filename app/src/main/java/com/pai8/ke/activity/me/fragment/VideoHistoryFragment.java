package com.pai8.ke.activity.me.fragment;


import com.chad.library.adapter.base.BaseQuickAdapter;
import com.pai8.ke.R;
import com.pai8.ke.activity.me.adapter.VideoHistoryAdapter;
import com.pai8.ke.activity.me.contract.HistoryWatchContract;
import com.pai8.ke.activity.me.presenter.HistoryWatchPresenter;
import com.pai8.ke.activity.video.tiktok.TikTokActivity;
import com.pai8.ke.base.BaseMvpFragment;
import com.pai8.ke.entity.Video;
import com.pai8.ke.global.GlobalConstants;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import butterknife.BindView;

/**
 * @author Created by zzf
 * @time 21:36
 * Description：
 */
public class VideoHistoryFragment extends BaseMvpFragment<HistoryWatchPresenter> implements HistoryWatchContract.View, SwipeRefreshLayout.OnRefreshListener, BaseQuickAdapter.RequestLoadMoreListener  {

    @BindView(R.id.rv_video_history)
    RecyclerView rvVideoHistory;
    @BindView(R.id.sr_layout)
    SwipeRefreshLayout srLayout;
    private VideoHistoryAdapter mAdapter;
    private List<Video> mList = new ArrayList<>();
    private int page = 1;
    @Override
    protected int getLayoutId() {
        return R.layout.fragment_video_history;
    }

    @Override
    protected void initView(Bundle arguments) {
        srLayout.setOnRefreshListener(this);
        srLayout.setColorSchemeResources(R.color.colorPrimary);
        mAdapter = new VideoHistoryAdapter(mList);
        rvVideoHistory.setLayoutManager(new GridLayoutManager(mActivity,2));
        rvVideoHistory.setHasFixedSize(true);
        mAdapter.setOnLoadMoreListener(this,rvVideoHistory);
        mAdapter.setEnableLoadMore(true);
        mAdapter.setEmptyView(R.layout.layout_empty_view, new LinearLayout(mActivity));
        rvVideoHistory.setAdapter(mAdapter);
    }

    @Override
    protected void initListener() {
        super.initListener();
        mAdapter.setOnItemClickListener((adapter, view, position) -> {
            Video videoResp = mList.get(position);
            TikTokActivity.launch(getActivity(),mList, videoResp.getPage(), position
                    , position);
        });
    }

    @Override
    public void initData() {
        mPresenter.reqVideoList(page);
    }


    @Override
    public void onRefresh() {
        page = 1;
        mPresenter.reqVideoList(page);
    }

    @Override
    public void onLoadMoreRequested() {
        page++;
        mPresenter.reqVideoList(page);
    }

    @Override
    public void getHistorySuccess(int total, List<Video> data) {
        if (data != null) {
            if (page == 1) {
                mAdapter.replaceData(data);
            } else {
                mAdapter.addData(data);
            }
            if (data.size() < GlobalConstants.SIZE) {
                mAdapter.loadMoreEnd();
            }else {
                mAdapter.loadMoreComplete();
            }
        }
    }

    @Override
    public void isRefresh() {
        srLayout.setRefreshing(true);
    }

    @Override
    public void completeRefresh() {
        srLayout.setRefreshing(false);
    }

    @Override
    public void completeLoadMore() {
        mAdapter.loadMoreComplete();
    }

    @Override
    public HistoryWatchPresenter initPresenter() {
        return new HistoryWatchPresenter(this);
    }
}
