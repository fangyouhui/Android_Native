package com.pai8.ke.activity.takeaway.entity;

import java.util.List;


public class FoodClassifyInfo {
    public int id;
    public int shop_id;
    public String name;
    public String desc;
    public List<FoodGoodInfo> goods;
    public boolean isSelected;
}
