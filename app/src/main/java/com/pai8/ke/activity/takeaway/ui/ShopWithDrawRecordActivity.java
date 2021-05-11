package com.pai8.ke.activity.takeaway.ui;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;

import com.bigkoo.pickerview.builder.TimePickerBuilder;
import com.bigkoo.pickerview.listener.OnTimeSelectListener;
import com.bigkoo.pickerview.view.TimePickerView;
import com.lhs.library.base.BaseActivity;
import com.pai8.ke.databinding.ActivityShopWithDrawRecordBinding;
import com.pai8.ke.viewmodel.ShopWithDrawlViewModel;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.listener.OnRefreshLoadMoreListener;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * 商家提现记录
 */
public class ShopWithDrawRecordActivity extends BaseActivity<ShopWithDrawlViewModel, ActivityShopWithDrawRecordBinding> {

    private ShopWithDrawRecordAdapter adapter;
    private int page = 1;
    private int month = 5;

    @Override
    public void initView(@Nullable Bundle savedInstanceState) {
        mBinding.tvYearMonth.setOnClickListener(v -> showDate());
        mBinding.smartRefreshLayout.setOnRefreshLoadMoreListener(new OnRefreshLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull @NotNull RefreshLayout refreshLayout) {
                page++;
                initData();
            }

            @Override
            public void onRefresh(@NonNull @NotNull RefreshLayout refreshLayout) {
                page = 1;
                initData();
            }
        });

        mBinding.recyclerView.setAdapter(adapter = new ShopWithDrawRecordAdapter(this, null));
    }

    @Override
    public void initData() {
        mViewModel.shopCashList(page, month);
    }

    @Override
    public void addObserve() {
        mViewModel.getShopCashListData().observe(this, data -> {
            if (mBinding.smartRefreshLayout.isRefreshing()) {
                adapter.setData(data);
                mBinding.smartRefreshLayout.finishRefresh();
                return;
            }
            if (mBinding.smartRefreshLayout.isLoading()) {
                adapter.addData(data);
                mBinding.smartRefreshLayout.finishLoadMore();
                return;
            }
            adapter.setData(data);
        });
    }

    private void showDate() {
        Calendar selectedDate = Calendar.getInstance();
        Calendar startDate = Calendar.getInstance();
        startDate.set(2000, 0, 1);
        Calendar endDate = Calendar.getInstance();
        endDate.set(2099, 11, 1);
        TimePickerView startTime = new TimePickerBuilder(this, (date, v) -> {//选中事件回
            DateFormat df = new SimpleDateFormat("yyyy-MM");
            mBinding.tvYearMonth.setText(df.format(date));
            mBinding.smartRefreshLayout.autoRefresh();
        })
                .setType(new boolean[]{true, true, false, false, false, false})// 默认全部显示
                .setDate(selectedDate)// 如果不设置的话，默认是系统时间*/
                .setRangDate(startDate, endDate)//起始终止年月日设定
                .setLabel("年", "月", "日", "时", "分", "秒")//默认设置为年月日时分秒
                //    .setDecorView((ViewGroup) findViewById(R.id.tuangou_view))
                .setTitleText("选择时间")
                .setTitleColor(Color.parseColor("#111111"))
                .setTitleSize(16)
                .setCancelColor(Color.parseColor("#999999"))
                .setSubmitColor(Color.parseColor("#2f2f2f"))
                .build();
        startTime.show();
    }
}
