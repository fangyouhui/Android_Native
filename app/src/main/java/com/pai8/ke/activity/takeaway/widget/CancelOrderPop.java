package com.pai8.ke.activity.takeaway.widget;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import com.pai8.ke.R;

import razerdp.basepopup.BasePopupWindow;

public class CancelOrderPop extends BasePopupWindow implements View.OnClickListener {


    private int status;

    public CancelOrderPop(Context context,int status) {
        super(context);
        this.status = status;
        setPopupGravity(Gravity.BOTTOM);
        setOutSideDismiss(true);
        initUI();
    }

    private void initUI() {

        TextView tv = findViewById(R.id.tv_delete);

        if(status == 0){
            tv.setText("取消订单");
        }else {
            tv.setText("申请退款");
        }
        findViewById(R.id.tv_delete).setOnClickListener(this);
        findViewById(R.id.btn_cancle).setOnClickListener(this);

    }





    @Override
    public View onCreateContentView() {
        View v = createPopupById(R.layout.popwindow_cancel);
        return v;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.btn_cancle) {
            dismiss();
        }else if(id == R.id.tv_delete){
            if(onSelectListener!=null)
                onSelectListener.onSelect(status);
            dismiss();
        }
    }


    public interface OnSelectListener {
        void onSelect(int status);

    }

    private OnSelectListener onSelectListener;

    public void setOnSelectListener(OnSelectListener listener) {
        this.onSelectListener = listener;
    }
}
