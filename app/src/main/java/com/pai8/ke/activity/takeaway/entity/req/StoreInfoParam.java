package com.pai8.ke.activity.takeaway.entity.req;

public class StoreInfoParam {
    public String shop_id;//店铺id
    public String shop_name;//店铺名称
    public String shop_img;//店铺图片七牛key
    public String mobile;//店铺联系电话
    public String cate_id;//店铺所属分类id
    public String address;//店铺详细地址
    public String shop_desc;//店铺描述
    public String province;//:店铺所属省份名称
    public String city;//:店铺所属城市名称（额外说明：去掉‘市’这个字，例如南京，南京市）
    public String city_id;//:店铺所属城市id值（public/area接口返回），当传city参数时，此参数必传
    public String district;//:店铺所属区域名称
    public String logo_img;//:店铺logo七牛key
    public String house_number;//:店铺门牌号
    public String longitude;//:店铺经度
    public String latitude;//:店铺纬度
}

