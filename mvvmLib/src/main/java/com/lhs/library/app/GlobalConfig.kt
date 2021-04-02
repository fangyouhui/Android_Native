package com.aleyn.mvvm.app

import androidx.lifecycle.ViewModelProvider
import com.lhs.library.base.ViewModelFactory
import com.lhs.library.network.ExceptionHandle

interface GlobalConfig {

    fun viewModelFactory(): ViewModelProvider.Factory? = ViewModelFactory.getInstance()

    fun globalExceptionHandle(e: Throwable) = ExceptionHandle.handleException(e)

}