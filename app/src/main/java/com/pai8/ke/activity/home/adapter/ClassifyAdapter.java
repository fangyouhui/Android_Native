package com.pai8.ke.activity.home.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.pai8.ke.R;
import com.pai8.ke.base.BaseRecyclerViewAdapter;
import com.pai8.ke.base.BaseViewHolder;
import com.pai8.ke.utils.StringUtils;
import com.pai8.ke.utils.TextViewUtils;
import com.pai8.ke.widget.CircleImageView;
import com.pai8.ke.widget.DrawableCenterTextView;

import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;

public class ClassifyAdapter extends BaseRecyclerViewAdapter<String> {

    public ClassifyAdapter(Context context) {
        mContext = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(mContext).inflate(R.layout.item_classify, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        ViewHolder viewHolder = (ViewHolder) holder;
        String str = mDataList.get(position);
        DrawableCenterTextView tv = viewHolder.tv;
        if (StringUtils.equals("外卖", str)) {
            TextViewUtils.drawableTop(tv, R.mipmap.ic_home_waimai);
        } else if (StringUtils.equals("团购", str)) {
            TextViewUtils.drawableTop(tv, R.mipmap.ic_home_tuangou);
        }
        tv.setText(str);
    }

    static class ViewHolder extends BaseViewHolder {
        @BindView(R.id.tv)
        DrawableCenterTextView tv;

        public ViewHolder(View itemView) {
            super(itemView);
        }

    }
}
