package com.pai8.ke.activity.takeaway.ui;

import android.content.Intent;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.pai8.ke.R;
import com.pai8.ke.activity.takeaway.adapter.DeliveryAddressAdapter;
import com.pai8.ke.activity.takeaway.contract.DeliveryContract;
import com.pai8.ke.activity.takeaway.entity.resq.AddressInfo;
import com.pai8.ke.activity.takeaway.presenter.DeliveryPresenter;
import com.pai8.ke.activity.takeaway.widget.AddAddressPop;
import com.pai8.ke.base.BaseMvpActivity;
import com.pai8.ke.utils.ToastUtils;

import java.util.List;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class DeliveryAddressActivity extends BaseMvpActivity<DeliveryPresenter> implements View.OnClickListener, DeliveryContract.View {


    private RecyclerView mRvAddress;
    private DeliveryAddressAdapter mAdapter;
    private int mId;
    private int mType;


    @Override
    public int getLayoutId() {
        return R.layout.activity_delivery_address;
    }

    @Override
    public void initView() {
        setImmersionBar(R.id.base_tool_bar);
        findViewById(R.id.toolbar_back_all).setOnClickListener(this);
        mRvAddress = findViewById(R.id.rv_address);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        mRvAddress.setLayoutManager(layoutManager);
        findViewById(R.id.tv_add_address).setOnClickListener(this);

    }


    @Override
    public void initData() {
        super.initData();

        mId = getIntent().getIntExtra("id", 0);
        mType = getIntent().getIntExtra("TYPE", 0);
        mAdapter = new DeliveryAddressAdapter(null,mType);
        mRvAddress.setAdapter(mAdapter);

        mAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                if (view.getId() == R.id.tv_status) {
                    AddAddressPop pop = new AddAddressPop(DeliveryAddressActivity.this, mAdapter.getData().get(position));
                    pop.setOnSelectListener(new AddAddressPop.OnSelectListener() {

                        @Override
                        public void onSelect(String name, String phone, String address, String number, String lat, String lon) {
                            mPresenter.editAddress(mAdapter.getData().get(position).id + "", name, phone, address, number, lat, lon);
                        }

                        @Override
                        public void delete(int id) {
                            mPresenter.deleteAddress(id, position);
                        }
                    });

                    pop.showPopupWindow();
                }
            }
        });
        mAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                if (mType == 0) {
                    return;
                }else if (mType == 2){
                    Intent intent = new Intent();
                    intent.putExtra("lat", mAdapter.getData().get(position).latitude);
                    intent.putExtra("lng", mAdapter.getData().get(position).longitude);
                    intent.putExtra("address", mAdapter.getData().get(position).address);
                    intent.putExtra("id", mAdapter.getData().get(position).id);
                    setResult(RESULT_OK, intent);
                    finish();
                    return;
                }
                Intent intent = new Intent();
                intent.putExtra("name", mAdapter.getData().get(position).linkman);
                intent.putExtra("phone", mAdapter.getData().get(position).phone);
                intent.putExtra("address", mAdapter.getData().get(position).address);
                intent.putExtra("id", mAdapter.getData().get(position).id);
                setResult(RESULT_OK, intent);
                finish();
            }
        });

        mPresenter.getAddress();

    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.toolbar_back_all) {
            finish();
        } else if (v.getId() == R.id.tv_add_address) {
            AddAddressPop pop = new AddAddressPop(this, null);
            pop.setOnSelectListener(new AddAddressPop.OnSelectListener() {
                @Override
                public void onSelect(String name, String phone, String address, String number, String lat, String lon) {
                    mPresenter.upAddress(name, phone, address, number, lat, lon);
                }

                @Override
                public void delete(int id) {

                }
            });

            pop.showPopupWindow();


        }
    }

    @Override
    public DeliveryPresenter initPresenter() {
        return new DeliveryPresenter(this);
    }

    @Override
    public void getAddressSuccess(List<AddressInfo> data) {
        mAdapter.setNewData(data);
        for (int i = 0; i < data.size(); i++) {
            if (mId == data.get(i).id) {
                mAdapter.setCheckedPosition(i);
            }

        }

    }

    @Override
    public void addAddressSuccess(String msg) {
        ToastUtils.showShort("添加成功");
        mPresenter.getAddress();
    }

    @Override
    public void editAddressSuccess(String msg) {
        ToastUtils.showShort("更新成功");
        mPresenter.getAddress();
    }

    @Override
    public void deleteAddressSuccess(String msg, int position) {
        ToastUtils.showShort("删除成功");

        mAdapter.remove(position);


    }
}

