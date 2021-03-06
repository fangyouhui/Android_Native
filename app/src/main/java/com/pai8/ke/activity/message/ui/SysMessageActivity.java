package com.pai8.ke.activity.message.ui;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.hjq.bar.OnTitleBarListener;
import com.pai8.ke.R;
import com.pai8.ke.activity.message.adapter.ActiveMessageAdapter;
import com.pai8.ke.activity.message.adapter.SysMessageAdapter;
import com.pai8.ke.activity.message.contract.ActiveMessageContract;
import com.pai8.ke.activity.message.contract.SysMessageContract;
import com.pai8.ke.activity.message.entity.resp.MessageResp;
import com.pai8.ke.activity.message.presenter.ActiveMessagePresenter;
import com.pai8.ke.activity.message.presenter.SysMessagePresenter;
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
 * @time 11:21
 * Description：活动消息
 */
public class SysMessageActivity extends BaseMvpActivity<SysMessagePresenter> implements SysMessageContract.View, SwipeRefreshLayout.OnRefreshListener, BaseQuickAdapter.RequestLoadMoreListener {


    @BindView(R.id.rv_sys_message)
    RecyclerView rvSysMessage;
    @BindView(R.id.sr_layout)
    SwipeRefreshLayout srLayout;
    private SysMessageAdapter mAdapter;
    private List<MessageResp> mList = new ArrayList<>();
    private int page = 1;

    @Override
    public int getLayoutId() {
        return R.layout.activity_sys_message;
    }

    @Override
    public void initView() {
        mTitleBar.setTitle("系统消息");
        srLayout.setOnRefreshListener(this);
        srLayout.setColorSchemeResources(R.color.colorPrimary);
        mAdapter = new SysMessageAdapter(mList);
        rvSysMessage.setLayoutManager(new LinearLayoutManager(this));
        rvSysMessage.setHasFixedSize(true);
        mAdapter.setOnLoadMoreListener(this,rvSysMessage);
        mAdapter.setEnableLoadMore(true);
        mAdapter.setEmptyView(R.layout.layout_empty_view, new LinearLayout(this));
        rvSysMessage.setAdapter(mAdapter);
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
    public void getSysMessageSuccess(List<MessageResp> data) {
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
    public SysMessagePresenter initPresenter() {
        return new SysMessagePresenter(this);
    }

}
