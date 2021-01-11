package com.pai8.ke.widget;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.pai8.ke.R;


/**
 * 底部弹出对话框
 */
public class BottomDialog extends Dialog {

    protected View mView;
    private Context mContext;

    private boolean mIsCancelable = true;
    private boolean mIsCanceledOnTouchOutside = true;

    public void setIsCancelable(boolean mIsCancelable) {
        this.mIsCancelable = mIsCancelable;
    }

    public void setIsCanceledOnTouchOutside(boolean mIsCanceledOnTouchOutside) {
        this.mIsCanceledOnTouchOutside = mIsCanceledOnTouchOutside;
    }

    public BottomDialog(Context context, View view) {
        super(context, R.style.style_bottomDialog);
        this.mContext = context;
        this.mView = view;
    }

    public View getView() {
        return mView;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(mView == null ? getView() : mView);
        setCancelable(mIsCancelable);
        setCanceledOnTouchOutside(mIsCanceledOnTouchOutside);
        Window window = this.getWindow();
        window.setGravity(Gravity.BOTTOM);
        WindowManager.LayoutParams params = window.getAttributes();
        params.width = WindowManager.LayoutParams.MATCH_PARENT;
        params.height = WindowManager.LayoutParams.WRAP_CONTENT;
        window.setAttributes(params);
    }
}
