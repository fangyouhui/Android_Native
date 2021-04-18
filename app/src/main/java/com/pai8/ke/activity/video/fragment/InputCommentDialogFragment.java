package com.pai8.ke.activity.video.fragment;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;

import com.pai8.ke.R;
import com.pai8.ke.app.MyApp;
import com.pai8.ke.base.BaseDialogFragment;
import com.pai8.ke.utils.AppUtils;
import com.pai8.ke.utils.StringUtils;

import butterknife.BindView;
import butterknife.OnClick;

public class InputCommentDialogFragment extends BaseDialogFragment {
    @BindView(R.id.tv_comment)
    TextView tvComment;
    @BindView(R.id.et_input)
    EditText etInput;
    private InputCallback mInputCallback;
    private String mHint;

    public void setInputCallback(InputCallback inputCallback) {
        mInputCallback = inputCallback;
    }

    public static InputCommentDialogFragment newInstance(String hint) {
        InputCommentDialogFragment fragment = new InputCommentDialogFragment();
        Bundle bundle = new Bundle();
        bundle.putString("hint", hint);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_input_comment;
    }

    @Override
    public void dismiss() {
        super.dismiss();
        AppUtils.hideInput(getActivity());
    }

    @Override
    public void initView(Bundle arguments) {
        mHint = arguments.getString("hint", "");
        if (StringUtils.isNotEmpty(mHint)) {
            etInput.setHint("回复 " + mHint);
        }
    }

    @Override
    public void initData() {
        MyApp.getMyAppHandler().postDelayed(() -> AppUtils.showInput(getActivity(), etInput), 50);
    }

    @OnClick(R.id.tv_comment)
    public void onViewClicked() {
        if (StringUtils.isEmpty(StringUtils.getEditText(etInput))) {
            toast("请输入评论内容");
            return;
        }
        if (mInputCallback != null) {
            mInputCallback.commentContent(etInput.getText().toString());
        }
        dismiss();
    }

    public interface InputCallback {
        void commentContent(String txt);
    }
}
