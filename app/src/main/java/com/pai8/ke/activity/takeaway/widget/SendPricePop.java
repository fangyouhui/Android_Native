package com.pai8.ke.activity.takeaway.widget;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;

import com.pai8.ke.R;

import razerdp.basepopup.BasePopupWindow;

public class SendPricePop extends BasePopupWindow implements View.OnClickListener {


    private EditText mEtDiscountPrice;

    public SendPricePop(Context context) {
        super(context);
        setPopupGravity(Gravity.CENTER);
        setOutSideDismiss(true);
        initUI();
    }

    private void initUI() {

        findViewById(R.id.iv_close).setOnClickListener(this);
        findViewById(R.id.tv_next).setOnClickListener(this);
        mEtDiscountPrice = findViewById(R.id.et_discount_price);

    }

    @Override
    public View onCreateContentView() {
        View v = createPopupById(R.layout.pop_send_price);
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
        }else if(id == R.id.tv_next){
            String discountPrice = mEtDiscountPrice.getText().toString();
            if(onSelectListener!=null)
                onSelectListener.onSelect(discountPrice);
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
