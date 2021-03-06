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
import com.blankj.utilcode.util.KeyboardUtils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.lhs.library.base.BaseActivity;
import com.lhs.library.base.BaseBottomDialogFragment;
import com.luck.picture.lib.entity.LocalMedia;
import com.luck.picture.lib.listener.OnResultCallbackListener;
import com.pai8.ke.R;
import com.pai8.ke.activity.takeaway.adapter.GroupAddDetailAdapter;
import com.pai8.ke.activity.takeaway.adapter.GroupBannerAdapter;
import com.pai8.ke.activity.takeaway.entity.event.NotifyEvent;
import com.pai8.ke.activity.takeaway.entity.req.GroupFoodReq;
import com.pai8.ke.activity.takeaway.widget.ChooseShopVideoBottomDialogFragment;
import com.pai8.ke.databinding.ActivityAddGroupGoodBinding;
import com.pai8.ke.entity.BusinessTypeResult;
import com.pai8.ke.manager.AccountManager;
import com.pai8.ke.manager.UploadFileManager;
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
 * 添加团购商品
 */
public class AddGroupGoodActivity extends BaseActivity<AddGroupGoodViewModel, ActivityAddGroupGoodBinding> {
    private GroupBannerAdapter groupBannerAdapter;
    private GroupAddDetailAdapter groupDetailAdapter;

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
                    mBinding.ivVideo.setTag(videoPath);
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
            mBinding.ivAddBanner.setVisibility(groupBannerAdapter.getData().size() < 6 ? View.VISIBLE : View.GONE);
        });

        mBinding.tvCategory.setOnClickListener(v -> {
            KeyboardUtils.hideSoftInput(this);
            mViewModel.setvideotype();
        });
        mBinding.startTimeText.setOnClickListener(v -> {
            KeyboardUtils.hideSoftInput(this);
            timeStartChoose();
        });
        mBinding.endTimeText.setOnClickListener(v -> {
            KeyboardUtils.hideSoftInput(this);
            timeEndChoose();
        });

        mBinding.ivAdd.setOnClickListener(v -> chooseImgWithDetail());

        LinearLayoutManager linearLayoutManager2 = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        mBinding.rvDetail.setLayoutManager(linearLayoutManager2);
        groupDetailAdapter = new GroupAddDetailAdapter(this, null);
        mBinding.rvDetail.setAdapter(groupDetailAdapter);
        groupDetailAdapter.setListener((item, position) -> {
            groupDetailAdapter.remove(position);
            mBinding.ivAdd.setVisibility(View.VISIBLE);
        });

        mBinding.tvPublish.setOnClickListener(v -> publish());

    }

    @Override
    public void addObserve() {
        mViewModel.getVideotypeData().observe(this, list -> {
            List<String> options1Items = new ArrayList<>();
            for (int i = 0; i < list.size(); i++) {
                options1Items.add(list.get(i).getType_name());
            }

            OptionsPickerView mPvType = new OptionsPickerBuilder(AddGroupGoodActivity.this, (options1, option2, options3, v) -> {
                //返回的分别是三个级别的选中位置
                String tx = list.get(options1).getType_name();
                mBinding.tvCategory.setText(tx);
                mBinding.tvCategory.setTag(list.get(options1));
            }).setDecorView(findViewById(R.id.rl_merchant)).build();
            mPvType.setPicker(options1Items);
            mPvType.show();
        });

        mViewModel.getAddGoodData().observe(this, data -> {
            ToastUtils.showShort("上架成功");
            success();
        });

    }

    private void success() {
        EventBus.getDefault().post(new NotifyEvent(EVENT_TYPE_REFRESH_SHOP_GROUP));
        dismissLoading();
        finish();
    }

    private void chooseImgWithBanner() {
        List<LocalMedia> selectionData = groupBannerAdapter.getData();
        OnResultCallbackListener<LocalMedia> listener = new OnResultCallbackListener<LocalMedia>() {
            @Override
            public void onResult(List<LocalMedia> result) {
                mBinding.ivAddBanner.setVisibility(result.size() >= 6 ? View.GONE : View.VISIBLE);
                groupBannerAdapter.setData(result);
            }

            @Override
            public void onCancel() {

            }
        };
        PictureSelectorHelper.picMultiple(this, 6, selectionData, listener);
    }

    private void chooseImgWithDetail() {
        List<LocalMedia> selectionData = groupDetailAdapter.getData();
        OnResultCallbackListener<LocalMedia> listener = new OnResultCallbackListener<LocalMedia>() {
            @Override
            public void onResult(List<LocalMedia> result) {
                mBinding.ivAdd.setVisibility(result.size() >= 6 ? View.GONE : View.VISIBLE);
                groupDetailAdapter.setData(result);
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

        String stockNum = mBinding.etPackPrice.getText().toString();

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
        //上传视频
        String videoPath = (String) mBinding.ivVideo.getTag();
        UploadFileManager.getInstance().upload(videoPath, new UploadFileManager.Callback() {
            @Override
            public void onSuccess(String url, String key) {
                groupFoodReq.video = key;
                //上传轮播图
                List<String> bannerKeys = new ArrayList<>();
                for (LocalMedia datum : groupBannerAdapter.getData()) {
                    UploadFileManager.getInstance().upload(datum.getRealPath(), new UploadFileManager.Callback() {
                        @Override
                        public void onSuccess(String url, String key) {
                            bannerKeys.add(key);
                            if (bannerKeys.size() == groupBannerAdapter.getData().size()) {
                                StringBuilder coverBuilder = new StringBuilder();
                                for (String bannerKey : bannerKeys) {
                                    coverBuilder.append(bannerKey).append(",");
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
                                //上传详情图
                                List<String> detailKeys = new ArrayList<>();
                                for (LocalMedia groupDetailAdapterDatum : groupDetailAdapter.getData()) {
                                    UploadFileManager.getInstance().upload(groupDetailAdapterDatum.getRealPath(), new UploadFileManager.Callback() {
                                        @Override
                                        public void onSuccess(String url, String key) {
                                            detailKeys.add(key);
                                            if (detailKeys.size() == groupDetailAdapter.getData().size()) {
                                                StringBuilder detailBuilder = new StringBuilder();
                                                for (String detailKey : detailKeys) {
                                                    detailBuilder.append(detailKey).append(",");
                                                }
                                                groupFoodReq.details_img = detailBuilder.substring(0, detailBuilder.length() - 1);
                                                groupFoodReq.status = 1;
                                                mViewModel.addGood(groupFoodReq);

                                            }
                                        }

                                        @Override
                                        public void onError(String msg) {
                                            dismissLoading();
                                            ToastUtils.showShort("详情图上传失败：" + msg);
                                        }
                                    });
                                }

                            }
                        }

                        @Override
                        public void onError(String msg) {
                            dismissLoading();
                            ToastUtils.showShort("轮播图上传失败：" + msg);
                        }
                    });
                }
            }

            @Override
            public void onError(String msg) {
                dismissLoading();
                ToastUtils.showShort("视频上传失败：" + msg);
            }
        });


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


}
