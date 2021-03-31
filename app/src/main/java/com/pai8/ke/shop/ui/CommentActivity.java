package com.pai8.ke.shop.ui;

import android.os.Bundle;

import com.lhs.library.base.BaseActivity;
import com.lhs.library.base.NoViewModel;
import com.pai8.ke.databinding.ActivityCommentBinding;

import org.jetbrains.annotations.Nullable;

import per.wsj.library.AndRatingBar;

public class CommentActivity extends BaseActivity<NoViewModel, ActivityCommentBinding> {

    @Override
    public void initView(@Nullable Bundle savedInstanceState) {
        super.initView(savedInstanceState);
        mBinding.evaluateRatingBar.setOnRatingChangeListener(new AndRatingBar.OnRatingChangeListener() {
            @Override
            public void onRatingChanged(AndRatingBar ratingBar, float rating) {
                int score = (int) rating;
                mBinding.tvScore.setText(rating + "分");
                if (score == 1) {
                    mBinding.tvScoreTxt.setText("(极差)");
                } else if (2 == score) {
                    mBinding.tvScoreTxt.setText("(很差)");
                } else if (3 == score) {
                    mBinding.tvScoreTxt.setText("(一般般)");
                } else if (4 == score) {
                    mBinding.tvScoreTxt.setText("(还不错)");
                } else if (5 == score) {
                    mBinding.tvScoreTxt.setText("(很棒)");
                }
            }
        });
    }
}
