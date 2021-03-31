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
                mBinding.tvScore.setText(rating + "fen");
                if (rating == 1) {
                    mBinding.tvScoreTxt.setText("(极差)");
                } else if (2 == rating) {
                    mBinding.tvScoreTxt.setText("(很差)");
                } else if (3 == rating) {
                    mBinding.tvScoreTxt.setText("(一般般)");
                } else if (4 == rating) {
                    mBinding.tvScoreTxt.setText("(还不错)");
                } else if (5 == rating) {
                    mBinding.tvScoreTxt.setText("(很棒)");
                }
            }
        });
    }
}
