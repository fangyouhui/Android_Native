package com.pai8.ke.activity.me.fragment;


import com.chad.library.adapter.base.BaseQuickAdapter;
import com.pai8.ke.R;
import com.pai8.ke.activity.me.adapter.ShopHistoryAdapter;
import com.pai8.ke.activity.me.contract.HistoryWatchContract;
import com.pai8.ke.activity.me.presenter.HistoryWatchPresenter;
import com.pai8.ke.activity.takeaway.ui.StoreActivity;
import com.pai8.ke.activity.video.tiktok.TikTokActivity;
import com.pai8.ke.base.BaseMvpFragment;
import com.pai8.ke.entity.Shop;
import com.pai8.ke.entity.Video;
import com.pai8.ke.global.GlobalConstants;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import java.util.ArrayList;
import java.util.List;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import butterknife.BindView;

/**
 * @author Created by zzf
 * @time 21:33
 * Description：
 */
public class ShopHistoryFragment extends BaseMvpFragment<HistoryWatchPresenter> implements HistoryWatchContract.View, SwipeRefreshLayout.OnRefreshListener, BaseQuickAdapter.RequestLoadMoreListener {

    @BindView(R.id.rb_all)
    RadioButton rbAll;
    @BindView(R.id.rb_waimai)
    RadioButton rbWaimai;
    @BindView(R.id.rb_tuangou)
    RadioButton rbTuangou;
    @BindView(R.id.rv_shop_history)
    RecyclerView rvShopHistory;
    @BindView(R.id.sr_layout)
    SwipeRefreshLayout srLayout;
    @BindView(R.id.rg_choose)
    RadioGroup rgChoose;
    private ShopHistoryAdapter mAdapter;
    private List<Shop> mList = new ArrayList<>();
    private int page = 1;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_shop_history;
    }

    @Override
    protected void initListener() {
        super.initListener();
        mAdapter.setOnItemClickListener((adapter, view, position) -> {
            Shop shop = mList.get(position);
            StoreActivity.launch(getActivity(), shop.getId());
//            TikTokActivity.launch(getActivity(),mList, videoResp.getPage(), position
//                    , position);
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
    protected void initView(Bundle arguments) {
        rgChoose.setVisibility(View.GONE);
        srLayout.setOnRefreshListener(this);
        srLayout.setColorSchemeResources(R.color.colorPrimary);
        mAdapter = new ShopHistoryAdapter(mList);
        rvShopHistory.setLayoutManager(new GridLayoutManager(mActivity,2));
        rvShopHistory.setHasFixedSize(true);
        mAdapter.setOnLoadMoreListener(this,rvShopHistory);
        mAdapter.setEnableLoadMore(true);
        mAdapter.setEmptyView(R.layout.layout_empty_view, new LinearLayout(mActivity));
        rvShopHistory.setAdapter(mAdapter);
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
    public void getHistorySuccess(int total, List data) {
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
