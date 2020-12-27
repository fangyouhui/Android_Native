package com.pai8.ke.activity.takeaway.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.pai8.ke.R;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class SecondAdminManagerAdapter extends BaseQuickAdapter<String, BaseViewHolder> {
    public SecondAdminManagerAdapter(@Nullable List<String> data) {
        super(R.layout.item_second_admin_manager, data);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, String item) {

        RecyclerView rvAuthority = helper.getView(R.id.rv_authority);
        GridLayoutManager layoutManager = new GridLayoutManager(mContext,4);
        rvAuthority.setLayoutManager(layoutManager);
        List<String> list = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            list.add("1");
        }
        AdminManagerAuthorityAdapter adapter = new AdminManagerAuthorityAdapter(list);
        rvAuthority.setAdapter(adapter);
    }
}
