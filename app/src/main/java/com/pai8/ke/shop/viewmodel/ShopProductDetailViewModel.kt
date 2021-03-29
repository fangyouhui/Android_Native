package com.pai8.ke.shop.viewmodel

import androidx.lifecycle.MutableLiveData
import com.lhs.library.base.BaseViewModel
import com.pai8.ke.entity.GroupShopInfoResult
import com.pai8.ke.groupBuy.http.RetrofitClient

class ShopProductDetailViewModel : BaseViewModel() {

    val getGroupShopInfoData = MutableLiveData<GroupShopInfoResult>()
    fun getGroupShopInfo(shop_id: String) {
        launchOnlyResult({ RetrofitClient.getInstance().getMainService().getGroupShopInfo(shop_id) }, { getGroupShopInfoData.value = it })
    }

}