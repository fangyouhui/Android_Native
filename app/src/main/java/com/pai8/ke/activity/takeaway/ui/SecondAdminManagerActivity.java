package com.pai8.ke.activity.takeaway.ui;

import com.afollestad.materialdialogs.MaterialDialog;
import com.pai8.ke.R;
import com.pai8.ke.activity.takeaway.adapter.SecondAdminManagerAdapter;
import com.pai8.ke.activity.takeaway.contract.SecondAdminManagerContract;
import com.pai8.ke.activity.takeaway.entity.resq.SecondAdminManagerResq;
import com.pai8.ke.activity.takeaway.presenter.SecondAdminManagerPresenter;
import com.pai8.ke.base.BaseMvpActivity;

import android.content.Intent;
import android.view.View;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import butterknife.BindView;
import butterknife.OnClick;

/**
 * 二级管理员界面
 *
 * @author Created by zzf
 * @time 22:32
 * Description：
 */
public class SecondAdminManagerActivity extends BaseMvpActivity<SecondAdminManagerPresenter> implements SecondAdminManagerContract.View, SwipeRefreshLayout.OnRefreshListener {

    @BindView(R.id.rv_second_admin)
    RecyclerView mRvSecondAdmin;
    @BindView(R.id.sr_layout)
    SwipeRefreshLayout srLayout;
    private SecondAdminManagerAdapter mAdapter;
    private List<SecondAdminManagerResq> mList = new ArrayList<>();


    @Override
    public SecondAdminManagerPresenter initPresenter() {
        return new SecondAdminManagerPresenter(this);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_second_admin_manager;
    }

    @Override
    public void initView() {
        setImmersionBar(R.id.base_tool_bar);
    }

    @Override
    public void initData() {
        super.initData();
        srLayout.setOnRefreshListener(this);
        srLayout.setColorSchemeResources(R.color.colorPrimary);
        mAdapter = new SecondAdminManagerAdapter(mList);
        mAdapter.setEmptyView(R.layout.layout_empty_view, new LinearLayout(this));
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        mRvSecondAdmin.setLayoutManager(layoutManager);
        mRvSecondAdmin.setAdapter(mAdapter);
        mPresenter.getSecondAdminList();
    }

    @Override
    public void initListener() {
        super.initListener();
        mAdapter.setOnItemChildClickListener((adapter, view, position) -> {
            if(view.getId() == R.id.tv_del) {
                new MaterialDialog.Builder(this)
                        .title("温馨提示")
                        .content("确定删除该管理员吗？")
                        .positiveText("确认")
                        .negativeText("取消")
                        .onPositive((dialog, which) -> {
                            mPresenter.deleteSecondAdmin(mList.get(position).getId());
                        })
                        .show();
            } else if(view.getId() == R.id.tv_update) {
                SecondAdminManagerResq bean = mList.get(position);
                Intent intent = new Intent(this, AddSecondManagerActivity.class);
                intent.putExtra("managerId", bean.getId());
                intent.putExtra("phoneNumber", bean.getPhone());
                intent.putExtra("power", bean.getPower());
                startActivityForResult(intent, 1000);
            }
        });
    }

    @Override
    public void deleteSuccess() {
        toast("删除成功");
        mPresenter.getSecondAdminList();
    }

    @Override
    public void getListSuccess(List<SecondAdminManagerResq> data) {
        srLayout.setRefreshing(false);
        mAdapter.replaceData(data);
    }

    @OnClick({R.id.toolbar_back_all, R.id.tv_next})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.toolbar_back_all:
                finish();
                break;
            case R.id.tv_next:
                startActivityForResult(new Intent(this,
                        AddSecondManagerActivity.class), 1000);
                break;
            default:
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK){
            onRefresh();
        }
    }

    @Override
    public void onRefresh() {
        mPresenter.getSecondAdminList();
    }
}
