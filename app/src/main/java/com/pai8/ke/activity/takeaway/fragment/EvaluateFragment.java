package com.pai8.ke.activity.takeaway.fragment;

import android.os.Bundle;
import android.view.View;
import android.widget.RatingBar;
import android.widget.TextView;

import com.pai8.ke.R;
import com.pai8.ke.activity.takeaway.Constants;
import com.pai8.ke.activity.takeaway.adapter.EvaluateAdapter;
import com.pai8.ke.activity.takeaway.contract.EvaluateContract;
import com.pai8.ke.activity.takeaway.entity.event.ShopDataEvent;
import com.pai8.ke.activity.takeaway.entity.resq.CommentInfo;
import com.pai8.ke.activity.takeaway.presenter.EvaluatePresenter;
import com.pai8.ke.base.BaseMvpFragment;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class EvaluateFragment extends BaseMvpFragment<EvaluatePresenter> implements View.OnClickListener, EvaluateContract.View {

    private RecyclerView mRvEvaluate;
    private EvaluateAdapter mAdapter;
    private TextView mTvScore;
    private RatingBar mRatingBar;
    private int page = 1;


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
        EventBus.getDefault().register(this);
        mRvEvaluate = mRootView.findViewById(R.id.rv_seller_comment);
        mRvEvaluate.setLayoutManager(new LinearLayoutManager(getActivity()));
        mTvScore = mRootView.findViewById(R.id.score_text);
        mRatingBar = mRootView.findViewById(R.id.evaluate_ratingBar);



    }



    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(ShopDataEvent event) {
        if (event.type == Constants.EVENT_TYPE_SHOP_CONTENT) {
            mPresenter.shopComments(event.data.shop_info.id+"",page);
            mTvScore.setText(event.data.shop_info.score+"");
            mRatingBar.setNumStars((int) event.data.shop_info.score);
        }

    }


    @Override
    protected void initData() {
        super.initData();

        mAdapter = new EvaluateAdapter(null);
        mRvEvaluate.setAdapter(mAdapter);


    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public void getCommentSuccess(List<CommentInfo> data) {
        mAdapter.setNewData(data);
    }
}
