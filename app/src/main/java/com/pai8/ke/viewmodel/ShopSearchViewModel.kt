package com.pai8.ke.viewmodel

import androidx.lifecycle.MutableLiveData
import com.lhs.library.base.BaseViewModel
import com.pai8.ke.entity.resp.ShopList
import com.pai8.ke.groupBuy.http.RetrofitClient

class ShopSearchViewModel : BaseViewModel() {

    val shopSelectData = MutableLiveData<List<ShopList>>()
    fun shopSelect(pageNo: Int, keyWord: String) {
        launchOnlyResult({
            RetrofitClient.getInstance().getMainService().shopSelect(pageNo, keyWord)
        }, {
            shopSelectData.value = it
        })
    }
}