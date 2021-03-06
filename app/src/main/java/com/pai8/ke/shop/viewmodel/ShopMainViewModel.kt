package com.pai8.ke.shop.viewmodel

import androidx.lifecycle.MutableLiveData
import com.lhs.library.base.BaseViewModel
import com.pai8.ke.entity.BannerResult
import com.pai8.ke.entity.BusinessTypeResult
import com.pai8.ke.entity.GroupBuyTypeResult
import com.pai8.ke.entity.ShopTypeResult
import com.pai8.ke.groupBuy.http.RetrofitClient

class ShopMainViewModel : BaseViewModel() {
//    val getFoodSortListData = MutableLiveData<List<GroupBuyTypeResult>>()
//    fun getFoodSortList() {
//        launchOnlyResult({
//            RetrofitClient.getInstance().getMainService().getFoodSortList();
//        }, { getFoodSortListData.value = it })
//    }

    val videotypeData = MutableLiveData<List<BusinessTypeResult>>()
    fun setvideotype() {
        launchOnlyResult({ RetrofitClient.getInstance().getMainService().setvideotype() }, {
            videotypeData.value = it
        })
    }

    val getGroupGoodsListData = MutableLiveData<List<ShopTypeResult>>()
    fun getGroupGoodsList(page: String, food_type: String) {
        launchOnlyResult({
            val param = mapOf("page" to page, "size" to "10", "food_type" to food_type)
            RetrofitClient.getInstance().getMainService().getGroupGoodsList(param);
        }, { getGroupGoodsListData.value = it })
    }

    val bannerListData = MutableLiveData<List<BannerResult>>()
    fun getBannerList() {
        launchOnlyResult({
            RetrofitClient.getInstance().getMainService().getBannerList()
        }, {
            bannerListData.value = it
        })
    }

}