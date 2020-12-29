package com.pai8.ke.activity.takeaway.widget;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;

import com.pai8.ke.R;
import com.pai8.ke.utils.StringUtils;
import com.pai8.ke.utils.ToastUtils;

import razerdp.basepopup.BasePopupWindow;

public class SendPricePop extends BasePopupWindow implements View.OnClickListener {


    private EditText mEtDiscountPrice;
    private EditText mEtDistance;

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
        mEtDistance = findViewById(R.id.et_distance);

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
        } else if (id == R.id.tv_next) {
            String discountPrice = mEtDiscountPrice.getText().toString();
            String distance = mEtDistance.getText().toString();
            if (StringUtils.isEmpty(discountPrice)) {
                ToastUtils.showShort("请输入起送价");
                return;
            }
            if (StringUtils.isEmpty(distance)) {
                ToastUtils.showShort("请输入配送范围");
                return;
            }
            if (onSelectListener != null) {
                onSelectListener.onSelect(discountPrice, distance);
            }
            dismiss();
        }
    }

    public interface OnSelectListener {
        void onSelect(String content, String distance);

    }

    private OnSelectListener onSelectListener;

    public void setOnSelectListener(OnSelectListener listener) {
        this.onSelectListener = listener;
    }
}
