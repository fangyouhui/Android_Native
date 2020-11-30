package com.pai8.ke.fragment.msg;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.github.jdsjlzx.recyclerview.LuRecyclerViewAdapter;
import com.github.jdsjlzx.recyclerview.ProgressStyle;
import com.pai8.ke.R;
import com.pai8.ke.adapter.MsgCenterAdapter;
import com.pai8.ke.base.BaseFragment;
import com.pai8.ke.interfaces.OnItemClickListener;
import com.pai8.ke.widget.SpaceItemDecoration;

import java.util.ArrayList;
import java.util.List;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.OnClick;

public class TabMsgFragment extends BaseFragment {
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tv_msg_type_like)
    TextView tvMsgTypeLike;
    @BindView(R.id.tv_msg_type_comment)
    TextView tvMsgTypeComment;
    @BindView(R.id.tv_msg_type_follow)
    TextView tvMsgTypeFollow;
    @BindView(R.id.rv)
    RecyclerView rv;
    private MsgCenterAdapter mAdapter;

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
        List<String> strings = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            strings.add("");
        }
        mAdapter.setDataList(strings);
        mAdapter.setOnItemClickListener((view, position, tag) -> toast("此功能暂未开放,敬请期待"));
    }

    @OnClick({R.id.tv_msg_type_like, R.id.tv_msg_type_comment, R.id.tv_msg_type_follow})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_msg_type_like:
                toast("此功能暂未开放,敬请期待");
                break;
            case R.id.tv_msg_type_comment:
                toast("此功能暂未开放,敬请期待");
                break;
            case R.id.tv_msg_type_follow:
                toast("此功能暂未开放,敬请期待");
                break;
        }
    }
}
