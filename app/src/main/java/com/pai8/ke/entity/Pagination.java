package com.pai8.ke.entity;

import java.io.Serializable;

public class Pagination implements Serializable {
    private int page;
    private int size;
    private int has_more;

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public boolean noMore() {
        return has_more != 1;
    }

    public void setHas_more(int has_more) {
        this.has_more = has_more;
    }
}
