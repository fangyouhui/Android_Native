package com.pai8.ke.shop.viewmodel

import androidx.lifecycle.MutableLiveData
import com.lhs.library.base.BaseViewModel
import com.pai8.ke.entity.OrderPrepayResult
import com.pai8.ke.groupBuy.http.RetrofitClient
import com.pai8.ke.manager.AccountManager

class PaySelectBottomViwModel : BaseViewModel() {

    val orderPrepayWxData = MutableLiveData<OrderPrepayResult>()
    fun orderPrepayWithWx(order_no: String) {
        launchOnlyResult({ RetrofitClient.getInstance().getMainService().orderPrepayWithWx(order_no, AccountManager.getInstance().uid, 1) }, {
            orderPrepayWxData.value = it
        }, isShowDialog = true)
    }

    val orderPrepayAliData = MutableLiveData<String>()
    fun orderPrepayWithAli(order_no: String) {
        launchOnlyResult({ RetrofitClient.getInstance().getMainService().orderPrepayWithAli(order_no, AccountManager.getInstance().uid, 2) }, {
            orderPrepayAliData.value = it
        }, isShowDialog = true)
    }
}