package com.pai8.ke.viewmodel

import androidx.lifecycle.MutableLiveData
import com.lhs.library.base.BaseViewModel
import com.pai8.ke.activity.takeaway.entity.OrderDetailResult
import com.pai8.ke.groupBuy.http.RetrofitClient
import com.pai8.ke.manager.AccountManager

class ShopOrderViewModel : BaseViewModel() {

    val orderDetailData = MutableLiveData<OrderDetailResult>()

    fun orderDetail(orderNo: String) {
        launchOnlyResult({
            RetrofitClient.getInstance().getMainService().orderDetail(orderNo)
        }, {
            orderDetailData.value = it
        }, isShowDialog = true)
    }


    val cancelOrderData = MutableLiveData<String>()
    fun cancelOrder(orderNO: String) {
        launchOnlyResult({
            RetrofitClient.getInstance().getMainService().cancelOrder(orderNO, AccountManager.getInstance().uid)
        }, { cancelOrderData.value = it })
    }

    val shopDealOrderData = MutableLiveData<List<String>>()

    //0为接单 1为拒绝订单 2为同意退款申请 3为拒绝退款申请 4为订单制作完成 5为订单配送操作
    fun shopDealOrder(order_no: String, type: Int) {
        launchOnlyResult({
            RetrofitClient.getInstance().getMainService().shopDealOrder(AccountManager.getInstance().shopId, order_no, type)
        }, {
            shopDealOrderData.value = it
        })
    }


    val applyRefundData = MutableLiveData<List<String>>()
    fun applyRefund(order_no: String) {
        launchOnlyResult({
            RetrofitClient.getInstance().getMainService().applyRefund(AccountManager.getInstance().uid, order_no, "")
        }, {
            applyRefundData.value = it
        })
    }

}