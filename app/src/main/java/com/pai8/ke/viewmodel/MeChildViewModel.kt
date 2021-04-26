package com.pai8.ke.viewmodel

import androidx.lifecycle.MutableLiveData
import com.lhs.library.base.BaseViewModel
import com.pai8.ke.entity.resp.VideoListResp
import com.pai8.ke.groupBuy.http.RetrofitClient

class MeChildViewModel : BaseViewModel() {

    val data = MutableLiveData<VideoListResp>()

    fun myVideo(page: Int) {
        launchOnlyResult({
            RetrofitClient.getInstance().getMainService().myVideo(page, 10)
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

    fun myLike(page: Int) {
        launchOnlyResult({
            RetrofitClient.getInstance().getMainService().myLike(page, 10)
        }, {
            data.value = it
        })
    }

    fun myLink(page: Int) {
        launchOnlyResult({
            RetrofitClient.getInstance().getMainService().myLink(page, 10)
        }, {
            data.value = it
        })
    }
}