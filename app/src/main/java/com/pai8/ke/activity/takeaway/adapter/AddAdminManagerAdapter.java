package com.pai8.ke.activity.takeaway.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.pai8.ke.R;
import com.pai8.ke.activity.takeaway.entity.resq.SecondAdminManagerResq;
import com.pai8.ke.utils.ImageLoadUtils;

import android.widget.CheckBox;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * 
 * @author Created by zzf
 * @time  22:42
 * Description：
 */
public class AddAdminManagerAdapter extends BaseQuickAdapter<SecondAdminManagerResq.PowerArrayBean, BaseViewHolder> {

    public AddAdminManagerAdapter(@Nullable List<SecondAdminManagerResq.PowerArrayBean> data) {
        super(R.layout.item_add_second_manager, data);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, SecondAdminManagerResq.PowerArrayBean item) {
        helper.setText(R.id.tv_name, item.getTitle());
        if (item.isChoose()) {
            helper.setChecked(R.id.cb_choose, true);
        } else {
            helper.setChecked(R.id.cb_choose, false);
        }
        ((CheckBox) (helper.getView(R.id.cb_choose)))
                .setOnCheckedChangeListener((buttonView, isChecked) -> item.setChoose(isChecked));
    }
}
