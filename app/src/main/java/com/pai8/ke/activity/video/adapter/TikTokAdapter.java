package com.pai8.ke.activity.video.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.pai8.ke.R;
import com.pai8.ke.activity.common.NaviActivity;
import com.pai8.ke.activity.video.tiktok.TikTokView;
import com.pai8.ke.base.BaseRecyclerViewAdapter;
import com.pai8.ke.entity.Shop;
import com.pai8.ke.entity.Video;
import com.pai8.ke.interfaces.OnVideoControllerListener;
import com.pai8.ke.utils.ImageLoadUtils;
import com.pai8.ke.utils.ResUtils;
import com.pai8.ke.utils.StringUtils;
import com.pai8.ke.utils.cache.PreloadManager;
import com.pai8.ke.widget.CircleImageView;

import java.util.List;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

public class TikTokAdapter extends BaseRecyclerViewAdapter<Video> {

    private OnVideoControllerListener mVideoControllerListener;

    public void setVideoControllerListener(OnVideoControllerListener videoControllerListener) {
        mVideoControllerListener = videoControllerListener;
    }

    public TikTokAdapter(Context context) {
        mContext = context;
    }


    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position, List payloads) {
        super.onBindViewHolder(holder, position, payloads);
        if (payloads.isEmpty()) {
            onBindViewHolder(holder, position);
        } else {
            if (payloads.get(0) instanceof Video) {
                ViewHolder viewHolder = (ViewHolder) holder;
                Video videoData = (Video) payloads.get(0);

                mDataList.get(position).setFollow_status(videoData.getFollow_status());
                mDataList.get(position).setComment_counts(videoData.getComment_counts());
                mDataList.get(position).setLike_counts(videoData.getLike_counts());
                mDataList.get(position).setLike_status(videoData.getLike_status());

                viewHolder.tvLike.setText(videoData.getLike_counts() + "");
                viewHolder.tvComment.setText(videoData.getComment_counts() + "");
                //点赞状态
                if (videoData.getLike_status() == 1) {
                    Drawable drawable = ResUtils.getDrawable(R.mipmap.ic_video_like_s);
                    drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
                    viewHolder.tvLike.setCompoundDrawables(null, drawable, null, null);
                } else {
                    Drawable drawable = ResUtils.getDrawable(R.mipmap.ic_video_like_u);
                    drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
                    viewHolder.tvLike.setCompoundDrawables(null, drawable, null, null);
                }

                //关注状态
                if (videoData.getFollow_status() == 1) {
                    viewHolder.ivFocus.setImageResource(R.mipmap.ic_video_follow_s);
                } else {
                    viewHolder.ivFocus.setImageResource(R.mipmap.ic_video_follow_u);
                }
            }
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(mContext).inflate(R.layout.item_tik_tok, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        ViewHolder viewHolder = (ViewHolder) holder;
        Context context = holder.itemView.getContext();
        Video videoData = mDataList.get(position);
        //开始预加载
        PreloadManager.getInstance(context).addPreloadTask(videoData.getVideo_path(), position);
        ImageLoadUtils.loadPicsFitWidth(context, videoData.getCover_path(), viewHolder.mThumb);
        if (videoData.getUser() != null) {
            ImageLoadUtils.loadImage(mContext, videoData.getUser().getAvatar(), viewHolder.civAvatar, R.mipmap.img_head_def);
            viewHolder.tvName.setText(videoData.getUser().getNickname());
        }
        viewHolder.tvLike.setText(videoData.getLike_counts() + "");
        viewHolder.tvComment.setText(videoData.getComment_counts() + "");
        viewHolder.tvSign.setText(videoData.getVideo_desc());

        Shop shop = videoData.getShop();
        if (shop != null) {
            ImageLoadUtils.loadImage(mContext, shop.getImg(), viewHolder.civCover, R.mipmap.ic_shop_def_circle);
            viewHolder.tvCoverName.setText(shop.getName());
            viewHolder.ivBottomBg.setVisibility(VISIBLE);
            viewHolder.tvCoverName.setVisibility(VISIBLE);
            viewHolder.civCover.setVisibility(VISIBLE);
            viewHolder.tvBtnGoSee.setVisibility(VISIBLE);
        } else {
            viewHolder.ivBottomBg.setVisibility(GONE);
            viewHolder.tvCoverName.setVisibility(GONE);
            viewHolder.civCover.setVisibility(GONE);
            viewHolder.tvBtnGoSee.setVisibility(GONE);
        }

        if (videoData.getJuli_state() == 1) {
            viewHolder.tvLoc.setVisibility(VISIBLE);
            if (StringUtils.isEmpty(videoData.getBusiness_district())) {
                viewHolder.tvLoc.setText(videoData.getDistance());
            } else {
                viewHolder.tvLoc.setText(videoData.getDistance() + " | " + videoData.getBusiness_district());
            }
        } else {
            viewHolder.tvLoc.setVisibility(View.INVISIBLE);
        }


        //点赞状态
        if (videoData.getLike_status() == 1) {
            Drawable drawable = ResUtils.getDrawable(R.mipmap.ic_video_like_s);
            drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
            viewHolder.tvLike.setCompoundDrawables(null, drawable, null, null);
        } else {
            Drawable drawable = ResUtils.getDrawable(R.mipmap.ic_video_like_u);
            drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
            viewHolder.tvLike.setCompoundDrawables(null, drawable, null, null);
        }

        //关注状态
        if (videoData.getFollow_status() == 1) {
            viewHolder.ivFocus.setImageResource(R.mipmap.ic_video_follow_s);
        } else {
            viewHolder.ivFocus.setImageResource(R.mipmap.ic_video_follow_u);
        }

        if (shop == null) {
            viewHolder.tvContactUs.setVisibility(GONE);
        } else {
            viewHolder.tvContactUs.setVisibility(VISIBLE);
        }

        if (StringUtils.isEmpty(videoData.getBusiness_district())) {
            viewHolder.tvClassify.setVisibility(GONE);
        } else {
            viewHolder.tvClassify.setText("去 #" + videoData.getBusiness_district());
            viewHolder.tvClassify.setVisibility(VISIBLE);
        }

        viewHolder.tvLoc.setOnClickListener(view -> {
            if (shop == null) return;
            NaviActivity.launch(mContext, videoData.getShop().getName(), videoData.getDistance(),
                    videoData.getLongitude(), videoData.getLatitude());
        });

        viewHolder.civAvatar.setOnClickListener(view -> {
            mVideoControllerListener.onAvatarClick();
        });
        viewHolder.ivFocus.setOnClickListener(view -> {
            mVideoControllerListener.onFollowClick();
        });
        viewHolder.ivBtnClose.setOnClickListener(view -> {
            mVideoControllerListener.onCloseClick();
        });
        viewHolder.ivBtnMore.setOnClickListener(view -> {
            mVideoControllerListener.onMoreClick();
        });
        viewHolder.ivBtnHome.setOnClickListener(view -> {
            mVideoControllerListener.onHomeClick();
        });
        viewHolder.tvContactUs.setOnClickListener(view -> {
            mVideoControllerListener.onContactUsClick();
        });
        viewHolder.tvShare.setOnClickListener(view -> {
            mVideoControllerListener.onShareClick();
        });
        viewHolder.tvComment.setOnClickListener(view -> {
            mVideoControllerListener.onCommentClick();
        });
        viewHolder.tvLike.setOnClickListener(view -> {
            mVideoControllerListener.onLikeClick();
        });
        viewHolder.civCover.setOnClickListener(view -> {
        });
        viewHolder.tvCoverName.setOnClickListener(view -> {
        });
        viewHolder.tvBtnGoSee.setOnClickListener(view -> {
            mVideoControllerListener.onGoSee();
        });

        viewHolder.mPosition = position;

    }

    @Override
    public void onViewDetachedFromWindow(@NonNull RecyclerView.ViewHolder holder) {
        super.onViewDetachedFromWindow(holder);
        ViewHolder viewHolder = (ViewHolder) holder;
        Video videoData = mDataList.get(viewHolder.mPosition);
        //取消预加载
        PreloadManager.getInstance(holder.itemView.getContext()).removePreloadTask(videoData.getVideo_path());
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public int mPosition;
        public ImageView mThumb;//封面图
        public TikTokView mTikTokView;
        public FrameLayout mPlayerContainer;

        public CircleImageView civAvatar;
        public ImageView ivFocus;
        public TextView tvName;
        public TextView tvSign;
        public ImageView ivBtnClose;
        public ImageView ivBtnMore;
        public ImageView ivBtnHome;
        public TextView tvContactUs;
        public TextView tvShare;
        public TextView tvComment;
        public TextView tvLike;
        public ImageView ivBottomBg;
        public CircleImageView civCover;
        public TextView tvCoverName;
        public TextView tvBtnGoSee;
        public TextView tvLoc;
        public TextView tvClassify;

        public ViewHolder(View itemView) {
            super(itemView);
            mTikTokView = itemView.findViewById(R.id.tiktok_View);
            mThumb = mTikTokView.findViewById(R.id.iv_thumb);
            civAvatar = mTikTokView.findViewById(R.id.civ_avatar);
            ivFocus = mTikTokView.findViewById(R.id.iv_focus);
            tvName = mTikTokView.findViewById(R.id.tv_name);
            tvSign = mTikTokView.findViewById(R.id.tv_sign);
            ivBtnClose = mTikTokView.findViewById(R.id.iv_btn_close);
            ivBtnMore = mTikTokView.findViewById(R.id.iv_btn_more);
            ivBtnHome = mTikTokView.findViewById(R.id.iv_btn_home);
            tvContactUs = mTikTokView.findViewById(R.id.tv_contact_us);
            tvShare = mTikTokView.findViewById(R.id.tv_share);
            tvComment = mTikTokView.findViewById(R.id.tv_comment);
            tvLike = mTikTokView.findViewById(R.id.tv_like);
            ivBottomBg = mTikTokView.findViewById(R.id.iv_bottom_bg);
            civCover = mTikTokView.findViewById(R.id.civ_cover);
            tvCoverName = mTikTokView.findViewById(R.id.tv_cover_name);
            tvBtnGoSee = mTikTokView.findViewById(R.id.tv_btn_go_see);
            tvLoc = mTikTokView.findViewById(R.id.tv_loc);
            tvClassify = mTikTokView.findViewById(R.id.tv_classify);
            mPlayerContainer = itemView.findViewById(R.id.container);
            itemView.setTag(this);
        }
    }

}