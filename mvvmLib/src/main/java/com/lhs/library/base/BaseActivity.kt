package com.lhs.library.base

import android.content.res.Configuration
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.ViewModelProvider
import androidx.viewbinding.ViewBinding
import com.aleyn.mvvm.app.MVVMLin
import com.blankj.utilcode.util.BarUtils
import com.blankj.utilcode.util.LogUtils
import com.blankj.utilcode.util.ToastUtils
import com.lhs.library.R
import com.lhs.library.event.Message
import com.lhs.library.widget.CustomProgressDialog
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type


abstract class BaseActivity<VM : BaseViewModel, DB : ViewBinding> : AppCompatActivity() {

    protected lateinit var mViewModel: VM

    protected lateinit var mBinding: DB

    private var progressDialog: CustomProgressDialog? = null

    protected lateinit var TAG: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        TAG = javaClass.simpleName
        LogUtils.e(TAG, "激活页面：$TAG")
        initViewDataBinding()
        lifecycle.addObserver(mViewModel)
        //注册 UI事件
        registerDefUIChange()
        initFontScale()
        BarUtils.setStatusBarLightMode(window, true) //如果亮色,设置状态栏文字为黑色
        BarUtils.transparentStatusBar(this)
        initToolBar()
        initView(savedInstanceState)
        initData()
    }

    open fun layoutId(): Int = 0
    open fun initView(savedInstanceState: Bundle?) {}
    open fun initData() {}

    /**
     * DataBinding or ViewBinding
     */
    private fun initViewDataBinding() {
        val type = javaClass.genericSuperclass
        if (type is ParameterizedType) {
            val cls = type.actualTypeArguments[1] as Class<*>
            when {

                ViewBinding::class.java.isAssignableFrom(cls) && cls != ViewBinding::class.java -> {
                    cls.getDeclaredMethod("inflate", LayoutInflater::class.java).let {
                        @Suppress("UNCHECKED_CAST")
                        mBinding = it.invoke(null, layoutInflater) as DB
                        setContentView(mBinding.root)
                    }
                }
                else -> {
                    if (layoutId() == 0) throw IllegalArgumentException("If you don't use ViewBinding, you need to override method layoutId")
                    setContentView(layoutId())
                }
            }
            createViewModel(type.actualTypeArguments[0])
        } else throw IllegalArgumentException("Generic error")
    }


    /**
     * 注册 UI 事件
     */
    private fun registerDefUIChange() {
        mViewModel.defUI.showDialog.observe(this, {
            showLoading()
        })
        mViewModel.defUI.dismissDialog.observe(this, {
            dismissLoading()
        })
        mViewModel.defUI.toastEvent.observe(this, {
            ToastUtils.showShort(it)
        })
        mViewModel.defUI.msgEvent.observe(this, {
            handleEvent(it)
        })
    }

    open fun handleEvent(msg: Message) {}

    /**
     * 打开等待框
     */
    private fun showLoading() {
        if (progressDialog == null) {
            progressDialog = CustomProgressDialog(this)
        }
        progressDialog?.show()
    }

    /**
     * 关闭等待框
     */
    private fun dismissLoading() {
        progressDialog?.cancel()
    }


    /**
     * 创建 ViewModel
     */
    @Suppress("UNCHECKED_CAST")
    private fun createViewModel(type: Type) {
        val tClass = type as? Class<VM> ?: BaseViewModel::class.java
        mViewModel = ViewModelProvider(viewModelStore, defaultViewModelProviderFactory)
                .get(tClass) as VM
    }

    override fun getDefaultViewModelProviderFactory(): ViewModelProvider.Factory {
        return MVVMLin.getConfig().viewModelFactory() ?: super.getDefaultViewModelProviderFactory()
    }

    /**
     * 是否显示后退按钮,默认显示,可在子类重写该方法.
     *
     * @return
     */
    open fun isShowBacking(): Boolean {
        return true
    }

    private var toolbar: Toolbar? = null
    private var toolbarTitle: TextView? = null

    /**
     * 初始化 Toolbar
     */
    private fun initToolBar() {
        //note 如果是include toolbar_layout 记得include  android:id="@+id/toolBar" 否则mToolbar==null
        toolbar = findViewById(R.id.toolBar)
        toolbarTitle = findViewById(R.id.toolBarTitle)

        toolbar?.let {
            setSupportActionBar(it) //将Toolbar显示到界面
        }

        supportActionBar?.let { actionNotNull ->
            actionNotNull.setDisplayHomeAsUpEnabled(isShowBacking())  //后退按钮
            actionNotNull.setDisplayShowTitleEnabled(false) //设置默认的标题不显示
        }

        if (toolbarTitle != null) {
            setToolBarTitle(title) //getTitle()的值是activity的android:lable属性值
        }
        if (toolbar != null && isShowBacking()) {
            setNavBackClickListener()
        }
    }

    /**
     * 版本号小于21的后退按钮图片
     */
    private fun setNavBackClickListener() {
        toolbar?.setNavigationOnClickListener { onBackPressed() }
    }

    /**
     * 设置头部标题
     *
     * @param title
     */
    fun setToolBarTitle(title: CharSequence?) {
        if (toolbarTitle != null) {
            toolbarTitle?.text = title
        } else {
            toolbar?.title = title
        }
    }

    open fun initFontScale() {
        val configuration: Configuration = resources.configuration
        configuration.fontScale = 1.toFloat()
        //0.85 小, 1 标准大小, 1.15 大，1.3 超大 ，1.45 特大
        val metrics = DisplayMetrics()
        windowManager.defaultDisplay.getMetrics(metrics)
        metrics.scaledDensity = configuration.fontScale * metrics.density
        baseContext.resources.updateConfiguration(configuration, metrics)
    }

}