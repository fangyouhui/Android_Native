package com.pai8.ke.activity.takeaway.ui;

import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import androidx.recyclerview.widget.LinearLayoutManager;

import com.bigkoo.pickerview.builder.OptionsPickerBuilder;
import com.bigkoo.pickerview.builder.TimePickerBuilder;
import com.bigkoo.pickerview.listener.OnTimeSelectListener;
import com.bigkoo.pickerview.view.OptionsPickerView;
import com.bigkoo.pickerview.view.TimePickerView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.lhs.library.base.BaseActivity;
import com.lhs.library.base.BaseAppConstants;
import com.lhs.library.base.BaseBottomDialogFragment;
import com.luck.picture.lib.entity.LocalMedia;
import com.luck.picture.lib.listener.OnResultCallbackListener;
import com.pai8.ke.R;
import com.pai8.ke.activity.takeaway.adapter.GroupAddDetailAdapter;
import com.pai8.ke.activity.takeaway.adapter.GroupBannerAdapter;
import com.pai8.ke.activity.takeaway.entity.event.NotifyEvent;
import com.pai8.ke.activity.takeaway.entity.req.GroupFoodReq;
import com.pai8.ke.activity.takeaway.entity.resq.GoodsInfoModel;
import com.pai8.ke.activity.takeaway.entity.resq.term;
import com.pai8.ke.activity.takeaway.widget.ChooseShopVideoBottomDialogFragment;
import com.pai8.ke.databinding.ActivityEditGroupGoodBinding;
import com.pai8.ke.entity.BusinessTypeResult;
import com.pai8.ke.manager.AccountManager;
import com.pai8.ke.manager.UploadFileManager;
import com.pai8.ke.utils.ImageLoadUtils;
import com.pai8.ke.utils.PictureSelectorHelper;
import com.pai8.ke.utils.ToastUtils;
import com.pai8.ke.viewmodel.AddGroupGoodViewModel;

import org.greenrobot.eventbus.EventBus;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static com.pai8.ke.activity.takeaway.Constants.EVENT_TYPE_REFRESH_SHOP_GROUP;

/**
 * 编辑团购商品
 */
public class EditGroupGoodActivity extends BaseActivity<AddGroupGoodViewModel, ActivityEditGroupGoodBinding> {
    private GroupBannerAdapter groupBannerAdapter;
    private GroupAddDetailAdapter groupDetailAdapter;
    private String mGoodId = "";

    @Override
    public void initView(@org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        mBinding.ivVideo.setOnClickListener(v -> {
            ChooseShopVideoBottomDialogFragment dialogFragment = new ChooseShopVideoBottomDialogFragment();
            dialogFragment.setListener(new BaseBottomDialogFragment.OnDialogListener() {
                @Override
                public void onConfirmClickListener(@NotNull Object data) {
                    String videoPath = (String) data;
                    Glide.with(getBaseContext())
                            .load(Uri.fromFile(new File(videoPath)))
                            .skipMemoryCache(true) // 不使用内存缓存
                            .diskCacheStrategy(DiskCacheStrategy.NONE) // 不使用磁盘缓存
                            .into(mBinding.ivVideo);
                    UploadFileManager.getInstance().upload(videoPath, new UploadFileManager.Callback() {
                        @Override
                        public void onSuccess(String url, String key) {
                            mBinding.ivVideo.setTag(key);
                        }

                        @Override
                        public void onError(String msg) {

                        }
                    });
                }
            });

            dialogFragment.showNow(getSupportFragmentManager(), "choose_video");

        });
        mBinding.ivAddBanner.setOnClickListener(v -> chooseImgWithBanner());

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        mBinding.rvBanner.setLayoutManager(linearLayoutManager);
        groupBannerAdapter = new GroupBannerAdapter(this, null);
        mBinding.rvBanner.setAdapter(groupBannerAdapter);
        groupBannerAdapter.setListener((item, position) -> { //删除操作
            groupBannerAdapter.remove(position);
            mBinding.ivAddBanner.setVisibility(View.VISIBLE);
            for (String key : goodsInfoModel.cover_key) {
                if (item.getRealPath().contains(key)) {
                    goodsInfoModel.cover_key.remove(key);
                    break;
                }
            }
        });

        mBinding.tvCategory.setOnClickListener(v -> {
            List<BusinessTypeResult> list = mViewModel.getVideotypeData().getValue();
            List<String> options1Items = new ArrayList<>();
            for (int i = 0; i < list.size(); i++) {
                options1Items.add(list.get(i).getType_name());
            }

            OptionsPickerView mPvType = new OptionsPickerBuilder(this, (options1, option2, options3, view) -> {
                //返回的分别是三个级别的选中位置
                String tx = list.get(options1).getType_name();
                mBinding.tvCategory.setText(tx);
                mBinding.tvCategory.setTag(list.get(options1));
            }).setDecorView(findViewById(R.id.rl_merchant)).build();
            mPvType.setPicker(options1Items);
            mPvType.show();
        });
        mBinding.startTimeText.setOnClickListener(v -> timeStartChoose());
        mBinding.endTimeText.setOnClickListener(v -> timeEndChoose());

        mBinding.ivAdd.setOnClickListener(v -> chooseImgWithDetail());

        LinearLayoutManager linearLayoutManager2 = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        mBinding.rvDetail.setLayoutManager(linearLayoutManager2);
        groupDetailAdapter = new GroupAddDetailAdapter(this, null);
        mBinding.rvDetail.setAdapter(groupDetailAdapter);
        groupDetailAdapter.setListener((item, position) -> { //删除操作
            groupDetailAdapter.remove(position);
            mBinding.ivAdd.setVisibility(View.VISIBLE);
            for (String key : goodsInfoModel.details_key) {
                if (item.getRealPath().contains(key)) {
                    goodsInfoModel.details_key.remove(key);
                    break;
                }
            }
        });

        mBinding.tvDel.setOnClickListener(v -> mViewModel.groupFoodDelete(mGoodId));
        mBinding.tvPublish.setOnClickListener(v -> publish());
    }

    @Override
    public void initData() {
        mGoodId = getIntent().getStringExtra(BaseAppConstants.BundleConstant.ARG_PARAMS_0);
        mViewModel.setvideotype();
    }

    @Override
    public void addObserve() {
        mViewModel.getVideotypeData().observe(this, list -> {
            mViewModel.getGoodInfo(mGoodId);
        });

        mViewModel.getGoodInfoData().observe(this, data -> bindViewData(data));

        mViewModel.getGroupFoodDeleteData().observe(this, data -> {
            ToastUtils.showShort("下架成功");
            success();
        });

        mViewModel.getEditGoodsData().observe(this, data -> {
            ToastUtils.showShort("编辑成功");
            success();
        });

    }

    private void success() {
        EventBus.getDefault().post(new NotifyEvent(EVENT_TYPE_REFRESH_SHOP_GROUP));
        dismissLoading();
        finish();
    }

    private void chooseImgWithBanner() {
        List<LocalMedia> selectionData = new ArrayList<>();
        OnResultCallbackListener<LocalMedia> listener = new OnResultCallbackListener<LocalMedia>() {
            @Override
            public void onResult(List<LocalMedia> result) {
                groupBannerAdapter.addData(result);
                mBinding.ivAddBanner.setVisibility(groupBannerAdapter.getData().size() >= 6 ? View.GONE : View.VISIBLE);
                for (LocalMedia localMedia : result) {
                    UploadFileManager.getInstance().upload(localMedia.getRealPath(), new UploadFileManager.Callback() {
                        @Override
                        public void onSuccess(String url, String key) {
                            goodsInfoModel.cover_key.add(key);
                        }

                        @Override
                        public void onError(String msg) {
                            ToastUtils.showShort("轮播图上传失败:" + msg);
                        }
                    });
                }
            }

            @Override
            public void onCancel() {

            }
        };
        PictureSelectorHelper.picMultiple(this, 6, selectionData, listener);
    }

    private void chooseImgWithDetail() {
        List<LocalMedia> selectionData = new ArrayList<>();
        OnResultCallbackListener<LocalMedia> listener = new OnResultCallbackListener<LocalMedia>() {
            @Override
            public void onResult(List<LocalMedia> result) {
                groupDetailAdapter.addData(result);
                mBinding.ivAdd.setVisibility(groupDetailAdapter.getData().size() >= 6 ? View.GONE : View.VISIBLE);
                for (LocalMedia localMedia : result) {
                    UploadFileManager.getInstance().upload(localMedia.getRealPath(), new UploadFileManager.Callback() {
                        @Override
                        public void onSuccess(String url, String key) {
                            goodsInfoModel.details_key.add(key);
                        }

                        @Override
                        public void onError(String msg) {
                            ToastUtils.showShort("商品详情图上传失败:" + msg);
                        }
                    });
                }
            }

            @Override
            public void onCancel() {

            }
        };
        PictureSelectorHelper.picMultiple(this, 6, selectionData, listener);
    }


    private void publish() {
        if (mBinding.ivVideo.getTag() == null) {
            ToastUtils.showShort("封面视频不能为空");
            return;
        }
        if (groupBannerAdapter.getData().isEmpty()) {
            ToastUtils.showShort("轮播图片不能为空");
            return;
        }
        String shopName = mBinding.etName.getText().toString();
        if (TextUtils.isEmpty(shopName)) {
            ToastUtils.showShort("商品名称不能为空");
            return;
        }
        String originPriceT = mBinding.etPrice.getText().toString();
        if (TextUtils.isEmpty(originPriceT)) {
            ToastUtils.showShort("商品价格不能为空");
            return;
        }
        String sellerPrice = mBinding.etPrice2.getText().toString();
        if (TextUtils.isEmpty(sellerPrice)) {
            ToastUtils.showShort("团购价格不能为空");
            return;
        }
        String stockNum = mBinding.etStock.getText().toString();
        if (TextUtils.isEmpty(stockNum)) {
            ToastUtils.showShort("商品库存不能为空");
            return;
        }
        if (mBinding.tvCategory.getTag() == null) {
            ToastUtils.showShort("团购分类不能为空");
            return;
        }
        if (TextUtils.isEmpty(mBinding.groupDesc.getText().toString())) {
            ToastUtils.showShort("团购内容不能为空");
            return;
        }
        if (TextUtils.isEmpty(mBinding.startTimeText.getText().toString())) {
            ToastUtils.showShort("有效日期开始时间不能为空");
            return;
        }
        if (TextUtils.isEmpty(mBinding.endTimeText.getText().toString())) {
            ToastUtils.showShort("有效日期结束时间不能为空");
            return;
        }
        if (TextUtils.isEmpty(mBinding.etZhuYi.getText().toString())) {
            ToastUtils.showShort("注意事项不能为空");
            return;
        }
        if (TextUtils.isEmpty(mBinding.etDesc.getText().toString())) {
            ToastUtils.showShort("商品详情不能为空");
            return;
        }
        if (groupDetailAdapter.getData().isEmpty()) {
            ToastUtils.showShort("详情图片不能为空");
            return;
        }

        showLoading();

        GroupFoodReq groupFoodReq = new GroupFoodReq();
        groupFoodReq.shop_id = AccountManager.getInstance().getShopId();
        groupFoodReq.goods_id = mGoodId;
        groupFoodReq.video = (String) mBinding.ivVideo.getTag();
        StringBuilder coverBuilder = new StringBuilder();
        for (String coverKey : goodsInfoModel.cover_key) {
            coverBuilder.append(coverKey).append(",");
        }
        groupFoodReq.cover = coverBuilder.substring(0, coverBuilder.length() - 1);

        groupFoodReq.title = shopName;
        groupFoodReq.origin_price = originPriceT;
        groupFoodReq.sell_price = sellerPrice;
        groupFoodReq.stock = stockNum;
        BusinessTypeResult businessTypeResult = (BusinessTypeResult) mBinding.tvCategory.getTag();
        groupFoodReq.food_type = businessTypeResult.getId() + "";
        groupFoodReq.desc = mBinding.groupDesc.getText().toString();
        groupFoodReq.term = getTime(mBinding.startTimeText.getText().toString())
                + "-" + getTime(mBinding.endTimeText.getText().toString());
        groupFoodReq.is_weekend = mBinding.checkbox.isChecked() ? "true" : "false";
        groupFoodReq.matter = mBinding.etZhuYi.getText().toString();
        groupFoodReq.details = mBinding.etDesc.getText().toString();


        StringBuilder detailBuilder = new StringBuilder();
        for (String detailKey : goodsInfoModel.details_key) {
            detailBuilder.append(detailKey).append(",");
        }
        groupFoodReq.details_img = detailBuilder.substring(0, detailBuilder.length() - 1);
        groupFoodReq.status = 1;
        mViewModel.editGoods(groupFoodReq);

    }


    // 将字符串转为时间戳
    private String getTime(String user_time) {
        String re_time = null;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date d;
        try {
            d = sdf.parse(user_time);
            long l = d.getTime();
            String str = String.valueOf(l);
            re_time = str.substring(0, 10);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return re_time;
    }

    // 将时间戳转为字符串
    private String getStrTime(String cc_time) {
        String re_StrTime = null;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        // 例如：
        long lcc_time = Long.valueOf(cc_time);
        re_StrTime = sdf.format(new Date(lcc_time * 1000L));
        return re_StrTime;
    }

    private void timeEndChoose() {
        if (TextUtils.isEmpty(mBinding.startTimeText.getText().toString())) {
            ToastUtils.showShort("请先选择开始日期！");
            return;
        }
        Calendar selectedDate = Calendar.getInstance();
        Calendar startDate = Calendar.getInstance();
        startDate.set(2000, 0, 1);
        Calendar endDate = Calendar.getInstance();
        endDate.set(2099, 11, 1);
        TimePickerView endTime = new TimePickerBuilder(this, new OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date, View v) {//选中事件回
                DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
                Date startT = null;
                try {
                    startT = df.parse(mBinding.startTimeText.getText().toString());
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                if (startT.getTime() > date.getTime()) {
                    ToastUtils.showShort("请选择比开始时间后的日期");
                    return;
                }
                mBinding.endTimeText.setText(df.format(date));
            }
        })
                .setType(new boolean[]{true, true, true, false, false, false})// 默认全部显示
                .setDate(selectedDate)// 如果不设置的话，默认是系统时间*/
                .setRangDate(startDate, endDate)//起始终止年月日设定
                .setLabel("年", "月", "日", "时", "分", "秒")//默认设置为年月日时分秒
                //    .setDecorView((ViewGroup) findViewById(R.id.tuangou_view))
                .setTitleText("选择时间")
                .setTitleColor(Color.parseColor("#111111"))
                .setTitleSize(16)
                .setCancelColor(Color.parseColor("#999999"))
                .setSubmitColor(Color.parseColor("#2f2f2f"))
                .build();
        endTime.show();
    }

    private void timeStartChoose() {
        Calendar selectedDate = Calendar.getInstance();
        Calendar startDate = Calendar.getInstance();
        startDate.set(2000, 0, 1);
        Calendar endDate = Calendar.getInstance();
        endDate.set(2099, 11, 1);
        TimePickerView startTime = new TimePickerBuilder(this, new OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date, View v) {//选中事件回
                DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
                mBinding.startTimeText.setText(df.format(date));
            }
        })
                .setType(new boolean[]{true, true, true, false, false, false})// 默认全部显示
                .setDate(selectedDate)// 如果不设置的话，默认是系统时间*/
                .setRangDate(startDate, endDate)//起始终止年月日设定
                .setLabel("年", "月", "日", "时", "分", "秒")//默认设置为年月日时分秒
                //    .setDecorView((ViewGroup) findViewById(R.id.tuangou_view))
                .setTitleText("选择时间")
                .setTitleColor(Color.parseColor("#111111"))
                .setTitleSize(16)
                .setCancelColor(Color.parseColor("#999999"))
                .setSubmitColor(Color.parseColor("#2f2f2f"))
                .build();
        startTime.show();

    }

    private GoodsInfoModel goodsInfoModel;

    private void bindViewData(GoodsInfoModel data) {
        goodsInfoModel = data;
        ImageLoadUtils.loadCover(this, data.video_url, mBinding.ivVideo);
        mBinding.ivVideo.setTag(data.video);

        List<LocalMedia> coverList = new ArrayList<>();
        for (String url : data.cover) {
            LocalMedia localMedia = new LocalMedia();
            localMedia.setRealPath(url);
            coverList.add(localMedia);
        }
        groupBannerAdapter.setData(coverList);
        mBinding.etName.setText(data.title);
        mBinding.etPrice.setText(data.origin_price);
        mBinding.etPrice2.setText(data.sell_price);
        mBinding.etStock.setText(data.stock);
        mBinding.groupDesc.setText(data.desc);
        term te = data.term;
        mBinding.startTimeText.setText(getStrTime(te.start_time));
        mBinding.endTimeText.setText(getStrTime(te.end_time));
        mBinding.checkbox.setChecked(data.is_weekend.equals("true"));
        mBinding.etZhuYi.setText(data.matter);
        mBinding.etDesc.setText(data.details);

        List<LocalMedia> list2 = new ArrayList<>();
        for (String s : data.details_img) {
            LocalMedia localMedia = new LocalMedia();
            localMedia.setRealPath(s);
            list2.add(localMedia);
        }

        groupDetailAdapter.setData(list2);
        for (BusinessTypeResult businessTypeResult : mViewModel.getVideotypeData().getValue()) {
            if (String.valueOf(businessTypeResult.getId()).equals(data.food_type)) {
                mBinding.tvCategory.setText(businessTypeResult.getType_name());
                mBinding.tvCategory.setTag(businessTypeResult);
                break;
            }
        }
    }


}
