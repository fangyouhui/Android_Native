package com.pai8.ke.activity.takeaway.adapter;

import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.pai8.ke.R;
import com.pai8.ke.activity.takeaway.entity.resq.AddressInfo;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class DeliveryAddressAdapter extends BaseQuickAdapter<AddressInfo, BaseViewHolder> {
    public DeliveryAddressAdapter(@Nullable List<AddressInfo> data) {
        super(R.layout.item_delivery_address, data);
    }


    private int mSelectedPosition;


    public void setCheckedPosition(int checkedPosition) {
        this.mSelectedPosition = checkedPosition;
        notifyDataSetChanged();
    }



    @Override
    protected void convert(@NonNull BaseViewHolder helper, AddressInfo item) {
        helper.setText(R.id.tv_name,item.linkman+"     "+item.phone);
        helper.setText(R.id.tv_address,item.address);
        helper.addOnClickListener(R.id.tv_status);

        ImageView ivChoose = helper.getView(R.id.iv_choose);


        if(mSelectedPosition == helper.getLayoutPosition()) {
            ivChoose.setBackgroundResource(R.mipmap.ic_radio_selector);
        }else{
            ivChoose.setBackgroundResource(R.mipmap.ic_radio_normal);
        }

    }
}
