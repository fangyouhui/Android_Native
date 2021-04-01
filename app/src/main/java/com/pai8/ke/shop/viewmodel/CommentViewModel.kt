package com.pai8.ke.shop.viewmodel

import android.net.Uri
import androidx.lifecycle.MutableLiveData
import com.blankj.utilcode.util.UriUtils
import com.lhs.library.base.BaseViewModel
import com.luck.picture.lib.config.PictureMimeType
import com.luck.picture.lib.entity.LocalMedia
import com.pai8.ke.activity.takeaway.entity.OrderListResult
import com.pai8.ke.entity.UpCommentParam
import com.pai8.ke.groupBuy.http.RetrofitClient
import com.pai8.ke.manager.UploadFileManager

class CommentViewModel : BaseViewModel() {


    fun submit(images: List<LocalMedia>, bean: OrderListResult) {
        images.forEach {
            UploadFileManager.getInstance().upload(getPathByLocalMedia(it), object : UploadFileManager.Callback {
                override fun onSuccess(url: String, key: String) {


                }

                override fun onError(msg: String) {}
            })
        }

    }


    val upCommentData = MutableLiveData<String>()

    fun upComment(order_no: String, image: String, video: String, score: String, content: String, shop_id: String) {
        launchOnlyResult({
            val param = UpCommentParam(order_no, image, video, score, content, shop_id)
            RetrofitClient.getInstance().getMainService().upComment(param)
        }, {
            upCommentData.value = it
        })

    }


    private fun getPathByLocalMedia(media: LocalMedia): String {
        var path: String = if (media.isCut && !media.isCompressed) { // 裁剪过
            media.cutPath
        } else if (media.isCompressed || media.isCut && media.isCompressed) { // 压缩过,或者裁剪同时压缩过,以最终压缩过图片为准
            media.compressPath
        } else {  // 原图
            media.path
        }
        if (PictureMimeType.isContent(path) && !media.isCut && !media.isCompressed) {
            val uri = Uri.parse(path)
            path = UriUtils.uri2File(uri).absolutePath
        }
        return path;
    }
}