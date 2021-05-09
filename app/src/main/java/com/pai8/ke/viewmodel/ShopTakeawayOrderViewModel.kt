package com.pai8.ke.viewmodel

import androidx.lifecycle.MutableLiveData
import com.lhs.library.base.BaseViewModel
import com.pai8.ke.activity.takeaway.entity.OrderInfo
import com.pai8.ke.groupBuy.http.RetrofitClient
import com.pai8.ke.manager.AccountManager

class ShopTakeawayOrderViewModel : BaseViewModel() {

    val shopOrderListData = MutableLiveData<List<OrderInfo>>()
    fun shopOrderList(order_status: String, page: Int) { //0为待支付 1为已支付 2为商家已接单 7为订单制作完成 3为配送中 4为订单已完成 5为订单已申请退款 6订单被拒绝退款 8为订单已退款 9为订单已取消 -1为支付超时 -2订单拒绝接单
        launchOnlyResult({
            RetrofitClient.getInstance().getMainService().shopOrderList(AccountManager.getInstance().shopId, order_status, 2, page)
        }, {
            shopOrderListData.value = it
        }, {
            // 后台返回这个{"code":0,"msg":"未查询到商家订单","result":[]}，需要特殊处理
            shopOrderListData.value = arrayListOf()
        })

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


    fun groudOrderList(page: Int) {
        launchOnlyResult({
            RetrofitClient.getInstance().getMainService().shopOrderList(AccountManager.getInstance().shopId, "4", 3, page)
        }, {
            shopOrderListData.value = it
        }, {
            shopOrderListData.value = arrayListOf()
        })
    }

}