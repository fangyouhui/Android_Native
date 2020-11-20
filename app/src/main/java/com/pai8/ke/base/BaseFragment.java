package com.pai8.ke.base;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import com.pai8.ke.utils.EventBusUtils;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import androidx.annotation.LayoutRes;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import butterknife.ButterKnife;


/**
 * Fragment基类
 * Created by gh on 2018/7/27.
 */
public abstract class BaseFragment extends Fragment {

    /**
     * 贴附的activity
     */
    protected BaseActivity mActivity;
    /**
     * 根view
     */
    protected View mRootView;
    /**
     * 是否对用户可见
     */
    protected boolean mIsVisible = false;
    /**
     * 是否加载完成
     * 当执行完oncreatview,View的初始化方法后方法后即为true
     */
    protected boolean mIsPrepare = false;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mActivity = getBaseActivity();
    }

    public BaseActivity getBaseActivity() {
        return (BaseActivity) this.getActivity();
    }

    @Override
    @Nullable
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        if (mRootView == null) {
            mRootView = inflater.inflate(getLayoutId(), container, false);
            ButterKnife.bind(this, mRootView);
            if (isRegisterEventBus()) {
                EventBusUtils.register(this);
            }
            mIsPrepare = true;
            initView(getArguments());
            initData();
            initListener();
        }
        return mRootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (isRegisterEventBus()) {
            EventBusUtils.unregister(this);
        }
    }

    /**
     * 设置根布局资源id
     */
    @LayoutRes
    protected abstract int getLayoutId();

    /**
     * 初始化View
     *
     * @param arguments 接收到的从其他地方传递过来的参数
     */
    protected abstract void initView(Bundle arguments);

    /**
     * 初始化数据
     */
    protected void initData() {

    }

    /**
     * 设置监听事件
     */
    protected void initListener() {

    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        this.mIsVisible = isVisibleToUser;
        if (isVisibleToUser) {
            onVisibleToUser();
        }
    }

    /**
     * 用户可见时执行的操作
     */
    protected void onVisibleToUser() {
        if (mIsPrepare && mIsVisible) {
            onLazyLoad();
        }
    }

    /**
     * 懒加载，仅当用户可见切view初始化结束后才会执行
     */
    protected void onLazyLoad() {

    }

    /**
     * 是否注册事件分发
     *
     * @return true绑定EventBus事件分发，默认不绑定，子类需要绑定的话复写此方法返回true.
     */
    protected boolean isRegisterEventBus() {
        return false;
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventBusCome(BaseEvent event) {
        if (event != null) {
            receiveEvent(event);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void onStickyEventBusCome(BaseEvent event) {
        if (event != null) {
            receiveStickyEvent(event);
        }
    }

    /**
     * 接收到分发到事件
     *
     * @param event 事件
     */
    protected void receiveEvent(BaseEvent event) {

    }

    /**
     * 接受到分发的粘性事件
     *
     * @param event 粘性事件
     */
    protected void receiveStickyEvent(BaseEvent event) {

    }

    public void showLoadingDialog(String strMsg) {
        mActivity.showLoadingDialog(strMsg);
    }

    public void dismissLoadingDialog() {
        mActivity.dismissLoadingDialog();
    }

    public void toast(String strMsg) {
        mActivity.toast(strMsg);
    }

    public void launch(Class clazz) {
        mActivity.launch(clazz);
    }

    public void launch(Class clazz, Bundle bundle) {
        mActivity.launch(clazz, bundle);
    }

    public void launchInterceptLogin(Class clazz) {
        mActivity.launchInterceptLogin(clazz);
    }

}
