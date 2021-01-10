package com.pai8.ke.activity.wallet.adapter;

import android.graphics.Color;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.pai8.ke.R;
import com.pai8.ke.activity.wallet.data.InOutRecordBean;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

public class InOutRecordAdapter extends BaseQuickAdapter<InOutRecordBean, BaseViewHolder> {

    public InOutRecordAdapter(@Nullable List<InOutRecordBean> data) {
        super(R.layout.item_record, data);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, InOutRecordBean item) {
        helper.setText(R.id.tv_type, item.getIncome_title());
        helper.setText(R.id.tv_amount, item.getMoney());
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(Long.valueOf(item.getAdd_time()));
        String time = new SimpleDateFormat("yyyy-MM-dd HH:mm").format(calendar.getTime());
        helper.setText(R.id.tv_time, time);
        if("1".equals(item.getIncome_type())) {
            helper.<TextView>getView(R.id.tv_amount).setTextColor(Color.rgb(0xFF,0x7F, 0x47));
        } else {
            helper.<TextView>getView(R.id.tv_amount).setTextColor(Color.rgb(0x2F,0x2F, 0x2F));
        }
    }
}
