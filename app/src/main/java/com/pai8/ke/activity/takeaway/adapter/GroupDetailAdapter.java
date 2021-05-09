package com.pai8.ke.activity.takeaway.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.pai8.ke.R;
import com.pai8.ke.utils.ImageLoadUtils;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.ButterKnife;

public class GroupDetailAdapter extends RecyclerView.Adapter<GroupDetailHolder> {


    private List<String> specailList;

    private int mSelectedPosition;
    private Context mContext = null;

    public void setList(List<String> list) {
        this.specailList = list;
        notifyDataSetChanged();
    }

    public GroupDetailAdapter(Context context) {
        this.mContext = context;
        mInflater = LayoutInflater.from(context);
    }

    public void setCheckedPosition(int checkedPosition) {
        this.mSelectedPosition = checkedPosition;
        notifyDataSetChanged();
    }

    private LayoutInflater mInflater;


    @NonNull
    @Override
    public GroupDetailHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.item_group_detail, parent, false);

        GroupDetailHolder holder = new GroupDetailHolder(view);
        return holder;

    }

    public GroupDetailAdapter.OnItemClickListener mOnItemClickListener;

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    public void setOnItemClickListener(GroupDetailAdapter.OnItemClickListener mOnItemClickLitener) {
        this.mOnItemClickListener = mOnItemClickLitener;
    }

    public static boolean isHttpUrl(String urls) {
        boolean isurl = false;
        //设置正则表达式
        String regex = "(((https|http)?://)?([a-z0-9]+[.])|(www.))"
                + "\\w+[.|\\/]([a-z0-9]{0,})?[[.]([a-z0-9]{0,})]+((/[\\S&&[^,;\u4E00-\u9FA5]]+)+)?([.][a-z0-9]{0,}+|/?)";
        //对比
        Pattern pat = Pattern.compile(regex.trim());
        Matcher mat = pat.matcher(urls.trim());
        //判断是否匹配
        isurl = mat.matches();
        if (isurl) {
            isurl = true;
        }
        return isurl;
    }


    @Override
    public void onBindViewHolder(@NonNull GroupDetailHolder holder, int position) {
        String bean = specailList.get(position);
        if (isHttpUrl(bean)) {
            ImageLoadUtils.setCircularImage(mContext, bean, holder.bannerImage, R.mipmap.icon_shop_product_detail_add);
        } else {
            holder.bannerImage.setImageResource(R.mipmap.icon_shop_product_detail_add);
        }
//        holder.bannerImage.setImageResource(R.mipmap.ic_video_follow_s);
        holder.outView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mOnItemClickListener != null) {
                    mOnItemClickListener.onItemClick(holder.bannerImage, position);
                }
            }
        });
    }

    /**
     * Returns the total number of items in the data set held by the adapter.
     *
     * @return The total number of items in this adapter.
     */
    @Override
    public int getItemCount() {
        return specailList.size();
    }
}

class GroupDetailHolder extends RecyclerView.ViewHolder {
    @BindView(R.id.OutView)
    LinearLayout outView;
    @BindView(R.id.banner_Imgage)
    ImageView bannerImage;

    public GroupDetailHolder(@NonNull View itemView) {

        super(itemView);
        ButterKnife.bind(this, itemView);
        itemView.setTag(this);
    }
}
