package com.pai8.ke.activity.takeaway.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseViewHolder;
import com.lhs.library.base.BaseRecyclerViewAdapter;
import com.pai8.ke.R;
import com.pai8.ke.activity.takeaway.entity.FoodGoodInfo;
import com.pai8.ke.activity.takeaway.entity.GoodsInfo;
import com.pai8.ke.databinding.ItemUserTakeawayOrderFoodBinding;
import com.pai8.ke.utils.ImageLoadUtils;

import java.util.List;

public class UserTakeawayOrderFoodAdapter extends BaseRecyclerViewAdapter<GoodsInfo> {


    public UserTakeawayOrderFoodAdapter(Context context, List<GoodsInfo> list) {
        super(context, list);
    }



    @Override
    protected RecyclerView.ViewHolder onCreateNormalViewHolder(ViewGroup parent, int viewType) {
        ItemUserTakeawayOrderFoodBinding binding = ItemUserTakeawayOrderFoodBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new UserTakeawayOrderFoodViewHolder(binding);
    }

    @Override
    protected void onBindNormalViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {
        if (viewHolder instanceof UserTakeawayOrderFoodViewHolder) {
            GoodsInfo bean = getItem(position);
            UserTakeawayOrderFoodViewHolder holder = (UserTakeawayOrderFoodViewHolder) viewHolder;
            holder.binding.tvName.setText(bean.getTitle());
            ImageLoadUtils.loadImage(bean.getCover().get(0), holder.binding.ivAvatar);
        }
    }


    class UserTakeawayOrderFoodViewHolder extends com.lhs.library.base.BaseViewHolder<ItemUserTakeawayOrderFoodBinding> {

        public UserTakeawayOrderFoodViewHolder(@NonNull ItemUserTakeawayOrderFoodBinding viewBinding) {
            super(viewBinding);
        }
    }
}
