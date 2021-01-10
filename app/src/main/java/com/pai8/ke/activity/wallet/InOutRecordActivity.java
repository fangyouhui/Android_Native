package com.pai8.ke.activity.wallet;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatSpinner;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.hjq.bar.OnTitleBarListener;
import com.pai8.ke.R;

import com.pai8.ke.activity.me.adapter.FansAdapter;
import com.pai8.ke.activity.takeaway.api.TakeawayApi;
import com.pai8.ke.activity.wallet.adapter.InOutRecordAdapter;
import com.pai8.ke.activity.wallet.contract.InOutRecordContract;
import com.pai8.ke.activity.wallet.data.InOutRecordBean;
import com.pai8.ke.activity.wallet.data.InOutRecordRequest;
import com.pai8.ke.activity.wallet.data.InOutRecordResp;
import com.pai8.ke.activity.wallet.presenter.InOutRecordPresenter;
import com.pai8.ke.base.BaseActivity;
import com.pai8.ke.base.BaseMvpActivity;
import com.pai8.ke.global.GlobalConstants;
import com.pai8.ke.widget.BottomDialog;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import butterknife.BindView;

public class InOutRecordActivity extends BaseMvpActivity<InOutRecordPresenter> implements InOutRecordContract.View, SwipeRefreshLayout.OnRefreshListener, BaseQuickAdapter.RequestLoadMoreListener {

    @BindView(R.id.recycler_view)
    RecyclerView recycler_view;
    @BindView(R.id.sr_layout)
    SwipeRefreshLayout srLayout;
    @BindView(R.id.tv_balance)
    TextView tvBalance;
    @BindView(R.id.tv_total_amount)
    TextView tvTotalAmount;
    @BindView(R.id.sp_month)
    AppCompatSpinner spMonth;
    InOutRecordAdapter mAdapter;

    BottomDialog mDatePickerDialog;
    private List<InOutRecordBean> mList = new ArrayList<>();
    private int page = 1;
    private String month = "";

    @Override
    public int getLayoutId() {
        return R.layout.activity_in_out_record;
    }

    @Override
    public void initView() {
        mTitleBar.setTitle("收支记录");
        srLayout.setOnRefreshListener(this);
        srLayout.setColorSchemeResources(R.color.colorPrimary);
        mAdapter = new InOutRecordAdapter(mList);
        recycler_view.setLayoutManager(new LinearLayoutManager(this));
        recycler_view.setHasFixedSize(true);
        mAdapter.setEnableLoadMore(true);
        mAdapter.setOnLoadMoreListener(this,recycler_view);
        mAdapter.setEmptyView(R.layout.layout_empty_view, new LinearLayout(this));
        recycler_view.setAdapter(mAdapter);
        Calendar calendar = Calendar.getInstance();
        List<String> list = new ArrayList<>();
        while (calendar.get(Calendar.YEAR) > 2010) {
            list.add(calendar.get(Calendar.YEAR) + "-" + (calendar.get(Calendar.MONTH) + 1));
            calendar.add(Calendar.MONTH, -1);
        }
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, R.layout.item_month, list);
        spMonth.setAdapter(arrayAdapter);
    }

    @Override
    public void initListener() {
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

        spMonth.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String month = spMonth.getAdapter().getItem(position).toString();
                InOutRecordActivity.this.month = month;
                onRefresh();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

//        tvMonth.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//            }
//        });
//        mAdapter.setOnItemChildClickListener((adapter, view, position) -> {
//            if (mList.get(position).getIs_follow()== 1) {
//                showOperateDialog(true, mList.get(position).getUser().getId() + "");
//            } else {
//                showOperateDialog(false, mList.get(position).getUser().getId() + "");
//            }
//        });
    }

    @Override
    public void initData() {
        mPresenter.getInOutRecord(month, page);
    }

    @Override
    public InOutRecordPresenter initPresenter() {
        return new InOutRecordPresenter(this);
    }

    @Override
    public void onRefresh() {
        page = 1;
        mPresenter.getInOutRecord(month, page);

    }

    @Override
    public void onLoadMoreRequested() {
        page++;
        mPresenter.getInOutRecord(month, page);
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
    public void getInOutRecordSuccess(InOutRecordResp data) {
        if (data != null && data.getList() != null) {
            if (page == 1) {
                mAdapter.replaceData(data.getList());
            } else {
                mAdapter.addData(data.getList());
            }
            if (data.getList().size() < GlobalConstants.SIZE) {
                mAdapter.loadMoreEnd();
            }else {
                mAdapter.loadMoreComplete();
            }
        }
        tvBalance.setText("收益余额：￥" + data.getBalance());
        tvTotalAmount.setText("本月累计收益：￥" + data.getLast_money());
    }
}