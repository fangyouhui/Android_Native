package com.pai8.ke.activity.takeaway.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.pai8.ke.R;
import com.pai8.ke.activity.takeaway.Constants;
import com.pai8.ke.activity.takeaway.adapter.GroupBuyManagerAdapter;
import com.pai8.ke.activity.takeaway.contract.shopGroupManagerContract;
import com.pai8.ke.activity.takeaway.entity.event.NotifyEvent;
import com.pai8.ke.activity.takeaway.entity.resq.smallGoodsInfo;
import com.pai8.ke.activity.takeaway.presenter.AddGroupGoodPresenter;
import com.pai8.ke.activity.takeaway.ui.AddGoodActivity;
import com.pai8.ke.activity.takeaway.ui.AddGroupGoodActivity;
import com.pai8.ke.activity.takeaway.widget.CheckListener;
import com.pai8.ke.base.BaseMvpFragment;
import com.pai8.ke.global.GlobalConstants;

import java.util.ArrayList;
import java.util.List;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;

public class GroupBuyManagerFragment extends BaseMvpFragment<AddGroupGoodPresenter.GroupBuyManagerPresenter> implements View.OnClickListener, CheckListener, shopGroupManagerContract.View ,BaseQuickAdapter.RequestLoadMoreListener,SwipeRefreshLayout.OnRefreshListener{

    @BindView(R.id.rv_group_buy)
    RecyclerView mRvGroupBuy;

    @BindView(R.id.sr_layout)
    SwipeRefreshLayout srLayout;
    private AddGroupGoodPresenter.GroupBuyManagerPresenter presenter;

    private GroupBuyManagerAdapter mAdapter;
    private int page = 1;
    private List<smallGoodsInfo> mList = new ArrayList<>();

    @Override
    public AddGroupGoodPresenter.GroupBuyManagerPresenter initPresenter() {
        return new AddGroupGoodPresenter.GroupBuyManagerPresenter(this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_group_buy_manager;
    }

    @Override
    protected void initView(Bundle arguments) {
        srLayout.setOnRefreshListener(this);
        srLayout.setColorSchemeResources(R.color.colorPrimary);

        mRvGroupBuy = mRootView.findViewById(R.id.rv_group_buy);
        mRootView.findViewById(R.id.tv_add).setOnClickListener(this);
        GridLayoutManager layoutManager = new GridLayoutManager(getActivity(),2);
        mRvGroupBuy.setLayoutManager(layoutManager);

        mAdapter = new GroupBuyManagerAdapter(mList);
        mRvGroupBuy.setAdapter(mAdapter);


        mRvGroupBuy.setHasFixedSize(true);
        mAdapter.setOnLoadMoreListener(this,mRvGroupBuy);
        mAdapter.setEnableLoadMore(true);


        mAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                if(view.getId() == R.id.tv_edit){
                    smallGoodsInfo smModel = mList.get(position);
                    Intent intent = new Intent(mActivity,AddGroupGoodActivity.class);
                    intent.putExtra("type",3);
                    intent.putExtra("id",smModel.id);

                    startActivity(intent);

                }
            }
        });


    }

    @Override
    protected void initData() {
        super.initData();
        presenter = new AddGroupGoodPresenter.GroupBuyManagerPresenter((shopGroupManagerContract.View) this);
        presenter.ShopGroupList(page);
    }


    @Override
    public void getCategoryListSuccess(List<smallGoodsInfo> data) {

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
    public void deleteCategorySuccess(int position) {

    }

    @Override
    public void addUpCategorySuccess(smallGoodsInfo data) {

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
    public void onClick(View v) {
        if(v.getId() == R.id.tv_add){
            Intent intent = new Intent(mActivity, AddGroupGoodActivity.class);
            intent.putExtra("type",2);
            startActivity(intent);
        }
    }

    @Override
    public void check(int position, boolean isScroll) {

    }

    @Override
    public void onLoadMoreRequested() {
        page++;
        presenter.ShopGroupList(page);

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(NotifyEvent event) {
        if (event.type == Constants.EVENT_TYPE_REFRESH_SHOP_GOOD) {
            page=1;
            presenter.ShopGroupList(page);
        }

    }
    /**
     * Called when a swipe gesture triggers a refresh.
     */
    @Override
    public void onRefresh() {
        page=1;
        presenter.ShopGroupList(page);

    }
}
