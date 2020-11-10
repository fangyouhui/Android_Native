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
import com.pai8.ke.utils.CollectionUtils;
import com.pai8.ke.utils.DateUtils;
import com.pai8.ke.utils.ImageLoadUtils;
import com.pai8.ke.widget.CircleImageView;

import java.util.List;

import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;

public class CommentAdapter extends BaseRecyclerViewAdapter<CommentResp> {

    private Click mClick;

    public void setClick(Click click) {
        mClick = click;
    }

    public CommentAdapter(Context context) {
        mContext = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(mContext).inflate(R.layout.item_comment, parent,
                false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        ViewHolder viewHolder = (ViewHolder) holder;
        CommentResp data = mDataList.get(position);
        ImageLoadUtils.loadImage(mContext, data.getAvatar(), viewHolder.civCommentAvatar,
                R.mipmap.img_avatar_def);
        viewHolder.tvCommentName.setText(data.getUser_nickname());
        viewHolder.tvParentComment.setText(data.getContent());
        viewHolder.tvDate.setText(DateUtils.formatYYYYMMDD(data.getCreate_time() + "000"));
        CommentsView commentsView = viewHolder.commentsView;
        List<Comments> son = data.getSon();
        if (CollectionUtils.isNotEmpty(son)) {
            commentsView.setVisibility(View.VISIBLE);
            commentsView.setList(son);
            commentsView.setOnItemClickListener((position1, bean) -> mClick.commentChild(data, bean));
        } else {
            commentsView.setVisibility(View.GONE);
        }
        viewHolder.itemView.setOnClickListener(view -> {
            mClick.comment(data);
        });
    }

    static class ViewHolder extends BaseViewHolder {
        @BindView(R.id.civ_comment_avatar)
        CircleImageView civCommentAvatar;
        @BindView(R.id.tv_comment_name)
        TextView tvCommentName;
        @BindView(R.id.tv_parent_comment)
        TextView tvParentComment;
        @BindView(R.id.tv_date)
        TextView tvDate;
        @BindView(R.id.tv_reply)
        TextView tvReply;
        @BindView(R.id.comments_view)
        CommentsView commentsView;

        public ViewHolder(View itemView) {
            super(itemView);
        }

    }

    public interface Click {
        void comment(CommentResp comment);

        void commentChild(CommentResp comment, Comments comments);
    }
}

