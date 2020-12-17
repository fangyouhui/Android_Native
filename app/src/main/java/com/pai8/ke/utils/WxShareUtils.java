package com.pai8.ke.utils;

import com.pai8.ke.entity.resp.ShareMiniResp;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.wechat.friends.Wechat;

public class WxShareUtils {

    public static void shareMini(ShareMiniResp resp, PlatformActionListener platformActionListener) {
        Platform platform = ShareSDK.getPlatform(Wechat.NAME);
        Platform.ShareParams shareParams = new Platform.ShareParams();
        shareParams.setText(resp.getDescription());
        shareParams.setTitle(resp.getTitle());
        shareParams.setUrl(StringUtils.isEmptyDefaultValue(resp.getWebpage_url(), "http://www.baidu.com"));
        shareParams.setImageUrl(resp.getThumb());
        shareParams.setWxPath(resp.getApp_path());
        shareParams.setWxUserName(resp.getApp_id()); // 小程序的原始ID
        shareParams.setWxMiniProgramType(resp.getMiniprogramType()); // 0: 正式 1:开发版本 2:体验版本
        shareParams.setWxWithShareTicket(true);
        shareParams.setShareType(Platform.SHARE_WXMINIPROGRAM);
        platform.setPlatformActionListener(platformActionListener);
        platform.share(shareParams);
    }

}
