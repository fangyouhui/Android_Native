package com.pai8.ke.utils;

import com.pai8.ke.entity.resp.ShareMiniResp;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.wechat.friends.Wechat;
import cn.sharesdk.wechat.moments.WechatMoments;

public class WxShareUtils {
    /**
     * 分享给朋友
     *
     * @param resp
     * @param platformActionListener
     */
    public static void shareToWeChat(ShareMiniResp resp, PlatformActionListener platformActionListener) {
        share(Wechat.NAME, resp, platformActionListener);
    }

    /**
     * 分享到朋友圈
     *
     * @param resp
     * @param platformActionListener
     */

    public static void shareToWeChatMoments(ShareMiniResp resp, PlatformActionListener platformActionListener) {
        share(WechatMoments.NAME, resp, platformActionListener);
    }

    private static void share(String name, ShareMiniResp resp, PlatformActionListener platformActionListener) {
        Platform.ShareParams shareParams = new Platform.ShareParams();
        shareParams.setText(resp.getDescription());
        shareParams.setTitle(resp.getTitle());
        shareParams.setUrl(StringUtils.isEmptyDefaultValue(resp.getWebpage_url(), "http://www.baidu.com"));
        shareParams.setImageUrl(resp.getThumb());
        shareParams.setWxPath(resp.getApp_path());
        shareParams.setWxUserName(resp.getApp_id()); // 小程序的原始ID
        shareParams.setWxMiniProgramType(resp.getMiniprogramType()); // 0: 正式 1:开发版本 2:体验版本
        shareParams.setWxWithShareTicket(true);
        shareParams.setShareType(Wechat.NAME.equalsIgnoreCase(name) ? Platform.SHARE_WXMINIPROGRAM : Platform.SHARE_WEBPAGE);

        Platform platform = ShareSDK.getPlatform(name);
        platform.setPlatformActionListener(platformActionListener);
        platform.share(shareParams);

    }

}
