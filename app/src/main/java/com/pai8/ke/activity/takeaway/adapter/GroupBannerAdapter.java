package com.pai8.ke.activity.takeaway.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.entity.LocalMedia;
import com.pai8.ke.R;
import com.pai8.ke.activity.takeaway.entity.resq.smallGoodsInfo;
import com.pai8.ke.activity.takeaway.ui.AddGroupGoodActivity;
import com.pai8.ke.utils.ImageLoadUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class GroupBannerAdapter extends RecyclerView.Adapter<GroupBannerHolder> {

    private List<String> specailList;

    private int mSelectedPosition;
    private Context mContext=null;

    public void setList(List<String> list) {
        this.specailList = list;
        notifyDataSetChanged();
    }

    public GroupBannerAdapter(Context context) {
        this.mContext=context;
        mInflater = LayoutInflater.from(context);
    }

    public void setCheckedPosition(int checkedPosition) {
        this.mSelectedPosition = checkedPosition;
        notifyDataSetChanged();
    }

    private LayoutInflater mInflater;


   
    @NonNull
    @Override
    public GroupBannerHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.item_group_img, parent, false);

        GroupBannerHolder holder = new GroupBannerHolder(view);
        return holder;

    }
    public OnItemClickListener mOnItemClickListener;

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    public void setOnItemClickListener(OnItemClickListener mOnItemClickLitener) {
        this.mOnItemClickListener = mOnItemClickLitener;
    }

  
    @Override
    public void onBindViewHolder(@NonNull GroupBannerHolder holder, int position) {
        String bean = specailList.get(position);
        if (bean.indexOf("http")==1){
            ImageLoadUtils.setCircularImage(mContext, bean, holder.bannerImage, R.mipmap.img_share_cover);
        }
        else{
            holder.bannerImage.setImageResource(R.mipmap.img_share_cover);
        }
//        holder.bannerImage.setImageResource(R.mipmap.ic_video_follow_s);
        holder.outView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mOnItemClickListener!=null){
                    mOnItemClickListener.onItemClick(holder.bannerImage,position);
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
class GroupBannerHolder extends RecyclerView.ViewHolder{
    @BindView(R.id.OutView)
    LinearLayout outView;
    @BindView(R.id.banner_Imgage)
    ImageView bannerImage;
    public GroupBannerHolder(@NonNull View itemView) {

        super(itemView);
        ButterKnife.bind(this,itemView);
        itemView.setTag(this);
    }
}
