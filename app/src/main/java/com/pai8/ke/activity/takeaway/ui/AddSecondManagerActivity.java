package com.pai8.ke.activity.takeaway.ui;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.pai8.ke.R;
import com.pai8.ke.activity.me.adapter.FansAdapter;
import com.pai8.ke.activity.takeaway.adapter.AddAdminManagerAdapter;
import com.pai8.ke.activity.takeaway.adapter.SecondAdminManagerAdapter;
import com.pai8.ke.activity.takeaway.contract.AddSecondManagerContract;
import com.pai8.ke.activity.takeaway.entity.resq.SecondAdminManagerResq;
import com.pai8.ke.activity.takeaway.presenter.AddSecondManagerPresenter;
import com.pai8.ke.base.BaseMvpActivity;
import com.pai8.ke.base.BaseMvpFragment;
import com.pai8.ke.base.BasePresenter;
import com.pai8.ke.utils.StringUtils;

import android.os.Bundle;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.OnClick;

/**
 * 添加二级管理员
 *
 * @author Created by zzf
 * @time 23:44
 * Description：
 */
public class AddSecondManagerActivity extends BaseMvpActivity<AddSecondManagerPresenter> implements AddSecondManagerContract.View {

    @BindView(R.id.et_phone)
    EditText etPhone;
    @BindView(R.id.rv_limit)
    RecyclerView rvLimit;
    private AddAdminManagerAdapter mAdapter;
    private List<SecondAdminManagerResq.PowerArrayBean> mList = new ArrayList<>();

    @Override
    public AddSecondManagerPresenter initPresenter() {
        return new AddSecondManagerPresenter(this);
    }

    @OnClick(R.id.tv_send)
    public void onViewClicked() {
        String phone = etPhone.getText().toString();
        if (StringUtils.isEmpty(phone)) {
            toast("请输入手机号");
            return;
        }
        if (phone.length() != 11) {
            toast("请输入正确手机号");
            return;
        }

        StringBuilder builder = new StringBuilder();
        for (SecondAdminManagerResq.PowerArrayBean bean : mList) {
            if (bean.isChoose()) {
                builder.append(bean.getId()).append(",");
            }
        }
        if (builder.length() == 0) {
            toast("请添加权限");
            return;
        }
        new MaterialDialog.Builder(this)
                .title("温馨提示")
                .content("确定要将手机号为" + phone + "的用户添加为管理员吗？")
                .positiveText("确认")
                .negativeText("取消")
                .onPositive((dialog, which) -> {
                    mPresenter.addSecondManager(phone, builder.toString());
                })
                .show();
    }

    @Override
    public void getListSuccess(List<SecondAdminManagerResq.PowerArrayBean> data) {
        mList.clear();
        mList.addAll(data);
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void addAdminSuccess() {
        toast("已发送邀请");
        setResult(RESULT_OK);
        finish();
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_add_second_manager;
    }

    @Override
    public void initView() {
        mTitleBar.setTitle("添加二级管理员");
    }

    @Override
    public void initData() {
        super.initData();
        mAdapter = new AddAdminManagerAdapter(mList);
        rvLimit.setLayoutManager(new LinearLayoutManager(this));
        rvLimit.setHasFixedSize(true);
        rvLimit.setAdapter(mAdapter);
        mPresenter.getList();
    }
}
