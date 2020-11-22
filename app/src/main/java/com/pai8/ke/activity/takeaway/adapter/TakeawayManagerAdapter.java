package com.pai8.ke.activity.takeaway.adapter;

import android.view.View;
import android.widget.EditText;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.pai8.ke.R;
import com.pai8.ke.activity.takeaway.entity.resq.ShopInfo;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class TakeawayManagerAdapter extends BaseQuickAdapter<ShopInfo, BaseViewHolder> {

    private int mSelectedPosition;


    public void setCheckedPosition(int checkedPosition) {
        this.mSelectedPosition = checkedPosition;
        notifyDataSetChanged();
    }



    public TakeawayManagerAdapter(@Nullable List<ShopInfo> data) {
        super(R.layout.item_takeway_manager, data);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, ShopInfo item) {

        helper.setText(R.id.tv_classify,item.name);
        helper.addOnClickListener(R.id.iv_edit,R.id.iv_del);
        EditText tvClassify = helper.getView(R.id.tv_classify);
        tvClassify.setFocusable(false);
        tvClassify.setFocusableInTouchMode(false);
        tvClassify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tvClassify.setFocusable(true);
                tvClassify.setFocusableInTouchMode(true);
                tvClassify.requestFocus();
            }
        });

    }
}
