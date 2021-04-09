package com.pai8.ke.viewmodel

import androidx.lifecycle.MutableLiveData
import com.lhs.library.base.BaseViewModel
import com.pai8.ke.entity.resp.ShareMiniResp
import com.pai8.ke.groupBuy.http.RetrofitClient

class ShareViewModel : BaseViewModel() {
    val shareMiniData = MutableLiveData<ShareMiniResp>()
    fun shareMini(type: Int, id: String) {
        launchOnlyResult({ RetrofitClient.getInstance().getMainService().shareMini(type, id) }, {
            shareMiniData.value = it
        })

    }

}