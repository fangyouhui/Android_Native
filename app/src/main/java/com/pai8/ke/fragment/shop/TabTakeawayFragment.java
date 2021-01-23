package com.pai8.ke.fragment.shop;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.pai8.ke.R;
import com.pai8.ke.activity.takeaway.adapter.TakeawayAdapter;
import com.pai8.ke.activity.takeaway.contract.TakeawayContract;
import com.pai8.ke.activity.takeaway.entity.resq.TakeawayResq;
import com.pai8.ke.activity.takeaway.presenter.TakeawayPresenter;
import com.pai8.ke.activity.takeaway.ui.AddressActivity;
import com.pai8.ke.activity.takeaway.ui.StoreActivity;
import com.pai8.ke.app.MyApp;
import com.pai8.ke.base.BaseEvent;
import com.pai8.ke.base.BaseMvpFragment;
import com.pai8.ke.entity.Address;
import com.pai8.ke.global.EventCode;
import com.pai8.ke.utils.ImageLoadUtils;
import com.pai8.ke.utils.LogUtils;
import com.youth.banner.Banner;
import com.youth.banner.loader.ImageLoader;

import org.greenrobot.eventbus.EventBus;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import razerdp.util.KeyboardUtils;

public class TabTakeawayFragment extends BaseMvpFragment<TakeawayPresenter> implements View.OnClickListener, TakeawayContract.View {

    @BindView(R.id.rv_store)
    RecyclerView mRvStore;
    @BindView(R.id.banner)
    Banner mBanner;
    @BindView(R.id.et_search)
    EditText mEtSearch;
    @BindView(R.id.toolbar_iv_menu)
    TextView mTvAddress;
    @BindView(R.id.refresh)
    SwipeRefreshLayout swipeRefreshLayout;

    private TakeawayPresenter p;
    private TakeawayAdapter mAdapter;
    private String key = "";
    private int page = 1;
    private String lon, lat;
    private Address mAddress;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_tab_takeaway;
    }

    @Override
    protected void initView(Bundle arguments) {
        mTvAddress.setOnClickListener(this);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        mRvStore.setLayoutManager(linearLayoutManager);
        mAdapter = new TakeawayAdapter(null);
        mRvStore.setAdapter(mAdapter);
        mAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                page++;
                p.getShopList(key, page, lon, lat);
            }
        }, mRvStore);

        mAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {

                Intent intent = new Intent(getActivity(), StoreActivity.class);
                intent.putExtra("storeInfo", (Serializable) mAdapter.getData().get(position));
                startActivity(intent);
            }
        });

        mEtSearch.setOnKeyListener((v, keyCode, event) -> {        // 开始搜索
            if (keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_DOWN) {
                KeyboardUtils.close(getActivity());
                //搜索逻辑
                page = 1;
                key = mEtSearch.getText().toString();
                p.getShopList(key, page, lon, lat);
                return true;
            }
            return false;
        });

    }

    @Override
    protected void receiveEvent(BaseEvent event) {
        super.receiveEvent(event);
        switch (event.getCode()) {
            case EventCode.EVENT_CHOOSE_ADDRESS:
                page = 1;
                mAddress = (Address) event.getData();
                mTvAddress.setText(mAddress.getAddress());
                lat = mAddress.getLat() + "";
                lon = mAddress.getLon() + "";
                mPresenter.getShopList(key, page, lon, lat);
                break;
        }
    }

    @Override
    public void initData() {
        super.initData();
        EventBus.getDefault().register(this);
        mTvAddress.setText(MyApp.getLngLat().get(2));
        p = new TakeawayPresenter(this);
        lat = MyApp.getLngLat().get(1);
        lon = MyApp.getLngLat().get(0);
        mPresenter.getShopList(key, page, lon, lat);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                page = 1;
                mPresenter.getShopList(key, page, lon, lat);

            }
        });

    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.toolbar_iv_menu) {
            startActivity(new Intent(getActivity(), AddressActivity.class));
        }
    }

    @Override
    public void getTakeawaySuccess(TakeawayResq data) {
        swipeRefreshLayout.setRefreshing(false);
        if (data.banner != null && data.banner.size() > 0) {
            final List<String> images = new ArrayList<>();
            for (int i = 0; i < data.banner.size(); i++) {
                images.add(data.banner.get(i).imgurl);
            }
            mBanner.setImageLoader(new GlideImageLoader());
            mBanner.setImages(images);
            mBanner.start();
        }
        if (page == 1) {
            mAdapter.setNewData(data.shop_list);
            if (data.shop_list.size() < 10) {
                mAdapter.loadMoreEnd(true);
            }
        } else {
            if (data.shop_list.size() < 10) {
                mAdapter.loadMoreEnd(true);
            }
            mAdapter.addData(data.shop_list);
        }
    }

    @Override
    public TakeawayPresenter initPresenter() {
        return new TakeawayPresenter(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    public class GlideImageLoader extends ImageLoader {

        @Override
        public void displayImage(Context context, Object path, ImageView imageView) {
            if (context != null) {
                imageView.setScaleType(ImageView.ScaleType.FIT_XY);
                ImageLoadUtils.setRectImage(context, (String) path, imageView);

            }
        }
    }
}
