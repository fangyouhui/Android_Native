package com.pai8.ke.activity.takeaway.ui;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.pai8.ke.R;
import com.pai8.ke.activity.takeaway.adapter.TakeawayAdapter;
import com.pai8.ke.activity.takeaway.contract.TakeawayContract;
import com.pai8.ke.activity.takeaway.entity.resq.TakeawayResq;
import com.pai8.ke.activity.takeaway.presenter.TakeawayPresenter;
import com.pai8.ke.base.BaseMvpActivity;
import com.pai8.ke.base.BasePresenter;
import com.pai8.ke.utils.ImageLoadUtils;
import com.youth.banner.Banner;
import com.youth.banner.loader.ImageLoader;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class TakeawayActivity extends BaseMvpActivity implements View.OnClickListener , TakeawayContract.View {


    private TakeawayPresenter p;
    private TakeawayAdapter mAdapter;
    private RecyclerView mRvStore;
    private Banner mBanner;

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
        findViewById(R.id.toolbar_iv_menu).setOnClickListener(this);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        mRvStore.setLayoutManager(linearLayoutManager);
        mAdapter = new TakeawayAdapter(null);
        mRvStore.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {

                Intent intent = new Intent(TakeawayActivity.this, StoreActivity.class);
                intent.putExtra("storeInfo", (Serializable) mAdapter.getData().get(position));
                startActivity(intent);
            }
        });
    }


    @Override
    public void initData() {
        super.initData();
        p = new TakeawayPresenter(this);
        p.getShopList("",1);


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
        if (!TextUtils.isEmpty(data.banner )) {
            final List<String> images = new ArrayList<>();
            images.add(data.banner);
            mBanner.setImageLoader(new GlideImageLoader());
            mBanner.setImages(images);
            mBanner.start();
        }
        mAdapter.setNewData(data.shop_list);
    }

    @Override
    public BasePresenter initPresenter() {
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
}
