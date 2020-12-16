package com.pai8.ke.activity.me.entity.resp;

import com.pai8.ke.activity.me.entity.PaginationBean;
import com.pai8.ke.entity.Video;

import java.util.List;

/**
 * @author Created by zzf
 * @time 2020/12/13 22:43
 * Description：
 */
public class HistoryResp {
    /**
     * items : [{"id":91,"video_desc":"茅台 三块，三块，茅台","like_counts":0,"longitude":"109.161655","latitude":"21.447187","shop_id":0,"comment_counts":0,"look_counts":2,"create_time":"2020-11-30","user":{"id":99,"nickname":"戴鹏","avatar":"","phone":"","wechat":""},"user_id":99,"user_nickname":"","avatar":"","like_status":0,"follow_status":0,"shop_type":"外卖","onsale":0,"shop":null,"distance":"8657km","cover_path":"https://jianshen.fyh5p8.com/o_1ehj6us2fs02ms01vjs17r8khca.mp4?vframe/jpg/offset/0","video_path":"https://jianshen.fyh5p8.com/o_1ehj6us2fs02ms01vjs17r8khca.mp4","share_url":"https://jianshen.fyh5p8.com/o_1ehj6us2fs02ms01vjs17r8khca.mp4","index":0}]
     * pagination : {"has_more":0}
     */
    private List<Video> items;

    private PaginationBean pagination;

    public PaginationBean getPagination() {
        return pagination;
    }

    public void setPagination(PaginationBean pagination) {
        this.pagination = pagination;
    }

    public List<Video> getItems() {
        return items;
    }

    public void setItems(List<Video> items) {
        this.items = items;
    }

}
