package com.pai8.ke.viewmodel

import androidx.lifecycle.MutableLiveData
import com.lhs.library.base.BaseViewModel
import com.pai8.ke.activity.takeaway.entity.req.GroupFoodReq
import com.pai8.ke.activity.takeaway.entity.resq.GoodsInfoModel
import com.pai8.ke.activity.takeaway.entity.resq.smallGoodsInfo
import com.pai8.ke.entity.BusinessTypeResult
import com.pai8.ke.groupBuy.http.RetrofitClient

class AddGroupGoodViewModel : BaseViewModel() {
    val videotypeData = MutableLiveData<List<BusinessTypeResult>>()
    fun setvideotype() {
        launchOnlyResult({ RetrofitClient.getInstance().getMainService().setvideotype() }, {
            videotypeData.value = it
        })
    }

    val groupFoodDeleteData = MutableLiveData<smallGoodsInfo>()
    fun groupFoodDelete(food_id: String) {
        launchOnlyResult({
            RetrofitClient.getInstance().getMainService().setGroupGoodsStatus(food_id, "2")
        }, {
            groupFoodDeleteData.value = it
        })

    }

    val addGoodData = MutableLiveData<smallGoodsInfo>()
    fun addGood(req: GroupFoodReq) {
        launchOnlyResult({
            RetrofitClient.getInstance().getMainService().addGroupFood(req)
        }, {
            addGoodData.value = it
        })
    }

    val editGoodsData = MutableLiveData<smallGoodsInfo>()
    fun editGoods(req: GroupFoodReq) {
        launchOnlyResult({
            RetrofitClient.getInstance().getMainService().editGroupFood(req)
        }, {
            editGoodsData.value = it
        })
    }

    val goodInfoData = MutableLiveData<GoodsInfoModel>()
    fun getGoodInfo(food_id: String) {
        launchOnlyResult({
            RetrofitClient.getInstance().getMainService().getGroupFood(food_id)
        }, {
            goodInfoData.value = it
        })
    }
}