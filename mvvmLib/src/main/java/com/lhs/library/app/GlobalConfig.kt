package com.aleyn.mvvm.app

import androidx.lifecycle.ViewModelProvider
import com.lhs.library.base.ViewModelFactory
import com.lhs.library.network.ExceptionHandle

/**
 *   @auther : Aleyn
 *   time   : 2019/11/12
 */
interface GlobalConfig {

    fun viewModelFactory(): ViewModelProvider.Factory? = ViewModelFactory.getInstance()

    fun globalExceptionHandle(e: Throwable) = ExceptionHandle.handleException(e)

}