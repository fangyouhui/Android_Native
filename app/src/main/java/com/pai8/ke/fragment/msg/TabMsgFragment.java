package com.pai8.ke.fragment.msg;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import com.lhs.library.base.BaseFragment;
import com.pai8.ke.R;
import com.pai8.ke.activity.message.entity.MsgCountInfo;
import com.pai8.ke.activity.message.entity.resp.MsgCountResp;
import com.pai8.ke.activity.message.ui.ActiveMessageActivity;
import com.pai8.ke.activity.message.ui.AttentionActivity;
import com.pai8.ke.activity.message.ui.CommentActivity;
import com.pai8.ke.activity.message.ui.LikesActivity;
import com.pai8.ke.activity.message.ui.OneToOneChatRecordActivity;
import com.pai8.ke.activity.message.ui.OrderMessageActivity;
import com.pai8.ke.activity.message.ui.SysMessageActivity;
import com.pai8.ke.activity.message.utils.BitmapDotUtil;
import com.pai8.ke.adapter.MsgCenterAdapter;
import com.pai8.ke.databinding.FragmentTabMsgBinding;
import com.pai8.ke.viewmodel.TabMsgViewModel;
import com.pai8.ke.widget.SpaceItemDecoration;

import java.util.ArrayList;
import java.util.List;

public class TabMsgFragment extends BaseFragment<TabMsgViewModel, FragmentTabMsgBinding> {

    private MsgCenterAdapter mAdapter;
    private List<MsgCountInfo> mList = new ArrayList<>();

    @Override
    public void initView(Bundle arguments) {
        mAdapter = new MsgCenterAdapter(getActivity());
        mBinding.rv.addItemDecoration(new SpaceItemDecoration(0, 0, 0, 2));
        mBinding.rv.setHasFixedSize(true);
        mBinding.rv.setAdapter(mAdapter);

        mAdapter.setOnItemClickListener((view, position, tag) -> {
            if (position == 0) {
                startActivity(new Intent(getContext(), OrderMessageActivity.class));
            } else if (position == 1) {
                startActivity(new Intent(getContext(), SysMessageActivity.class));
            } else if (position == 2) {
                startActivity(new Intent(getContext(), ActiveMessageActivity.class));
            } else if (position == 3) {
                startActivity(new Intent(getContext(), OneToOneChatRecordActivity.class));
            }
        });

        mBinding.llMsgTypeLike.setOnClickListener(v -> startActivity(new Intent(getContext(), LikesActivity.class)));
        mBinding.llMsgTypeComment.setOnClickListener(v -> startActivity(new Intent(getContext(), CommentActivity.class)));
        mBinding.llMsgTypeFollow.setOnClickListener(v -> startActivity(new Intent(getContext(), AttentionActivity.class)));
    }


    @Override
    public void initData() {
        mViewModel.msgIndex();
    }

    @Override
    public void addObserve() {
        mViewModel.getMsgIndexData().observe(getViewLifecycleOwner(), data -> {
            getMsgCountSuccess(data);
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        initData();
    }

    private void getMsgCountSuccess(MsgCountResp data) {
        if (data == null) {
            return;
        }
        if (0 != data.getAgree_msg().getCount()) {
            mBinding.ivLikes.setImageBitmap(BitmapDotUtil.generatorNumIcon2(getContext(),
                    BitmapFactory.decodeResource(getResources(), R.mipmap.ic_msg_type_like), true,
                    data.getAgree_msg().getCount() + ""));
        } else {
            mBinding.ivLikes.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_msg_type_like));
        }
        if (0 != data.getComment_msg().getCount()) {
            mBinding.ivComment.setImageBitmap(BitmapDotUtil.generatorNumIcon2(getContext(),
                    BitmapFactory.decodeResource(getResources(), R.mipmap.ic_msg_type_comment), true,
                    data.getComment_msg().getCount() + ""));
        } else {
            mBinding.ivComment.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_msg_type_comment));
        }
        if (0 != data.getFocus_msg().getCount()) {
            mBinding.ivAttention.setImageBitmap(BitmapDotUtil.generatorNumIcon2(getContext(),
                    BitmapFactory.decodeResource(getResources(), R.mipmap.ic_msg_type_follow), true,
                    data.getFocus_msg().getCount() + ""));
        } else {
            mBinding.ivAttention.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_msg_type_follow));
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

}
