package com.pai8.ke.interfaces.contract;


import com.pai8.ke.base.BasePresenter;
import com.pai8.ke.base.BaseView;
import com.pai8.ke.entity.Video;
import com.pai8.ke.entity.resp.ShareMiniResp;

import java.util.List;
import java.util.Map;

import cn.sharesdk.framework.PlatformActionListener;

public interface ShareContract {

    interface View extends BaseView {

        void shareMini(ShareMiniResp resp);

    }


    interface Presenter extends BasePresenter {

        void shareVideo(String videoId);

        void shareShop(String shopId);

        void shareGoods(String goodsId);

    }
}
