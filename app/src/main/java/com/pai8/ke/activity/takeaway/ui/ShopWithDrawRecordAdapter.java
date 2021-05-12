package com.pai8.ke.activity.takeaway.ui;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.lhs.library.base.BaseRecyclerViewAdapter;
import com.lhs.library.base.BaseViewHolder;
import com.pai8.ke.activity.wallet.data.InOutRecordBean;
import com.pai8.ke.databinding.ItemShopWithDrawRecordBinding;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class ShopWithDrawRecordAdapter extends BaseRecyclerViewAdapter<InOutRecordBean> {
    public ShopWithDrawRecordAdapter(Context context, List<InOutRecordBean> list) {
        super(context, list);
    }

    @Override
    protected RecyclerView.ViewHolder onCreateNormalViewHolder(ViewGroup parent, int viewType) {
        ItemShopWithDrawRecordBinding binding = ItemShopWithDrawRecordBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new ItemShopWithDrawRecordViewHolder(binding);
    }

    @Override
    protected void onBindNormalViewHolder(@NonNull @NotNull RecyclerView.ViewHolder viewHolder, int position) {
        if (viewHolder instanceof ItemShopWithDrawRecordViewHolder) {
            InOutRecordBean bean = getItem(position);
            ItemShopWithDrawRecordViewHolder holder = (ItemShopWithDrawRecordViewHolder) viewHolder;
            holder.binding.tvTime.setText(bean.getAdd_time());
            holder.binding.tvMoney.setText(bean.getMoney());
        }
    }

    class ItemShopWithDrawRecordViewHolder extends BaseViewHolder<ItemShopWithDrawRecordBinding> {

        public ItemShopWithDrawRecordViewHolder(@NonNull @NotNull ItemShopWithDrawRecordBinding viewBinding) {
            super(viewBinding);
        }
    }
}
