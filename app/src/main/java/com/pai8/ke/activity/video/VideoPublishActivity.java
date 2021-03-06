package com.pai8.ke.activity.video;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;

import com.bigkoo.pickerview.builder.OptionsPickerBuilder;
import com.bigkoo.pickerview.view.OptionsPickerView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.gh.qiniushortvideo.ChooseVideo;
import com.gh.qiniushortvideo.activity.ConfigActivity;
import com.gh.qiniushortvideo.activity.MediaSelectActivity;
import com.gh.qiniushortvideo.activity.VideoRecordActivity;
import com.lhs.library.base.BaseActivity;
import com.lhs.library.base.BaseAppConstants;
import com.pai8.ke.activity.account.LoginActivity;
import com.pai8.ke.activity.common.VideoViewActivity;
import com.pai8.ke.activity.me.AddressChooseActivity;
import com.pai8.ke.app.MyApp;
import com.pai8.ke.base.BaseEvent;
import com.pai8.ke.databinding.ActivityVideoPublishBinding;
import com.pai8.ke.entity.Address;
import com.pai8.ke.entity.BusinessTypeResult;
import com.pai8.ke.entity.req.VideoPublishReq;
import com.pai8.ke.entity.resp.ShopList;
import com.pai8.ke.manager.AccountManager;
import com.pai8.ke.utils.EventBusUtils;
import com.pai8.ke.utils.StringUtils;
import com.pai8.ke.utils.ToastUtils;
import com.pai8.ke.viewmodel.VideoPublishViewModel;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static com.pai8.ke.global.EventCode.EVENT_VIDEO_LIST_REFRESH;

/**
 * 发布视频
 * Created by gh on 2020/11/14.
 */
public class VideoPublishActivity extends BaseActivity<VideoPublishViewModel, ActivityVideoPublishBinding> {

    private int mBusinessTypePosition;

    private int mBusinessTypeId;
    private String mCoverVideoUrl = "";
    private String mCoverVideoPath = "";
    private Address mAddress;
    private ActivityResultLauncher activityResultLauncher;
    private ActivityResultLauncher shopSearchActivityResultLauncher;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        EventBusUtils.register(this);
        super.onCreate(savedInstanceState);
        activityResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
            if (result.getResultCode() == Activity.RESULT_OK) {
                mAddress = (Address) result.getData().getSerializableExtra(BaseAppConstants.BundleConstant.ARG_PARAMS_0);
                mBinding.tvAddress.setText(mAddress.getTitle());
            }
        });
        shopSearchActivityResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
            if (result.getResultCode() == RESULT_OK) {
                ShopList shopInfo = (ShopList) result.getData().getSerializableExtra(BaseAppConstants.BundleConstant.ARG_PARAMS_0);
                //    mBinding.tvLinkShop.setText(shopInfo.getShop_name());
                mBinding.tvLinkShopLable.setText(shopInfo.getShop_name());
                mBinding.tvLinkShop.setVisibility(View.INVISIBLE);
                mBinding.tvLinkShopLable.setTag(shopInfo);
            }
            if (result.getResultCode() == ShopSearchListActivity.REQUEST_CODE_CANCEL) {
                mBinding.tvLinkShopLable.setText("关联商铺");
                mBinding.tvLinkShop.setVisibility(View.VISIBLE);
                mBinding.tvLinkShopLable.setTag(null);
            }
        });
    }

    @Override
    protected void onDestroy() {
        EventBusUtils.unregister(this);
        super.onDestroy();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventChooseVideo(ChooseVideo event) {
        if (event == null) return;
        mBinding.cvWrapVideo.setVisibility(View.VISIBLE);
        mBinding.llWrapBtnVideo.setVisibility(View.GONE);
        mCoverVideoPath = event.getPath();

        Glide.with(this)
                .load(Uri.fromFile(new File(mCoverVideoPath)))
                .skipMemoryCache(true) // 不使用内存缓存
                .diskCacheStrategy(DiskCacheStrategy.NONE) // 不使用磁盘缓存
                .into(mBinding.civVideoCover);
        showLoading();
        mViewModel.uploadVideo(mCoverVideoPath);
    }

    @Override
    public void addObserve() {
        mViewModel.getUploadVideoData().observe(this, data -> {
            dismissLoading();
            mCoverVideoUrl = data;
        });
        mViewModel.getUpVideoData().observe(this, data -> {
            ToastUtils.showShort("视频发布成功");
            finish();
            EventBusUtils.sendEvent(new BaseEvent(EVENT_VIDEO_LIST_REFRESH));
        });

        mViewModel.getVideoTypeData().observe(this, data -> {
            List<String> options1Items = new ArrayList<>();
            for (BusinessTypeResult businessType : data) {
                options1Items.add(businessType.getType_name());
            }

            OptionsPickerView pvOptions = new OptionsPickerBuilder(this, (options1, option2, options3, v) -> {
                mBusinessTypePosition = options1;
                mBusinessTypeId = data.get(options1).getId();
                mBinding.tvClassify.setText(data.get(options1).getType_name());
            }).build();
            pvOptions.setNPicker(options1Items, null, null);
            pvOptions.setSelectOptions(mBusinessTypePosition);
            pvOptions.setTitleText("选择分类");
            pvOptions.show();
        });
    }

//    @Subscribe(threadMode = ThreadMode.MAIN)
//    public void receiveEvent(BaseEvent event) {
//        switch (event.getCode()) {
//            case EventCode.EVENT_CHOOSE_SHOP:
//                mShopInfo = (ShopList) event.getData();
//                mBinding.tvLinkShop.setText(mShopInfo.getShop_name());
//                break;
////            case EventCode.EVENT_CHOOSE_ADDRESS:
////                mAddress = (Address) event.getData();
////                mBinding.tvAddress.setText(mAddress.getTitle());
////                break;
//        }
//    }

    @Override
    public void initView(@Nullable Bundle savedInstanceState) {
        mBinding.tvBtnGalley.setOnClickListener(v -> {
            Intent it = new Intent(this, MediaSelectActivity.class);
            it.putExtra(MediaSelectActivity.TYPE, MediaSelectActivity.TYPE_VIDEO_EDIT);
            startActivity(it);
        });

        mBinding.tvBtnTakePhoto.setOnClickListener(v -> {
            Intent intent = new Intent(this, VideoRecordActivity.class);
            intent.putExtra(VideoRecordActivity.PREVIEW_SIZE_RATIO, ConfigActivity.PREVIEW_SIZE_RATIO_POS);
            intent.putExtra(VideoRecordActivity.PREVIEW_SIZE_LEVEL, ConfigActivity.PREVIEW_SIZE_LEVEL_POS);
            intent.putExtra(VideoRecordActivity.ENCODING_MODE, ConfigActivity.ENCODING_MODE_LEVEL_POS);
            intent.putExtra(VideoRecordActivity.ENCODING_SIZE_LEVEL, ConfigActivity.ENCODING_SIZE_LEVEL_POS);
            intent.putExtra(VideoRecordActivity.ENCODING_BITRATE_LEVEL, ConfigActivity.ENCODING_BITRATE_LEVEL_POS);
            intent.putExtra(VideoRecordActivity.AUDIO_CHANNEL_NUM, ConfigActivity.AUDIO_CHANNEL_NUM_POS);
            startActivity(intent);
        });

        mBinding.ivBtnPlayer.setOnClickListener(v -> {
            VideoViewActivity.launch(this, VideoViewActivity.TYPE_LOCAL, mCoverVideoPath);
        });

        mBinding.ivBtnDelete.setOnClickListener(v -> {
            mCoverVideoPath = "";
            mCoverVideoUrl = "";
            mBinding.cvWrapVideo.setVisibility(View.GONE);
            mBinding.llWrapBtnVideo.setVisibility(View.VISIBLE);
        });

        mBinding.rlBtnAddress.setOnClickListener(v -> {
            Intent intent = new Intent(this, AddressChooseActivity.class);
            //   startActivity(new Intent(this, AddressChooseActivity.class));
            activityResultLauncher.launch(intent);
        });
        mBinding.rlBtnLinkShop.setOnClickListener(v -> {
            //   startActivity(new Intent(this, ShopSearchListActivity.class));
            boolean showMenu = mBinding.tvLinkShopLable.getTag() != null;
            shopSearchActivityResultLauncher.launch(new Intent(this, ShopSearchListActivity.class)
                    .putExtra(BaseAppConstants.BundleConstant.ARG_PARAMS_0, showMenu));
        });
        mBinding.rlBtnClassify.setOnClickListener(v -> {
            mViewModel.videoType();
        });
        mBinding.btnSubmit.setOnClickListener(v -> submit());
    }


    private void submit() {
        if (StringUtils.isEmpty(mCoverVideoUrl)) {
            ToastUtils.showShort("请上传视频");
            return;
        }
        if (StringUtils.isEmpty(StringUtils.getEditText(mBinding.etContent))) {
            ToastUtils.showShort("请添加文字描述");
            return;
        }
        if (!AccountManager.getInstance().isLogin()) {
            Intent intent2 = new Intent(this, LoginActivity.class);
            intent2.putExtras(new Bundle());
            startActivity(intent2);
            return;
        }

        VideoPublishReq req = new VideoPublishReq();
        if (mAddress != null) {
            req.setLongitude(String.valueOf(mAddress.getLon()));
            req.setLatitude(String.valueOf(mAddress.getLat()));
        }

        if (mBinding.tvLinkShopLable.getTag() != null && mBinding.tvLinkShopLable.getTag() instanceof ShopList) {
            ShopList shopInfo = (ShopList) mBinding.tvLinkShopLable.getTag();
            req.setLongitude(shopInfo.getLongitude());
            req.setLatitude(shopInfo.getLatitude());
            req.setShop_id(String.valueOf(shopInfo.getId()));
        }
        if (mAddress == null && mBinding.tvLinkShopLable.getTag() == null) {
            req.setLongitude(MyApp.getLngLat().get(0));
            req.setLatitude(MyApp.getLngLat().get(1));
        }
        req.setJuli_state(mAddress == null ? 0 : 1);
        if (mBinding.tvLinkShopLable.getTag() == null) {
            req.setShop_id(String.valueOf(0));
        }
        req.setType_id(String.valueOf(mBusinessTypeId));
        req.setVideo_desc(StringUtils.getEditText(mBinding.etContent));
        req.setVideo_path(mCoverVideoUrl);
        req.setCity(MyApp.getCity());
        mViewModel.upVideo(req);
    }

}
