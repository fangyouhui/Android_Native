package com.pai8.ke.activity.me.fragment;


import com.chad.library.adapter.base.BaseQuickAdapter;
import com.pai8.ke.R;
import com.pai8.ke.activity.me.adapter.AttentionMineAdapter;
import com.pai8.ke.activity.me.contract.AttentionMineContract;
import com.pai8.ke.activity.me.presenter.AttentionMinePresenter;
import com.pai8.ke.base.BaseMvpFragment;
import com.pai8.ke.base.BasePresenter;
import com.pai8.ke.entity.User;
import com.pai8.ke.entity.resp.AttentionMine;
import com.pai8.ke.global.GlobalConstants;

import android.os.Bundle;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import butterknife.BindView;

/**
 * @author Created by zzf
 * @time 21:33
 * Description：
 */
public class ShopAttentionFragment extends BaseMvpFragment<AttentionMinePresenter> implements AttentionMineContract.View,SwipeRefreshLayout.OnRefreshListener, BaseQuickAdapter.RequestLoadMoreListener  {

    private AttentionMineAdapter mAdapter;
    private List<AttentionMine> mList = new ArrayList<>();
    private int page = 1;
    @BindView(R.id.sr_layout)
    SwipeRefreshLayout srLayout;
    @BindView(R.id.rv_shop_attention)
    RecyclerView rvShopAttention;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_shop_attention;
    }


    @Override
    protected void initView(Bundle arguments) {
        srLayout.setOnRefreshListener(this);
        srLayout.setColorSchemeResources(R.color.colorPrimary);
        mAdapter = new AttentionMineAdapter(mList);
        rvShopAttention.setLayoutManager(new LinearLayoutManager(mActivity));
        rvShopAttention.setHasFixedSize(true);
        mAdapter.setOnLoadMoreListener(this,rvShopAttention);
        mAdapter.setEnableLoadMore(true);
        mAdapter.setEmptyView(R.layout.layout_empty_view, new LinearLayout(mActivity));
        rvShopAttention.setAdapter(mAdapter);
    }


    @Override
    public void initData() {
        mPresenter.reqMessageList(page, "shop");
    }

    @Override
    public void onRefresh() {
        page = 1;
        mPresenter.reqMessageList(page, "shop");
    }

    @Override
    public void onLoadMoreRequested() {
        page++;
        mPresenter.reqMessageList(page, "shop");
    }

    @Override
    public void getAttentionMineSuccess(int total,List<AttentionMine> data) {
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
    public AttentionMinePresenter initPresenter() {
        return new AttentionMinePresenter(this);
    }

}
