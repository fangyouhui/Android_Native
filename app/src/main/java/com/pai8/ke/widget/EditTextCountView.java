package com.pai8.ke.widget;

import android.content.Context;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.pai8.ke.R;
import com.pai8.ke.utils.StringUtils;

/**
 * 带字数监听的EditText
 */
public class EditTextCountView extends LinearLayout {

    private EditText mEtContent;
    private TextView mTvCount;

    private int mMaxNum = 500;

    public EditTextCountView(Context context) {
        this(context, null);
    }

    public EditTextCountView(Context context, AttributeSet attrs) {

        super(context, attrs);

        LayoutInflater.from(context).inflate(R.layout.view_edittext_count, this, true);
        mEtContent = findViewById(R.id.et_content);
        mTvCount = findViewById(R.id.tv_count);

        int length = mEtContent.getText().toString().length();
        mTvCount.setText("(" + length + "/" + mMaxNum + ")");

        mEtContent.setFilters(new InputFilter[]{new InputFilter.LengthFilter(mMaxNum)});
        mEtContent.addTextChangedListener(mTextWatcher);

    }

    public void setLength(int num) {
        mMaxNum = num;
        mEtContent.setFilters(new InputFilter[]{new InputFilter.LengthFilter(mMaxNum)});
        int length = mEtContent.getText().toString().length();
        mTvCount.setText("(" + length + "/" + mMaxNum + ")");
    }

    public void setEtHint(String str) {
        mEtContent.setHint(str);
    }

    public void setEtText(String str) {
        mEtContent.setText(str);
    }

    public String getText() {
        return StringUtils.getEditText(mEtContent);
    }

    private TextWatcher mTextWatcher = new TextWatcher() {

        public void afterTextChanged(Editable s) {
            int length = mEtContent.getText().toString().length();
            mTvCount.setText("(" + length + "/" + mMaxNum + ")");
        }

        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

    };

}