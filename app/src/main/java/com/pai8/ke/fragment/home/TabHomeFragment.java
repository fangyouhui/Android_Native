package com.pai8.ke.fragment.home;

import android.os.Bundle;

import com.pai8.ke.R;
import com.pai8.ke.activity.video.VideoDetailActivity;
import com.pai8.ke.base.BaseFragment;

public class TabHomeFragment extends BaseFragment {

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_tab_home;
    }

    @Override
    protected void initView(Bundle arguments) {

    }

    @Override
    protected void onLazyLoad() {
        launch(VideoDetailActivity.class);
    }
}
