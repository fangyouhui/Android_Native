package com.pai8.ke.activity.video.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.pai8.ke.R;
import com.pai8.ke.activity.video.CommentsView;
import com.pai8.ke.base.BaseRecyclerViewAdapter;
import com.pai8.ke.base.BaseViewHolder;
import com.pai8.ke.entity.resp.CommentResp;
import com.pai8.ke.entity.resp.Comments;
import com.pai8.ke.entity.resp.ShopList;
import com.pai8.ke.entity.resp.ShopListResp;
import com.pai8.ke.utils.CollectionUtils;
import com.pai8.ke.utils.DateUtils;
import com.pai8.ke.utils.ImageLoadUtils;
import com.pai8.ke.widget.CircleImageView;

import java.util.List;

import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;

public class ShopSearchListAdapter extends BaseRecyclerViewAdapter<ShopList> {

    private Click mClick;

    public void setClick(Click click) {
        mClick = click;
    }

    public ShopSearchListAdapter(Context context) {
        mContext = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(mContext).inflate(R.layout.item_shop_search_list, parent,
                false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        ViewHolder viewHolder = (ViewHolder) holder;
        ShopList data = mDataList.get(position);
        ImageLoadUtils.loadImage(mContext, data.getShop_img(), viewHolder.civCover,
                R.mipmap.ic_shop_def_react);
        viewHolder.tvShopName.setText(data.getShop_name());
        viewHolder.tvShopAddress.setText(data.getShop_desc());
    }

    static class ViewHolder extends BaseViewHolder {
        @BindView(R.id.civ_cover)
        CircleImageView civCover;
        @BindView(R.id.tv_shop_name)
        TextView tvShopName;
        @BindView(R.id.tv_shop_address)
        TextView tvShopAddress;

        public ViewHolder(View itemView) {
            super(itemView);
        }

    }

    public interface Click {
        void data(ShopListResp resp);
    }
}

