package com.pai8.ke.groupBuy.viewmodel

import androidx.lifecycle.MutableLiveData
import com.lhs.library.base.BaseViewModel
import com.pai8.ke.entity.BannerResult
import com.pai8.ke.entity.BusinessTypeResult
import com.pai8.ke.entity.GetGroupShopListParam
import com.pai8.ke.entity.GetGroupShopListResult
import com.pai8.ke.groupBuy.http.RetrofitClient

class GroupBuyMainViewModel : BaseViewModel() {

    val bannerData = MutableLiveData<List<BannerResult>>()
    fun getBannerList() {
        launchOnlyResult({
            RetrofitClient.getInstance().getMainService().getBannerList()
        }, {
            bannerData.value = it
        })
    }

    val videotypeData = MutableLiveData<List<BusinessTypeResult>>()
    fun setvideotype() {
        launchOnlyResult({ RetrofitClient.getInstance().getMainService().setvideotype() }, {
            videotypeData.value = it
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