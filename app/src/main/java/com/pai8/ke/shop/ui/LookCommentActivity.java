package com.pai8.ke.shop.ui;

import android.content.pm.ActivityInfo;
import android.graphics.Paint;
import android.os.Bundle;
import android.text.TextUtils;

import com.lhs.library.base.BaseActivity;
import com.lhs.library.base.BaseAppConstants;
import com.lhs.library.base.NoViewModel;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;
import com.luck.picture.lib.style.PictureSelectorUIStyle;
import com.pai8.ke.R;
import com.pai8.ke.activity.takeaway.entity.GoodsInfo;
import com.pai8.ke.activity.takeaway.entity.OrderDetailResult;
import com.pai8.ke.databinding.ActivityLookCommentBinding;
import com.pai8.ke.shop.adapter.CommentImageAdapter;
import com.pai8.ke.utils.GlideEngine;
import com.pai8.ke.utils.ImageLoadUtils;

import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

/**
 * 查看评价
 */
public class LookCommentActivity extends BaseActivity<NoViewModel, ActivityLookCommentBinding> {

    private OrderDetailResult bean;

    @Override
    public void initView(@Nullable Bundle savedInstanceState) {
        bean = (OrderDetailResult) getIntent().getSerializableExtra(BaseAppConstants.BundleConstant.ARG_PARAMS_0);
        String size = bean.getGoods_info().size() + "";
        GoodsInfo info = bean.getGoods_info().get(0);
        if (info.getCover() != null) {
            ImageLoadUtils.loadImage(info.getCover().get(0), mBinding.ivProductImg);
        } else {
            ImageLoadUtils.loadImage(info.getGoods_img().get(0), mBinding.ivProductImg);
        }

        mBinding.tvProductName.setText(TextUtils.isEmpty(info.getTitle()) ? info.getGoods_title() : info.getTitle());
        mBinding.tvCount2.setText("X" + size);
        mBinding.tvSellPrice.setText("¥" + (TextUtils.isEmpty(info.getSell_price()) ? info.getGoods_sell_price() : info.getSell_price()));
        mBinding.tvOriginPrice.setText("¥" + (TextUtils.isEmpty(info.getOrigin_price()) ? info.getGoods_origin_price() : info.getOrigin_price()));
        mBinding.tvOriginPrice.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);

        OrderDetailResult.Comment comment = bean.getComment();
        int rating = comment.getScore();
        mBinding.evaluateRatingBar.setRating(rating);
        mBinding.tvScore.setText(rating + "分");
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
        mBinding.etContent.setText(comment.getContent());
        mBinding.tvLength.setText(String.format("(%d/100)", comment.getContent().length()));

        List<LocalMedia> medias = new ArrayList<>();

        if (!TextUtils.isEmpty(comment.getImage())) {
            String[] images = comment.getImage().split(",");
            for (String image : images) {
                LocalMedia entity = new LocalMedia();
                entity.setPath(image);
                entity.setMimeType(PictureMimeType.MIME_TYPE_IMAGE);
                medias.add(entity);
            }
        }

        if (!TextUtils.isEmpty(comment.getVideo())) {
            String[] videos = comment.getImage().split(",");
            for (String video : videos) {
                LocalMedia entity = new LocalMedia();
                entity.setPath(video);
                entity.setMimeType(PictureMimeType.MIME_TYPE_VIDEO);
                medias.add(entity);
            }
        }

        CommentImageAdapter adapter = new CommentImageAdapter(this, medias);
        mBinding.recycler.setAdapter(adapter);

        adapter.setListener((item, position) -> {
            int mediaType = PictureMimeType.getMimeType(item.getMimeType());
            if (PictureConfig.TYPE_VIDEO == mediaType) {
                PictureSelector.create(LookCommentActivity.this)
                        .themeStyle(R.style.picture_default_style)
                        .setPictureUIStyle(PictureSelectorUIStyle.ofDefaultStyle())// 动态自定义相册主题
                        .externalPictureVideo(item.getPath());
            } else {
                PictureSelector.create(LookCommentActivity.this)
                        .themeStyle(R.style.picture_default_style) // xml设置主题
                        .setPictureUIStyle(PictureSelectorUIStyle.ofDefaultStyle())// 动态自定义相册主题
                        .setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED)// 设置相册Activity方向，不设置默认使用系统
                        .isNotPreviewDownload(true)// 预览图片长按是否可以下载
                        //.bindCustomPlayVideoCallback(new MyVideoSelectedPlayCallback(getContext()))// 自定义播放回调控制，用户可以使用自己的视频播放界面
                        .imageEngine(GlideEngine.createGlideEngine())// 外部传入图片加载引擎，必传项
                        .openExternalPreview(position, medias);
            }
        });

    }


}
