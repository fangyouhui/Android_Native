package com.pai8.ke.activity.takeaway.ui;

import com.bigkoo.pickerview.builder.OptionsPickerBuilder;
import com.bigkoo.pickerview.listener.OnOptionsSelectListener;
import com.bigkoo.pickerview.view.OptionsPickerView;
import com.hjq.bar.OnTitleBarListener;
import com.pai8.ke.R;
import com.pai8.ke.activity.me.entity.resp.CouponResp;
import com.pai8.ke.api.Api;
import com.pai8.ke.base.BaseActivity;
import com.pai8.ke.base.BaseEvent;
import com.pai8.ke.base.retrofit.BaseObserver;
import com.pai8.ke.base.retrofit.RxSchedulers;
import com.pai8.ke.entity.resp.CouponGetListResp;
import com.pai8.ke.global.EventCode;
import com.pai8.ke.utils.AppUtils;
import com.pai8.ke.utils.DateUtils;
import com.pai8.ke.utils.EventBusUtils;
import com.pai8.ke.utils.PickerUtils;
import com.pai8.ke.utils.StringUtils;
import com.pai8.ke.widget.BottomDialog;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.IntDef;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 添加/编辑 商家优惠券
 */
public class AddCouponActivity extends BaseActivity {

    @BindView(R.id.et_price)
    EditText etPrice;
    @BindView(R.id.et_full)
    EditText etFull;
    @BindView(R.id.tv_choose_time)
    TextView tvChooseTime;
    @BindView(R.id.tv_choose_num)
    TextView tvChooseNum;
    @BindView(R.id.tv_choose_type)
    TextView tvChooseType;
    private int numType;
    private int num = 0;

    private BottomDialog mChooseBottomDialog;

    public static final int TYPE_ADD = 1;
    public static final int TYPE_EDIT = 2;
    private CouponResp.CouponListBean mCouponGetList;
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

    public static void launchEdit(Context context, CouponResp.CouponListBean bean) {
        Intent intent = new Intent(context, AddCouponActivity.class);
        Bundle bundle = new Bundle();
        intent.putExtra("type", TYPE_EDIT);
        intent.putExtra("couponGetListResp", bean);
        intent.putExtras(bundle);
        context.startActivity(intent);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_add_coupon;
    }

    @SuppressLint("DefaultLocale")
    @Override
    public void initView() {
        Bundle extras = getIntent().getExtras();
        mType = extras.getInt("type");
        mCouponGetList = (CouponResp.CouponListBean) extras.getSerializable("couponGetListResp");

        if (mType == TYPE_ADD) {
            mTitleBar.setTitle("创建优惠券");
        } else {
            mTitleBar.setTitle("编辑优惠券");
            if (mCouponGetList.getType() == TYPE_ADD) {
                tvChooseType.setText("满减券");
            } else {
                tvChooseType.setText("运费抵扣券");
            }
            num = mCouponGetList.getNum();
            tvChooseTime.setText(String.format("%d天", (mCouponGetList.getEnd_time() - mCouponGetList.getAdd_time()) / (60 * 60 * 24)));
            etPrice.setText(mCouponGetList.getDis_price());
            etFull.setText(mCouponGetList.getTrig_price());
            tvChooseNum.setText(num == 0 ? "无限制" : num + "");
        }
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

    @OnClick({R.id.rl_time, R.id.rl_num, R.id.tv_create, R.id.rl_type})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.rl_type:
                if (mType == TYPE_EDIT) {
                    return;
                }
                if (mPvType == null) {
                    List<String> list = new ArrayList<>();
                    list.add("满减券");
                    list.add("运费抵扣券");
                    mPvType = new OptionsPickerBuilder(this, (options1, option2, options3, v) -> {
                        tvChooseType.setText(list.get(options1));
                        mOptions = options1;
                    })
                            .isRestoreItem(false)
                            .setDecorView(findViewById(R.id.rl_merchant))
                            .build();
                    mPvType.setPicker(list);
                }
                mPvType.show();
                break;
            case R.id.rl_time:
                AppUtils.hideInput(this);
                PickerUtils.showDays(this, (position, day) -> {
                    tvChooseTime.setText(day);
                });
                break;
            case R.id.rl_num:
                AppUtils.hideInput(this);
                choose();
                break;
            case R.id.tv_create:
                if (mType == TYPE_ADD) {
                    if (mOptions == -1) {
                        toast("请选择优惠券类型");
                        return;
                    }
                }
                if (StringUtils.isEmpty(StringUtils.getEditText(etPrice))) {
                    toast("请输入金额");
                    return;
                }
                if (StringUtils.isEmpty(StringUtils.getEditText(etFull))) {
                    toast("请输入满减条件");
                    return;
                }
                if (StringUtils.isEmpty(StringUtils.getTextView(tvChooseTime))) {
                    toast("请选择有效时间");
                    return;
                }
                if (StringUtils.isEmpty(StringUtils.getTextView(tvChooseNum))) {
                    toast("请选择发放数量");
                    return;
                }
                showLoadingDialog(null);
                if (mType == TYPE_ADD) {
                    Api.getInstance().addCoupon(mAccountManager.getShopId(), StringUtils.getEditText(etPrice),
                            StringUtils.getEditText(etFull), StringUtils.getTextView(tvChooseTime).replace(
                                    "天", ""), String.valueOf(mOptions + 1), num + "")
                            .doOnSubscribe(disposable -> {
                            })
                            .compose(RxSchedulers.io_main())
                            .subscribe(new BaseObserver() {
                                @Override
                                protected void onSuccess(Object o) {
                                    dismissLoadingDialog();
                                    toast("添加成功");
                                    finish();
                                    EventBusUtils.sendEvent(new BaseEvent(EventCode.EVENT_COUPON));
                                }

                                @Override
                                protected void onError(String msg, int errorCode) {
                                    dismissLoadingDialog();
                                    super.onError(msg, errorCode);
                                }
                            });
                } else {
                    Api.getInstance().editCoupon(mCouponGetList.getId() + "", mAccountManager.getShopId(),
                            StringUtils.getEditText(etPrice),
                            StringUtils.getEditText(etFull), StringUtils.getTextView(tvChooseTime).replace(
                                    "天", ""), "1", num + "")
                            .doOnSubscribe(disposable -> {
                            })
                            .compose(RxSchedulers.io_main())
                            .subscribe(new BaseObserver() {
                                @Override
                                protected void onSuccess(Object o) {
                                    dismissLoadingDialog();
                                    toast("修改成功");
                                    finish();
                                    EventBusUtils.sendEvent(new BaseEvent(EventCode.EVENT_COUPON));
                                }

                                @Override
                                protected void onError(String msg, int errorCode) {
                                    dismissLoadingDialog();
                                    super.onError(msg, errorCode);
                                }
                            });
                }

                break;
            default:
                break;
        }
    }

    @SuppressLint("DefaultLocale")
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
                tvChooseNum.setText("无限制");
            } else {
                num = Integer.valueOf(StringUtils.getEditText(et_num));
                tvChooseNum.setText(String.format("%d", num));
            }
            mChooseBottomDialog.dismiss();
        });
        if (mChooseBottomDialog == null) {
            mChooseBottomDialog = new BottomDialog(this, view);
        }
        mChooseBottomDialog.setIsCanceledOnTouchOutside(true);
        mChooseBottomDialog.show();
    }
}
