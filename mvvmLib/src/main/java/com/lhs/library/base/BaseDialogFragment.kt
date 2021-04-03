package com.lhs.library.base

import android.R
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.ViewModelProvider
import androidx.viewbinding.ViewBinding
import com.aleyn.mvvm.app.MVVMLin
import com.blankj.utilcode.util.LogUtils
import java.lang.reflect.ParameterizedType

/**
 * Created by lihongshi
 * 底部弹窗
 */
abstract class BaseDialogFragment<VM : BaseViewModel, DB : ViewBinding> : DialogFragment() {
    private var TAG = javaClass.simpleName
    protected lateinit var mViewModel: VM
    protected lateinit var mBinding: DB

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        LogUtils.eTag(TAG, "激活fragment页面：" + javaClass.simpleName)
    }

    override fun onDestroy() {
        super.onDestroy()
        LogUtils.eTag(TAG, "销毁fragment页面：" + javaClass.simpleName)
    }

    open fun initView() {}
    open fun addObserve() {}
    open fun initData() {}

    override fun onStart() {
        super.onStart()
//        val window: Window = dialog?.window!!
//        val params: WindowManager.LayoutParams = window.attributes
//        params.gravity = Gravity.BOTTOM
//        params.width = WindowManager.LayoutParams.MATCH_PARENT
//        window.attributes = params
//        window.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        setWindow()
    }

    private fun setWindow() {
        // 设置宽度为屏宽、靠近屏幕底部。
        val window = dialog!!.window
        window!!.decorView.setPadding(60, 0, 60, 0) //消除边距
        val wlp = window.attributes
        wlp.width = WindowManager.LayoutParams.MATCH_PARENT
        wlp.height = WindowManager.LayoutParams.WRAP_CONTENT
        window.attributes = wlp
        dialog!!.window!!.setBackgroundDrawableResource(R.color.transparent)
    }

    /**
     * 弹窗高度，默认为屏幕高度的四分之三
     * 子类可重写该方法返回peekHeight
     *
     * @return height
     */
    protected open fun getDialogHeight(): Int {
        val peekHeight = resources.displayMetrics.heightPixels
        //设置弹窗高度为屏幕高度的3/4
        return peekHeight - peekHeight / 3
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return initBinding(inflater, container)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        createViewModel()
        lifecycle.addObserver(mViewModel)
        initView()
        addObserve()
        initData()
    }

    /**
     * 使用 DataBinding时,要重写此方法返回相应的布局 id
     * 使用ViewBinding时，不用重写此方法
     */
    open fun layoutId(): Int = 0

    private fun initBinding(inflater: LayoutInflater, container: ViewGroup?): View {
        val type = javaClass.genericSuperclass
        if (type is ParameterizedType) {
            val cls = type.actualTypeArguments[1] as Class<*>
            return when {
                ViewBinding::class.java.isAssignableFrom(cls) && cls != ViewBinding::class.java -> {
                    cls.getDeclaredMethod("inflate", LayoutInflater::class.java).let {
                        @Suppress("UNCHECKED_CAST")
                        mBinding = it.invoke(null, inflater) as DB
                        mBinding.root
                    }
                }
                else -> {
                    if (layoutId() == 0) throw IllegalArgumentException("If you don't use ViewBinding, you need to override method layoutId")
                    inflater.inflate(layoutId(), container, false)
                }
            }
        } else throw IllegalArgumentException("Generic error")
    }

    override fun show(manager: FragmentManager, tag: String?) {
        if (!this.isAdded) {
            super.show(manager, tag)
        } else {
            Log.d(TAG, " has add to FragmentManager")
        }
    }

    /**
     * 创建 ViewModel
     *
     * 共享 ViewModel的时候，重写  Fragmnt 的 getViewModelStore() 方法，
     * 返回 activity 的  ViewModelStore 或者 父 Fragmnt 的 ViewModelStore
     */
    @Suppress("UNCHECKED_CAST")
    private fun createViewModel() {
        val type = javaClass.genericSuperclass
        if (type is ParameterizedType) {
            val tp = type.actualTypeArguments[0]
            val tClass = tp as? Class<VM> ?: BaseViewModel::class.java
            //     mViewModel = ViewModelProvider(viewModelStore, defaultViewModelProviderFactory).get(tClass) as VM

            val viewModelStore = if (isShareVM()) activity!!.viewModelStore else this.viewModelStore
            mViewModel = ViewModelProvider(viewModelStore, defaultViewModelProviderFactory).get(tClass) as VM
        }
    }

    /**
     * 是否和 Activity 共享 ViewModel,默认不共享
     * Fragment 要和宿主 Activity 的泛型是同一个 ViewModel
     */
    open fun isShareVM(): Boolean = false

    override fun getDefaultViewModelProviderFactory(): ViewModelProvider.Factory {
        return MVVMLin.getConfig().viewModelFactory() ?: super.getDefaultViewModelProviderFactory()
    }


    protected lateinit var mDialogListener: OnDialogListener

    open fun setListener(dialogListener: OnDialogListener) {
        this.mDialogListener = dialogListener;
    }

    abstract class OnDialogListener {
        open fun onRightClickListener() {}
        open fun onLeftClickListener() {}
        open fun onCloseClickListener() {}
        open fun onConfirmClickListener(data: Any) {}
    }


}