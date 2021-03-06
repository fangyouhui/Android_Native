package com.pai8.ke.activity.takeaway.ui;

import android.os.Bundle;

import com.lhs.library.base.BaseAppConstants;
import com.lhs.library.base.BaseBottomDialogFragment;
import com.lhs.library.utils.SerializableUtil;
import com.pai8.ke.activity.takeaway.adapter.CategoryAdapter;
import com.pai8.ke.databinding.FragmentCategoryBottomDialogFragmentBinding;
import com.pai8.ke.entity.BusinessTypeResult;
import com.pai8.ke.viewmodel.CategoryViewModel;

import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class CategoryBottomDialogFragment extends BaseBottomDialogFragment<CategoryViewModel, FragmentCategoryBottomDialogFragmentBinding> {

    private CategoryAdapter adapter;

    public static CategoryBottomDialogFragment newInstance(ArrayList<String> select) {
        CategoryBottomDialogFragment fragment = new CategoryBottomDialogFragment();
        if (select != null && !select.isEmpty()) {
            Bundle bundle = new Bundle();
            bundle.putStringArrayList(BaseAppConstants.BundleConstant.ARG_PARAMS_0, select);
            fragment.setArguments(bundle);
        }
        return fragment;
    }


    @Override
    public void initView(@Nullable Bundle savedInstanceState) {
        List<BusinessTypeResult> list = SerializableUtil.readObjectForList(getContext(), "BusinessTypeData");
        adapter = new CategoryAdapter(getContext(), list);
        mBinding.recyclerView.setAdapter(adapter);
        mBinding.ivClose.setOnClickListener(v -> dismiss());
        mBinding.btnOk.setOnClickListener(v -> {
            callBack();
        });
    }

    @Override
    public void addObserve() {
        mViewModel.getVideoTypeData().observe(getViewLifecycleOwner(), data -> {
            SerializableUtil.saveSerializable(getContext(), data, "BusinessTypeData");
            if (getArguments() != null && getArguments().containsKey(BaseAppConstants.BundleConstant.ARG_PARAMS_0)) {
                ArrayList<String> select = getArguments().getStringArrayList(BaseAppConstants.BundleConstant.ARG_PARAMS_0);
                for (BusinessTypeResult datum : data) {
                    for (String s : select) {
                        if (s.equalsIgnoreCase(datum.getType_name())) {
                            datum.setSelected(true);
                        }
                    }
                }
            }
            adapter.setData(data);
        });
    }

    @Override
    public void initData() {
        mViewModel.videoType();
    }

    private void callBack() {
        List<BusinessTypeResult> list = new ArrayList<>();
        for (BusinessTypeResult datum : adapter.getData()) {
            if (datum.isSelected()) {
                list.add(datum);
            }
        }
        if (mDialogListener != null) {
            mDialogListener.onConfirmClickListener(list);
        }
        dismiss();
    }

}
