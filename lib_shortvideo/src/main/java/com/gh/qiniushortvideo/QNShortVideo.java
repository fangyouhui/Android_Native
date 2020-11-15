package com.gh.qiniushortvideo;

import android.content.Context;

import com.qiniu.pili.droid.shortvideo.PLShortVideoEnv;

import org.lasque.tusdk.core.TuSdk;

public class QNShortVideo {

    public static void init(Context context) {
        PLShortVideoEnv.init(context.getApplicationContext());
        TuSdk.enableDebugLog(false);
        TuSdk.init(context.getApplicationContext(), "5be4d67909e1d60d-03-bshmr1");
    }
}
