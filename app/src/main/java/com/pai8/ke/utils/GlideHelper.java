package com.pai8.ke.utils;

import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.pai8.ke.R;

public class GlideHelper {
    private final static int ID_PLACEHOLDER = R.color.colorPrimary;
    private final static int ID_ERROR = R.color.colorPrimary;


    public static void loadImageWithNormal(String url, final ImageView imageView) {
        Glide.with(imageView.getContext()).load(url)
                .placeholder(ID_PLACEHOLDER)
                .error(ID_PLACEHOLDER)
                .into(imageView);
    }

    public static void loadImageWithRoundedCorners(String url, final ImageView imageView) {
        Glide.with(imageView.getContext()).load(url)
                .placeholder(ID_PLACEHOLDER)
                .error(ID_PLACEHOLDER)
                .transform(new RoundedCorners(8)) // 数字根据自己需求来改
                .into(imageView);

    }

    public static void loadImageWithCircleCrop(String url, final ImageView imageView) {
        Glide.with(imageView.getContext()).load(url)
                .placeholder(ID_PLACEHOLDER)
                .error(ID_PLACEHOLDER)
                .transform(new CircleCrop()) // 数字根据自己需求来改 // 数字根据自己需求来改
                .into(imageView);
    }

}
