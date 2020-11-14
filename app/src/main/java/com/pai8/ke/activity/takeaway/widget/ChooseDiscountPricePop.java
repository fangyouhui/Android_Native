package com.pai8.ke.activity.takeaway.widget;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.pai8.ke.R;

import razerdp.basepopup.BasePopupWindow;

public class ChooseDiscountPricePop extends BasePopupWindow implements View.OnClickListener {


    private ImageView mIvNoDiscount,mIvDiscount;
    private EditText mEtDiscountPrice;

    public ChooseDiscountPricePop(Context context) {
        super(context);
        setPopupGravity(Gravity.BOTTOM);
        setOutSideDismiss(true);
        initUI();
    }

    private void initUI() {

        findViewById(R.id.iv_close).setOnClickListener(this);
        findViewById(R.id.tv_next).setOnClickListener(this);
        findViewById(R.id.rl_not_add).setOnClickListener(this);
        findViewById(R.id.rl_choose).setOnClickListener(this);
        mEtDiscountPrice = findViewById(R.id.et_discount_price);
        mIvNoDiscount = findViewById(R.id.iv_no_discount);
        mIvDiscount = findViewById(R.id.iv_discount);

    }

    @Override
    public View onCreateContentView() {
        View v = createPopupById(R.layout.pop_choose_discount_price);
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
        }else if(id == R.id.rl_not_add){

            mIvNoDiscount.setImageResource(R.mipmap.ic_radio_selector);
            mIvDiscount.setImageResource(R.mipmap.ic_radio_normal);
            mEtDiscountPrice.setText("");
        }else if(id == R.id.rl_choose){
            mIvNoDiscount.setImageResource(R.mipmap.ic_radio_normal);
            mIvDiscount.setImageResource(R.mipmap.ic_radio_selector);

        } else if(id == R.id.tv_next){
            String discountPrice = mEtDiscountPrice.getText().toString();
            if(onSelectListener!=null)
                onSelectListener.onSelect(discountPrice);
            dismiss();
        }
    }


    public interface OnSelectListener {
        void onSelect(String content);

    }

    private ShopCarPop.OnSelectListener onSelectListener;

    public void setOnSelectListener(ShopCarPop.OnSelectListener listener) {
        this.onSelectListener = listener;
    }
}
