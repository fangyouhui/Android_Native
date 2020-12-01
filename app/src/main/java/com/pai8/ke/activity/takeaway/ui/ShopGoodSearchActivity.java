package com.pai8.ke.activity.takeaway.ui;

import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;

import com.google.gson.Gson;
import com.pai8.ke.R;
import com.pai8.ke.activity.takeaway.adapter.ShopSearchFoodGoodAdapter;
import com.pai8.ke.activity.takeaway.api.TakeawayApi;
import com.pai8.ke.activity.takeaway.entity.FoodGoodInfo;
import com.pai8.ke.base.BaseActivity;
import com.pai8.ke.base.retrofit.BaseObserver;
import com.pai8.ke.base.retrofit.RxSchedulers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import razerdp.util.KeyboardUtils;

public class ShopGoodSearchActivity extends BaseActivity implements View.OnClickListener {

    private RecyclerView mRvShop;
    private EditText mEtSearch;
    private String key;
    private int shopId ;

    private ShopSearchFoodGoodAdapter mAdapter;

    @Override
    public int getLayoutId() {
        return R.layout.activity_shop_good_search;
    }

    @Override
    public void initView() {
        setImmersionBar(R.id.base_tool_bar);
        findViewById(R.id.toolbar_back_all).setOnClickListener(this);
        mEtSearch = findViewById(R.id.et_search);
        mRvShop = findViewById(R.id.rv_shop);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        mRvShop.setLayoutManager(layoutManager);
        mEtSearch.setOnKeyListener((v, keyCode, event) -> {        // 开始搜索
            if (keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_DOWN) {
                KeyboardUtils.close(this);
                //搜索逻辑
                key = mEtSearch.getText().toString();
                foodSearch(shopId+"",key);
                return true;
            }
            return false;
        });

    }


    @Override
    public void initData() {
        super.initData();
        shopId = getIntent().getIntExtra("shopId",0);


    }



    public void foodSearch(String shopid,String keywords){
        HashMap<String,Object> map = new HashMap<>();
        map.put("shop_id",shopid);
        map.put("keywords",keywords);
        TakeawayApi.getInstance().foodSearch(createRequestBody(map))
                .doOnSubscribe(disposable -> {
                })
                .compose(RxSchedulers.io_main())
                .subscribe(new BaseObserver<List<FoodGoodInfo>>() {
                    @Override
                    protected void onSuccess(List<FoodGoodInfo> data){
                        mAdapter = new ShopSearchFoodGoodAdapter(data);
                        mRvShop.setAdapter(mAdapter);
                    }
                    @Override
                    protected void onError(String msg, int errorCode) {
                        super.onError(msg, errorCode);
                    }
                });
    }




    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.toolbar_back_all) {
            finish();
        } else if (v.getId() == R.id.tv_relocation) {
        }
    }




    public RequestBody createRequestBody(Map map) {
        Gson gson = new Gson();
        String json = gson.toJson(map);
        RequestBody requestBody = RequestBody.create(json, MediaType.parse("application/json"));
        return requestBody;
    }

}
