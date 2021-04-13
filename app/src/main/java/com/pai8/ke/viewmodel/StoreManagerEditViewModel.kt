package com.pai8.ke.viewmodel

import androidx.lifecycle.MutableLiveData
import com.lhs.library.base.BaseViewModel
import com.pai8.ke.activity.takeaway.entity.req.StoreInfoReq
import com.pai8.ke.activity.takeaway.entity.resq.StoreInfo
import com.pai8.ke.entity.resp.BusinessType
import com.pai8.ke.entity.resp.Province
import com.pai8.ke.groupBuy.http.RetrofitClient
import com.pai8.ke.manager.AccountManager
import org.json.JSONObject

class StoreManagerEditViewModel : BaseViewModel() {

    val shopEditInfoData = MutableLiveData<StoreInfo>()
    fun shopEditInfo() {
        launchOnlyResult({
            RetrofitClient.getInstance().getMainService().shopEditInfo(AccountManager.getInstance().shopId, AccountManager.getInstance().uid)
        }, {
            shopEditInfoData.value = it
        })
    }

    val businessTypeData = MutableLiveData<List<BusinessType>>()
    fun businessType() {
        launchOnlyResult({
            RetrofitClient.getInstance().getMainService().getBusinessType()
        }, {
            businessTypeData.value = it
        })
    }


    val areaData = MutableLiveData<List<Province>>()
    fun area() {
        launchOnlyResult({
            RetrofitClient.getInstance().getMainService().getArea()
        }, {
            areaData.value = it
        })
    }

    val editShopData = MutableLiveData<JSONObject>()
    fun editShop(param: StoreInfoReq) {
        launchOnlyResult({
            RetrofitClient.getInstance().getMainService().editShop(param)
        }, {
            editShopData.value = it
        })

    }
}