package com.pai8.ke.activity.common;

import android.os.Bundle;

import com.lhs.library.base.BaseAppConstants;
import com.lhs.library.base.BaseBottomDialogFragment;
import com.pai8.ke.databinding.FragmentShareBottomDialogBinding;
import com.pai8.ke.entity.resp.ShareMiniResp;
import com.pai8.ke.utils.ToastUtils;
import com.pai8.ke.utils.WxShareUtils;
import com.pai8.ke.viewmodel.ShareViewModel;

import org.jetbrains.annotations.Nullable;

import java.util.HashMap;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;

/**
 * 分享底部弹窗
 */
public class ShareBottomDialogFragment extends BaseBottomDialogFragment<ShareViewModel, FragmentShareBottomDialogBinding> {

    private int type; //1:视频  2:店铺  3:商品
    private String id;
    private int shareType = 1;//1:朋友  2:朋友圈

    public static ShareBottomDialogFragment newInstance(int type, String id) {
        ShareBottomDialogFragment fragment = new ShareBottomDialogFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(BaseAppConstants.BundleConstant.ARG_PARAMS_0, type);
        bundle.putString(BaseAppConstants.BundleConstant.ARG_PARAMS_1, id);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        type = getArguments().getInt(BaseAppConstants.BundleConstant.ARG_PARAMS_0, 1);
        id = getArguments().getString(BaseAppConstants.BundleConstant.ARG_PARAMS_1);
    }

    @Override
    public void initView(@Nullable Bundle savedInstanceState) {
        mBinding.btnCancel.setOnClickListener(v -> dismiss());
        mBinding.tvBtnWechatFriend.setOnClickListener(v -> {
            shareType = 1;
            mViewModel.shareMini(type, id);
        });
        mBinding.tvBtnWechatMoments.setOnClickListener(v -> {
            shareType = 2;
            mViewModel.shareMini(type, id);
        });
    }

    @Override
    public void addObserve() {
        mViewModel.getShareMiniData().observe(this, data -> shareMini(data));
    }

    private void shareMini(ShareMiniResp resp) {
        PlatformActionListener platformActionListener = new PlatformActionListener() {
            @Override
            public void onComplete(Platform platform, int i, HashMap<String, Object> hashMap) {
                dismiss();
            }

            @Override
            public void onError(Platform platform, int i, Throwable throwable) {
                ToastUtils.showShort("分享失败");
            }

            @Override
            public void onCancel(Platform platform, int i) {

            }
        };

        if (shareType == 1) {
            WxShareUtils.shareToWeChat(resp, platformActionListener);
        } else {
            WxShareUtils.shareToWeChatMoments(resp, platformActionListener);
        }

    }


}
