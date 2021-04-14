package com.pai8.ke.viewmodel

import androidx.lifecycle.MutableLiveData
import com.lhs.library.base.BaseViewModel
import com.pai8.ke.activity.takeaway.entity.req.StoreInfoParam
import com.pai8.ke.activity.takeaway.entity.resq.StoreInfoResult
import com.pai8.ke.groupBuy.http.RetrofitClient
import com.pai8.ke.manager.AccountManager
import org.json.JSONObject

class StoreManagerEditViewModel : BaseViewModel() {

    val shopEditInfoData = MutableLiveData<StoreInfoResult>()
    fun shopEditInfo() {
        launchOnlyResult({
            RetrofitClient.getInstance().getMainService().shopEditInfo(AccountManager.getInstance().shopId, AccountManager.getInstance().uid)
        }, {
            shopEditInfoData.value = it
        })
    }

    val editShopData = MutableLiveData<JSONObject>()
    fun editShop(param: StoreInfoParam) {
        launchOnlyResult({
            RetrofitClient.getInstance().getMainService().editShop(param)
        }, {
            editShopData.value = it
        })

    }
}