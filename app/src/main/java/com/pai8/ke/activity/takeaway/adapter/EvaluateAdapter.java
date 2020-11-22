package com.pai8.ke.activity.takeaway.adapter;


import android.widget.RatingBar;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.pai8.ke.R;
import com.pai8.ke.activity.takeaway.entity.resq.CommentInfo;
import com.pai8.ke.utils.ImageLoadUtils;

import java.util.List;

import androidx.annotation.Nullable;

/**
 * 商品评价
 */
public class EvaluateAdapter extends BaseQuickAdapter<CommentInfo, BaseViewHolder> {
    public EvaluateAdapter(@Nullable List<CommentInfo> data) {
        super(R.layout.item_evaluate, data);
    }
    @Override
    protected void convert(BaseViewHolder helper, CommentInfo item) {

        ImageLoadUtils.loadRoundImage(mContext,item.avatar,helper.getView(R.id.item_comment_iv_user)
        ,R.mipmap.img_avatar_def_me);

        helper.setText(R.id.item_comment_tv_name,item.user_nickname);


        RatingBar ratingBar = helper.getView(R.id.item_comment_rb_seller);
        ratingBar.setNumStars((int) item.score);

        ImageLoadUtils.setCircularImage(mContext,item.image,helper.getView(R.id.rv_comment_picture)
        ,R.mipmap.ic_launcher);

        helper.setText(R.id.item_comment_tv_time,item.create_time);

        helper.setText(R.id.tv_seller_comment_content,item.content);


    }
}