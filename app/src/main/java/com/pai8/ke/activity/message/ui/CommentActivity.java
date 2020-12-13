package com.pai8.ke.activity.message.ui;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.hjq.bar.OnTitleBarListener;
import com.pai8.ke.R;
import com.pai8.ke.activity.message.adapter.CommentAdapter;
import com.pai8.ke.activity.message.contract.CommentContract;
import com.pai8.ke.activity.message.entity.resp.MessageResp;
import com.pai8.ke.activity.message.presenter.CommentPresenter;
import com.pai8.ke.base.BaseActivity;
import com.pai8.ke.base.BaseEvent;
import com.pai8.ke.base.BaseMvpActivity;
import com.pai8.ke.entity.resp.CommentResp;
import com.pai8.ke.global.GlobalConstants;

import android.view.View;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import butterknife.BindView;

import static com.pai8.ke.global.EventCode.EVENT_COUPON;

/**
 *
 * @author Created by zzf
 * @time  19:50
 * Description：评论
 */
public class CommentActivity extends BaseMvpActivity<CommentPresenter> implements CommentContract.View,SwipeRefreshLayout.OnRefreshListener, BaseQuickAdapter.RequestLoadMoreListener {

    @BindView(R.id.rv_likes)
    RecyclerView rvLikes;
    @BindView(R.id.sr_layout)
    SwipeRefreshLayout srLayout;
    private CommentAdapter mAdapter;
    private List<MessageResp> mList = new ArrayList<>();
    private int page = 1;


    @Override
    public int getLayoutId() {
        return R.layout.activity_likes;
    }

    @Override
    public void initView() {
        mTitleBar.setTitle("评论");
        srLayout.setOnRefreshListener(this);
        srLayout.setColorSchemeResources(R.color.colorPrimary);
        mAdapter = new CommentAdapter(mList);
        rvLikes.setLayoutManager(new LinearLayoutManager(this));
        rvLikes.setHasFixedSize(true);
        mAdapter.setEnableLoadMore(true);
        mAdapter.setEmptyView(R.layout.layout_empty_view, new LinearLayout(this));
        rvLikes.setAdapter(mAdapter);

    }

    @Override
    public void initListener() {
        super.initListener();
        mTitleBar.setOnTitleBarListener(new OnTitleBarListener() {
            @Override
            public void onLeftClick(View v) {
                finish();
            }

            @Override
            public void onTitleClick(View v) {

            }

            @Override
            public void onRightClick(View v) {

            }
        });
    }

    @Override
    public void initData() {
        mPresenter.reqMessageList(page);
    }

    @Override
    public void onRefresh() {
        page = 1;
        mPresenter.reqMessageList(page);
    }

    @Override
    public void onLoadMoreRequested() {
        page++;
        mPresenter.reqMessageList(page);
    }


    @Override
    public CommentPresenter initPresenter() {
        return new CommentPresenter(this);
    }

    @Override
    public void getCommentSuccess(List<MessageResp> data) {
        if (data != null) {
            if (data.size() < GlobalConstants.SIZE) {
                mAdapter.loadMoreComplete();
            }
            if (page == 1) {
                mAdapter.replaceData(data);
            } else {
                mAdapter.addData(data);
            }
        }
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
}
