package com.pai8.ke.groupBuy.viewmodel

import androidx.lifecycle.MutableLiveData
import com.pai8.ke.groupBuy.http.RetrofitClient
import com.lhs.library.base.BaseViewModel
import com.pai8.ke.entity.BusinessTypeResult
import com.pai8.ke.entity.GetGroupShopListParam
import com.pai8.ke.entity.GetGroupShopListResult

class GroupBuyMainViewModel : BaseViewModel() {

    val businessTypeData = MutableLiveData<List<BusinessTypeResult>>()

    fun businessType() {
        launchOnlyResult({ RetrofitClient.getInstance().getMainService().businessType() }, {
            businessTypeData.value = it
        })
    }

    val getGroupShopListData = MutableLiveData<GetGroupShopListResult>()
    fun getGroupShopList(page: String, size: String, shop_name: String, cate_id: String) {
        launchOnlyResult({
            val param = GetGroupShopListParam(page, size, shop_name, cate_id)
            RetrofitClient.getInstance().getMainService().getGroupShopList(param)
        }, {
            getGroupShopListData.value = it
        })
    }

}