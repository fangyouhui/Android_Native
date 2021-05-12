package com.pai8.ke.shop.viewmodel

import androidx.lifecycle.MutableLiveData
import com.lhs.library.base.BaseViewModel
import com.pai8.ke.activity.takeaway.entity.resq.StoreInfoResult
import com.pai8.ke.entity.BusinessTypeResult
import com.pai8.ke.entity.GroupShopInfoResult
import com.pai8.ke.groupBuy.http.RetrofitClient
import com.pai8.ke.manager.AccountManager

class ShopQrCodeViewModel : BaseViewModel() {

    val groupShopInfoData = MutableLiveData<GroupShopInfoResult>()
    fun getGroupShopInfo(shop_id: String) {
        launchOnlyResult({
            RetrofitClient.getInstance().getMainService().getGroupShopInfo(shop_id)
        }, { groupShopInfoData.value = it })
    }

    val videoTypeData = MutableLiveData<List<BusinessTypeResult>>()
    fun videoType() {
        launchOnlyResult({
            RetrofitClient.getInstance().getMainService().videoType()
        }, {
            videoTypeData.value = it
        })
    }

    val shopIndexData = MutableLiveData<StoreInfoResult>()
    fun shopIndex() {
        launchOnlyResult({
            RetrofitClient.getInstance().getMainService().shopIndex(AccountManager.getInstance().shopId)
        }, { shopIndexData.value = it })
    }

}
