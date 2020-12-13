package com.pai8.ke.activity.message.ui;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.hjq.bar.OnTitleBarListener;
import com.pai8.ke.R;
import com.pai8.ke.activity.message.adapter.ActiveMessageAdapter;
import com.pai8.ke.activity.message.contract.ActiveMessageContract;
import com.pai8.ke.activity.message.entity.resp.MessageResp;
import com.pai8.ke.activity.message.presenter.ActiveMessagePresenter;
import com.pai8.ke.base.BaseMvpActivity;
import com.pai8.ke.global.GlobalConstants;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author Created by zzf
 * @time 11:21
 * Description：活动消息
 */
public class ActiveMessageActivity extends BaseMvpActivity<ActiveMessagePresenter> implements ActiveMessageContract.View, SwipeRefreshLayout.OnRefreshListener, BaseQuickAdapter.RequestLoadMoreListener {


    @BindView(R.id.rv_active_message)
    RecyclerView rvActiveMessage;
    @BindView(R.id.sr_layout)
    SwipeRefreshLayout srLayout;
    private ActiveMessageAdapter mAdapter;
    private List<MessageResp> mList = new ArrayList<>();
    private int page = 1;

    @Override
    public int getLayoutId() {
        return R.layout.activity_active_message;
    }

    @Override
    public void initView() {
        mTitleBar.setTitle("活动消息");
        srLayout.setOnRefreshListener(this);
        srLayout.setColorSchemeResources(R.color.colorPrimary);
        mAdapter = new ActiveMessageAdapter(mList);
        rvActiveMessage.setLayoutManager(new LinearLayoutManager(this));
        rvActiveMessage.setHasFixedSize(true);
        mAdapter.setEnableLoadMore(true);
        mAdapter.setEmptyView(R.layout.layout_empty_view, new LinearLayout(this));
        rvActiveMessage.setAdapter(mAdapter);
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
    public void getActiveMessageSuccess(List<MessageResp> data) {
        if (data != null) {
            if (page == 1) {
                mAdapter.replaceData(data);
            } else {
                mAdapter.addData(data);
            }
            if (data.size() < GlobalConstants.SIZE) {
                mAdapter.setEnableLoadMore(false);
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
        mAdapter.setEnableLoadMore(true);
        mPresenter.reqMessageList(page);
    }

    @Override
    public void onLoadMoreRequested() {
        page++;
        mPresenter.reqMessageList(page);
    }

    @Override
    public ActiveMessagePresenter initPresenter() {
        return new ActiveMessagePresenter(this);
    }

}
