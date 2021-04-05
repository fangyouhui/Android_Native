package com.lhs.library.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.viewbinding.ViewBinding
import com.aleyn.mvvm.app.MVVMLin
import com.blankj.utilcode.util.ToastUtils
import com.lhs.library.event.Message
import com.lhs.library.widget.CustomProgressDialog
import java.lang.reflect.ParameterizedType

abstract class BaseFragment<VM : BaseViewModel, DB : ViewBinding> : Fragment() {

    protected lateinit var mViewModel: VM

    protected lateinit var mBinding: DB

    //是否第一次加载
    private var isFirst: Boolean = true

    private var progressDialog: CustomProgressDialog? = null

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        return initBinding(inflater, container)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        onVisible()
        createViewModel()
        lifecycle.addObserver(mViewModel)
        //注册 UI事件
        registerDefUIChange()
        addObserve()
        initView(savedInstanceState)
        initData()
    }

    open fun initView(savedInstanceState: Bundle?) {}
    open fun initData() {}
    open fun addObserve() {}
    override fun onResume() {
        super.onResume()
        onVisible()
    }

    /**
     * 使用 DataBinding时,要重写此方法返回相应的布局 id
     * 使用ViewBinding时，不用重写此方法
     */
    open fun layoutId(): Int = 0

    /**
     * 是否需要懒加载
     */
    private fun onVisible() {
        if (lifecycle.currentState == Lifecycle.State.STARTED && isFirst) {
            lazyLoadData()
            isFirst = false
        }
    }

    /**
     * 懒加载
     */
    open fun lazyLoadData() {}

    /**
     * 注册 UI 事件
     */
    private fun registerDefUIChange() {
        mViewModel.defUI.showDialog.observe(viewLifecycleOwner, {
            showLoading()
        })
        mViewModel.defUI.dismissDialog.observe(viewLifecycleOwner, {
            dismissLoading()
        })
        mViewModel.defUI.toastEvent.observe(viewLifecycleOwner, {
            ToastUtils.showShort(it)
        })
        mViewModel.defUI.msgEvent.observe(viewLifecycleOwner, {
            handleEvent(it)
        })
    }

    open fun handleEvent(msg: Message) {}

    /**
     * 打开等待框
     */
    private fun showLoading() {
        if (progressDialog == null) {
            progressDialog = CustomProgressDialog(context)
        }
        progressDialog?.show()
    }

    /**
     * 关闭等待框
     */
    private fun dismissLoading() {
        progressDialog?.cancel()
    }


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
            mViewModel = ViewModelProvider(viewModelStore, defaultViewModelProviderFactory).get(tClass) as VM
        }
    }

    override fun getDefaultViewModelProviderFactory(): ViewModelProvider.Factory {
        return MVVMLin.getConfig().viewModelFactory() ?: super.getDefaultViewModelProviderFactory()
    }

}