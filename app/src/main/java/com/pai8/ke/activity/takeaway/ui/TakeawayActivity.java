package com.pai8.ke.activity.takeaway.ui;

import android.content.Context;
import android.content.Intent;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.pai8.ke.R;
import com.pai8.ke.activity.takeaway.adapter.TakeawayAdapter;
import com.pai8.ke.activity.takeaway.contract.TakeawayContract;
import com.pai8.ke.activity.takeaway.entity.resq.TakeawayResq;
import com.pai8.ke.activity.takeaway.presenter.TakeawayPresenter;
import com.pai8.ke.app.MyApp;
import com.pai8.ke.base.BaseEvent;
import com.pai8.ke.base.BaseMvpActivity;
import com.pai8.ke.entity.Address;
import com.pai8.ke.global.EventCode;
import com.pai8.ke.utils.ImageLoadUtils;
import com.youth.banner.Banner;
import com.youth.banner.loader.ImageLoader;

import org.greenrobot.eventbus.EventBus;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import razerdp.util.KeyboardUtils;

public class TakeawayActivity extends BaseMvpActivity<TakeawayPresenter> implements View.OnClickListener , TakeawayContract.View {


    private TakeawayPresenter p;
    private TakeawayAdapter mAdapter;
    private RecyclerView mRvStore;
    private Banner mBanner;
    private EditText mEtSearch;
    private String key;
    private TextView mTvAddress;
    private int page = 1;
    private String lon,lat;

    @Override
    public int getLayoutId() {
        return R.layout.activity_takeaway;
    }

    @Override
    public void initView() {
        setImmersionBar(R.id.base_tool_bar);
        mRvStore = findViewById(R.id.rv_store);
        mBanner = findViewById(R.id.banner);
        findViewById(R.id.toolbar_back_all).setOnClickListener(this);
        mTvAddress = findViewById(R.id.toolbar_iv_menu);
        mEtSearch = findViewById(R.id.et_search);
        mTvAddress.setOnClickListener(this);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        mRvStore.setLayoutManager(linearLayoutManager);
        mAdapter = new TakeawayAdapter(null);
        mRvStore.setAdapter(mAdapter);
        mAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                page++;
                p.getShopList(key,page,lon,lat);
            }
        }, mRvStore);

        mAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {

                Intent intent = new Intent(TakeawayActivity.this, StoreActivity.class);
                intent.putExtra("storeInfo", (Serializable) mAdapter.getData().get(position));
                startActivity(intent);
            }
        });

        mEtSearch.setOnKeyListener((v, keyCode, event) -> {        // 开始搜索
            if (keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_DOWN) {
                KeyboardUtils.close(this);
                //搜索逻辑
                page = 1;
                key = mEtSearch.getText().toString();
                p.getShopList(key,page,lon,lat);
                return true;
            }
            return false;
        });

    }
    private Address mAddress;


    @Override
    protected void receiveEvent(BaseEvent event) {
        super.receiveEvent(event);
        switch (event.getCode()) {
            case EventCode.EVENT_CHOOSE_ADDRESS:
                page = 1;
                mAddress = (Address) event.getData();
                mTvAddress.setText(mAddress.getAddress());
                lat = mAddress.getLat()+"";
                lon = mAddress.getLon()+"";
                mPresenter.getShopList(key,page,lon,lat);
                break;
        }
    }

    @Override
    public void initData() {
        super.initData();
        EventBus.getDefault().register(this);
        mTvAddress.setText(MyApp.getLngLat().get(2));
        p = new TakeawayPresenter(this);
        lat = MyApp.getLngLat().get(0);
        lon =  MyApp.getLngLat().get(1);
        mPresenter.getShopList(key,page,lon,lat);


    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.toolbar_back_all) {
            startActivity(new Intent(this, StoreManagerActivity.class));
        } else if (v.getId() == R.id.toolbar_iv_menu) {
            startActivity(new Intent(this, AddressActivity.class));
        }
    }

    @Override
    public void getTakeawaySuccess(TakeawayResq data) {
        if (data.banner!=null && data.banner.size()>0) {
            final List<String> images = new ArrayList<>();
            for(int i=0;i<data.banner.size();i++){
                images.add(data.banner.get(i).imgurl);
            }
            mBanner.setImageLoader(new GlideImageLoader());
            mBanner.setImages(images);
            mBanner.start();
        }
        if(page == 1){
            mAdapter.setNewData(data.shop_list);
        }else{
            mAdapter.addData(data.shop_list);
        }
    }

    @Override
    public TakeawayPresenter initPresenter() {
        return new TakeawayPresenter(this);
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
