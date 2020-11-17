package com.pai8.ke.activity.takeaway.widget;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;

import com.pai8.ke.R;

import razerdp.basepopup.BasePopupWindow;

public class OrderPayPop extends BasePopupWindow implements View.OnClickListener {


    private ImageView mIvWXPay,mIvZFBPay;

    private int payWay = 1;

    public OrderPayPop(Context context) {
        super(context);
        setPopupGravity(Gravity.BOTTOM);
        setOutSideDismiss(true);
        initUI();
    }

    private void initUI() {

        findViewById(R.id.iv_close).setOnClickListener(this);
        findViewById(R.id.tv_next).setOnClickListener(this);
        findViewById(R.id.rl_zfb_pay).setOnClickListener(this);
        findViewById(R.id.rl_wx_pay).setOnClickListener(this);
        mIvWXPay = findViewById(R.id.iv_wx_pay);
        mIvZFBPay = findViewById(R.id.iv_zfb_pay);

    }

    @Override
    public View onCreateContentView() {
        View v = createPopupById(R.layout.pop_order_pay);
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
        }else if(id == R.id.rl_wx_pay){
            mIvWXPay.setImageResource(R.mipmap.ic_radio_selector);
            mIvZFBPay.setImageResource(R.mipmap.ic_radio_normal);
        }else if(id == R.id.rl_zfb_pay){
            mIvWXPay.setImageResource(R.mipmap.ic_radio_normal);
            mIvZFBPay.setImageResource(R.mipmap.ic_radio_selector);

        } else if(id == R.id.tv_next){
            if(onSelectListener!=null)
                onSelectListener.onSelect(payWay);
            dismiss();
        }
    }


    public interface OnSelectListener {
        void onSelect(int payWay);

    }

    private OnSelectListener onSelectListener;

    public void setOnSelectListener(OnSelectListener listener) {
        this.onSelectListener = listener;
    }
}
