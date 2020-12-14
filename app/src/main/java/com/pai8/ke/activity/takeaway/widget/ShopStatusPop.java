package com.pai8.ke.activity.takeaway.widget;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import com.pai8.ke.R;

import razerdp.basepopup.BasePopupWindow;

public class ShopStatusPop extends BasePopupWindow implements View.OnClickListener {

    private final int status;

    public ShopStatusPop(Context context,int status) {
        super(context);
        setPopupGravity(Gravity.CENTER);
        setOutSideDismiss(true);
        this.status = status;
        initUI();
    }

    private void initUI() {

        findViewById(R.id.btn_common_dialog_double_left).setOnClickListener(this);
        findViewById(R.id.btn_common_dialog_double_right).setOnClickListener(this);

        TextView tvContent = findViewById(R.id.tv_common_dialog_content);
        if(status == 2){
            tvContent.setText("确定要修改状态为“休息中”吗");
        }else{
            tvContent.setText("确定要修改状态为“营业中”吗");
        }

    }





    @Override
    public View onCreateContentView() {
        View v = createPopupById(R.layout.pop_shop_status);
        return v;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.btn_common_dialog_double_right) {
            dismiss();
        }else if(id == R.id.btn_common_dialog_double_left){
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
