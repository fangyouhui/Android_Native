package com.pai8.ke.activity.wallet;

import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.widget.AppCompatSpinner;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.hjq.bar.OnTitleBarListener;
import com.pai8.ke.R;
import com.pai8.ke.activity.takeaway.api.TakeawayApi;
import com.pai8.ke.activity.wallet.adapter.InOutRecordAdapter;
import com.pai8.ke.activity.wallet.data.InOutRecordBean;
import com.pai8.ke.activity.wallet.data.InOutRecordRequest;
import com.pai8.ke.activity.wallet.data.InOutRecordResp;
import com.pai8.ke.base.BaseActivity;
import com.pai8.ke.base.retrofit.BaseObserver;
import com.pai8.ke.base.retrofit.RxSchedulers;
import com.pai8.ke.global.GlobalConstants;
import com.pai8.ke.utils.ToastUtils;
import com.pai8.ke.widget.BottomDialog;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import butterknife.BindView;

/**
 * Created by atian
 * on 1/11/21
 * describe:提现记录
 */
public class OutRecordActivity extends BaseActivity implements SwipeRefreshLayout.OnRefreshListener, BaseQuickAdapter.RequestLoadMoreListener {

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
        mTitleBar.setTitle("提现记录");
        tvBalance.setVisibility(View.GONE);
        tvTotalAmount.setVisibility(View.GONE);
        srLayout.setOnRefreshListener(this);
        srLayout.setColorSchemeResources(R.color.colorPrimary);
        mAdapter = new InOutRecordAdapter(mList);
        recycler_view.setLayoutManager(new LinearLayoutManager(this));
        recycler_view.setHasFixedSize(true);
        mAdapter.setEnableLoadMore(true);
        mAdapter.setOnLoadMoreListener(this, recycler_view);
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
                OutRecordActivity.this.month = month;
                onRefresh();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }


    @Override
    public void initData() {
        getData(month, page);
    }

    @Override
    public void onRefresh() {
        page = 1;
        getData(month, page);
    }

    @Override
    public void onLoadMoreRequested() {
        page++;
        getData(month, page);
    }

    private void getData(String month, int page) {
        InOutRecordRequest request = new InOutRecordRequest();
        request.setMonth(month);
        request.setPage(page);
        request.setPage_count(20);
        TakeawayApi.getInstance().outRecord(request)
                .doOnSubscribe(disposable -> {
                })
                .compose(RxSchedulers.io_main())
                .subscribe(new BaseObserver<InOutRecordResp>() {
                    @Override
                    protected void onSuccess(InOutRecordResp data) {
                        if (page == 1) {
                            srLayout.setRefreshing(false);
                        } else {
                            mAdapter.loadMoreComplete();
                        }
                        if (data != null && data.getList() != null) {
                            if (data != null && data.getList() != null) {
                                if (page == 1) {
                                    mAdapter.replaceData(data.getList());
                                } else {
                                    mAdapter.addData(data.getList());
                                }
                                if (data.getList().size() < GlobalConstants.SIZE) {
                                    mAdapter.loadMoreEnd();
                                } else {
                                    mAdapter.loadMoreComplete();
                                }
                            }
                        } else {
                            ToastUtils.showShort("数据异常");
                        }
                    }

                    @Override
                    protected void onError(String msg, int errorCode) {
                        super.onError(msg, errorCode);
                        if (page == 1) {
                            srLayout.setRefreshing(false);
                        } else {
                            mAdapter.loadMoreComplete();
                        }
                    }
                });
    }
}