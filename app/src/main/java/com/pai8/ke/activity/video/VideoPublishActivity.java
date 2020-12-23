package com.pai8.ke.activity.video;

import android.content.Intent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gh.qiniushortvideo.ChooseVideo;
import com.gh.qiniushortvideo.activity.ConfigActivity;
import com.gh.qiniushortvideo.activity.MediaSelectActivity;
import com.gh.qiniushortvideo.activity.VideoRecordActivity;
import com.hjq.bar.OnTitleBarListener;
import com.pai8.ke.R;
import com.pai8.ke.activity.common.VideoViewActivity;
import com.pai8.ke.activity.me.AddressChooseActivity;
import com.pai8.ke.api.Api;
import com.pai8.ke.app.MyApp;
import com.pai8.ke.base.BaseActivity;
import com.pai8.ke.base.BaseEvent;
import com.pai8.ke.base.retrofit.BaseObserver;
import com.pai8.ke.base.retrofit.RxSchedulers;
import com.pai8.ke.entity.Address;
import com.pai8.ke.entity.req.VideoPublishReq;
import com.pai8.ke.entity.resp.ShopList;
import com.pai8.ke.entity.resp.ShopListResp;
import com.pai8.ke.global.EventCode;
import com.pai8.ke.global.GlobalConstants;
import com.pai8.ke.manager.UploadFileManager;
import com.pai8.ke.utils.EventBusUtils;
import com.pai8.ke.utils.ImageLoadUtils;
import com.pai8.ke.utils.PickerUtils;
import com.pai8.ke.utils.StringUtils;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

import androidx.cardview.widget.CardView;
import butterknife.BindView;
import butterknife.OnClick;

import static com.pai8.ke.global.EventCode.EVENT_VIDEO_LIST_REFRESH;

/**
 * 发布视频
 * Created by gh on 2020/11/14.
 */
public class VideoPublishActivity extends BaseActivity {

    @BindView(R.id.ll_wrap_btn_video)
    LinearLayout llWrapBtnVideo;
    @BindView(R.id.civ_video_cover)
    ImageView ivVideoCover;
    @BindView(R.id.cv_wrap_video)
    CardView cvWrapVideo;
    @BindView(R.id.tv_address)
    TextView tvAddress;
    @BindView(R.id.tv_link_shop)
    TextView tvLinkShop;
    @BindView(R.id.tv_classify)
    TextView tvClassify;
    @BindView(R.id.et_content)
    EditText etContent;
    @BindView(R.id.ll_container)
    LinearLayout llContainer;

    private int mBusinessTypePosition;

    private int mBusinessTypeId;
    private String mCoverVideoUrl = "";
    private String mCoverVideoPath = "";
    private ShopList mShopInfo;
    private Address mAddress;

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventChooseVideo(ChooseVideo event) {
        if (event == null) return;
        cvWrapVideo.setVisibility(View.VISIBLE);
        llWrapBtnVideo.setVisibility(View.GONE);
        mCoverVideoPath = event.getPath();
        ImageLoadUtils.loadCover(this, mCoverVideoPath, ivVideoCover);
        showLoadingDialog("视频上传...");
        UploadFileManager.getInstance().upload(mCoverVideoPath, new UploadFileManager.Callback() {
            @Override
            public void onSuccess(String url, String key) {
                dismissLoadingDialog();
                toast("视频上传成功");
                mCoverVideoUrl = key;
            }

            @Override
            public void onError(String msg) {
                dismissLoadingDialog();
                toast("视频上传失败：" + msg);
                mCoverVideoUrl = "";
            }
        });
    }

    @Override
    protected boolean isRegisterEventBus() {
        return true;
    }

    @Override
    protected void receiveEvent(BaseEvent event) {
        super.receiveEvent(event);
        switch (event.getCode()) {
            case EventCode.EVENT_CHOOSE_SHOP:
                mShopInfo = (ShopList) event.getData();
                tvLinkShop.setText(mShopInfo.getShop_name());
                break;
            case EventCode.EVENT_CHOOSE_ADDRESS:
                mAddress = (Address) event.getData();
                tvAddress.setText(mAddress.getTitle());
                break;
        }
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_video_publish;
    }

    @Override
    public void initView() {
        mTitleBar.setTitle("发布视频");
    }


    @Override
    public void initListener() {
        mTitleBar.setOnTitleBarListener(new OnTitleBarListener() {
            @Override
            public void onLeftClick(View v) {
                finish();
            }

            @Override
            public void onTitleClick(View v) {

            }

            @Override
            public void onRightClick(View v) {

            }
        });
    }

    @OnClick({R.id.tv_btn_galley, R.id.tv_btn_take_photo, R.id.iv_btn_player, R.id.iv_btn_delete,
            R.id.rl_btn_address, R.id.rl_btn_link_shop, R.id.rl_btn_classify, R.id.btn_submit})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_btn_galley:
                Intent it = new Intent(this, MediaSelectActivity.class);
                it.putExtra(MediaSelectActivity.TYPE, MediaSelectActivity.TYPE_VIDEO_EDIT);
                startActivity(it);
                break;
            case R.id.tv_btn_take_photo:
                Intent intent = new Intent(this, VideoRecordActivity.class);
                intent.putExtra(VideoRecordActivity.PREVIEW_SIZE_RATIO,
                        ConfigActivity.PREVIEW_SIZE_RATIO_POS);
                intent.putExtra(VideoRecordActivity.PREVIEW_SIZE_LEVEL,
                        ConfigActivity.PREVIEW_SIZE_LEVEL_POS);
                intent.putExtra(VideoRecordActivity.ENCODING_MODE,
                        ConfigActivity.ENCODING_MODE_LEVEL_POS);
                intent.putExtra(VideoRecordActivity.ENCODING_SIZE_LEVEL,
                        ConfigActivity.ENCODING_SIZE_LEVEL_POS);
                intent.putExtra(VideoRecordActivity.ENCODING_BITRATE_LEVEL,
                        ConfigActivity.ENCODING_BITRATE_LEVEL_POS);
                intent.putExtra(VideoRecordActivity.AUDIO_CHANNEL_NUM,
                        ConfigActivity.AUDIO_CHANNEL_NUM_POS);
                startActivity(intent);
                break;
            case R.id.iv_btn_player:
                VideoViewActivity.launch(this, VideoViewActivity.TYPE_LOCAL, mCoverVideoPath);
                break;
            case R.id.iv_btn_delete:
                cvWrapVideo.setVisibility(View.GONE);
                llWrapBtnVideo.setVisibility(View.VISIBLE);
                mCoverVideoUrl = "";
                break;
            case R.id.rl_btn_address:
                launch(AddressChooseActivity.class);
                break;
            case R.id.rl_btn_link_shop:
                launch(ShopSearchListActivity.class);
                break;
            case R.id.rl_btn_classify:
                showLoadingDialog(null);
                PickerUtils.showBusinessType(this, mBusinessTypePosition, (position, id, name) -> {
                    dismissLoadingDialog();
                    if (position == -1) {
                        return;
                    }
                    mBusinessTypePosition = position;
                    mBusinessTypeId = id;
                    tvClassify.setText(name);
                });
                break;
            case R.id.btn_submit:
                if (StringUtils.isEmpty(mCoverVideoUrl)) {
                    toast("请上传视频");
                    return;
                }
                if (StringUtils.isEmpty(StringUtils.getEditText(etContent))) {
                    toast("请添加文字描述");
                    return;
                }
//                if (mAddress == null) {
//                    toast("请添加地址");
//                    return;
//                }
//                if (mShopInfo == null || mShopInfo.getId() == 0) {
//                    toast("请选择关联商铺");
//                    return;
//                }
//                if (mBusinessTypeId == 0) {
//                    toast("请选择分类");
//                    return;
//                }
                VideoPublishReq req = new VideoPublishReq();
                if (mAddress != null) {
                    req.setLongitude(String.valueOf(mAddress.getLon()));
                    req.setLatitude(String.valueOf(mAddress.getLat()));
                }
                if (mShopInfo != null) {
                    req.setLongitude(mShopInfo.getLongitude());
                    req.setLatitude(mShopInfo.getLatitude());
                }
                if (mAddress == null && mShopInfo == null) {
                    req.setLongitude(MyApp.getLngLat().get(0));
                    req.setLatitude(MyApp.getLngLat().get(1));
                }
                req.setType_id(String.valueOf(mBusinessTypeId));
                req.setVideo_desc(StringUtils.getEditText(etContent));
                req.setVideo_path(mCoverVideoUrl);
                req.setCity(MyApp.getCity());
                if (mShopInfo == null) {
                    req.setShop_id(String.valueOf(0));
                } else {
                    req.setShop_id(String.valueOf(mShopInfo.getId()));
                }
                Api.getInstance().upVideo(req)
                        .doOnSubscribe(disposable -> {

                        })
                        .compose(RxSchedulers.io_main())
                        .subscribe(new BaseObserver<Object>() {
                            @Override
                            protected void onSuccess(Object o) {
                                toast("视频发布成功");
                                finish();
                                EventBusUtils.sendEvent(new BaseEvent(EVENT_VIDEO_LIST_REFRESH));
                            }

                            @Override
                            protected void onError(String msg, int errorCode) {
                                super.onError(msg, errorCode);
                            }
                        });
                break;
        }
    }

}
