package com.pai8.ke.viewmodel

import androidx.lifecycle.MutableLiveData
import com.lhs.library.base.BaseViewModel
import com.pai8.ke.entity.resp.BusinessType
import com.pai8.ke.groupBuy.http.RetrofitClient

class CategoryViewModel : BaseViewModel() {

    val businessTypeData = MutableLiveData<List<BusinessType>>()
    fun businessType() {
        launchOnlyResult({
            RetrofitClient.getInstance().getMainService().getBusinessType()
        }, {
            businessTypeData.value = it
        })
    }


}