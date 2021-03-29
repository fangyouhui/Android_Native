package com.pai8.ke.shop.viewmodel

import androidx.lifecycle.MutableLiveData
import com.lhs.library.base.BaseViewModel
import com.pai8.ke.entity.GroupShopInfoResult
import com.pai8.ke.entity.ShopGroupListResult
import com.pai8.ke.entity.ShopVideoResult
import com.pai8.ke.groupBuy.http.RetrofitClient

class BusinessHomeViewModel : BaseViewModel() {

    val getGroupShopInfoData = MutableLiveData<GroupShopInfoResult>()
    fun getGroupShopInfo(shop_id: String) {
        launchOnlyResult({ RetrofitClient.getInstance().getMainService().getGroupShopInfo(shop_id) }, { getGroupShopInfoData.value = it })
    }

    val shopGroupListData = MutableLiveData<List<ShopGroupListResult>>()
    fun getShopGroupList(shop_id: String, page: String) {
        launchOnlyResult({ RetrofitClient.getInstance().getMainService().getShopGroupList(shop_id, page, "10") }, { shopGroupListData.value = it })
    }


    val shopVideoListData = MutableLiveData<List<ShopVideoResult>>()
    fun getShopVideoList(shop_id: String, page: String) {
        launchOnlyResult({ RetrofitClient.getInstance().getMainService().getShopVideoList(shop_id, page, "10") }, { shopVideoListData.value = it })
    }
}
