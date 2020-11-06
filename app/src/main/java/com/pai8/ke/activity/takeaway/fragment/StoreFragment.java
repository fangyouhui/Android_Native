package com.pai8.ke.activity.takeaway.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.pai8.ke.R;
import com.pai8.ke.activity.takeaway.ItemType;
import com.pai8.ke.activity.takeaway.adapter.FoodClassifyAdapter;
import com.pai8.ke.activity.takeaway.adapter.FoodGoodAdapter;
import com.pai8.ke.activity.takeaway.entity.FoodClassifyInfo;
import com.pai8.ke.activity.takeaway.entity.FoodGoodInfo;
import com.pai8.ke.activity.takeaway.order.ConfirmOrderActivity;
import com.pai8.ke.activity.takeaway.presenter.StorePresenter;
import com.pai8.ke.base.BaseMvpFragment;
import com.pai8.ke.utils.RecyclerviewUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class StoreFragment extends BaseMvpFragment<StorePresenter> implements View.OnClickListener {

    private RecyclerView mRvClassify;
    private RecyclerView mRvGoods;
    private FoodClassifyAdapter mClassifyAdapter;
    private FoodGoodAdapter mGoodAdapter;
    private TextView mTvOrder;

    private final List<FoodClassifyInfo> mLeftList = new ArrayList<>();

    private final List<FoodGoodInfo> mRightList = new ArrayList<>();

    private final Map<Integer, Integer> indexMap = new HashMap<>();

    @Override
    public StorePresenter initPresenter() {
        return new StorePresenter();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.frament_store;
    }

    @Override
    protected void initView(Bundle arguments) {
        mRvClassify = mRootView.findViewById(R.id.rv_classify);
        mRvClassify.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRvGoods = mRootView.findViewById(R.id.rv_goods);
        mRvGoods.setLayoutManager(new LinearLayoutManager(getActivity()));

        mTvOrder = mRootView.findViewById(R.id.tv_order);
        mTvOrder.setOnClickListener(this);
    }


    @Override
    protected void initData() {
        super.initData();
        for (int i = 0; i < 30; i++) {
            FoodClassifyInfo bean = new FoodClassifyInfo();
            bean.bigSortId = i;
            bean.bigSortName = "大分类" + i;
            List<FoodClassifyInfo.ListBean> list = new ArrayList<>();
            for (int j = 0; j < 10; j++) {
                FoodClassifyInfo.ListBean listBean = new FoodClassifyInfo.ListBean();
                listBean.smallSortId = j;
                listBean.smallSortName = "小标签" + j;
                list.add(listBean);
            }
            bean.list = list;
            mLeftList.add(bean);
        }
        // 右侧的list是将每一个大类和小类按次序添加，并且标记大类的位置
        for (int i = 0; i < mLeftList.size(); i++) {
            FoodGoodInfo bigItem = new FoodGoodInfo();
            bigItem.viewType = ItemType.BIG_SORT;
            bigItem.id = mLeftList.get(i).bigSortId;
            bigItem.name = mLeftList.get(i).bigSortName;
            // 标记大类的位置，所有项的position默认是-1，如果是大类就添加position，让他和左侧位置对应
            bigItem.position = i;
            mRightList.add(bigItem);
            for (int j = 0; j < mLeftList.get(i).list.size(); j++) {
                FoodGoodInfo smallItem = new FoodGoodInfo();
                smallItem.viewType = ItemType.SMALL_SORT;
                smallItem.id = mLeftList.get(i).list.get(j).smallSortId;
                smallItem.name = mLeftList.get(i).list.get(j).smallSortName;
                mRightList.add(smallItem);
            }
        }
        // 点击左侧需要知道对应右侧的位置，用map先保存起来
        for (int i = 0; i < mRightList.size(); i++) {
            if (mRightList.get(i).position != -1) {
                indexMap.put(mRightList.get(i).position, i);
            }
        }

        mClassifyAdapter = new FoodClassifyAdapter(mLeftList);
        mRvClassify.setAdapter(mClassifyAdapter);
        mClassifyAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                mClassifyAdapter.setSelectedPosition(position);
                RecyclerviewUtils.moveToMiddle(mRvClassify,position);
                // 右侧滑到对应位置
                ((LinearLayoutManager) mRvGoods.getLayoutManager())
                        .scrollToPositionWithOffset(indexMap.get(position),0);
            }
        });

        mGoodAdapter = new FoodGoodAdapter(mRightList);
        mRvGoods.setAdapter(mGoodAdapter);


    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.tv_order){

            startActivity(new Intent(mActivity, ConfirmOrderActivity.class));
        }
    }
}
