package com.pai8.ke.entity.resp;

import com.pai8.ke.entity.Pagination;

import java.io.Serializable;
import java.util.List;

public class VideoListResp implements Serializable {

    private List<VideoResp> items;
    private Pagination pagination;

    public List<VideoResp> getItems() {
        return items;
    }

    public void setItems(List<VideoResp> items) {
        this.items = items;
    }

    public Pagination getPagination() {
        return pagination;
    }

    public void setPagination(Pagination pagination) {
        this.pagination = pagination;
    }
}
