package com.pai8.ke.activity.me.ui;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.hjq.bar.OnTitleBarListener;
import com.pai8.ke.R;
import com.pai8.ke.activity.me.adapter.ReceiveLikesAdapter;
import com.pai8.ke.activity.me.contract.ReceiveLikesContract;
import com.pai8.ke.activity.me.presenter.ReceiveLikesPresenter;
import com.pai8.ke.activity.message.entity.resp.MessageResp;
import com.pai8.ke.base.BaseMvpActivity;
import com.pai8.ke.entity.Video;
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
 * Description：获赞
 */
public class ReceiveLikesActivity extends BaseMvpActivity<ReceiveLikesPresenter> implements ReceiveLikesContract.View,SwipeRefreshLayout.OnRefreshListener, BaseQuickAdapter.RequestLoadMoreListener {

    @BindView(R.id.rv_receive_likes)
    RecyclerView rvReceiveLikes;
    @BindView(R.id.sr_layout)
    SwipeRefreshLayout srLayout;
    private ReceiveLikesAdapter mAdapter;
    private List<Video> mList = new ArrayList<>();
    private int page = 1;


    @Override
    public int getLayoutId() {
        return R.layout.activity_receive_likes;
    }

    @Override
    public void initView() {
        mTitleBar.setTitle("获赞");
        srLayout.setOnRefreshListener(this);
        srLayout.setColorSchemeResources(R.color.colorPrimary);
        mAdapter = new ReceiveLikesAdapter(mList);
        rvReceiveLikes.setLayoutManager(new LinearLayoutManager(this));
        rvReceiveLikes.setHasFixedSize(true);
        mAdapter.setEnableLoadMore(true);
        mAdapter.setEmptyView(R.layout.layout_empty_view, new LinearLayout(this));
        rvReceiveLikes.setAdapter(mAdapter);
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
    public void getReceiveLikesSuccess(int total ,List<Video> data) {
        if(total != 0){
            mTitleBar.setTitle("获赞(" + total + ")");
        }
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
    public ReceiveLikesPresenter initPresenter() {
        return new ReceiveLikesPresenter(this);
    }
}
