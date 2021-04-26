package com.pai8.ke.viewmodel

import androidx.lifecycle.MutableLiveData
import com.lhs.library.base.BaseViewModel
import com.pai8.ke.entity.resp.VideoListResp
import com.pai8.ke.groupBuy.http.RetrofitClient

class VideoHomeViewModel : BaseViewModel() {
    
    val data = MutableLiveData<VideoListResp>()
    fun nearby(page: Int) {
        launchOnlyResult({
            RetrofitClient.getInstance().getMainService().nearby(page, 10)
        }, {
            data.value = it
        })
    }

    fun flow(page: Int) {
        launchOnlyResult({
            RetrofitClient.getInstance().getMainService().flow(page, 10)
        }, {
            data.value = it
        })
    }

    fun follow(page: Int) {
        launchOnlyResult({
            RetrofitClient.getInstance().getMainService().follow(page, 10)
        }, {
            data.value = it
        })
    }
}