package com.pai8.ke.activity.takeaway.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.lhs.library.base.BaseRecyclerViewAdapter;
import com.pai8.ke.activity.takeaway.entity.resq.ShopInfo;
import com.pai8.ke.databinding.ItemOrderStatusBinding;

import java.util.List;

public class TakeawayProductCategoryAdapter extends BaseRecyclerViewAdapter<ShopInfo> {

    private int selectedPosition = -1;

    public TakeawayProductCategoryAdapter(Context context, List<ShopInfo> list) {
        super(context, list, false);
    }


    public void setSelectedPosition(int selectedPosition) {
        this.selectedPosition = selectedPosition;
    }

    public ShopInfo getSelect() {
        return getItem(selectedPosition);
    }

    //    @Override
//    protected void convert(BaseViewHolder helper, ShopInfo item) {
//
//        RelativeLayout rlStatus = helper.getView(R.id.rl_status);
//        helper.setText(R.id.tv_content, item.getName());
//        if (lastClickPosition == helper.getLayoutPosition()) {
//            rlStatus.setBackgroundResource(R.drawable.shape_store_business_radius16);
//            helper.setTextColor(R.id.tv_content, Color.parseColor("#FF7F47"));
//        } else {
//            rlStatus.setBackgroundResource(R.drawable.shape_gray_radius16);
//            helper.setTextColor(R.id.tv_content, Color.parseColor("#111111"));
//        }
//
//    }

    @Override
    protected RecyclerView.ViewHolder onCreateNormalViewHolder(ViewGroup parent, int viewType) {
        ItemOrderStatusBinding binding = ItemOrderStatusBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new TakeawayProductViewHolder(binding);
    }

    @Override
    protected void onBindNormalViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {
        if (viewHolder instanceof TakeawayProductViewHolder) {
            ShopInfo bean = getItem(position);
            TakeawayProductViewHolder holder = (TakeawayProductViewHolder) viewHolder;
            holder.binding.tvContent.setText(bean.getName());
            holder.binding.tvContent.setSelected(position == selectedPosition);
            holder.binding.tvContent.setOnClickListener(v -> {
                selectedPosition = position;
                notifyDataSetChanged();
            });

        }


    }


    class TakeawayProductViewHolder extends com.lhs.library.base.BaseViewHolder<ItemOrderStatusBinding> {

        public TakeawayProductViewHolder(@NonNull ItemOrderStatusBinding viewBinding) {
            super(viewBinding);
        }
    }
}
