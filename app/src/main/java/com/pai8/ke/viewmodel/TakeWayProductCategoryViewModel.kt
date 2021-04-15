package com.pai8.ke.viewmodel

import androidx.lifecycle.MutableLiveData
import com.lhs.library.base.BaseViewModel
import com.pai8.ke.activity.takeaway.entity.resq.ShopInfo
import com.pai8.ke.entity.resp.BusinessType
import com.pai8.ke.groupBuy.http.RetrofitClient
import com.pai8.ke.manager.AccountManager

class TakeWayProductCategoryViewModel : BaseViewModel() {

    val categoryListData = MutableLiveData<List<ShopInfo>>()
    fun categoryList() {
        launchOnlyResult({
            RetrofitClient.getInstance().getMainService().categoryList(AccountManager.getInstance().shopId)
        }, {
            categoryListData.value = it
        })
    }


}