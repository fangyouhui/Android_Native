package com.pai8.ke.activity.takeaway.widget;

import android.content.Intent;
import android.os.Bundle;

import com.gh.qiniushortvideo.ChooseVideo;
import com.gh.qiniushortvideo.activity.ConfigActivity;
import com.gh.qiniushortvideo.activity.MediaSelectActivity;
import com.gh.qiniushortvideo.activity.VideoRecordActivity;
import com.lhs.library.base.BaseBottomDialogFragment;
import com.lhs.library.base.NoViewModel;
import com.pai8.ke.databinding.FragmentBottomDialogChooseQnvideoBinding;
import com.pai8.ke.utils.EventBusUtils;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.jetbrains.annotations.Nullable;

public class ChooseShopVideoBottomDialogFragment extends BaseBottomDialogFragment<NoViewModel, FragmentBottomDialogChooseQnvideoBinding> {

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBusUtils.register(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBusUtils.unregister(this);
    }

    @Override
    public void initView(@Nullable Bundle savedInstanceState) {
        mBinding.ivClose.setOnClickListener(v -> dismiss());
        mBinding.tvBtnGalley.setOnClickListener(v -> selectLocalVideo());
        mBinding.tvBtnTakePhoto.setOnClickListener(v -> selectTakePhoto());
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventChooseVideo(ChooseVideo event) {
        if (event == null) return;
        String videoPath = event.getPath();
        mDialogListener.onConfirmClickListener(videoPath);
        dismiss();
    }

    private void selectLocalVideo() {
        Intent it = new Intent(getContext(), MediaSelectActivity.class);
        it.putExtra(MediaSelectActivity.TYPE, MediaSelectActivity.TYPE_VIDEO_EDIT);
        startActivity(it);
    }

    private void selectTakePhoto() {
        Intent intent = new Intent(getContext(), VideoRecordActivity.class);
        intent.putExtra(VideoRecordActivity.PREVIEW_SIZE_RATIO, ConfigActivity.PREVIEW_SIZE_RATIO_POS);
        intent.putExtra(VideoRecordActivity.PREVIEW_SIZE_LEVEL, ConfigActivity.PREVIEW_SIZE_LEVEL_POS);
        intent.putExtra(VideoRecordActivity.ENCODING_MODE, ConfigActivity.ENCODING_MODE_LEVEL_POS);
        intent.putExtra(VideoRecordActivity.ENCODING_SIZE_LEVEL, ConfigActivity.ENCODING_SIZE_LEVEL_POS);
        intent.putExtra(VideoRecordActivity.ENCODING_BITRATE_LEVEL, ConfigActivity.ENCODING_BITRATE_LEVEL_POS);
        intent.putExtra(VideoRecordActivity.AUDIO_CHANNEL_NUM, ConfigActivity.AUDIO_CHANNEL_NUM_POS);
        startActivity(intent);
    }

}
