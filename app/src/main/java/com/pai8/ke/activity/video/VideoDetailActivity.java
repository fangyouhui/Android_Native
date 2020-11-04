package com.pai8.ke.activity.video;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.gyf.immersionbar.ImmersionBar;
import com.pai8.ke.R;
import com.pai8.ke.activity.video.adapter.VideoDetailAdapter;
import com.pai8.ke.app.MyApp;
import com.pai8.ke.base.BaseActivity;
import com.pai8.ke.interfaces.OnVideoControllerListener;
import com.pai8.ke.interfaces.OnViewPagerListener;
import com.pai8.ke.manager.ViewPagerLayoutManager;
import com.pai8.ke.global.MockData;
import com.pai8.ke.widget.FullScreenVideoView;
import com.pai8.ke.widget.LikeView;

import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import butterknife.BindView;

/**
 * 视频详情
 * Created by gh on 2020/11/3.
 */
public class VideoDetailActivity extends BaseActivity {

    @BindView(R.id.rv)
    RecyclerView mLuRv;
    @BindView(R.id.sr_layout)
    SwipeRefreshLayout mRefreshLayout;

    private ImageView mIvCurCover;
    private ImageView mIvPlay;
    private FullScreenVideoView mVideoView;

    private VideoDetailAdapter mVideoAdapter;
    private ViewPagerLayoutManager mViewPagerLayoutManager;

    private int mCurPlayPos = -1;

    @Override
    public int getLayoutId() {
        return R.layout.activity_video_detail;
    }

    @Override
    public void initView() {
        //透明状态栏，字体深色
        ImmersionBar.with(this)
                .transparentStatusBar()
                .statusBarDarkFont(true)
                .init();

        mVideoView = new FullScreenVideoView(this);

        mVideoAdapter = new VideoDetailAdapter(this);
        mLuRv.setAdapter(mVideoAdapter);
        setViewPagerLayoutManager();

        mVideoAdapter.setDataList(MockData.getVideoData());
    }

    @Override
    public void onPause() {
        super.onPause();
        mVideoView.pause();
    }

    @Override
    public void onStop() {
        super.onStop();
        mVideoView.stopPlayback();
    }

    private void setViewPagerLayoutManager() {
        mViewPagerLayoutManager = new ViewPagerLayoutManager(this);
        mLuRv.setLayoutManager(mViewPagerLayoutManager);
        mLuRv.scrollToPosition(0);
        mViewPagerLayoutManager.setOnViewPagerListener(new OnViewPagerListener() {
            @Override
            public void onInitComplete() {
                playVideo(0);
            }

            @Override
            public void onPageRelease(boolean isNext, int position) {
                if (mIvCurCover != null) {
                    mIvCurCover.setVisibility(View.VISIBLE);
                }
                if (mIvPlay != null) {
                    mIvPlay.setVisibility(View.GONE);
                }
            }

            @Override
            public void onPageSelected(int position, boolean isBottom) {
                playVideo(position);
            }
        });
    }

    private void playVideo(int position) {
        if (position == mCurPlayPos) return;

        View itemView = mViewPagerLayoutManager.findViewByPosition(position);

        if (itemView == null) return;

        ViewGroup rootView = itemView.findViewById(R.id.rl_container);
        VideoControllerView videoControllerView = rootView.findViewById(R.id.video_controller_view);
        LikeView likeView = rootView.findViewById(R.id.like_view);
        mIvPlay = rootView.findViewById(R.id.iv_play);
        ImageView ivCover = rootView.findViewById(R.id.iv_cover);
        mIvPlay.setAlpha(0.4f);

        onVideoControllerClick(videoControllerView);

        likeView.setOnPlayPauseListener(() -> {
            if (mVideoView.isPlaying()) {
                mVideoView.pause();
                mIvPlay.setVisibility(View.VISIBLE);
            } else {
                mVideoView.start();
                mIvPlay.setVisibility(View.GONE);
            }
        });

        mCurPlayPos = position;

        // 添加VideoView到当前需要播放的item中，添加进item之前，保证VideoView没有父view。
        ViewGroup parent = (ViewGroup) mVideoView.getParent();
        if (parent != null) {
            parent.removeView(mVideoView);
        }
        rootView.addView(mVideoView, 0);

        // VideoView设置并播放
        String videoPath =
                "android.resource://" + getPackageName() + "/" + mVideoAdapter.getDataList().get(position).getVideoRes();
        mVideoView.setVideoPath(videoPath);
//        mVideoView.setVideoURI(Uri.parse(""));
        mVideoView.start();
        mVideoView.setOnPreparedListener(mp -> {
            mp.setLooping(true);
            // 延迟取消封面，避免加载视频黑屏
            MyApp.getMyAppHandler().postDelayed(() -> {
                ivCover.setVisibility(View.GONE);
                mIvCurCover = ivCover;
            }, 150);
        });
    }

    /**
     * 视频外部控制器点击事件
     *
     * @param videoControllerView
     */
    private void onVideoControllerClick(VideoControllerView videoControllerView) {
        videoControllerView.setVideoControllerListener(new OnVideoControllerListener() {
            @Override
            public void onAvatarClick() {

            }

            @Override
            public void onFollowClick() {

            }

            @Override
            public void onHomeClick() {

            }

            @Override
            public void onCloseClick() {

            }

            @Override
            public void onLikeClick() {

            }

            @Override
            public void onCommentClick() {

            }

            @Override
            public void onShareClick() {

            }

            @Override
            public void onContactUsClick() {

            }

            @Override
            public void onGoSee() {

            }
        });
    }

}
