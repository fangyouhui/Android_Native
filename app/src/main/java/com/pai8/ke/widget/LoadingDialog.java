package com.pai8.ke.widget;

import android.app.Dialog;
import android.content.Context;
import android.widget.TextView;

import com.pai8.ke.R;


/**
 * 自定义加载进度对话框
 */
public class LoadingDialog extends Dialog {

    private TextView mTvMsg;

    public LoadingDialog(Context context) {
        super(context, R.style.style_loadingDialog);
        setContentView(R.layout.view_dialog_loading);
        mTvMsg = findViewById(R.id.tv_text);
        setCanceledOnTouchOutside(false);
        setCancelable(false);
    }

    public LoadingDialog setMessage(String message) {
        mTvMsg.setText(message);
        return this;
    }
}
