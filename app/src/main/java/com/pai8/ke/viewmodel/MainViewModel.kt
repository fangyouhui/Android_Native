package com.pai8.ke.viewmodel

import androidx.lifecycle.MutableLiveData
import com.lhs.library.base.BaseViewModel
import com.pai8.ke.entity.resp.MyInfoResp
import com.pai8.ke.entity.resp.VersionResp
import com.pai8.ke.groupBuy.http.RetrofitClient

class MainViewModel : BaseViewModel() {

    val checkUpgradeData = MutableLiveData<VersionResp>()
    fun checkUpgrade() {
        launchOnlyResult({
            RetrofitClient.getInstance().getMainService().checkUpgrade()
        }, {
            checkUpgradeData.value = it
        })
    }

    val myInfoData = MutableLiveData<MyInfoResp>()
    fun getMyInfo() {
        launchOnlyResult({
            RetrofitClient.getInstance().getMainService().ucenter()
        }, {
            myInfoData.value = it
        })
    }

}