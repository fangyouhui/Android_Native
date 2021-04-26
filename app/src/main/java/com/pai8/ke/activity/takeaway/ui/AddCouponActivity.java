package com.pai8.ke.activity.takeaway.ui;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.IntDef;

import com.bigkoo.pickerview.builder.OptionsPickerBuilder;
import com.bigkoo.pickerview.builder.TimePickerBuilder;
import com.bigkoo.pickerview.listener.OnTimeSelectListener;
import com.bigkoo.pickerview.view.OptionsPickerView;
import com.bigkoo.pickerview.view.TimePickerView;
import com.blankj.utilcode.constant.TimeConstants;
import com.blankj.utilcode.util.KeyboardUtils;
import com.blankj.utilcode.util.TimeUtils;
import com.lhs.library.base.BaseActivity;
import com.lhs.library.base.NoViewModel;
import com.pai8.ke.R;
import com.pai8.ke.activity.me.entity.resp.ShopCouponListResult;
import com.pai8.ke.api.Api;
import com.pai8.ke.base.BaseEvent;
import com.pai8.ke.base.retrofit.BaseObserver;
import com.pai8.ke.base.retrofit.RxSchedulers;
import com.pai8.ke.databinding.ActivityAddCouponBinding;
import com.pai8.ke.global.EventCode;
import com.pai8.ke.manager.AccountManager;
import com.pai8.ke.utils.AppUtils;
import com.pai8.ke.utils.EventBusUtils;
import com.pai8.ke.utils.StringUtils;
import com.pai8.ke.utils.ToastUtils;
import com.pai8.ke.widget.BottomDialog;

import org.jetbrains.annotations.Nullable;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * 添加/编辑 商家优惠券
 */
public class AddCouponActivity extends BaseActivity<NoViewModel, ActivityAddCouponBinding> {

    private int numType;
    private int num = 0;

    private BottomDialog mChooseBottomDialog;

    public static final int TYPE_ADD = 1;
    public static final int TYPE_EDIT = 2;
    private ShopCouponListResult.CouponListBean mCouponGetList;
    private OptionsPickerView mPvType;
    private int mOptions = -1;

    @IntDef({TYPE_ADD, TYPE_EDIT})
    public @interface Type {
    }

    private int mType;

    public static void launchAdd(Context context) {
        Intent intent = new Intent(context, AddCouponActivity.class);
        Bundle bundle = new Bundle();
        intent.putExtra("type", TYPE_ADD);
        intent.putExtras(bundle);
        context.startActivity(intent);
    }

    public static void launchEdit(Context context, ShopCouponListResult.CouponListBean bean) {
        Intent intent = new Intent(context, AddCouponActivity.class);
        Bundle bundle = new Bundle();
        intent.putExtra("type", TYPE_EDIT);
        intent.putExtra("couponGetListResp", bean);
        intent.putExtras(bundle);
        context.startActivity(intent);
    }

    @Override
    public void initView(@Nullable Bundle savedInstanceState) {
        Bundle extras = getIntent().getExtras();
        mType = extras.getInt("type");
        mCouponGetList = (ShopCouponListResult.CouponListBean) extras.getSerializable("couponGetListResp");
        if (mType == TYPE_ADD) {
            setToolBarTitle("创建优惠券");
        } else {
            setToolBarTitle("编辑优惠券");
            if (mCouponGetList.getType() == TYPE_ADD) {
                mBinding.tvChooseType.setText("满减券");
            } else {
                mBinding.tvChooseType.setText("运费抵扣券");
            }
            num = mCouponGetList.getNum();
            //tvChooseTime.setText(String.format("%d天", (mCouponGetList.getEnd_time() - mCouponGetList.getAdd_time()) / (60 * 60 * 24)));
            mBinding.etPrice.setText(mCouponGetList.getDis_price());
            mBinding.etFull.setText(mCouponGetList.getTrig_price());
            mBinding.tvChooseNum.setText(num == 0 ? "无限制" : num + "");
        }
        initListener();
    }

    private void initListener() {
        mBinding.rlType.setOnClickListener(v -> {
            if (mType == TYPE_EDIT) {
                return;
            }
            if (mPvType == null) {
                List<String> list = new ArrayList<>();
                list.add("满减券");
                list.add("运费抵扣券");
                mPvType = new OptionsPickerBuilder(this, (options1, option2, options3, view) -> {
                    mBinding.rlManJian.setVisibility(options1 == 1 ? View.GONE : View.VISIBLE);
                    mBinding.tvChooseType.setText(list.get(options1));
                    mOptions = options1;
                })
                        .isRestoreItem(false)
                        .setDecorView(findViewById(R.id.rl_merchant))
                        .build();
                mPvType.setPicker(list);
            }
            mPvType.show();
        });

        mBinding.rlNum.setOnClickListener(v -> {
            AppUtils.hideInput(this);
            choose();
        });

        mBinding.tvCreate.setOnClickListener(v -> {
            create();
        });
        mBinding.rlStartTime.setOnClickListener(v -> {
            KeyboardUtils.hideSoftInput(this);
            timeStartChoose();
        });

        mBinding.rlEndTime.setOnClickListener(v -> {
            KeyboardUtils.hideSoftInput(this);
            timeEndChoose();
        });
    }

    private void create() {
        if (mType == TYPE_ADD) {
            if (mOptions == -1) {
                ToastUtils.showShort("请选择优惠券类型");
                return;
            }
        }
        if (StringUtils.isEmpty(StringUtils.getEditText(mBinding.etPrice))) {
            ToastUtils.showShort("请输入金额");
            return;
        }
        if (mOptions == 0 && StringUtils.isEmpty(StringUtils.getEditText(mBinding.etFull))) {
            ToastUtils.showShort("请输入满减条件");
            return;
        }
        if (StringUtils.isEmpty(StringUtils.getTextView(mBinding.tvChooseStartTime))) {
            ToastUtils.showShort("请选择开始时间");
            return;
        }
        if (StringUtils.isEmpty(StringUtils.getTextView(mBinding.tvChooseEndTime))) {
            ToastUtils.showShort("请选择结束时间");
            return;
        }
        if (StringUtils.isEmpty(StringUtils.getTextView(mBinding.tvChooseNum))) {
            ToastUtils.showShort("请选择发放数量");
            return;
        }

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        int day = (int) TimeUtils.getTimeSpan(mBinding.tvChooseEndTime.getText().toString(), mBinding.tvChooseStartTime.getText().toString(), simpleDateFormat, TimeConstants.DAY);
        if (mType == TYPE_ADD) {
            Api.getInstance().addCoupon(AccountManager.getInstance().getShopId(), StringUtils.getEditText(mBinding.etPrice),
                    StringUtils.getEditText(mBinding.etFull), String.valueOf(day), String.valueOf(mOptions + 1), num + "", "")
                    .doOnSubscribe(disposable -> {
                    })
                    .compose(RxSchedulers.io_main())
                    .subscribe(new BaseObserver() {
                        @Override
                        protected void onSuccess(Object o) {
                            ToastUtils.showShort("添加成功");
                            finish();
                            EventBusUtils.sendEvent(new BaseEvent(EventCode.EVENT_COUPON));
                        }

                        @Override
                        protected void onError(String msg, int errorCode) {
                            super.onError(msg, errorCode);
                        }
                    });
        } else {
            Api.getInstance().editCoupon(mCouponGetList.getId() + "", AccountManager.getInstance().getShopId(),
                    StringUtils.getEditText(mBinding.etPrice),
                    StringUtils.getEditText(mBinding.etFull), String.valueOf(day), "1", num + "", "")
                    .doOnSubscribe(disposable -> {
                    })
                    .compose(RxSchedulers.io_main())
                    .subscribe(new BaseObserver() {
                        @Override
                        protected void onSuccess(Object o) {
                            ToastUtils.showShort("修改成功");
                            finish();
                            EventBusUtils.sendEvent(new BaseEvent(EventCode.EVENT_COUPON));
                        }

                        @Override
                        protected void onError(String msg, int errorCode) {
                            super.onError(msg, errorCode);
                        }
                    });
        }

    }

    private void choose() {
        numType = 1;
        num = 0;
        View view = View.inflate(this, R.layout.view_dialog_publish_coupon_list, null);
        ImageButton itnClose = view.findViewById(R.id.itn_close);
        TextView tvBtnSubmit = view.findViewById(R.id.tv_btn_submit);
        RelativeLayout rl1 = view.findViewById(R.id.rl_1);
        RelativeLayout rl2 = view.findViewById(R.id.rl_2);
        ImageView iv_cb_1 = view.findViewById(R.id.iv_cb_1);
        ImageView iv_cb_2 = view.findViewById(R.id.iv_cb_2);
        EditText et_num = view.findViewById(R.id.et_num);

        rl1.setOnClickListener(view12 -> {
            numType = 1;
            iv_cb_1.setImageResource(R.mipmap.ic_cb_s);
            iv_cb_2.setImageResource(R.mipmap.ic_cb_n);
        });
        rl2.setOnClickListener(view13 -> {
            numType = 2;
            iv_cb_1.setImageResource(R.mipmap.ic_cb_n);
            iv_cb_2.setImageResource(R.mipmap.ic_cb_s);
        });

        itnClose.setOnClickListener(view1 -> {
            mChooseBottomDialog.dismiss();
        });
        tvBtnSubmit.setOnClickListener(view1 -> {
            if (numType == 1) {
                num = 0;
                mBinding.tvChooseNum.setText("无限制");
            } else {
                num = Integer.valueOf(StringUtils.getEditText(et_num));
                mBinding.tvChooseNum.setText(String.format("%d", num));
            }
            mChooseBottomDialog.dismiss();
        });
        if (mChooseBottomDialog == null) {
            mChooseBottomDialog = new BottomDialog(this, view);
        }
        mChooseBottomDialog.setIsCanceledOnTouchOutside(true);
        mChooseBottomDialog.show();
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

                mBinding.tvChooseStartTime.setText(df.format(date));
            }
        })
                .setType(new boolean[]{true, true, true, false, false, false})// 默认全部显示
                .setDate(selectedDate)// 如果不设置的话，默认是系统时间*/
                .setRangDate(startDate, endDate)//起始终止年月日设定
                .setLabel("年", "月", "日", "时", "分", "秒")//默认设置为年月日时分秒
                .setDecorView((ViewGroup) findViewById(R.id.tuangou_view))
                .setTitleText("选择时间")
                .setTitleColor(Color.parseColor("#111111"))
                .setTitleSize(16)
                .setCancelColor(Color.parseColor("#999999"))
                .setSubmitColor(Color.parseColor("#2f2f2f"))
                .build();
        startTime.show();

    }

    private void timeEndChoose() {
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
                    startT = df.parse(mBinding.tvChooseStartTime.getText().toString());

                } catch (ParseException e) {
                    e.printStackTrace();
                }
                if (startT.getTime() > date.getTime()) {
                    ToastUtils.showShort("请选择比开始时间后的日期");

                    return;
                }
                mBinding.tvChooseEndTime.setText(df.format(date));
            }
        })
                .setType(new boolean[]{true, true, true, false, false, false})// 默认全部显示
                .setDate(selectedDate)// 如果不设置的话，默认是系统时间*/
                .setRangDate(startDate, endDate)//起始终止年月日设定
                .setLabel("年", "月", "日", "时", "分", "秒")//默认设置为年月日时分秒
                .setDecorView((ViewGroup) findViewById(R.id.tuangou_view))
                .setTitleText("选择时间")
                .setTitleColor(Color.parseColor("#111111"))
                .setTitleSize(16)
                .setCancelColor(Color.parseColor("#999999"))
                .setSubmitColor(Color.parseColor("#2f2f2f"))
                .build();
        endTime.show();
    }

}
