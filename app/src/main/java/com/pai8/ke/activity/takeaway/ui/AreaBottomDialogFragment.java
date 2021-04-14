package com.pai8.ke.activity.takeaway.ui;

import com.lhs.library.base.BaseBottomDialogFragment;
import com.pai8.ke.databinding.FragmentAreaBottomDialogFragmentBinding;
import com.pai8.ke.viewmodel.AreaViewModel;

public class AreaBottomDialogFragment extends BaseBottomDialogFragment<AreaViewModel, FragmentAreaBottomDialogFragmentBinding> {
    
    public static AreaBottomDialogFragment newInstance() {
        AreaBottomDialogFragment fragment = new AreaBottomDialogFragment();
        return fragment;
    }


    @Override
    public void addObserve() {
        mViewModel.getAreaData().observe(getViewLifecycleOwner(), data -> {


        });
    }

    @Override
    public void initData() {
        mViewModel.getArea();
    }

    private void callBack() {


    }

}
