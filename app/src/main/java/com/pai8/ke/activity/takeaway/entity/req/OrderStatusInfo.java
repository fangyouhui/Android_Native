package com.pai8.ke.activity.takeaway.entity.req;

public class OrderStatusInfo {

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
