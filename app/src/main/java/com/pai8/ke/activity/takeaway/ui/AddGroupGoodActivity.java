package com.pai8.ke.activity.takeaway.ui;

import android.content.Intent;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.bigkoo.pickerview.builder.OptionsPickerBuilder;
import com.bigkoo.pickerview.builder.TimePickerBuilder;
import com.bigkoo.pickerview.listener.OnOptionsSelectListener;
import com.bigkoo.pickerview.listener.OnTimeSelectListener;
import com.bigkoo.pickerview.view.OptionsPickerView;
import com.bigkoo.pickerview.view.TimePickerView;
import com.gh.qiniushortvideo.ChooseVideo;
import com.gh.qiniushortvideo.activity.ConfigActivity;
import com.gh.qiniushortvideo.activity.MediaSelectActivity;
import com.gh.qiniushortvideo.activity.VideoRecordActivity;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.entity.LocalMedia;
import com.pai8.ke.R;
import com.pai8.ke.activity.takeaway.api.TakeawayApi;
import com.pai8.ke.activity.takeaway.contract.AddGoodContract;
import com.pai8.ke.activity.takeaway.contract.AddGroupGoodContract;
import com.pai8.ke.activity.takeaway.entity.req.UpCategoryReq;
import com.pai8.ke.activity.takeaway.entity.resq.ShopInfo;
import com.pai8.ke.activity.takeaway.presenter.AddGoodPresenter;
import com.pai8.ke.activity.takeaway.presenter.AddGroupGoodPresenter;
import com.pai8.ke.activity.takeaway.utils.SoftHideKeyBoardUtil;
import com.pai8.ke.activity.takeaway.widget.ChooseShopCoverPop;
import com.pai8.ke.base.BaseMvpActivity;
import com.pai8.ke.base.retrofit.BaseObserver;
import com.pai8.ke.base.retrofit.RxSchedulers;
import com.pai8.ke.entity.resp.BusinessType;
import com.pai8.ke.manager.AccountManager;
import com.pai8.ke.manager.UploadFileManager;
import com.pai8.ke.utils.ChoosePicUtils;
import com.pai8.ke.utils.ImageLoadUtils;
import com.pai8.ke.widget.BottomDialog;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import butterknife.BindView;

public class AddGroupGoodActivity extends BaseMvpActivity<AddGroupGoodPresenter> implements View.OnClickListener,
        AddGroupGoodContract.View {
    @BindView(R.id.rv_group_banner)
    RecyclerView mRvGroupBuy;

    private final int RESULT_PICTURE = 1000;  //图片详情

    private final int RESULT_VIDEO = 1001;
    private ImageButton isweek;  //视频封面

    private ImageView mIvCover;  //视频封面
    private EditText zhuyiTextView;   //注意事项
    private EditText detailTextView;   //详情
    private EditText neirongTextView;   //注意事项

    private EditText titleLab;   //标题
    private EditText originPrice;   //原价
    private EditText sellPrice;   //团购价
    private EditText stock;   //库存
    private TextView Category;   //分类
    private TextView startTimeBtn;   //开始日期
    private TextView endTimeBtn;   //结束日期

    private String mFoodPath;
    private String videoKey;
    private int mType;     //3:编辑团购商品
    private OptionsPickerView  mPvType;
    private String goodId;
    private boolean isweekday;
    private TimePickerView startTime;
    private TimePickerView endTime;
    private BottomDialog mChooseBottomDialog;

    @Override
    protected boolean isRegisterEventBus() {
        return true;
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_add_group_good;
    }
    /**
     * 七牛视频事件
     *
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventChooseVideo(ChooseVideo event) {
        if (event == null) return;
        mFoodPath = event.getPath();
        ImageLoadUtils.loadCover(this, mFoodPath, mIvCover);
        showLoadingDialog("视频上传...");
        UploadFileManager.getInstance().upload(mFoodPath, new UploadFileManager.Callback() {
            @Override
            public void onSuccess(String url, String key) {
                dismissLoadingDialog();
                toast("视频上传成功");
                videoKey = key;
            }

            @Override
            public void onError(String msg) {
                dismissLoadingDialog();
                toast("视频上传失败：" + msg);
            }
        });
    }
    @Override
    public void initView() {
        mType = getIntent().getIntExtra("type", 0);
        isweekday = false;
        SoftHideKeyBoardUtil.assistActivity(this);
        setImmersionBar(R.id.base_tool_bar);
        findViewById(R.id.toolbar_back_all).setOnClickListener(this);

        mIvCover = findViewById(R.id.iv_cover);
        mIvCover.setOnClickListener(this);

        titleLab = findViewById(R.id.et_name);
        titleLab.setOnClickListener(this);

        originPrice = findViewById(R.id.et_price);
        originPrice.setOnClickListener(this);


        sellPrice = findViewById(R.id.et_price2);
        sellPrice.setOnClickListener(this);

        stock = findViewById(R.id.et_pack_price);
        stock.setOnClickListener(this);

        Category = findViewById(R.id.tv_category);
        Category.setOnClickListener(this);

        startTimeBtn = findViewById(R.id.start_time_text);
        startTimeBtn.setOnClickListener(this);

        endTimeBtn = findViewById(R.id.end_time_text);
        endTimeBtn.setOnClickListener(this);

        zhuyiTextView = findViewById(R.id.zhuyi);
        zhuyiTextView.setOnClickListener(this);

        detailTextView = findViewById(R.id.group_desc);
        detailTextView.setOnClickListener(this);


        neirongTextView = findViewById(R.id.et_desc);
        neirongTextView.setOnClickListener(this);

        isweek = findViewById(R.id.itn_close);
        isweek.setOnClickListener(this);
    }
    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.toolbar_back_all) {
            finish();
        } else if (v.getId() == R.id.tv_category) {
            getCategoryList();

        }
        else if (v.getId() == R.id.itn_close) {

            isweekday = !isweekday;
            if (isweekday){
                isweek.setImageResource(R.mipmap.ic_cb_s);
            }
            else{
                //mipmap/ic_cb_n
                isweek.setImageResource(R.mipmap.ic_cb_n);

            }
        }
        else if (v.getId() == R.id.start_time_text){
            timeStartChoose();

        }
        else if (v.getId() == R.id.end_time_text){
            timeEndChoose();

        }
        else if (v.getId() == R.id.iv_cover){

            chooseImg(1);
        }
    }

    private void chooseImg(int i) {
        View view = View.inflate(AddGroupGoodActivity.this, R.layout.view_dialog_choose_qnvideo,
                null);
        TextView tvBtnGalley = view.findViewById(R.id.tv_btn_galley);
        TextView tvBtnTakePhoto = view.findViewById(R.id.tv_btn_take_photo);
        ImageButton itnClose = view.findViewById(R.id.itn_close);
        tvBtnGalley.setOnClickListener(view12 -> {
            Intent it = new Intent(AddGroupGoodActivity.this, MediaSelectActivity.class);
            it.putExtra(MediaSelectActivity.TYPE, MediaSelectActivity.TYPE_VIDEO_EDIT);
            startActivity(it);
            mChooseBottomDialog.dismiss();
        });
        tvBtnTakePhoto.setOnClickListener(view13 -> {
            mChooseBottomDialog.dismiss();
            Intent intent = new Intent(AddGroupGoodActivity.this, VideoRecordActivity.class);
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
        });

        itnClose.setOnClickListener(view1 -> {
            mChooseBottomDialog.dismiss();
        });
        if (mChooseBottomDialog == null) {
            mChooseBottomDialog = new BottomDialog(AddGroupGoodActivity.this, view);
        }
        mChooseBottomDialog.setIsCanceledOnTouchOutside(true);
        mChooseBottomDialog.show();



    }

    private void timeEndChoose() {
        Calendar selectedDate = Calendar.getInstance();
        Calendar startDate = Calendar.getInstance();
        startDate.set(2000, 0, 1);
        Calendar endDate = Calendar.getInstance();
        endDate.set(2099, 11, 1);
        endTime = new TimePickerBuilder(this, new OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date, View v) {//选中事件回

                DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
                Date startT = null;
                try {
                    startT = df.parse(startTimeBtn.getText().toString());

                } catch (ParseException e) {
                    e.printStackTrace();
                }
                if (startT.getTime() > date.getTime()) {
                    toast("请选择比开始时间后的日期");

                    return;
                }
                endTimeBtn.setText(df.format(date));
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

    private void timeStartChoose() {
        Calendar selectedDate = Calendar.getInstance();
        Calendar startDate = Calendar.getInstance();
        startDate.set(2000, 0, 1);
        Calendar endDate = Calendar.getInstance();
        endDate.set(2099, 11, 1);
        startTime = new TimePickerBuilder(this, new OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date, View v) {//选中事件回
                DateFormat df = new SimpleDateFormat("yyyy-MM-dd");

                startTimeBtn.setText(df.format(date));
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

    private void getCategoryList() {
        List<String> options1Items = new ArrayList<>();

        TakeawayApi.getInstance().getTuanCategoryList()
                .doOnSubscribe(disposable -> {
                })
                .compose(RxSchedulers.io_main())
                .subscribe(new BaseObserver<List<BusinessType>>() {
                    @Override
                    protected void onSuccess(List<BusinessType> list) {

                        for (int i = 0; i < list.size(); i++) {
                            options1Items.add(list.get(i).type_name);
                        }

                        if (mPvType == null) {
                            mPvType = new OptionsPickerBuilder(AddGroupGoodActivity.this, new OnOptionsSelectListener() {
                                @Override
                                public void onOptionsSelect(int options1, int option2, int options3, View v) {
                                    //返回的分别是三个级别的选中位置
                                    String tx = list.get(options1).type_name;
                                    Category.setText(tx);
                                    goodId = list.get(options1).id + "";

                                }
                            })
                                    .setDecorView(findViewById(R.id.rl_merchant))
                                    .build();
                        }
                        mPvType.setPicker(options1Items);
                        mPvType.show();
                    }

                    @Override
                    protected void onError(String msg, int errorCode) {
                        super.onError(msg, errorCode);
                    }
                });

    }


    @Override
    public void addGoodSuccess(String data) {

    }

    @Override
    public void editGoodSuccess(String data) {

    }

    @Override
    public void deleteGoodSuccess(String data) {

    }

    @Override
    public void fail() {

    }

    @Override
    public AddGroupGoodPresenter initPresenter() {
        return new AddGroupGoodPresenter(this);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case RESULT_PICTURE:
                    setPic(data, 0);
                    break;
                case RESULT_VIDEO:
                    setPic(data, 1);
                    break;

            }
        }

    }

    public int uploadType;

    private void setPic(Intent data, int type) {
        String path = "";
        List<LocalMedia> selectList = PictureSelector.obtainMultipleResult(data);
        if (null != selectList && selectList.size() > 0) {
            if (selectList.get(0).isCompressed()) {
                path = selectList.get(0).getCompressPath();
            } else {
                path = selectList.get(0).getPath();
            }
        }
        if (type == 0) {  //图片
            ImageLoadUtils.setRectImage(this, path, mIvCover);
            mFoodPath = path;
            uploadType = 1;

        } else if (type == 1) {
            ImageLoadUtils.setRectImage(this, path, mIvCover);
            mFoodPath = path;
            uploadType = 2;
        }

    }



}
