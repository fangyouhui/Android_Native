package com.pai8.ke.activity.me.ui;

import com.afollestad.materialdialogs.MaterialDialog;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.hjq.bar.OnTitleBarListener;
import com.pai8.ke.R;
import com.pai8.ke.activity.me.adapter.AttentionMineAdapter;
import com.pai8.ke.activity.me.contract.AttentionMineContract;
import com.pai8.ke.activity.me.presenter.AttentionMinePresenter;
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
 * @time 11:21
 * Description： 关注
 */
public class AttentionMineActivity extends BaseMvpActivity<AttentionMinePresenter> implements AttentionMineContract.View,SwipeRefreshLayout.OnRefreshListener, BaseQuickAdapter.RequestLoadMoreListener {


    @BindView(R.id.rv_attention)
    RecyclerView rvAttentionMine;
    @BindView(R.id.sr_layout)
    SwipeRefreshLayout srLayout;
    private AttentionMineAdapter mAdapter;
    private List<MessageResp> mList = new ArrayList<>();
    private int page = 1;

    @Override
    public int getLayoutId() {
        return R.layout.activity_attention;
    }

    @Override
    public void initView() {
        mTitleBar.setTitle("关注");
        srLayout.setOnRefreshListener(this);
        srLayout.setColorSchemeResources(R.color.colorPrimary);
        mAdapter = new AttentionMineAdapter(mList);
        rvAttentionMine.setLayoutManager(new LinearLayoutManager(this));
        rvAttentionMine.setHasFixedSize(true);
        mAdapter.setEnableLoadMore(true);
        mAdapter.setEmptyView(R.layout.layout_empty_view, new LinearLayout(this));
        rvAttentionMine.setAdapter(mAdapter);
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
        mAdapter.setOnItemChildClickListener((adapter, view, position) -> {
            if (mList.get(position).is_focus == 1) {
                showOperateDialog(true, mList.get(position).builder_id + "");
            } else {
                showOperateDialog(false, mList.get(position).builder_id + "");
            }
        });
    }

    private void showOperateDialog(boolean cancel, String id) {
        new MaterialDialog.Builder(this)
                .title("温馨提示")
                .content(cancel ? "确定取消关注对方？" : "确定关注对方？")
                .positiveText(R.string.cancel)
                .negativeText(R.string.confirm)
                .onPositive((dialog, which) -> {
                    if (cancel) {
                        mPresenter.cancelAttention(id);
                    } else {
                        mPresenter.getAttention(id);
                    }
                })
                .show();
    }

    @Override
    public void initData() {
        mPresenter.reqMessageList(page);
    }

    @Override
    public void cancelAttentionSuccess() {
        page = 1;
        mPresenter.reqMessageList(page);
    }

    @Override
    public void attentionSuccess() {
        page = 1;
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
    public void getAttentionMineSuccess(List<MessageResp> data) {
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
    public AttentionMinePresenter initPresenter() {
        return new AttentionMinePresenter(this);
    }
}
