package com.pai8.ke.viewmodel

import androidx.lifecycle.MutableLiveData
import com.lhs.library.base.BaseViewModel
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
}