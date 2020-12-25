package com.pai8.ke.activity.takeaway.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.pai8.ke.R;
import com.pai8.ke.activity.takeaway.entity.resq.SecondAdminManagerResq;
import com.pai8.ke.utils.ImageLoadUtils;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * 
 * @author Created by zzf
 * @time  22:42
 * Description：
 */
public class AdminManagerAuthorityAdapter extends BaseQuickAdapter<SecondAdminManagerResq.PowerArrayBean, BaseViewHolder> {
   
    public AdminManagerAuthorityAdapter(@Nullable List<SecondAdminManagerResq.PowerArrayBean> data) {
        super(R.layout.item_admin_manager_authority, data);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, SecondAdminManagerResq.PowerArrayBean item) {
        helper.setText(R.id.tv_name,item.getTitle());
        ImageLoadUtils.loadImage(mContext, item.getUrl(), helper.getView(R.id.iv_icon),
                R.mipmap.img_avatar_def);
    }
}
