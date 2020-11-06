package com.pai8.ke.activity.takeaway.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.pai8.ke.R;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class OrderAdapter extends BaseQuickAdapter<String, BaseViewHolder> {
    public OrderAdapter(@Nullable List<String> data) {
        super(R.layout.item_order, data);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, String item) {

        RecyclerView rvFood = helper.getView(R.id.rv_foods);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mContext);
        linearLayoutManager.setOrientation(RecyclerView.HORIZONTAL);
        rvFood.setLayoutManager(linearLayoutManager);
        List<String> list = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            list.add("1");
        }
        OrderFoodAdapter adapter = new OrderFoodAdapter(list);
        rvFood.setAdapter(adapter);


    }
}
