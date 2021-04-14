package com.pai8.ke.viewmodel

import androidx.lifecycle.MutableLiveData
import com.lhs.library.base.BaseViewModel
import com.pai8.ke.entity.UserInfo
import com.pai8.ke.entity.resp.MyInfoResp
import com.pai8.ke.groupBuy.http.RetrofitClient
import com.pai8.ke.manager.AccountManager

class TabMeViewModel : BaseViewModel() {

    val myInfoData = MutableLiveData<MyInfoResp>()
    fun getMyInfo() {
        launchOnlyResult({
            RetrofitClient.getInstance().getMainService().ucenter()
        }, {
            myInfoData.value = it
        })
    }

    val infoByUidData = MutableLiveData<UserInfo>()
    fun getInfoByUid() {
        launchOnlyResult({ RetrofitClient.getInstance().getMainService().getUserInfoById(AccountManager.getInstance().uid) }, { infoByUidData.value = it })
    }
}