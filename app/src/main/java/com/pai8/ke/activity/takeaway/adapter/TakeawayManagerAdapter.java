package com.pai8.ke.activity.takeaway.adapter;

import android.widget.EditText;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.pai8.ke.R;
import com.pai8.ke.activity.takeaway.api.TakeawayApi;
import com.pai8.ke.activity.takeaway.entity.req.EditCategoryReq;
import com.pai8.ke.activity.takeaway.entity.resq.ShopInfo;
import com.pai8.ke.base.retrofit.BaseObserver;
import com.pai8.ke.base.retrofit.RxSchedulers;

import java.util.List;

public class TakeawayManagerAdapter extends BaseQuickAdapter<ShopInfo, BaseViewHolder> {

    private int mSelectedPosition;


    public void setCheckedPosition(int checkedPosition) {
        this.mSelectedPosition = checkedPosition;
        notifyDataSetChanged();
    }


    public TakeawayManagerAdapter(@Nullable List<ShopInfo> data) {
        super(R.layout.item_takeway_manager, data);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, ShopInfo item) {
        helper.setText(R.id.tv_classify, item.name);
        helper.addOnClickListener(R.id.iv_del);
        ImageView ivEdit = helper.getView(R.id.iv_edit);
        EditText tvClassify = helper.getView(R.id.tv_classify);
        ivEdit.setOnClickListener(v -> {
            tvClassify.setFocusable(true);
            tvClassify.setFocusableInTouchMode(true);
            tvClassify.requestFocus();
        });
        tvClassify.setOnFocusChangeListener((v, hasFocus) -> {
            if (!hasFocus) {
                item.setName(tvClassify.getText().toString());
                editCategory(item.getId(), item.getName());
            }
        });

//        EditText tvClassify = helper.getView(R.id.tv_classify);
//        tvClassify.setFocusable(false);
//        tvClassify.setFocusableInTouchMode(false);
//        tvClassify.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                tvClassify.setFocusable(true);
//                tvClassify.setFocusableInTouchMode(true);
//                tvClassify.requestFocus();
//            }
//        });

    }

    /**
     * 编辑外卖分类
     */
    private void editCategory(int categoryId, String name) {
        EditCategoryReq upCategoryReq = new EditCategoryReq();
        upCategoryReq.id = categoryId + "";
        upCategoryReq.name = name;
        TakeawayApi.getInstance().categoryEdit(upCategoryReq)
                .doOnSubscribe(disposable -> {
                })
                .compose(RxSchedulers.io_main())
                .subscribe(new BaseObserver<String>() {
                    @Override
                    protected void onSuccess(String data) {

                    }

                    @Override
                    protected void onError(String msg, int errorCode) {
                        super.onError(msg, errorCode);
                    }
                });

    }
}
