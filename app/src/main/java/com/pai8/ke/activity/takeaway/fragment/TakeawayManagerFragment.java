package com.pai8.ke.activity.takeaway.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.pai8.ke.R;
import com.pai8.ke.activity.takeaway.adapter.TakeawayFoodAdapter;
import com.pai8.ke.activity.takeaway.adapter.TakeawayManagerAdapter;
import com.pai8.ke.activity.takeaway.contract.TakeawayManagerContract;
import com.pai8.ke.activity.takeaway.entity.resq.ShopInfo;
import com.pai8.ke.activity.takeaway.presenter.TakeawayManagerPresenter;
import com.pai8.ke.activity.takeaway.ui.AddGoodActivity;
import com.pai8.ke.activity.takeaway.utils.SoftHideKeyBoardUtil;
import com.pai8.ke.base.BaseMvpFragment;

import java.util.ArrayList;
import java.util.List;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import razerdp.util.KeyboardUtils;

public class TakeawayManagerFragment extends BaseMvpFragment<TakeawayManagerPresenter> implements OnClickListener, TakeawayManagerContract.View {

    private EditText mEtType;

    private TakeawayManagerPresenter presenter;
    private RecyclerView mRvClassify,mRvGood;
    private TakeawayFoodAdapter mAdapter;
    private TakeawayManagerAdapter mTakeawayAdapter;

    @Override
    public TakeawayManagerPresenter initPresenter() {
        return new TakeawayManagerPresenter(this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_takeaway_manager;
    }

    @Override
    protected void initView(Bundle arguments) {
        SoftHideKeyBoardUtil.assistActivity(getActivity());
        mEtType = mRootView.findViewById(R.id.et_type);
        mRvClassify = mRootView.findViewById(R.id.rv_classify);
        mRootView.findViewById(R.id.tv_add).setOnClickListener(this);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        mRvClassify.setLayoutManager(layoutManager);
        mRootView.findViewById(R.id.tv_add_classify).setOnClickListener(this);
        mRvGood = mRootView.findViewById(R.id.rv_goods);
        LinearLayoutManager layoutManager1 = new LinearLayoutManager(getActivity());
        mRvGood.setLayoutManager(layoutManager1);
        mEtType.setOnKeyListener((v, keyCode, event) -> {        // 开始搜索
            if (keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_DOWN) {
                KeyboardUtils.close(getActivity());
                //搜索逻辑
                String name = mEtType.getText().toString();
                presenter.addUpCategory(name,"1");
                mEtType.setText("");
                mEtType.setVisibility(View.GONE);
                return true;
            }
            return false;
        });

    }

    @Override
    protected void initData() {
        super.initData();
        List<String> list = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            list.add("1");
        }
        presenter = new TakeawayManagerPresenter(this);

        mTakeawayAdapter  = new TakeawayManagerAdapter(null);
        mRvClassify.setAdapter(mTakeawayAdapter);
        mTakeawayAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                if(view.getId() == R.id.iv_edit){


                }else if(view.getId() == R.id.iv_del){

                    presenter.deleteCategory(mTakeawayAdapter.getData().get(position).id,position);

                }
            }
        });

        mAdapter  = new TakeawayFoodAdapter(list);
        mRvGood.setAdapter(mAdapter);
        mAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                if(view.getId() == R.id.tv_edit_goods){

                    Intent intent = new Intent(mActivity,AddGoodActivity.class);
                    intent.putExtra("type",1);
                    startActivity(intent);

                }
            }
        });
        presenter.getCategoryList(1);

    }






    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.tv_add){
            startActivity(new Intent(mActivity, AddGoodActivity.class));

        }else if(v.getId() == R.id.tv_add_classify){  //添加分类
            mEtType.setVisibility(View.VISIBLE);

        }
    }

    @Override
    public void getCategoryListSuccess(List<ShopInfo> data) {
        mTakeawayAdapter.setNewData(data);
    }

    @Override
    public void deleteCategorySuccess(int position) {
        mTakeawayAdapter.remove(position);
    }

    @Override
    public void addUpCategorySuccess(ShopInfo data) {
        mTakeawayAdapter.getData().add(data);
        mTakeawayAdapter.notifyDataSetChanged();

    }
}
