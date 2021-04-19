package com.pai8.ke.activity.video.tiktok;

import android.os.Bundle;

import com.blankj.utilcode.util.ClipboardUtils;
import com.blankj.utilcode.util.PhoneUtils;
import com.lhs.library.base.BaseAppConstants;
import com.lhs.library.base.BaseBottomDialogFragment;
import com.lhs.library.base.NoViewModel;
import com.pai8.ke.databinding.FragmentPhoneBottomDialogBinding;
import com.pai8.ke.utils.AppUtils;

import org.jetbrains.annotations.Nullable;

public class PhoneBottomDialogFragment extends BaseBottomDialogFragment<NoViewModel, FragmentPhoneBottomDialogBinding> {

    private String mobile;
    private String name;

    public static PhoneBottomDialogFragment newInstance(String mobile, String name) {
        PhoneBottomDialogFragment fragment = new PhoneBottomDialogFragment();
        Bundle bundle = new Bundle();
        bundle.putString(BaseAppConstants.BundleConstant.ARG_PARAMS_0, mobile);
        bundle.putString(BaseAppConstants.BundleConstant.ARG_PARAMS_1, name);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mobile = getArguments().getString(BaseAppConstants.BundleConstant.ARG_PARAMS_0, "");
        name = getArguments().getString(BaseAppConstants.BundleConstant.ARG_PARAMS_1, "");
    }

    @Override
    public void initView(@Nullable Bundle savedInstanceState) {
        mBinding.btnCall.setOnClickListener(v -> {
            PhoneUtils.dial(mobile);
            dismiss();
        });
        mBinding.btnCopy.setOnClickListener(v -> {
            ClipboardUtils.copyText(mobile);
            dismiss();
        });
        mBinding.btnAdd.setOnClickListener(v -> {
            AppUtils.intentContactAdd(getActivity(), name, "", mobile);
            dismiss();
        });
        mBinding.btnCancel.setOnClickListener(v -> dismiss());
    }
}
