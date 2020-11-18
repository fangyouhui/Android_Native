package com.pai8.ke.activity.takeaway.entity.event;

import com.pai8.ke.activity.takeaway.entity.FoodGoodInfo;

import java.util.List;

public class AddGoodEvent {

    public int type;
    public int xy1;
    public int xy0;
    public int number;
    public int position;
    public List<FoodGoodInfo> shopCarGoodsList;

    public AddGoodEvent(int type, int xy0, int xy1,int position, int number, List<FoodGoodInfo> shopCarGoodsList) {
        this.type = type;
        this.xy0 = xy0;
        this.xy1 = xy1;
        this.position = position;
        this.number = number;
        this.shopCarGoodsList = shopCarGoodsList;
    }
    public AddGoodEvent(int type, int xy0, int xy1,int number, List<FoodGoodInfo> shopCarGoodsList) {
        this.type = type;
        this.xy0 = xy0;
        this.xy1 = xy1;
        this.number = number;
        this.shopCarGoodsList = shopCarGoodsList;
    }

    public AddGoodEvent(int type, int number, List<FoodGoodInfo> shopCarGoodsList) {
        this.type = type;
        this.number = number;
        this.shopCarGoodsList = shopCarGoodsList;
    }
}
