package com.pai8.ke.shop.viewmodel

import androidx.lifecycle.MutableLiveData
import com.lhs.library.base.BaseViewModel
import com.pai8.ke.entity.GroupShopInfoResult
import com.pai8.ke.entity.ShopGroupListResult
import com.pai8.ke.entity.Video
import com.pai8.ke.groupBuy.http.RetrofitClient
import com.pai8.ke.manager.AccountManager

class BusinessHomeViewModel : BaseViewModel() {

    val getGroupShopInfoData = MutableLiveData<GroupShopInfoResult>()
    fun getGroupShopInfo(shop_id: String) {
        launchOnlyResult({ RetrofitClient.getInstance().getMainService().getGroupShopInfo(shop_id) }, { getGroupShopInfoData.value = it })
    }

    val shopGroupListData = MutableLiveData<List<ShopGroupListResult>>()
    fun getShopGroupList(shop_id: String, page: String) {
        launchOnlyResult({ RetrofitClient.getInstance().getMainService().getShopGroupList(shop_id, page, "10") }, { shopGroupListData.value = it })
    }

    val isUserFollowData = MutableLiveData<Boolean>()
    fun isUserFollow(shop_id: String) {
        launchOnlyResult({ RetrofitClient.getInstance().getMainService().isUserFollow(shop_id, AccountManager.getInstance().uid) }, {
            isUserFollowData.value = true
        }, {
            isUserFollowData.value = false
        })
    }


    val shopVideoListData = MutableLiveData<List<Video>>()
    fun getShopVideoList(shop_id: String, page: String) {
        launchOnlyResult({ RetrofitClient.getInstance().getMainService().getShopVideoList(shop_id, page, "10") }, { shopVideoListData.value = it })
    }

    val shopCollectData = MutableLiveData<Boolean>()
    fun shopCollect(shop_id: String) {
        launchOnlyResult({ RetrofitClient.getInstance().getMainService().shopCollect(shop_id) }, {
            shopCollectData.value = true
        })
    }


    fun shopUncollect(shop_id: String) {
        launchOnlyResult({ RetrofitClient.getInstance().getMainService().shopUncollect(shop_id) }, {
            shopCollectData.value = false
        })
    }


}
