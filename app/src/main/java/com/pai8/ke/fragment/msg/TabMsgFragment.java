package com.pai8.ke.fragment.msg;

import com.luck.picture.lib.tools.BitmapUtils;
import com.pai8.ke.R;
import com.pai8.ke.activity.message.contract.TabMsgContract;
import com.pai8.ke.activity.message.entity.MsgCountInfo;
import com.pai8.ke.activity.message.entity.resp.MsgCountResp;
import com.pai8.ke.activity.message.presenter.TabMsgPresenter;
import com.pai8.ke.activity.message.ui.ActiveMessageActivity;
import com.pai8.ke.activity.message.ui.AttentionActivity;
import com.pai8.ke.activity.message.ui.CommentActivity;
import com.pai8.ke.activity.message.ui.LikesActivity;
import com.pai8.ke.activity.message.ui.OneToOneChatRecordActivity;
import com.pai8.ke.activity.message.ui.OrderMessageActivity;
import com.pai8.ke.activity.message.ui.SysMessageActivity;
import com.pai8.ke.activity.message.utils.BitmapDotUtil;
import com.pai8.ke.adapter.MsgCenterAdapter;
import com.pai8.ke.base.BaseFragment;
import com.pai8.ke.base.BaseMvpFragment;
import com.pai8.ke.widget.SpaceItemDecoration;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.OnClick;

public class TabMsgFragment extends BaseMvpFragment<TabMsgPresenter> implements TabMsgContract.View {
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.rv)
    RecyclerView rv;
    @BindView(R.id.iv_likes)
    ImageView ivLikes;
    @BindView(R.id.iv_comment)
    ImageView ivComment;
    @BindView(R.id.iv_attention)
    ImageView ivAttention;
    private MsgCenterAdapter mAdapter;
    private List<MsgCountInfo> mList = new ArrayList<>();

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_tab_msg;
    }

    @Override
    protected void initView(Bundle arguments) {
        mAdapter = new MsgCenterAdapter(getActivity());
        rv.setLayoutManager(new LinearLayoutManager(getActivity()));
        rv.addItemDecoration(new SpaceItemDecoration(0, 0, 0, 2));
        rv.setHasFixedSize(true);
        rv.setAdapter(mAdapter);
    }

    @Override
    protected void initListener() {
        super.initListener();
        mAdapter.setOnItemClickListener((view, position, tag) -> {
            if (position == 0) {
                startActivity(new Intent(mActivity, OrderMessageActivity.class));
            } else if (position == 1) {
                startActivity(new Intent(mActivity, SysMessageActivity.class));
            } else if (position == 2) {
                startActivity(new Intent(mActivity, ActiveMessageActivity.class));
            } else if (position == 3) {
                startActivity(new Intent(mActivity, OneToOneChatRecordActivity.class));
            }
        });
    }

    @Override
    protected void initData() {
        super.initData();
        mPresenter.reqMessageList();
    }

    @OnClick({R.id.ll_msg_type_like, R.id.ll_msg_type_comment, R.id.ll_msg_type_follow})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ll_msg_type_like:
                startActivity(new Intent(mActivity, LikesActivity.class));
                break;
            case R.id.ll_msg_type_comment:
                startActivity(new Intent(mActivity, CommentActivity.class));
                break;
            case R.id.ll_msg_type_follow:
                startActivity(new Intent(mActivity, AttentionActivity.class));
                break;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        mPresenter.reqMessageList();
    }

    @Override
    public TabMsgPresenter initPresenter() {
        return new TabMsgPresenter(this);
    }

    @Override
    public void getMsgCountSuccess(MsgCountResp data) {
        if (data == null) {
            return;
        }
        if (0 != data.getAgree_msg().getCount()) {
            ivLikes.setImageBitmap(BitmapDotUtil.generatorNumIcon2(mActivity,
                    BitmapFactory.decodeResource(getResources(), R.mipmap.ic_msg_type_like), true,
                    data.getAgree_msg().getCount() + ""));
        } else {
            ivLikes.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_msg_type_like));
        }
        if (0 != data.getComment_msg().getCount()) {
            ivComment.setImageBitmap(BitmapDotUtil.generatorNumIcon2(mActivity,
                    BitmapFactory.decodeResource(getResources(), R.mipmap.ic_msg_type_comment), true,
                    data.getComment_msg().getCount() + ""));
        } else {
            ivComment.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_msg_type_comment));
        }
        if (0 != data.getFocus_msg().getCount()) {
            ivAttention.setImageBitmap(BitmapDotUtil.generatorNumIcon2(mActivity,
                    BitmapFactory.decodeResource(getResources(), R.mipmap.ic_msg_type_follow), true,
                    data.getFocus_msg().getCount() + ""));
        } else {
            ivAttention.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_msg_type_follow));
        }
        mList.clear();
        for (int i = 0; i < 4; i++) {
            if (i == 0) {
                mList.add(new MsgCountInfo(data.getOrder_msg().getCount() + "", i));
            } else if (i == 1) {
                mList.add(new MsgCountInfo(data.getSys_msg().getCount() + "", i));
            } else if (i == 2) {
                mList.add(new MsgCountInfo(data.getEve_msg().getCount() + "", i));
            } else {
                mList.add(new MsgCountInfo(data.getCall_msg().getCount() + "", i));
            }
        }
        mAdapter.setDataList(mList);
    }

    @Override
    public void showProgress() {

    }

    @Override
    public void hideProgress() {

    }
}
