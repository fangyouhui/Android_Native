package com.pai8.ke.activity.takeaway.entity.resq;

import java.io.Serializable;
import java.util.List;

public class StoreInfo implements Serializable {

    public int shop_id;
    public int id;
    public String shop_name;
    public String shop_desc;
    public String begin_time;
    public String end_time;
    public String shop_img;
    public String shop_img_url;
    public String delivery_time;
    public double score;
    public int is_open;
    public String cate_id;
    public List<String> cate_name;
    public String send_cost;
    public String province;
    public String province_name;
    public String city;
    public String district;
    public String city_id;
    public String district_name;
    public String loge_img;
    public String floor_send_cost;
    public String packing_price;
    public String house_num;
    public String send_range;
    public String address;
    public String mobile;
    public String house_number;
    public String monthly_sale;
    public String month_sale_count;  //月销量
    public int is_collect;
    public String distance;
    public String business_license;
    public String health_permit;
    public String month_sale_price;  //	月销售额

    public String sale_count; //总销量（订单数）

    public String latitude;
    public String longitude;
}
