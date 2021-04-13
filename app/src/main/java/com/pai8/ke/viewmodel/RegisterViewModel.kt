package com.pai8.ke.viewmodel

import androidx.lifecycle.MutableLiveData
import com.lhs.library.base.BaseViewModel
import com.pai8.ke.groupBuy.http.RetrofitClient
import org.json.JSONObject

class RegisterViewModel : BaseViewModel() {

    val getCodeData = MutableLiveData<JSONObject>()
    fun getCode(mobile: String) {
        launchOnlyResult({ RetrofitClient.getInstance().getMainService().getCode(mobile) }, {
            getCodeData.value = it
        })
    }

    val registerCodeData = MutableLiveData<JSONObject>()
    fun register(mobile: String, code: String, pwd: String) {
        launchOnlyResult({ RetrofitClient.getInstance().getMainService().register(mobile, code, pwd, pwd) }, {
            registerCodeData.value = it
        })
    }
}