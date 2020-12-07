package com.pai8.ke.activity.message.entity.resp;

import java.io.Serializable;

/**
 * @author Created by zzf
 * @time 2020/12/5 11:41
 * Description：
 */
public class MessageResp implements Serializable {

    /*
    接口地址Demo/msg/msgList
    返回值说明：

1、当请求参数type值为1时（系统消息）：
	id：消息表内id
    type：系统消息类型 1为店铺申请 2为获得优惠券 3为拉黑通知
	content：消息内容
	value：根据type不同，分别代表店铺id，优惠券id，
	receive_id：消息接收者id
	add_time：消息添加时间
	read_status：消息是否已读 1为已读 2为未读
	title：消息标题

2、当请求参数type值为2时（订单消息）：
	id：消息表内id
    type：订单消息类型 1为下单消息 2为订单处理消息
	content：消息内容
	value：订单id值
	receive_id：消息接收者id
	add_time：消息添加时间
	read_status：消息是否已读 1为已读 2为未读
	title：消息标题

3、当请求参数type值为3时（关注消息）：
	id：消息表内id
    type：关注消息类型 默认为1
	content：消息内容
	value：此值该类型下无用
	builder_id：消息触发者id
	receive_id：消息接收者id
	add_time：消息添加时间
	read_status：消息是否已读 1为已读 2为未读
	nickname：触发者用户昵称
	avatar：触发者用户头像url地址
	is_focus：是否已经关注触发者 1为已关注 2为未关注

4、当请求参数type值为4时（点赞消息）：
	id：消息表内id
    type：点赞消息类型 默认为1
	content：消息内容
	value：被点赞的视频的id值
	builder_id：消息触发者id
	receive_id：消息接收者id
	add_time：消息添加时间
	read_status：消息是否已读 1为已读 2为未读
	nickname：触发者用户昵称
	avatar：触发者用户头像url地址
	video_cover：视频封面url地址

5、当请求参数type值为5时（评论消息）：
	id：消息表内id
    type：评论消息类型 默认为1
	content：消息内容
	value：被评论的视频的id值
	builder_id：消息触发者id
	receive_id：消息接收者id
	add_time：消息添加时间
	read_status：消息是否已读 1为已读 2为未读
	nickname：触发者用户昵称
	avatar：触发者用户头像url地址
	video_cover：视频封面url地址

6、当请求参数type值为6时（活动消息）：
	id：消息表内id
    type：活动消息类型 1为美食节活动 2为配置活动
	content：消息内容
	value：暂时无用
	builder_id：消息触发者id
	receive_id：消息接收者id
	add_time：消息添加时间
	read_status：消息是否已读 1为已读 2为未读
	title：消息标题

7、当请求参数type值为7时（一对一通话消息）：
	id：消息表内id
    type：一对一通话消息类型 默认为1
	content：消息内容
	value：是否接听 1为已接听 2为未接听
	builder_id：消息触发者id
	receive_id：消息接收者id
	add_time：消息添加时间
	read_status：消息是否已读 1为已读 2为未读
	shop_name：店铺名称
	nickname：触发者用户昵称
	avatar：触发者用户头像url地址
	call_status：接听状态的文字描述

*/
    public long id;
    public int type;
    public String content;
    public String value;
    public long builder_id;
    public long receive_id;
    public int add_time;
    public int read_status;
    public String nickname;
    public String avatar;
    public String title;
    public int is_focus;
    public String video_cover;
    public String shop_name;
    public String call_status;
}
