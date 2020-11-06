package com.pai8.ke.activity.video;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.pai8.ke.R;
import com.pai8.ke.entity.resp.VideoResp;
import com.pai8.ke.interfaces.OnVideoControllerListener;
import com.pai8.ke.utils.ImageLoadUtils;
import com.pai8.ke.utils.ResUtils;
import com.pai8.ke.widget.CircleImageView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class VideoControllerView extends RelativeLayout {

    @BindView(R.id.civ_avatar)
    CircleImageView civAvatar;
    @BindView(R.id.iv_focus)
    ImageView ivFocus;
    @BindView(R.id.tv_name)
    TextView tvName;
    @BindView(R.id.tv_sign)
    TextView tvSign;
    @BindView(R.id.iv_btn_close)
    ImageView ivBtnClose;
    @BindView(R.id.iv_btn_more)
    ImageView ivBtnMore;
    @BindView(R.id.iv_btn_home)
    ImageView ivBtnHome;
    @BindView(R.id.tv_contact_us)
    TextView tvContactUs;
    @BindView(R.id.tv_share)
    TextView tvShare;
    @BindView(R.id.tv_comment)
    TextView tvComment;
    @BindView(R.id.tv_like)
    TextView tvLike;
    @BindView(R.id.iv_bottom_bg)
    ImageView ivBottomBg;
    @BindView(R.id.civ_cover)
    CircleImageView civCover;
    @BindView(R.id.tv_cover_name)
    TextView tvCoverName;
    @BindView(R.id.tv_btn_go_see)
    TextView tvBtnGoSee;

    private Context mContext;
    private VideoResp mVideoEntity;
    private OnVideoControllerListener mVideoControllerListener;

    public void setVideoControllerListener(OnVideoControllerListener videoControllerListener) {
        mVideoControllerListener = videoControllerListener;
    }

    public VideoControllerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        init();
    }

    private void init() {
        View rootView = LayoutInflater.from(getContext()).inflate(R.layout.view_video_controller, this);
        ButterKnife.bind(this, rootView);
    }

    public void setVideoData(VideoResp videoData) {
        mVideoEntity = videoData;
        ImageLoadUtils.loadImage(mContext, videoData.getAvatar(), civAvatar, R.mipmap.img_avatar_def);
        tvLike.setText(videoData.getLike_counts());
        tvComment.setText(videoData.getComment_counts());

        //点赞状态
        if (videoData.getLike_status() == 1) {
            Drawable drawable = ResUtils.getDrawable(R.mipmap.ic_video_like_s);
            drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
            tvLike.setCompoundDrawables(null, drawable, null, null);
        } else {
            Drawable drawable = ResUtils.getDrawable(R.mipmap.ic_video_like_u);
            drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
            tvLike.setCompoundDrawables(null, drawable, null, null);
        }

        //关注状态
        if (videoData.getFollow_status() == 1) {
            ivFocus.setImageResource(R.mipmap.ic_video_follow_s);
        } else {
            ivFocus.setImageResource(R.mipmap.ic_video_follow_u);
        }
    }


    @OnClick({R.id.civ_avatar, R.id.iv_focus, R.id.iv_btn_close, R.id.iv_btn_more, R.id.iv_btn_home,
            R.id.tv_contact_us, R.id.tv_share, R.id.tv_comment, R.id.tv_like, R.id.civ_cover,
            R.id.tv_cover_name, R.id.tv_btn_go_see})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.civ_avatar:
                mVideoControllerListener.onAvatarClick();
                break;
            case R.id.iv_focus:
                mVideoControllerListener.onFollowClick();
                break;
            case R.id.iv_btn_close:
                mVideoControllerListener.onCloseClick();
                break;
            case R.id.iv_btn_more:
                mVideoControllerListener.onMoreClick();
                break;
            case R.id.iv_btn_home:
                mVideoControllerListener.onHomeClick();
                break;
            case R.id.tv_contact_us:
                mVideoControllerListener.onContactUsClick();
                break;
            case R.id.tv_share:
                mVideoControllerListener.onShareClick();
                break;
            case R.id.tv_comment:
                mVideoControllerListener.onCommentClick();
                break;
            case R.id.tv_like:
                mVideoControllerListener.onLikeClick();
                break;
            case R.id.civ_cover:
                break;
            case R.id.tv_cover_name:
                break;
            case R.id.tv_btn_go_see:
                mVideoControllerListener.onGoSee();
                break;
        }
    }
}
