package com.pai8.ke.activity.me.ui;

import com.afollestad.materialdialogs.MaterialDialog;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.hjq.bar.OnTitleBarListener;
import com.pai8.ke.R;
import com.pai8.ke.activity.me.adapter.AttentionMineAdapter;
import com.pai8.ke.activity.me.adapter.HistoryWatchAdapter;
import com.pai8.ke.activity.me.contract.AttentionMineContract;
import com.pai8.ke.activity.me.contract.HistoryWatchContract;
import com.pai8.ke.activity.me.presenter.AttentionMinePresenter;
import com.pai8.ke.activity.me.presenter.HistoryWatchPresenter;
import com.pai8.ke.activity.message.entity.resp.MessageResp;
import com.pai8.ke.base.BaseMvpActivity;
import com.pai8.ke.global.GlobalConstants;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;

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
 * Description： 关注
 */
public class HistoryWatchActivity extends BaseMvpActivity<HistoryWatchPresenter> implements HistoryWatchContract.View, SwipeRefreshLayout.OnRefreshListener, BaseQuickAdapter.RequestLoadMoreListener {


    @BindView(R.id.rb_all)
    RadioButton rbAll;
    @BindView(R.id.rb_waimai)
    RadioButton rbWaimai;
    @BindView(R.id.rb_tuangou)
    RadioButton rbTuangou;
    @BindView(R.id.rg_choose)
    RadioGroup rgChoose;
    @BindView(R.id.rv_history)
    RecyclerView rvHistory;
    @BindView(R.id.sr_layout)
    SwipeRefreshLayout srLayout;
    private HistoryWatchAdapter mAdapter;
    private List<MessageResp> mList = new ArrayList<>();
    private int page = 1;

    @Override
    public int getLayoutId() {
        return R.layout.activity_history_watch;
    }

    @Override
    public void initView() {
        mTitleBar.setTitle("足迹");
        srLayout.setOnRefreshListener(this);
        srLayout.setColorSchemeResources(R.color.colorPrimary);
        mAdapter = new HistoryWatchAdapter(mList);
        rvHistory.setLayoutManager(new LinearLayoutManager(this));
        rvHistory.setHasFixedSize(true);
        mAdapter.setEnableLoadMore(true);
        mAdapter.setEmptyView(R.layout.layout_empty_view, new LinearLayout(this));
        rvHistory.setAdapter(mAdapter);
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
        rgChoose.setOnCheckedChangeListener((group, checkedId) -> {
            switch (checkedId) {
                case R.id.rb_all:

                    break;
                case R.id.rb_waimai:
                    break;
                case R.id.rb_tuangou:
                    break;
                default:
                    break;
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
    public void getHistorySuccess(List<MessageResp> data) {
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
    public HistoryWatchPresenter initPresenter() {
        return new HistoryWatchPresenter(this);
    }

}
