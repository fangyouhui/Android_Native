package com.pai8.ke.activity.me.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.pai8.ke.R;
import com.pai8.ke.activity.video.adapter.CommentAdapter;
import com.pai8.ke.base.BaseRecyclerViewAdapter;
import com.pai8.ke.entity.Address;
import com.pai8.ke.entity.resp.CommentResp;
import com.pai8.ke.entity.resp.Comments;

import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 地址选择Adapter
 */
public class AddressChooseAdapter extends BaseRecyclerViewAdapter<Address> {

    private int mSelectPosition = 0;
    private Click mClick;

    public void setClick(Click click) {
        mClick = click;
    }

    public void setSelectDef() {
        if (mDataList.size() > 0) {
            mSelectPosition = 0;
            notifyItemChanged(mSelectPosition);
        }
    }

    public Address getSelect() {
        if (mDataList.size() > 0) {
            return mDataList.get(mSelectPosition);
        }
        return null;
    }

    public AddressChooseAdapter(Context context) {
        this.mContext = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_address_choose, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {

        ViewHolder viewHolder = (ViewHolder) holder;
        Address address = mDataList.get(position);
        viewHolder.tvAdname.setText(address.getTitle());
        viewHolder.tvAddress.setText(address.getDistance() + " | " + address.getAddress());

        if (mSelectPosition == position) {
            viewHolder.ivCheck.setVisibility(View.VISIBLE);
        } else {
            viewHolder.ivCheck.setVisibility(View.INVISIBLE);
        }

        viewHolder.itemView.setOnClickListener(v -> {
            int lastIndex = mSelectPosition;
            mSelectPosition = position;
            notifyItemChanged(mSelectPosition);
            if (!(lastIndex < 0)) {
                notifyItemChanged(lastIndex);
            }
            mClick.click(mDataList.get(mSelectPosition));
        });
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_adname)
        TextView tvAdname;
        @BindView(R.id.tv_address)
        TextView tvAddress;
        @BindView(R.id.iv_check)
        ImageView ivCheck;

        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

    public interface Click {
        void click(Address address);
    }
}
