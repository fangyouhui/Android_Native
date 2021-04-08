package com.pai8.ke.shop.viewmodel

import androidx.lifecycle.MutableLiveData
import com.lhs.library.base.BaseViewModel
import com.pai8.ke.entity.GroupGoodsInfoResult
import com.pai8.ke.entity.GroupShopInfoResult
import com.pai8.ke.groupBuy.http.RetrofitClient

class ShopProductDetailViewModel : BaseViewModel() {

    val getGroupShopInfoData = MutableLiveData<GroupShopInfoResult>()
    fun getGroupShopInfo(shop_id: String) {
        launchOnlyResult({ RetrofitClient.getInstance().getMainService().getGroupShopInfo(shop_id) }, { getGroupShopInfoData.value = it })
    }


    val getGroupGoodsInfoData = MutableLiveData<GroupGoodsInfoResult>()
    fun getGroupGoodsInfo(productId: String) {
        launchOnlyResult({ RetrofitClient.getInstance().getMainService().getGroupGoodsInfo(productId) }, { getGroupGoodsInfoData.value = it })
    }


    val addGoodsCollectionData = MutableLiveData<Boolean>()
    fun addGoodsCollection(goods_id: String) {
        launchOnlyResult({ RetrofitClient.getInstance().getMainService().AddGoodsCollection(goods_id) }, {
            addGoodsCollectionData.value = true
        })
    }


    fun setGoodsUncollect(goods_id: String) {
        launchOnlyResult({ RetrofitClient.getInstance().getMainService().SetGoodsUncollect(goods_id) }, {
            addGoodsCollectionData.value = false
        })
    }
}