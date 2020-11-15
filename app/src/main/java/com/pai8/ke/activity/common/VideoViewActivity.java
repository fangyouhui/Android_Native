package com.pai8.ke.activity.common;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.VideoView;

import com.pai8.ke.R;
import com.pai8.ke.base.BaseActivity;

import androidx.annotation.IntDef;
import butterknife.BindView;
import butterknife.OnClick;

/**
 * 视频预览
 * Created by gh on 2020/11/14.
 */
public class VideoViewActivity extends BaseActivity {

    public static final int TYPE_LOCAL = 1;
    public static final int TYPE_REMOTE = 2;

    @IntDef({TYPE_LOCAL, TYPE_REMOTE})
    public @interface Type {
    }

    @BindView(R.id.video_view)
    VideoView mVideoView;
    private int mType;

    public static void launch(Context context, @Type int type, String path) {
        Intent intent = new Intent(context, VideoViewActivity.class);
        Bundle bundle = new Bundle();
        intent.putExtra("type", type);
        intent.putExtra("path", path);
        intent.putExtras(bundle);
        context.startActivity(intent);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_video_view;
    }

    @Override
    public void initView() {
        setBarDark(true);
        Bundle extras = getIntent().getExtras();
        mType = extras.getInt("type");
        String path = extras.getString("path");
        if (mType == TYPE_LOCAL) {
            mVideoView.setVideoPath(path);
        } else if (mType == TYPE_REMOTE) {
            mVideoView.setVideoURI(Uri.parse(path));
        }
        mVideoView.start();
        mVideoView.setOnPreparedListener(mp -> {
            mp.setLooping(true);
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (mVideoView != null) {
            mVideoView.start();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mVideoView != null) {
            mVideoView.pause();
        }
    }

    @OnClick(R.id.iv_close)
    public void onViewClicked() {
        finish();
    }
}
