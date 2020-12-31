package com.pai8.ke.activity.takeaway.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.pai8.ke.R;
import com.pai8.ke.activity.takeaway.entity.resq.SecondAdminManagerResq;
import com.pai8.ke.utils.ImageLoadUtils;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

/**
 * 
 * @author Created by zzf
 * @time  22:42
 * Description：
 */
public class SecondAdminManagerAdapter extends BaseQuickAdapter<SecondAdminManagerResq, BaseViewHolder> {
   
    public SecondAdminManagerAdapter(@Nullable List<SecondAdminManagerResq> data) {
        super(R.layout.item_second_admin_manager, data);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, SecondAdminManagerResq item) {
        helper.setText(R.id.tv_name,item.getUser_nickname());
        helper.setText(R.id.tv_time,item.getAdd_time());
        ImageLoadUtils.loadImage(mContext, item.getAvatar(), helper.getView(R.id.iv_avatar),
                R.mipmap.img_head_def);
        RecyclerView rvAuthority = helper.getView(R.id.rv_authority);
        AdminManagerAuthorityAdapter adapter = new AdminManagerAuthorityAdapter(item.getPower_array());
        GridLayoutManager layoutManager = new GridLayoutManager(mContext,4);
        rvAuthority.setLayoutManager(layoutManager);
        rvAuthority.setAdapter(adapter);
        helper.addOnClickListener(R.id.tv_del);
        helper.addOnClickListener(R.id.tv_update);
    }
}
