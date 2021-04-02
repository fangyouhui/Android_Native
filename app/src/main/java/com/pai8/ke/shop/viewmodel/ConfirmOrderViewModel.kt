package com.pai8.ke.shop.viewmodel

import androidx.lifecycle.MutableLiveData
import com.lhs.library.base.BaseViewModel
import com.pai8.ke.entity.AddOrderParam
import com.pai8.ke.groupBuy.http.RetrofitClient

class ConfirmOrderViewModel : BaseViewModel() {

    val addOrderData = MutableLiveData<String>()
    fun addOrder(param: AddOrderParam) {
        launchOnlyResult({ RetrofitClient.getInstance().getMainService().addOrder(param) }, {
            addOrderData.value = it
        }, isShowDialog = true)
    }

}