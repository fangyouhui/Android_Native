package com.pai8.ke.activity.takeaway.fragment;

import android.os.Bundle;
import android.view.View;

import com.pai8.ke.R;
import com.pai8.ke.activity.takeaway.adapter.EvaluateAdapter;
import com.pai8.ke.activity.takeaway.contract.EvaluateContract;
import com.pai8.ke.activity.takeaway.presenter.EvaluatePresenter;
import com.pai8.ke.base.BaseMvpFragment;

import java.util.ArrayList;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class EvaluateFragment extends BaseMvpFragment<EvaluatePresenter> implements View.OnClickListener, EvaluateContract.View {

    private RecyclerView mRvEvaluate;
    private EvaluateAdapter mAdapter;


    @Override
    public EvaluatePresenter initPresenter() {
        return new EvaluatePresenter(this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.frament_evaluate;
    }

    @Override
    protected void initView(Bundle arguments) {
        mRvEvaluate = mRootView.findViewById(R.id.rv_seller_comment);
        mRvEvaluate.setLayoutManager(new LinearLayoutManager(getActivity()));

    }


    @Override
    protected void initData() {
        super.initData();
        ArrayList<String> list = new ArrayList<>();
        for (int i = 0; i < 30; i++) {

            list.add("");
        }

        mAdapter = new EvaluateAdapter(list);
        mRvEvaluate.setAdapter(mAdapter);


    }

    @Override
    public void onClick(View v) {

    }
}
