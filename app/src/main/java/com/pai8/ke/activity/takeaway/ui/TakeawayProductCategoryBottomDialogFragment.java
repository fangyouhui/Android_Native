package com.pai8.ke.activity.takeaway.ui;

import android.os.Bundle;

import com.lhs.library.base.BaseAppConstants;
import com.lhs.library.base.BaseBottomDialogFragment;
import com.lhs.library.utils.SerializableUtil;
import com.pai8.ke.activity.takeaway.adapter.TakeawayProductCategoryAdapter;
import com.pai8.ke.activity.takeaway.entity.resq.ShopInfo;
import com.pai8.ke.databinding.FragmentBottomDialogTakewayProductCategoryBinding;
import com.pai8.ke.viewmodel.TakeWayProductCategoryViewModel;

import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

/**
 * 外卖产品分类
 */
public class TakeawayProductCategoryBottomDialogFragment extends BaseBottomDialogFragment<TakeWayProductCategoryViewModel, FragmentBottomDialogTakewayProductCategoryBinding> {

    private TakeawayProductCategoryAdapter adapter;

    public static TakeawayProductCategoryBottomDialogFragment newInstance(ArrayList<String> select) {
        TakeawayProductCategoryBottomDialogFragment fragment = new TakeawayProductCategoryBottomDialogFragment();
        if (select != null && !select.isEmpty()) {
            Bundle bundle = new Bundle();
            bundle.putStringArrayList(BaseAppConstants.BundleConstant.ARG_PARAMS_0, select);
            fragment.setArguments(bundle);
        }
        return fragment;
    }


    @Override
    public void initView(@Nullable Bundle savedInstanceState) {
        List<ShopInfo> data = SerializableUtil.readObjectForList(getContext(), "productCategory");
        adapter = new TakeawayProductCategoryAdapter(getContext(), data);
        mBinding.recyclerView.setAdapter(adapter);
        mBinding.ivClose.setOnClickListener(v -> dismiss());
        mBinding.btnOk.setOnClickListener(v -> callBack());
    }

    @Override
    public void addObserve() {
        mViewModel.getCategoryListData().observe(getViewLifecycleOwner(), data -> {
            SerializableUtil.saveSerializable(getContext(), data, "productCategory");
            int pos = 0;
            if (getArguments() != null && getArguments().containsKey(BaseAppConstants.BundleConstant.ARG_PARAMS_0)) {
                ArrayList<String> select = getArguments().getStringArrayList(BaseAppConstants.BundleConstant.ARG_PARAMS_0);
                for (int i = 0; i < data.size(); i++) {
                    ShopInfo datum = data.get(i);
                    for (String s : select) {
                        if (s.equalsIgnoreCase(datum.getName())) {
                            pos = i;
                        }
                    }
                }
            }

            adapter.setSelectedPosition(pos);
            adapter.setData(data);
        });
    }

    @Override
    public void initData() {
        mViewModel.categoryList();
    }

    private void callBack() {
        ShopInfo bean = adapter.getSelect();
        if (mDialogListener != null) {
            mDialogListener.onConfirmClickListener(bean);
        }
        dismiss();
    }

}
