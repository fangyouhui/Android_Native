package com.pai8.ke.entity.resp;

import java.io.Serializable;
import java.util.List;

public class ShopListResp implements Serializable {

    private List<ShopList> shop_list;

    public List<ShopList> getShop_list() {
        return shop_list;
    }

    public void setShop_list(List<ShopList> shop_list) {
        this.shop_list = shop_list;
    }

}
