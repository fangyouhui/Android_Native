package com.pai8.ke.activity.takeaway.entity;

import java.util.List;


public class FoodClassifyInfo {
    public int bigSortId;
    public String bigSortName;

    public List<ListBean> list;

    public static class ListBean {
        public int smallSortId;
        public String smallSortName;
    }

    public boolean isSelected;
}
