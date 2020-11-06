package com.pai8.ke.activity.takeaway.ui;

import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bigkoo.pickerview.builder.TimePickerBuilder;
import com.bigkoo.pickerview.listener.OnTimeSelectListener;
import com.bigkoo.pickerview.view.TimePickerView;
import com.pai8.ke.R;
import com.pai8.ke.activity.takeaway.adapter.IncomeDetailAdapter;
import com.pai8.ke.base.BaseMvpActivity;
import com.pai8.ke.base.BasePresenter;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class IncomeDetailActivity extends BaseMvpActivity implements View.OnClickListener {

    private RecyclerView mRvIncomeDetail;
    private IncomeDetailAdapter mAdapter;
    private TimePickerView mPvTime;
    private TextView mTvTime;


    @Override
    public BasePresenter initPresenter() {
        return null;
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_income_detail;
    }

    @Override
    public void initView() {
        setImmersionBar(R.id.base_tool_bar);
        findViewById(R.id.toolbar_back_all).setOnClickListener(this);
        mRvIncomeDetail = findViewById(R.id.rv_income_detail);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        mRvIncomeDetail.setLayoutManager(layoutManager);

        mTvTime = findViewById(R.id.tv_time);
        mTvTime.setOnClickListener(this);

        List<String> list = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            list.add("1");
        }
        mAdapter = new IncomeDetailAdapter(list);
        mRvIncomeDetail.setAdapter(mAdapter);

    }


    private void timeChoose() {
        Calendar selectedDate = Calendar.getInstance();
        Calendar startDate = Calendar.getInstance();
        startDate.set(2000, 0, 1);
        Calendar endDate = Calendar.getInstance();
        endDate.set(2099, 11, 1);
        mPvTime = new TimePickerBuilder(this, new OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date, View v) {//选中事件回

            }
        })
                .setType(new boolean[]{true, true, false, false, false, false})// 默认全部显示
                .setDate(selectedDate)// 如果不设置的话，默认是系统时间*/
                .setRangDate(startDate, endDate)//起始终止年月日设定
                .setLabel("年", "月", "日", "时", "分", "秒")//默认设置为年月日时分秒
                .setDecorView((ViewGroup) findViewById(R.id.rl_income_detail))
                .setTitleText("选择月份")
                .setTitleColor(Color.parseColor("#111111"))
                .setTitleSize(16)
                .setCancelColor(Color.parseColor("#999999"))
                .setSubmitColor(Color.parseColor("#2f2f2f"))
                .build();
        mPvTime.show();

    }



    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.toolbar_back_all){
            finish();
        }else if(v.getId() == R.id.tv_time){

            timeChoose();
        }
    }
}
