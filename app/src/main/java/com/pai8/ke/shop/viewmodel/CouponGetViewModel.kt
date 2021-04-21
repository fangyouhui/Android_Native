package com.pai8.ke.shop.viewmodel

import androidx.lifecycle.MutableLiveData
import com.lhs.library.base.BaseViewModel
import com.pai8.ke.activity.me.entity.resp.ShopCouponListResult
import com.pai8.ke.groupBuy.http.RetrofitClient
import com.pai8.ke.manager.AccountManager
import org.json.JSONObject

class CouponGetViewModel : BaseViewModel() {

    val shopCouponListData = MutableLiveData<ShopCouponListResult>()
    fun shopCouponList(shop_id: String) {
        launchOnlyResult({ RetrofitClient.getInstance().getMainService().shopCouponList(shop_id, AccountManager.getInstance().uid) },
                { shopCouponListData.value = it })
    }

    val getCouponData = MutableLiveData<JSONObject>()
    fun getCoupon(coupon_ids: String) {
        launchOnlyResult({ RetrofitClient.getInstance().getMainService().getCoupon(AccountManager.getInstance().uid, coupon_ids) },
                { getCouponData.value = it })
    }

}