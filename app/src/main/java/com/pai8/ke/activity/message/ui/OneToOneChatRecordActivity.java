package com.pai8.ke.activity.message.ui;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.hjq.bar.OnTitleBarListener;
import com.pai8.ke.R;
import com.pai8.ke.activity.message.adapter.ChatRecordAdapter;
import com.pai8.ke.activity.message.contract.ChatRecordContract;
import com.pai8.ke.activity.message.entity.resp.MessageResp;
import com.pai8.ke.activity.message.presenter.ChatRecordPresenter;
import com.pai8.ke.base.BaseActivity;
import com.pai8.ke.base.BaseEvent;
import com.pai8.ke.base.BaseMvpActivity;
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
 * @author Created by zzf
 * @time 11:21
 * Description：一对一聊天记录
 */
public class OneToOneChatRecordActivity extends BaseMvpActivity<ChatRecordPresenter> implements ChatRecordContract.View,SwipeRefreshLayout.OnRefreshListener, BaseQuickAdapter.RequestLoadMoreListener {


    @BindView(R.id.rv_chat_record)
    RecyclerView rvChatRecord;
    @BindView(R.id.sr_layout)
    SwipeRefreshLayout srLayout;
    private ChatRecordAdapter mAdapter;
    private List<MessageResp> mList = new ArrayList<>();
    private int page = 1;

    @Override
    public int getLayoutId() {
        return R.layout.activity_one_two_one_chat;
    }

    @Override
    public void initView() {
        mTitleBar.setTitle("一对一聊天记录");
        srLayout.setOnRefreshListener(this);
        srLayout.setColorSchemeResources(R.color.colorPrimary);
        mAdapter = new ChatRecordAdapter(mList);
        rvChatRecord.setLayoutManager(new LinearLayoutManager(this));
        rvChatRecord.setHasFixedSize(true);
        mAdapter.setEnableLoadMore(true);
        mAdapter.setOnLoadMoreListener(this,rvChatRecord);
        mAdapter.setEmptyView(R.layout.layout_empty_view, new LinearLayout(this));
        rvChatRecord.setAdapter(mAdapter);

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
    public void getChatRecordSuccess(List<MessageResp> data) {
        if (data != null) {
            if (page == 1) {
                mAdapter.replaceData(data);
            } else {
                mAdapter.addData(data);
            }
            if (data.size() < GlobalConstants.SIZE) {
                mAdapter.loadMoreEnd();
            }else {
                mAdapter.loadMoreComplete();
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

    @Override
    public ChatRecordPresenter initPresenter() {
        return new ChatRecordPresenter(this);
    }
}
