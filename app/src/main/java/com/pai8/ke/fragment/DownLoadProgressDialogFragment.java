package com.pai8.ke.fragment;

import android.os.Bundle;

import androidx.annotation.Nullable;

import com.lhs.library.base.BaseDialogFragment;
import com.lhs.library.base.NoViewModel;
import com.pai8.ke.base.BaseEvent;
import com.pai8.ke.databinding.FragmentDialogDownloadProgressBinding;
import com.pai8.ke.entity.event.DownStatusEvent;
import com.pai8.ke.global.EventCode;
import com.pai8.ke.utils.EventBusUtils;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

public class DownLoadProgressDialogFragment extends BaseDialogFragment<NoViewModel, FragmentDialogDownloadProgressBinding> {

    @Override
    public void onCreate(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBusUtils.register(this);
    }

    @Override
    public void onStart() {
        super.onStart();
        setCancelable(false);
        //点击window外的区域，是否消失
        getDialog().setCanceledOnTouchOutside(false);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBusUtils.unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventBusCome(BaseEvent event) {
        if (event.getCode() == EventCode.EVENT_UPDATE) {
            DownStatusEvent downStatusEvent = (DownStatusEvent) event.getData();
            mBinding.progressBar.setProgress(downStatusEvent.getProgress());
        }
    }
}
