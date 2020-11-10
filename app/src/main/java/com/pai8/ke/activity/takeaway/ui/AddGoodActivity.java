package com.pai8.ke.activity.takeaway.ui;

import android.content.Intent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.pai8.ke.R;
import com.pai8.ke.activity.takeaway.adapter.GoodCategoryAdapter;
import com.pai8.ke.activity.takeaway.api.TakeawayApi;
import com.pai8.ke.activity.takeaway.entity.req.CategoryInfo;
import com.pai8.ke.base.BaseMvpActivity;
import com.pai8.ke.base.BasePresenter;
import com.pai8.ke.base.retrofit.BaseObserver;
import com.pai8.ke.base.retrofit.RxSchedulers;
import com.pai8.ke.widget.BottomDialog;

import java.util.List;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class AddGoodActivity extends BaseMvpActivity implements View.OnClickListener {


    private TextView mTvCategory;   //分类


    private BottomDialog mGoodCategoryDialog;


    @Override
    public BasePresenter initPresenter() {
        return null;
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_add_good;
    }

    @Override
    public void initView() {

        setImmersionBar(R.id.base_tool_bar);
        findViewById(R.id.toolbar_back_all).setOnClickListener(this);
        findViewById(R.id.tv_next).setOnClickListener(this);
        mTvCategory = findViewById(R.id.tv_category);
        mTvCategory.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.toolbar_back_all){
            finish();
        }else if(v.getId() == R.id.tv_next){
            startActivity(new Intent(this,MerchantSettledSecondActivity.class));
        }else if(v.getId() == R.id.tv_category){
//            getUpCategory();
            showBottomDialog();

        }
    }


    private void getUpCategory(){
        TakeawayApi.getInstance().getUpCategory()
                .doOnSubscribe(disposable -> {
                })
                .compose(RxSchedulers.io_main())
                .subscribe(new BaseObserver<List<CategoryInfo>>() {
                    @Override
                    protected void onSuccess(List<CategoryInfo> list) {
                        showBottomDialog();
                    }

                    @Override
                    protected void onError(String msg, int errorCode) {
                        dismissLoadingDialog();
                        super.onError(msg, errorCode);
                    }
                });
    }


    private void showBottomDialog() {
        View view = View.inflate(this, R.layout.dialog_good_category, null);
        ImageButton itnClose = view.findViewById(R.id.itn_close);
        RecyclerView rvGoodCategory = view.findViewById(R.id.rv_good_category);
        GridLayoutManager layoutManager = new GridLayoutManager(this,3);
        rvGoodCategory.setLayoutManager(layoutManager);




        GoodCategoryAdapter adapter = new GoodCategoryAdapter(null);
        rvGoodCategory.setAdapter(adapter);
        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter1, View view, int position) {
                adapter.singleChoose(position);
            }
        });

        itnClose.setOnClickListener(view1 -> {
            mGoodCategoryDialog.dismiss();
        });

        if (mGoodCategoryDialog == null) {
            mGoodCategoryDialog = new BottomDialog(this, view);
        }
        mGoodCategoryDialog.setIsCanceledOnTouchOutside(true);
        mGoodCategoryDialog.show();
    }




}
