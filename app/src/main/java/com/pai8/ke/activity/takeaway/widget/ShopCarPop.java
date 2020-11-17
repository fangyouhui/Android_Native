package com.pai8.ke.activity.takeaway.widget;

import android.content.Context;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

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
    private TextView mTvShopNum;
    private TextView mTvPrice;
    private TextView mTvOrder;
    private ImageView mIvShopCar;
    private String number;
    Context context;
    public ShopCarPop(Context context,String number, List<FoodGoodInfo> goodInfoList) {
        super(context);
        this.context = context;
        setPopupGravity(Gravity.BOTTOM);
        this.mFoodGoodInfo = goodInfoList;
        this.number = number;
        setOutSideDismiss(true);
        initUI();
    }

    private void initUI() {

        findViewById(R.id.iv_close).setOnClickListener(this);
        mRvShopCar = findViewById(R.id.rv_shop_car);
        mTvOrder = findViewById(R.id.tv_order);
        mTvOrder.setOnClickListener(this);
        mIvShopCar = findViewById(R.id.iv_shop_car);
        mTvShopNum = findViewById(R.id.tv_shop_num);
        mTvPrice = findViewById(R.id.tv_price);
        mRvShopCar.setLayoutManager(new LinearLayoutManager(getContext()));
        mAdapter = new ShopCarAdapter(mFoodGoodInfo);
        mRvShopCar.setAdapter(mAdapter);
        mTvShopNum.setText(number);
        mTvShopNum.setVisibility(View.VISIBLE);
        setPrice(mFoodGoodInfo);

    }


    public void setPrice(List<FoodGoodInfo> mShopCarGoods) {
        int shopNum = 0;
        double toMoney = 0;
        boolean discount = false;
        double originalTotlMoney = 0;
        for (FoodGoodInfo pro : mShopCarGoods) {
            if(!TextUtils.isEmpty(pro.sell_price)){
                toMoney += (Double.parseDouble(pro.sell_price)*pro.num) ;
            }
            shopNum = shopNum + pro.num;
        }
        mTvPrice.setText("￥" + toMoney);
        if(shopNum<=0){
            mTvOrder.setEnabled(false);
            mTvOrder.setBackgroundResource(R.drawable.shape_orgin_gradient_gray);
        }else{
            mTvOrder.setBackgroundResource(R.drawable.shape_orgin_gradient);
            mTvOrder.setEnabled(true);

        }
        mIvShopCar.setBackground(context.getResources().getDrawable(R.mipmap.ic_shop_car));

        //只要有优惠就跳出循环
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
