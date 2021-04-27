package com.pai8.ke.viewmodel

import androidx.lifecycle.MutableLiveData
import com.lhs.library.base.BaseViewModel
import com.pai8.ke.activity.takeaway.entity.req.AddFoodReq
import com.pai8.ke.groupBuy.http.RetrofitClient

class OperateTakeawayViewModel : BaseViewModel() {
    val foodDeleteData = MutableLiveData<Boolean>()
    fun foodDelete(foodId: String) {
        launchOnlyResult({
            RetrofitClient.getInstance().getMainService().foodDelete(foodId)
        }, {
            foodDeleteData.value = true
        })
    }

    val editGoodsData = MutableLiveData<Boolean>()
    fun editGoods(param: AddFoodReq) {
        launchOnlyResult({
            RetrofitClient.getInstance().getMainService().editGoods(param)
        }, {
            editGoodsData.value = true
        })


    }


}