package com.pai8.ke.shop.viewmodel

import androidx.lifecycle.MutableLiveData
import com.lhs.library.base.BaseViewModel
import com.pai8.ke.entity.AddOrderParam
import com.pai8.ke.entity.OrderPrepayResult
import com.pai8.ke.groupBuy.http.RetrofitClient

class ConfirmOrderViewModel : BaseViewModel() {

    val addOrderData = MutableLiveData<String>()
    fun addOrder(param: AddOrderParam) {
        launchOnlyResult({ RetrofitClient.getInstance().getMainService().addOrder(param) }, {
            addOrderData.value = it
        }, isShowDialog = true)
    }

    val orderPrepayData = MutableLiveData<OrderPrepayResult>()
    fun orderPrepayWithWx(order_no: String, buyer_id: Int) {
        launchOnlyResult({ RetrofitClient.getInstance().getMainService().orderPrepayWithWx(order_no, buyer_id, 1) }, {
            orderPrepayData.value = it
        }, isShowDialog = true)
    }


    val orderPrepayData2 = MutableLiveData<String>()
    fun orderPrepayWithAli(order_no: String, buyer_id: Int) {
        launchOnlyResult({ RetrofitClient.getInstance().getMainService().orderPrepayWithAli(order_no, buyer_id, 2) }, {
            orderPrepayData2.value = it
        }, isShowDialog = true)
    }
}