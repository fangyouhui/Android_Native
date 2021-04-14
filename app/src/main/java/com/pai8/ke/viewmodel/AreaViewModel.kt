package com.pai8.ke.viewmodel

import androidx.lifecycle.MutableLiveData
import com.lhs.library.base.BaseViewModel
import com.pai8.ke.entity.resp.Province
import com.pai8.ke.groupBuy.http.RetrofitClient

class AreaViewModel : BaseViewModel() {

    val areaData = MutableLiveData<List<Province>>()
    fun getArea() {
        launchOnlyResult({
            RetrofitClient.getInstance().getMainService().getArea()
        }, {
            areaData.value = it
        })
    }

}