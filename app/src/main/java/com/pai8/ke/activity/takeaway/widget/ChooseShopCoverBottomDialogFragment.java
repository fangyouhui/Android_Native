package com.pai8.ke.activity.takeaway.widget;

import android.os.Bundle;

import com.lhs.library.base.BaseAppConstants;
import com.lhs.library.base.BaseBottomDialogFragment;
import com.lhs.library.base.NoViewModel;
import com.luck.picture.lib.entity.LocalMedia;
import com.luck.picture.lib.listener.OnResultCallbackListener;
import com.pai8.ke.databinding.PopChooseShopCoverBinding;
import com.pai8.ke.utils.ChoosePicUtils;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ChooseShopCoverBottomDialogFragment extends BaseBottomDialogFragment<NoViewModel, PopChooseShopCoverBinding> {
    @Override
    public void initView(@Nullable Bundle savedInstanceState) {
        mBinding.ivClose.setOnClickListener(v -> dismiss());
        mBinding.tvPicture.setOnClickListener(v -> selectPic());
        mBinding.tvVideo.setOnClickListener(v -> selectVideo());
    }

    private void selectPic() {
        OnResultCallbackListener onResultCallbackListener = new OnResultCallbackListener<LocalMedia>() {
            @Override
            public void onResult(List<LocalMedia> result) {
                String path = result.get(0).getRealPath();
                callBack(true, path);
                dismiss();
            }

            @Override
            public void onCancel() {
                dismiss();
            }
        };

        ChoosePicUtils.picSingle(getActivity(), onResultCallbackListener);
    }

    private void selectVideo() {
        ChooseShopVideoBottomDialogFragment dialogFragment = new ChooseShopVideoBottomDialogFragment();
        dialogFragment.setListener(new OnDialogListener() {
            @Override
            public void onConfirmClickListener(@NotNull Object data) {
                String videoPath = (String) data;
                callBack(false, videoPath);
                dismiss();
            }
        });

        dialogFragment.showNow(getChildFragmentManager(), "choose_video");

    }

    private void callBack(boolean isPic, String path) {
        if (mDialogListener == null) {
            return;
        }
        Map<String, String> map = new HashMap<>();
        map.put(BaseAppConstants.BundleConstant.ARG_PARAMS_0, isPic ? "1" : "2");
        map.put(BaseAppConstants.BundleConstant.ARG_PARAMS_1, path);
        mDialogListener.onConfirmClickListener(map);
    }

}
