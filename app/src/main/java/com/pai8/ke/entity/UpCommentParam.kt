package com.pai8.ke.entity

data class UpCommentParam(
        var order_no: String? = null, //订单号
        var image: String? = null,//图片
        var video: String? = null,//视频
        var score: String? = null,//评分（最高5分）
        var content: String? = null,//评论内容
        var shop_id: String? = null)   //商家id
