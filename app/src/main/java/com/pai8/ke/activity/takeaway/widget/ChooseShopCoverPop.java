package com.pai8.ke.activity.takeaway.widget;

import android.content.Context;
import android.view.Gravity;
import android.view.View;

import com.pai8.ke.R;

import razerdp.basepopup.BasePopupWindow;

public class ChooseShopCoverPop  extends BasePopupWindow implements View.OnClickListener {


    public ChooseShopCoverPop(Context context) {
        super(context);
        setPopupGravity(Gravity.BOTTOM);
        setOutSideDismiss(true);
        initUI();
    }

    private void initUI() {

        findViewById(R.id.iv_close).setOnClickListener(this);
        findViewById(R.id.tv_picture).setOnClickListener(this);
        findViewById(R.id.tv_video).setOnClickListener(this);

    }





    @Override
    public View onCreateContentView() {
        View v = createPopupById(R.layout.pop_choose_shop_cover);
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
        }else if(id == R.id.tv_video){
            if(onSelectListener!=null)
                onSelectListener.onSelect(1);
            dismiss();
        }else if(id == R.id.tv_picture){
            if(onSelectListener!=null)
                onSelectListener.onSelect(0);
            dismiss();
        }
    }


    public interface OnSelectListener {
        void onSelect(int type);

    }

    private OnSelectListener onSelectListener;

    public void setOnSelectListener(OnSelectListener listener) {
        this.onSelectListener = listener;
    }
}
