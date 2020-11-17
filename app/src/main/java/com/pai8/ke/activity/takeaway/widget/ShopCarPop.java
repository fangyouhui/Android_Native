package com.pai8.ke.activity.takeaway.widget;

import android.content.Context;
import android.view.Gravity;
import android.view.View;

import com.pai8.ke.R;
import com.pai8.ke.activity.takeaway.adapter.ShopCarAdapter;
import com.pai8.ke.activity.takeaway.entity.FoodGoodInfo;

import java.util.List;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import razerdp.basepopup.BasePopupWindow;

public class ShopCarPop extends BasePopupWindow implements View.OnClickListener {


    private RecyclerView mRvShopCar;
    private ShopCarAdapter mAdapter;
    private List<FoodGoodInfo> mFoodGoodInfo;

    public ShopCarPop(Context context, List<FoodGoodInfo> goodInfoList) {
        super(context);
        setPopupGravity(Gravity.BOTTOM);
        this.mFoodGoodInfo = goodInfoList;
        setOutSideDismiss(true);
        initUI();
    }

    private void initUI() {

        findViewById(R.id.iv_close).setOnClickListener(this);
        mRvShopCar = findViewById(R.id.rv_shop_car);
        mRvShopCar.setLayoutManager(new LinearLayoutManager(getContext()));
        mAdapter = new ShopCarAdapter(mFoodGoodInfo);
        mRvShopCar.setAdapter(mAdapter);


    }

    @Override
    public View onCreateContentView() {
        View v = createPopupById(R.layout.pop_shop_car);
        return v;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.iv_close) {
            dismiss();
        }
    }


    public interface OnSelectListener {
        void onSelect(String content);
    }

    private OnSelectListener onSelectListener;

    public void setOnSelectListener(OnSelectListener listener) {
        this.onSelectListener = listener;
    }
}
