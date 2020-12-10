package com.pai8.ke.activity.me.ui;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.hjq.bar.OnTitleBarListener;
import com.pai8.ke.R;
import com.pai8.ke.activity.me.adapter.FansAdapter;
import com.pai8.ke.activity.me.contract.FansContract;
import com.pai8.ke.activity.me.presenter.FansPresenter;
import com.pai8.ke.activity.message.entity.resp.MessageResp;
import com.pai8.ke.base.BaseMvpActivity;
import com.pai8.ke.global.GlobalConstants;

import android.view.View;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import butterknife.BindView;

/**
 * @author Created by zzf
 * @time 19:50
 * Description：粉丝
 */
public class FansActivity extends BaseMvpActivity<FansPresenter> implements FansContract.View,SwipeRefreshLayout.OnRefreshListener, BaseQuickAdapter.RequestLoadMoreListener {

    @BindView(R.id.rv_fans)
    RecyclerView rvFans;
    @BindView(R.id.sr_layout)
    SwipeRefreshLayout srLayout;
    private FansAdapter mAdapter;
    private List<MessageResp> mList = new ArrayList<>();
    private int page = 1;

    @Override
    public int getLayoutId() {
        return R.layout.activity_fans;
    }

    @Override
    public void initView() {
        mTitleBar.setTitle("粉丝");
        srLayout.setOnRefreshListener(this);
        mAdapter = new FansAdapter(mList);
        rvFans.setLayoutManager(new LinearLayoutManager(this));
        rvFans.setHasFixedSize(true);
        mAdapter.setEnableLoadMore(true);
        mAdapter.setEmptyView(R.layout.layout_empty_view, new LinearLayout(this));
        rvFans.setAdapter(mAdapter);
    }

    @Override
    public void initListener() {
        super.initListener();
        mTitleBar.setOnTitleBarListener(new OnTitleBarListener() {
            @Override
            public void onLeftClick(View v) {
                finish();
            }

            @Override
            public void onTitleClick(View v) {

            }

            @Override
            public void onRightClick(View v) {

            }
        });
    }

    @Override
    public void initData() {
        mPresenter.reqMessageList(page);
    }

    @Override
    public void getFansSuccess(List<MessageResp> data) {
        if (data != null) {
            if (data.size() < GlobalConstants.SIZE) {
                mAdapter.loadMoreComplete();
            }
            if (page == 1) {
                mAdapter.replaceData(data);
            } else {
                mAdapter.addData(data);
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
    public void onRefresh() {
        page = 1;
        mPresenter.reqMessageList(page);
    }

    @Override
    public void onLoadMoreRequested() {
        page++;
        mPresenter.reqMessageList(page);
    }

    @Override
    public FansPresenter initPresenter() {
        return new FansPresenter(this);
    }

}
