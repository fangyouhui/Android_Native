package com.pai8.ke.shop.viewmodel

import androidx.lifecycle.MutableLiveData
import com.lhs.library.base.BaseViewModel
import com.pai8.ke.activity.takeaway.entity.OrderListResult
import com.pai8.ke.groupBuy.http.RetrofitClient
import com.pai8.ke.manager.AccountManager

class OrderListViewModel : BaseViewModel() {

    val orderListData = MutableLiveData<List<OrderListResult>>()

    fun orderList(buyer_id: Int, order_type: Int) { //1为邮寄 2为外卖 3为核销
        launchOnlyResult(
                { RetrofitClient.getInstance().getMainService().orderList(buyer_id, order_type) },
                { orderListData.value = it })
    }

    val cancelOrderData = MutableLiveData<String>()
    fun cancelOrder(orderNO: String) {
        launchOnlyResult({
            RetrofitClient.getInstance().getMainService().cancelOrder(orderNO, AccountManager.getInstance().uid)
        }, { cancelOrderData.value = it })
    }

}