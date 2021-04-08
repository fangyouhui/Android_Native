package com.pai8.ke.viewmodel

import androidx.lifecycle.MutableLiveData
import com.lhs.library.base.BaseViewModel
import com.pai8.ke.activity.message.entity.resp.MsgCountResp
import com.pai8.ke.groupBuy.http.RetrofitClient
import com.pai8.ke.manager.AccountManager

class TabMsgViewModel : BaseViewModel() {
    val msgIndexData = MutableLiveData<MsgCountResp>()

    fun msgIndex() {
        launchOnlyResult({ RetrofitClient.getInstance().getMainService().msgIndex(AccountManager.getInstance().uid) }, {
            msgIndexData.value = it
        })
    }
}