package com.pai8.ke.activity.takeaway.ui;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.pai8.ke.R;
import com.pai8.ke.activity.takeaway.adapter.AddAdminManagerAdapter;
import com.pai8.ke.activity.takeaway.contract.AddSecondManagerContract;
import com.pai8.ke.activity.takeaway.entity.resq.SecondAdminManagerResq;
import com.pai8.ke.activity.takeaway.presenter.AddSecondManagerPresenter;
import com.pai8.ke.base.BaseMvpActivity;
import com.pai8.ke.utils.StringUtils;

import java.util.ArrayList;
import java.util.List;

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
    @BindView(R.id.tv_send)
    TextView tvSend;
    private AddAdminManagerAdapter mAdapter;
    private List<SecondAdminManagerResq.PowerArrayBean> mList = new ArrayList<>();

    private Integer managerId;
    private String power;
    private boolean editMode = false;

    @Override
    public void initCreate(@Nullable Bundle savedInstanceState) {
        super.initCreate(savedInstanceState);
        Intent intent = getIntent();
        if (intent != null) {
            Integer managerId = intent.getIntExtra("managerId", Integer.MIN_VALUE);
            String power = intent.getStringExtra("power");
            String phoneNumber = intent.getStringExtra("phoneNumber");
            if (managerId != Integer.MIN_VALUE && power != null && phoneNumber != null) {
                editMode = true;
                this.power = power;
                this.managerId = managerId;
                etPhone.setText(phoneNumber);
                etPhone.setKeyListener(null);
                tvSend.setText("提交");
            }
        }
    }

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
        builder.deleteCharAt(builder.length() - 1);
        if (!editMode) {
//            new MaterialDialog.Builder(this)
//                    .title("温馨提示")
//                    .content("确定要将手机号为" + phone + "的用户添加为管理员吗？")
//                    .positiveText("确认")
//                    .negativeText("取消")
//                    .onPositive((dialog, which) -> {
//                        mPresenter.addSecondManager(phone, builder.toString());
//                    })
//                    .show();
        } else {
//            new MaterialDialog.Builder(this)
//                    .title("温馨提示")
//                    .content("确定要提交吗？")
//                    .positiveText("确认")
//                    .negativeText("取消")
//                    .onPositive((dialog, which) -> {
//                        mPresenter.updateSecondAdmin(managerId, 1, builder.toString());
//                    })
//                    .show();
        }
    }

    @Override
    public void getListSuccess(List<SecondAdminManagerResq.PowerArrayBean> data) {
        mList.clear();
        mList.addAll(data);
        if (editMode && this.power != null) {
            String[] selected = this.power.split(",");
            for (SecondAdminManagerResq.PowerArrayBean bean : mList) {
                for (String s : selected) {
                    if (s.equals(String.valueOf(bean.getId()))) {
                        bean.setChoose(true);
                    }
                }
            }
        }
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void addAdminSuccess() {
        toast("已发送邀请");
        setResult(RESULT_OK);
        finish();
    }

    @Override
    public void updateSuccess() {
        toast("更新成功");
        setResult(RESULT_OK);
        finish();
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_add_second_manager;
    }

    @Override
    public void initView() {
        if (editMode) {
            mTitleBar.setTitle("编辑二级管理员");
        } else {
            mTitleBar.setTitle("添加二级管理员");
        }
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
