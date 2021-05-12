package com.pai8.ke.viewmodel

import androidx.lifecycle.MutableLiveData
import com.lhs.library.base.BaseViewModel
import com.pai8.ke.entity.BusinessTypeResult
import com.pai8.ke.entity.req.VideoPublishReq
import com.pai8.ke.groupBuy.http.RetrofitClient
import com.pai8.ke.manager.UploadFileManager
import com.pai8.ke.utils.ToastUtils

class VideoPublishViewModel : BaseViewModel() {

    val uploadVideoData = MutableLiveData<String>()
    fun uploadVideo(videoPath: String) {
        UploadFileManager.getInstance().upload(videoPath, object : UploadFileManager.Callback {
            override fun onSuccess(url: String, key: String) {
                uploadVideoData.value = key
            }

            override fun onError(msg: String) {
                ToastUtils.showShort("视频上传失败：$msg")
                uploadVideoData.value = ""
            }
        })
    }


    val upVideoData = MutableLiveData<Object>()
    fun upVideo(req: VideoPublishReq) {
        launchOnlyResult({
            RetrofitClient.getInstance().getMainService().upVideo(req)
        }, {
            upVideoData.value = it
        })
    }

    val videoTypeData = MutableLiveData<List<BusinessTypeResult>>()
    fun videoType() {
        launchOnlyResult({
            RetrofitClient.getInstance().getMainService().videoType()
        }, {
            videoTypeData.value = it
        })
    }

}