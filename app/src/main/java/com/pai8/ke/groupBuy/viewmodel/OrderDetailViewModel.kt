package com.pai8.ke.groupBuy.viewmodel

import androidx.lifecycle.MutableLiveData
import com.lhs.library.base.BaseViewModel
import com.pai8.ke.activity.takeaway.entity.OrderDetailResult
import com.pai8.ke.groupBuy.http.RetrofitClient

class OrderDetailViewModel : BaseViewModel() {

    val orderDetailData = MutableLiveData<OrderDetailResult>()

    fun orderList(orderNo: String) {
        launchOnlyResult({
            RetrofitClient.getInstance().getMainService().orderDetail(orderNo)
        }, {
            orderDetailData.value = it
        }, isShowDialog = true)
    }
}