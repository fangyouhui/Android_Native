package com.pai8.ke.activity.takeaway.entity.req;

import java.io.Serializable;

public class OrderStatusInfo implements Serializable {

    public String name; //名称
    public String status;
    public boolean isSelect;

    public boolean isSelect() {
        return isSelect;
    }

    public void setSelect(boolean isSelect) {
        this.isSelect = isSelect;
    }

}
