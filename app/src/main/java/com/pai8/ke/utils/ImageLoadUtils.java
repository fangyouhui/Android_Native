package com.pai8.ke.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Looper;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.DrawableRes;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.FutureTarget;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.pai8.ke.R;
import com.pai8.ke.global.GlobalConstants;
import com.pai8.ke.utils.transform.GlideCircleTransform;
import com.pai8.ke.utils.transform.GlideRadianTransform;
import com.pai8.ke.utils.transform.GlideRoundTransform;
import com.pai8.ke.utils.transform.GlideRoundTransform2;

import java.io.File;

/**
 * 基于Glide图片加载工具类
 */
public class ImageLoadUtils {

    /**
     * Don't let anyone instantiate this class.
     */
    private ImageLoadUtils() {
        throw new Error("Do not need instantiate!");
    }

    public static void setCircularImage(final Context context, String url, ImageView iv,
                                        @DrawableRes final int id) {
        if (context != null) {
            Glide.with(context)
                    .load(url).apply(new RequestOptions()
                    .error(id)
                    .placeholder(id).transform(new GlideRoundTransform2(context))
            ).into(iv);
        }

    }

    /**
     * 加载普通的图片
     *
     * @param context
     * @param strUrl
     * @param imageView
     * @param id
     */
    @Deprecated
    public static void loadImage(Context context, final String strUrl, final ImageView imageView, @DrawableRes final int id) {
        Glide.with(context)
                .load(strUrl)
                .apply(new RequestOptions()
                        .centerCrop()
                        .error(id)
//                        .placeholder(id)
                        .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC))
                .transition(new DrawableTransitionOptions().crossFade(300)) //使用变换效果
                .into(imageView);
    }

    public static void loadImage(String strUrl, ImageView imageView) {
        loadImage(imageView.getContext(), strUrl, imageView, R.color.colorPrimary);
    }

    public static void loadImageFitCenter(Context context, final String strUrl, final ImageView imageView,
                                          @DrawableRes final int id) {
        Glide.with(context)
                .load(strUrl)
                .apply(new RequestOptions()
                        .fitCenter()
                        .error(id)
//                        .placeholder(id)
                        .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC))
                .transition(new DrawableTransitionOptions().crossFade(100)) //使用变换效果
                .into(imageView);
    }

    /**
     * 加载有弧度的
     *
     * @param context
     * @param strUrl
     * @param imageView
     * @param radian
     * @param id
     */
    public static void loadRadianImage(Context context, final String strUrl, final ImageView imageView,
                                       float radian, @DrawableRes final int id) {
        Glide.with(context)
                .load(strUrl)
                .apply(new RequestOptions()
                        .centerCrop()
                        .transform(new GlideRadianTransform(radian))
                        .error(id)
                        .placeholder(id)
                        .diskCacheStrategy(DiskCacheStrategy.ALL))
                .transition(new DrawableTransitionOptions().crossFade(300)) //使用变换效果
                .into(imageView);
    }

    /**
     * 加载圆形的ImageView
     *
     * @param context
     * @param strUrl
     * @param imageView
     * @param id
     */
    public static void loadRoundImage(Context context, final String strUrl, final ImageView imageView,
                                      @DrawableRes final int id) {
        Glide.with(context)
                .load(strUrl)
                .apply(new RequestOptions()
                        .centerCrop()
                        .transform(new GlideRoundTransform())
                        .error(id)
//                        .placeholder(id)
                        .diskCacheStrategy(DiskCacheStrategy.ALL))
                .transition(new DrawableTransitionOptions().crossFade(300)) //使用变换效果
                .into(imageView);
    }

    public static void loadRoundBorderImage(Context context, final String strUrl, int borderWidth,
                                            int borderColor, final ImageView imageView,
                                            @DrawableRes final int id) {
        Glide.with(context)
                .load(strUrl)
                .apply(new RequestOptions()
                        .centerCrop()
                        .transform(new GlideCircleTransform(borderWidth, borderColor))
                        .error(id)
//                        .placeholder(id)
                        .diskCacheStrategy(DiskCacheStrategy.ALL))
//                .transition(new DrawableTransitionOptions().crossFade(300)) //使用变换效果
                .into(imageView);
    }

    public static void loadRoundBorderImage(Context context, @DrawableRes final int resId, int borderWidth,
                                            int borderColor, final ImageView imageView) {
        Glide.with(context)
                .load(resId)
                .apply(new RequestOptions()
                        .centerCrop()
                        .transform(new GlideCircleTransform(borderWidth, borderColor))
//                        .placeholder(id)
                        .diskCacheStrategy(DiskCacheStrategy.ALL))
//                .transition(new DrawableTransitionOptions().crossFade(300)) //使用变换效果
                .into(imageView);
    }

    public static void loadImageWH(Context context, String url, final ImageView imageView, int width,
                                   int height) {
        Glide.with(context)
                .load(url)
                .apply(new RequestOptions().override(width, height))
                .into(imageView);
    }

    public static Bitmap getBitmap(Context context, String url, int width, int height) {
        try {
            FutureTarget<Bitmap> bitmap = Glide.with(context)
                    .asBitmap()
                    .apply(new RequestOptions().override(width, height).centerCrop())
                    .load(url)
                    .submit();
            return bitmap.get();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 清除图片磁盘缓存
     */
    public static void clearImageDiskCache(final Context context) {
        try {
            if (Looper.myLooper() == Looper.getMainLooper()) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        Glide.get(context).clearDiskCache();
                    }
                }).start();
            } else {
                Glide.get(context).clearDiskCache();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 清除图片内存缓存
     */
    public static void clearImageMemoryCache(Context context) {
        try {
            if (Looper.myLooper() == Looper.getMainLooper()) {
                Glide.get(context).clearMemory();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static String getImageUrl(String url) {

        return GlobalConstants.HTTP_URL_TEST + url + ".png";
    }


    /**
     * 内存优化
     */
    public static void onTrimMemory(Context context, int level) {
        Glide.get(context).onTrimMemory(level);
    }

    public static void setRectImage(Context context, String path, ImageView imageView) {
        Glide.with(context).load(path).apply(new RequestOptions().centerInside().dontAnimate())
                .into(imageView);
    }

    /**
     * 加载帧作为封面
     */
    public static void loadCover(Context context, String url, ImageView imageView) {
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);

        if (context != null) {
            Glide.with(context)
                    .load(url).apply(new RequestOptions()
            ).into(imageView);
        }
//        Glide.with(context)
//                .setDefaultRequestOptions(
//                        new RequestOptions()
//                                .frame(4000000)
//                                .centerCrop()
////                                .error(R.)
////                                .placeholder(R.mipmap.ppppp)
//                )
//                .load(url)
//                .into(imageView);
    }

    public static void loadVideoCover(Context context, String url, ImageView imageView) {
        File videoFile = new File(url);
//        Glide.with(context).setDefaultRequestOptions(
//                new RequestOptions()
//                        .frame(1000000)
//                        .dontAnimate()
//        ).load(url).into(imageView);
        Glide.with(context).load(Uri.fromFile(videoFile)).placeholder(R.mipmap.ic_launcher).into(imageView);

    }

    public static void loadPicsFitWidth(Context context, final String imageUrl, final ImageView imageView) {
        Glide.with(context).load(imageUrl).skipMemoryCache(true).listener(new RequestListener<Drawable>() {
            @Override
            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target,
                                        boolean isFirstResource) {
                return false;
            }

            @Override
            public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target,
                                           DataSource dataSource, boolean isFirstResource) {
                if (imageView == null) {
                    return false;
                }
                if (imageView.getScaleType() != ImageView.ScaleType.FIT_XY) {
                    imageView.setScaleType(ImageView.ScaleType.FIT_XY);
                }
                ViewGroup.LayoutParams params = imageView.getLayoutParams();
                int vw = imageView.getWidth() - imageView.getPaddingLeft() - imageView.getPaddingRight();
                float scale = (float) vw / (float) resource.getIntrinsicWidth();
                int vh = Math.round(resource.getIntrinsicHeight() * scale);
                params.height = vh + imageView.getPaddingTop() + imageView.getPaddingBottom();
                imageView.setLayoutParams(params);
                return false;
            }
        }).into(imageView);
    }

}

