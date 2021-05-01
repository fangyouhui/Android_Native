package com.pai8.ke.activity.me.ui;

import android.content.Intent;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.hjq.bar.OnTitleBarListener;
import com.hjq.bar.TitleBar;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.entity.LocalMedia;
import com.pai8.ke.R;
import com.pai8.ke.activity.me.contract.EditPersonalInfoContract;
import com.pai8.ke.activity.me.entity.resp.UserInfoResp;
import com.pai8.ke.activity.me.presenter.EditPersonalInfoPresenter;
import com.pai8.ke.base.BaseMvpActivity;
import com.pai8.ke.utils.ChoosePicUtils;
import com.pai8.ke.utils.ImageLoadUtils;
import com.pai8.ke.utils.StringUtils;
import com.pai8.ke.utils.ToastUtils;
import com.pai8.ke.widget.CircleImageView;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * @author Created by zzf
 * @time 21:40
 * Description：
 */
public class EditPersonalInfoActivity extends BaseMvpActivity<EditPersonalInfoPresenter> implements EditPersonalInfoContract.View {

    @BindView(R.id.title_bar)
    TitleBar titleBar;
    @BindView(R.id.ric_head)
    CircleImageView ricHead;
    @BindView(R.id.et_nickname)
    EditText etNickname;
    @BindView(R.id.et_wechat)
    EditText etWechat;
    @BindView(R.id.tv_remind)
    TextView tvRemind;
    private final int CHOOSE_PHOTO = 100;
    private String mHeadPath = "";
    private String mNickname = "";
    private String mWechat = "";
    private String mHeadUrl = "";

    @Override
    public EditPersonalInfoPresenter initPresenter() {
        return new EditPersonalInfoPresenter(this);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_edit_personal_info;
    }

    @Override
    public void initView() {
        mTitleBar.setTitle("编辑个人信息");
    }

    @Override
    public void initData() {
        super.initData();
        mPresenter.getInfoByUid();
    }

    @Override
    public void initListener() {
        super.initListener();
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == CHOOSE_PHOTO && data != null) {
                setPic(data);
            }
        }
    }

    private void setPic(Intent data) {
        String path = "";
        List<LocalMedia> selectList = PictureSelector.obtainMultipleResult(data);
        if (null != selectList && selectList.size() > 0) {
            if (selectList.get(0).isCompressed()) {
                path = selectList.get(0).getCompressPath();
            } else {
                path = selectList.get(0).getPath();
            }
        }
        mHeadPath = path;
        ImageLoadUtils.setCircularImage(this, path, ricHead, R.mipmap.img_head_def);
    }


    @OnClick({R.id.ric_head, R.id.tv_save})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ric_head:
                ChoosePicUtils.picSingle(this, CHOOSE_PHOTO);
                break;
            case R.id.tv_save:
                String nickname = etNickname.getText().toString().trim();
                String wechat = etWechat.getText().toString().trim();

                if (StringUtils.isEmpty(nickname)) {
                    toast("请输入昵称");
                    return;
                }

                if (StringUtils.isEmpty(mHeadPath) && nickname.equals(mNickname)) {
                    if (StringUtils.isEmpty(wechat) && StringUtils.isEmpty(mWechat)) {
                        toast("你好数据未改变");
                        return;
                    } else {
                        if (!StringUtils.isEmpty(wechat) && !StringUtils.isEmpty(mWechat)
                                && wechat.equals(mWechat)) {
                            toast("你好数据未改变");
                            return;
                        }
                    }
                }

                if (StringUtils.isEmpty(mHeadPath)) {
                    mPresenter.submitChangeInfo("", mHeadUrl, nickname, wechat);
                } else {
                    mPresenter.submitChangeInfo(mHeadPath, "", nickname, wechat);
                }
                break;
            default:
                break;
        }
    }

    @Override
    public void getUserInfo(UserInfoResp resp) {
        if (resp != null) {
            ImageLoadUtils.loadImage(this, resp.getAvatar(), ricHead, R.mipmap.img_head_def);
            mNickname = resp.getUser_nickname();
            mWechat = resp.getWechat();
            mHeadUrl = resp.getAvatar();
            etNickname.setText(mNickname);
            etWechat.setText(mWechat);
        }
    }

    @Override
    public void saveSuccess() {
        ToastUtils.showShort("保存成功");
        setResult(RESULT_OK);
        finish();
    }
}
