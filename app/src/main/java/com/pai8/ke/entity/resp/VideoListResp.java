package com.pai8.ke.entity.resp;

import com.pai8.ke.entity.Pagination;
import com.pai8.ke.entity.Video;

import java.io.Serializable;
import java.util.List;

public class VideoListResp implements Serializable {

    private List<Video> items;
    private Pagination pagination;

    public List<Video> getItems() {
        return items;
    }

    public void setItems(List<Video> items) {
        this.items = items;
    }

    public Pagination getPagination() {
        return pagination;
    }

    public void setPagination(Pagination pagination) {
        this.pagination = pagination;
    }
}
