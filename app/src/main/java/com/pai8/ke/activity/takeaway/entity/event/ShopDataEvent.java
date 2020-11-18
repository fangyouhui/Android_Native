package com.pai8.ke.activity.takeaway.entity.event;

import com.pai8.ke.activity.takeaway.entity.resq.ShopContent;

public class ShopDataEvent {

    public int type;
    public ShopContent data;

    public ShopDataEvent(int type,ShopContent data){
        this.type = type;
        this.data = data;
    }

}
