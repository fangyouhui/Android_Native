package com.pai8.ke.adapter;

import android.content.Context;
import android.text.SpannableStringBuilder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.pai8.ke.R;
import com.pai8.ke.base.BaseRecyclerViewAdapter;
import com.pai8.ke.base.BaseViewHolder;
import com.pai8.ke.utils.ImageLoadUtils;
import com.pai8.ke.utils.ResUtils;
import com.pai8.ke.utils.SpanUtils;
import com.pai8.ke.utils.StringUtils;
import com.pai8.ke.utils.TextViewUtils;
import com.pai8.ke.widget.DrawableCenterTextView;

import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;

public class ShopAdapter extends BaseRecyclerViewAdapter<String> {

    public ShopAdapter(Context context) {
        mContext = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(mContext).inflate(R.layout.item_home_shop, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        ViewHolder viewHolder = (ViewHolder) holder;
        String str = mDataList.get(position);

        ImageLoadUtils.loadImage(mContext, "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000" +
                        "&sec=1606672594588&di=a6f9197b059850fea4eb0bdd27a9b742&imgtype=0&src=http%3A%2F" +
                        "%2Fwww.szthks" +
                        ".com%2Flocalimg" +
                        "%2F687474703a2f2f6777322e616c6963646e2e636f6d2f62616f2f75706c6f616465642f69322f5431644338785866706d5858636e327366635f3132353933362e6a7067.jpg"
                , viewHolder.ivCover, 0);
        viewHolder.tvTitle.setText("小玩偶布娃娃");
        SpannableStringBuilder span = SpanUtils.getBuilder("")
                .append(mContext, " 团购价：")
                .setProportion(0.88f)
                .append(mContext, "￥99  ")
                .setBold()
                .append(mContext, "￥129")
                .setProportion(0.88f)
                .setForegroundColor(ResUtils.getColor(R.color.color_mid_font))
                .setStrikethrough()
                .create(mContext);
        viewHolder.tvPrice.setText(span);

    }

    static class ViewHolder extends BaseViewHolder {

        @BindView(R.id.iv_cover)
        ImageView ivCover;
        @BindView(R.id.tv_title)
        TextView tvTitle;
        @BindView(R.id.tv_price)
        TextView tvPrice;

        public ViewHolder(View itemView) {
            super(itemView);
        }

    }
}
