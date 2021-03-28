package com.pai8.ke.activity.takeaway.ui;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bigkoo.pickerview.builder.OptionsPickerBuilder;
import com.bigkoo.pickerview.builder.TimePickerBuilder;
import com.bigkoo.pickerview.listener.OnOptionsSelectListener;
import com.bigkoo.pickerview.listener.OnTimeSelectListener;
import com.bigkoo.pickerview.view.OptionsPickerView;
import com.bigkoo.pickerview.view.TimePickerView;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.gh.qiniushortvideo.ChooseVideo;
import com.gh.qiniushortvideo.activity.ConfigActivity;
import com.gh.qiniushortvideo.activity.MediaSelectActivity;
import com.gh.qiniushortvideo.activity.VideoRecordActivity;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.entity.LocalMedia;
import com.pai8.ke.R;
import com.pai8.ke.activity.takeaway.adapter.GroupBannerAdapter;
import com.pai8.ke.activity.takeaway.adapter.GroupDetailAdapter;
import com.pai8.ke.activity.takeaway.api.TakeawayApi;
import com.pai8.ke.activity.takeaway.contract.AddGroupGoodContract;
import com.pai8.ke.activity.takeaway.entity.req.AddFoodReq;
import com.pai8.ke.activity.takeaway.entity.req.GroupFoodReq;
import com.pai8.ke.activity.takeaway.entity.resq.smallGoodsInfo;
import com.pai8.ke.activity.takeaway.presenter.AddGroupGoodPresenter;
import com.pai8.ke.activity.takeaway.utils.SoftHideKeyBoardUtil;
import com.pai8.ke.base.BaseMvpActivity;
import com.pai8.ke.base.retrofit.BaseObserver;
import com.pai8.ke.base.retrofit.RxSchedulers;
import com.pai8.ke.entity.resp.BusinessType;
import com.pai8.ke.manager.AccountManager;
import com.pai8.ke.manager.UploadFileManager;
import com.pai8.ke.utils.ChoosePicUtils;
import com.pai8.ke.utils.ImageLoadUtils;
import com.pai8.ke.utils.ToastUtils;
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
import java.util.Map;

import butterknife.BindView;

public class AddGroupGoodActivity extends BaseMvpActivity<AddGroupGoodPresenter> implements View.OnClickListener,
        AddGroupGoodContract.View {
    @BindView(R.id.rv_group_banner)
    RecyclerView mRvGroupBuy;

    @BindView(R.id.deatil_group_banner)
    RecyclerView mDetailGroupBuy;


    private List<String> mList = new ArrayList<String>();
    private List<String> detailList = new ArrayList<String>();

    private GroupBannerAdapter groupBannerAdapter;
    private GroupDetailAdapter groupDetailAdapter;
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
    private TextView sureBtn;   //结束日期

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
        groupFoodReq = new GroupFoodReq();

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

        sureBtn = findViewById(R.id.tv_publish);
        sureBtn.setOnClickListener(this);

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

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this.getBaseContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        mRvGroupBuy.setLayoutManager(linearLayoutManager);

        LinearLayoutManager linearLayoutManager2 = new LinearLayoutManager(this.getBaseContext());
        linearLayoutManager2.setOrientation(LinearLayoutManager.HORIZONTAL);

        mDetailGroupBuy.setLayoutManager(linearLayoutManager2);

        mList.add("1235");
        detailList.add("1235");

        groupBannerAdapter = new GroupBannerAdapter(this.getBaseContext());
        mRvGroupBuy.setAdapter(groupBannerAdapter);

        groupBannerAdapter.setList(mList);

        groupBannerAdapter.setOnItemClickListener(new GroupBannerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                uploadType=0;
                if (position<bannerKey.size()){
                    bannerKey.remove(position);
                    mList.remove(position);
                    groupBannerAdapter.setList(mList);
                }
                else{
                    ChoosePicUtils.picSingle(AddGroupGoodActivity.this, 0, RESULT_PICTURE);

                }
            }
        });

        groupDetailAdapter = new GroupDetailAdapter(this.getBaseContext());
        mDetailGroupBuy.setAdapter(groupDetailAdapter);

        groupDetailAdapter.setList(detailList);

        groupDetailAdapter.setOnItemClickListener(new GroupDetailAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                uploadType=1;
                if (position<detailKey.size()){
                    detailKey.remove(position);
                    detailList.remove(position);
                    groupDetailAdapter.setList(detailList);
                }
                else{
                    ChoosePicUtils.picSingle(AddGroupGoodActivity.this, 0, RESULT_PICTURE);

                }
            }
        });

//        groupBannerAdapter.setOnClickListener(this);

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
        else if (v.getId() == R.id.tv_publish){
            uploadGood();
        }
    }

    private void uploadGood() {
        if (TextUtils.isEmpty(videoKey) ) {
            ToastUtils.showShort("封面视频不能为空");
            return;
        }
        String shopName = titleLab.getText().toString();
        if (TextUtils.isEmpty(shopName)) {
            ToastUtils.showShort("商品名称不能为空");
            return;
        }
        String originPriceT = originPrice.getText().toString();
        if (TextUtils.isEmpty(originPriceT)) {
            ToastUtils.showShort("商品价格不能为空");
            return;
        }
        String sellerPrice = sellPrice.getText().toString();
        if (TextUtils.isEmpty(sellerPrice)) {
            ToastUtils.showShort("团购价格不能为空");
            return;
        }

        String stockNum = stock.getText().toString();

        if (TextUtils.isEmpty(stockNum)) {
            ToastUtils.showShort("商品库存不能为空");
            return;
        }


        if (TextUtils.isEmpty(goodId)) {
            ToastUtils.showShort("团购分类不能为空");
            return;
        }

        if (TextUtils.isEmpty(detailTextView.getText().toString())) {
            ToastUtils.showShort("团购内容不能为空");
            return;
        }


        if (TextUtils.isEmpty(startTimeBtn.getText().toString())) {
            ToastUtils.showShort("有效日期开始时间不能为空");
            return;
        }

        if (TextUtils.isEmpty(endTimeBtn.getText().toString())) {
            ToastUtils.showShort("有效日期结束时间不能为空");
            return;
        }


        if (TextUtils.isEmpty(zhuyiTextView.getText().toString())) {
            ToastUtils.showShort("注意事项不能为空");
            return;
        }

        if (TextUtils.isEmpty(neirongTextView.getText().toString())) {
            ToastUtils.showShort("商品详情不能为空");
            return;
        }
        if (detailKey.size()==0){
            ToastUtils.showShort("详情图片不能为空");
            return;
        }
        if (bannerKey.size()==0){
            ToastUtils.showShort("轮播图片不能为空");
            return;
        }
        showLoadingDialog("");

        groupFoodReq.shop_id = AccountManager.getInstance().getShopId();
        if (isweekday){
            groupFoodReq.is_weekend = "true";
        }
        else{
            groupFoodReq.is_weekend = "false";

        }
        groupFoodReq.title = shopName;
        groupFoodReq.video = videoKey;
        groupFoodReq.desc = neirongTextView.getText().toString();
        groupFoodReq.food_type = goodId;
        String detailStr = ListToString(detailKey);

        groupFoodReq.details_img = detailStr.substring(0,detailStr.length()-1);
        groupFoodReq.origin_price = originPriceT;
        groupFoodReq.sell_price = sellerPrice;
        groupFoodReq.origin_price = originPriceT;
        groupFoodReq.stock = stockNum;
        groupFoodReq.details = detailTextView.getText().toString();
        groupFoodReq.matter = zhuyiTextView.getText().toString();
        String bannerStr = ListToString(bannerKey);
        groupFoodReq.cover = bannerStr.substring(0,bannerStr.length()-1);
        groupFoodReq.term = getTime(startTimeBtn.getText().toString())+"-"+getTime(endTimeBtn.getText().toString());
        groupFoodReq.status = 1;
        mPresenter.addGood(groupFoodReq);
        dismissLoadingDialog();


    }
    // 将字符串转为时间戳
    public static String getTime(String user_time) {
        String re_time = null;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date d;
        try {
            d = sdf.parse(user_time);
            long l = d.getTime();
            String str = String.valueOf(l);
            re_time = str.substring(0, 10);
        }catch (ParseException e) {
            // TODO Auto-generated catch block e.printStackTrace();
        }
        return re_time;
    }
    // 将时间戳转为字符串
    public static String getStrTime(String cc_time) {
        String re_StrTime = null;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        // 例如：
        long lcc_time = Long.valueOf(cc_time);
        re_StrTime = sdf.format(new Date(lcc_time * 1000L));
        return re_StrTime;
    }



    private static final String SEP1 = ",";
    private static final String SEP2 = ",";
    private static final String SEP3 = ",";

    public static String ListToString(List<?> list) {
        StringBuffer sb = new StringBuffer();
        if (list != null && list.size() > 0) {
            for (int i = 0; i < list.size(); i++) {
                if (list.get(i) == null || list.get(i) == "") {
                    continue;
                }
                // 如果值是list类型则调用自己 
                if (list.get(i) instanceof List) {
                    sb.append(ListToString((List<?>) list.get(i)));
                    sb.append(SEP1);
                } else if (list.get(i) instanceof Map) {
                    sb.append(MapToString((Map<?, ?>) list.get(i)));
                    sb.append(SEP1);
                } else {
                    sb.append(list.get(i));
                    sb.append(SEP1);
                }
            }
        }
        return "L" + sb.toString();
    }

    public static String MapToString(Map<?, ?> map) {
        StringBuffer sb = new StringBuffer();
        // 遍历map
        for (Object obj : map.keySet()) {
            if (obj == null) {
                continue;
            }
            Object key = obj;
            Object value = map.get(key);
            if (value instanceof List<?>) {
                sb.append(key.toString() + SEP1 + ListToString((List<?>) value));
                sb.append(SEP2);
            } else if (value instanceof Map<?, ?>) {
                sb.append(key.toString() + SEP1
                        + MapToString((Map<?, ?>) value));
                sb.append(SEP2);
            } else {
                sb.append(key.toString() + SEP3 + value.toString());
                sb.append(SEP2);
            }
        }
        return "M" + sb.toString();
    }


    private GroupFoodReq groupFoodReq;

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
        mFoodPath = path;
        upload(uploadType);

//        if (type == 0) {  //轮播图片
//            ImageLoadUtils.setRectImage(this, path, mIvCover);
//            mFoodPath = path;
//            uploadType = 1;
//            upload(uploadType);
//        } else if (type == 1) {
//           ImageLoadUtils.setRectImage(this, path, mIvCover);
//            mFoodPath = path;
//            uploadType = 2;
//        }

    }

    private List<String> bannerKey = new ArrayList<String>();
    private List<String> detailKey = new ArrayList<String>();

    private void upload(int type) {
        UploadFileManager.getInstance().upload(mFoodPath, new UploadFileManager.Callback() {
            @Override
            public void onSuccess(String url, String key) {
                if (uploadType==0){
                    mList.add(mList.size()-1,url);
                    if (mList.size()==7){
                        mList.remove(6);
                    }
                    bannerKey.add(key);
                    groupBannerAdapter.setList(mList);
                }
                else{
                    detailList.add(detailList.size()-1,url);
                    if (detailList.size()==7){
                        detailList.remove(6);
                    }
                    detailKey.add(key);
                    groupDetailAdapter.setList(detailList);
                }


            }

            @Override
            public void onError(String msg) {
                ToastUtils.showShort(msg);
            }
        });
    }
}
