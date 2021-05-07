package com.pai8.ke.viewmodel

import androidx.lifecycle.MutableLiveData
import com.lhs.library.base.BaseViewModel
import com.pai8.ke.entity.BusinessTypeResult
import com.pai8.ke.groupBuy.http.RetrofitClient

class CategoryViewModel : BaseViewModel() {

    val videoTypeData = MutableLiveData<List<BusinessTypeResult>>()
    fun videoType() {
        launchOnlyResult({
            RetrofitClient.getInstance().getMainService().videoType()
        }, {
            videoTypeData.value = it
        })
    }


}