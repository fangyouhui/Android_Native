package com.pai8.ke.activity.wallet.adapter;

import android.content.Context;
import android.graphics.Color;
import android.text.TextUtils;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.pai8.ke.R;
import com.pai8.ke.activity.wallet.data.InOutRecordBean;
import com.pai8.ke.utils.StringUtils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class InOutRecordAdapter extends BaseQuickAdapter<InOutRecordBean, BaseViewHolder> {
    private Context context;


    public InOutRecordAdapter(@Nullable List<InOutRecordBean> data) {
        super(R.layout.item_record, data);
    }

    @NonNull
    @Override
    public BaseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        return super.onCreateViewHolder(parent, viewType);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, InOutRecordBean item) {
        helper.setText(R.id.tv_type, item.getIncome_title());
        helper.setText(R.id.tv_amount, item.getMoney());
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(Long.valueOf(item.getAdd_time())*1000);
        String time = new SimpleDateFormat("MM-dd HH:mm").format(calendar.getTime());
        helper.setText(R.id.tv_time, time);
        if("1".equals(item.getIncome_type())) {
            helper.<TextView>getView(R.id.tv_amount).setTextColor(context.getResources().getColor(R.color.color_FF7F47));
        } else {
            helper.<TextView>getView(R.id.tv_amount).setTextColor(context.getResources().getColor(R.color.color_2F2F2F));
        }
    }
}
