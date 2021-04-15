package com.pai8.ke.activity.takeaway.api;

import com.pai8.ke.activity.takeaway.entity.FoodGoodInfo;
import com.pai8.ke.activity.takeaway.entity.OrderInfo;
import com.pai8.ke.activity.takeaway.entity.ShopFoodGoodInfo;
import com.pai8.ke.activity.takeaway.entity.req.AddFoodReq;
import com.pai8.ke.activity.takeaway.entity.req.GroupFoodReq;
import com.pai8.ke.activity.takeaway.entity.req.MerchantSettledReq;
import com.pai8.ke.activity.takeaway.entity.req.RebateReq;
import com.pai8.ke.activity.takeaway.entity.req.ShopIdReq;
import com.pai8.ke.activity.takeaway.entity.req.ShopListReq;
import com.pai8.ke.activity.takeaway.entity.req.StoreInfoParam;
import com.pai8.ke.activity.takeaway.entity.req.UpCategoryReq;
import com.pai8.ke.activity.takeaway.entity.resq.AddressInfo;
import com.pai8.ke.activity.takeaway.entity.resq.CommentInfo;
import com.pai8.ke.activity.takeaway.entity.resq.GoodsInfoModel;
import com.pai8.ke.activity.takeaway.entity.resq.SecondAdminManagerResq;
import com.pai8.ke.activity.takeaway.entity.resq.ShopContent;
import com.pai8.ke.activity.takeaway.entity.resq.ShopInfo;
import com.pai8.ke.activity.takeaway.entity.resq.StoreInfoResult;
import com.pai8.ke.activity.takeaway.entity.resq.TakeawayResq;
import com.pai8.ke.activity.takeaway.entity.resq.WaimaiResq;
import com.pai8.ke.activity.takeaway.entity.resq.smallGoodsInfo;
import com.pai8.ke.activity.wallet.data.InOutRecordRequest;
import com.pai8.ke.activity.wallet.data.InOutRecordResp;
import com.pai8.ke.activity.wallet.data.MemberCashRequest;
import com.pai8.ke.activity.wallet.data.MemberWalletResponse;
import com.pai8.ke.base.BaseRespose;
import com.pai8.ke.entity.resp.BusinessType;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;

import io.reactivex.Observable;
import okhttp3.RequestBody;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/*
 * Api接口服务类
 */
public interface TakeawayApiService {
    /**
     * 商家申请
     */
    @POST("shop/upShop")
    Observable<BaseRespose> merchantSettled(@Body MerchantSettledReq param);


    /**
     * 添加分类
     *
     * @return
     */
    @POST("shop/upCategory")
    Observable<BaseRespose<ShopInfo>> addUpCategory(@Body UpCategoryReq parm);


    @POST("shop/categoryEdit")
    Observable<BaseRespose<ShopInfo>> categoryEdit(@Body UpCategoryReq parm);


    /**
     * 商家商品类别
     *
     * @param parm
     * @return
     */
    @POST("shop/categoryList")
    Observable<BaseRespose<List<ShopInfo>>> getCategoryList(@Body UpCategoryReq parm);

    /**
     * 团购商品类别
     *
     * @param parm
     * @return
     */
    @POST("Group/GetFoodSortList")
    Observable<BaseRespose<List<BusinessType>>> getTuanCategoryList();


    /**
     * 删除商品类别
     *
     * @param parm
     * @return
     */
    @POST("shop/categoryDelete")
    Observable<BaseRespose<String>> deleteCategory(@Body UpCategoryReq parm);

    /**
     * 添加商品
     *
     * @param parm
     * @return
     */
    @POST("shop/addFood")
    Observable<BaseRespose<List<String>>> addFood(@Body AddFoodReq parm);

    /**
     * 添加团购商品
     *
     * @param parm
     * @return
     */
    @POST("Group/AddGroupGoods")
    Observable<BaseRespose<smallGoodsInfo>> addGroupFood(@Body GroupFoodReq parm);

    /**
     * 修改团购商品
     *
     * @param parm
     * @return
     */
    @POST("Group/UpGroupGoods")
    Observable<BaseRespose<smallGoodsInfo>> editGroupFood(@Body GroupFoodReq parm);

    /**
     * 得到团购商品
     *
     * @return
     */
    @POST("Group/GetGroupGoodsInfo")
    Observable<BaseRespose<GoodsInfoModel>> getGroupFood(@Body RequestBody parm);

    /**
     * 修改团购商品状态
     *
     * @return
     */
    @POST("Group/SetGroupGoodsStatus")
    Observable<BaseRespose<smallGoodsInfo>> setGroupGoodsStatus(@Body RequestBody parm);


    @POST("shop/editGoods")
    Observable<BaseRespose<List<String>>> editGoods(@Body AddFoodReq parm);

    @POST("shop/foodDelete")
    Observable<BaseRespose<List<String>>> foodDelete(@Body RequestBody body);

    /**
     * 收获地址
     *
     * @return
     */
    @POST("shop/addressList")
    Observable<BaseRespose<List<AddressInfo>>> addressList();

    /**
     * 用户钱包提现
     *
     * @return
     */
    @POST("user/MemberCash")
    Observable<BaseRespose<String>> userMemberCash(@Body MemberCashRequest body);

    /**
     * 钱包页面
     *
     * @return
     */
    @POST("user/MemberWallet")
    Observable<BaseRespose<MemberWalletResponse>> memberWallet();


    /**
     * 设置商户拍客返点比例
     *
     * @return
     */
    @POST("shop/SetupShopbeatRebate")
    Observable<BaseRespose<String>> setupRebate(@Body RebateReq body);

    /**
     * 商户关注送礼商品编辑
     *
     * @return
     */
    @POST("shop/SetUpShopGift")
    Observable<BaseRespose<String>> setupGift(@Body RebateReq body);


    /**
     * 收支记录
     *
     * @param body
     * @return
     */
    @POST("user/MemberIncomeList")
    Observable<BaseRespose<InOutRecordResp>> inOutRecord(@Body InOutRecordRequest body);

    /**
     * 提现记录
     *
     * @param body
     * @return
     */
    @POST("user/MemberCashList")
    Observable<BaseRespose<InOutRecordResp>> outRecord(@Body InOutRecordRequest body);

    @POST("shop/upAddress")
    Observable<BaseRespose<List<String>>> upAddress(@Body RequestBody body);

    @POST("shop/editAddress")
    Observable<BaseRespose<List<String>>> editAddress(@Body RequestBody body);


    @POST("shop/addressDelete")
    Observable<BaseRespose<List<String>>> deleteAddress(@Body RequestBody body);

    /**
     * 收藏
     *
     * @param parm
     * @return
     */
    @POST("shop/shopCollect")
    Observable<BaseRespose<JSONObject>> collection(@Body ShopIdReq parm);


    @POST("shop/shopUncollect")
    Observable<BaseRespose<JSONObject>> unCollection(@Body ShopIdReq parm);

    @FormUrlEncoded
    @POST("Order/waimaiPrice")
    Observable<BaseRespose> outDistance(@Field("shop_id") String shopId, @Field("address_id") String addressId, @Field("box_price") String boxPrice);


    @POST("shop/shopContent")
    Observable<BaseRespose<ShopContent>> shopContent(@Body ShopIdReq parm);


    /**
     * 商家列表
     *
     * @param parm
     * @return
     */
    @POST("shop/shopList")
    Observable<BaseRespose<TakeawayResq>> getShopList(@Body ShopListReq parm);


    /**
     * 下单
     */
    @POST("Order/addOrder")
    Observable<BaseRespose<String>> addOrder(@Body RequestBody body);


    @POST("Order/shopOrderList")
    Observable<BaseRespose<List<OrderInfo>>> shopOrderList(@Body RequestBody body);

    /**
     * 获取订单列表
     *
     * @param body
     * @return
     */
    @POST("Order/orderList")
    Observable<BaseRespose<List<OrderInfo>>> orderList(@Body RequestBody body);

    @POST("Order/cancelOrder")
    Observable<BaseRespose<String>> cancelOrder(@Body RequestBody body);


    @POST("Order/orderDetail")
    Observable<BaseRespose<OrderInfo>> orderDetail(@Body RequestBody body);


    @POST("shop/shopComments")
    Observable<BaseRespose<List<CommentInfo>>> shopComments(@Body RequestBody body);


    @POST("shop/goodslist")
    Observable<BaseRespose<List<ShopInfo>>> goodslist(@Body RequestBody body);

    @POST("Group/GetShopGroupList")
    Observable<BaseRespose<List<smallGoodsInfo>>> ShopGroupList(@Body RequestBody body);


    @POST("Order/waimaiPrice")
    Observable<BaseRespose<WaimaiResq>> waimaiPrice(@Body RequestBody body);


    @POST("Order/addCart")
    Observable<BaseRespose<List<String>>> addCart(@Body RequestBody body);

    @POST("Order/updateCartNum")
    Observable<BaseRespose<List<String>>> updateCartNum(@Body RequestBody body);

    @POST("Order/getCart")
    Observable<BaseRespose<ShopFoodGoodInfo>> getCart(@Body RequestBody body);

    @POST("Order/applyRefund")
    Observable<BaseRespose<String>> applyRefund(@Body RequestBody body);

    @POST("Order/shopDealOrder")
    Observable<BaseRespose<String>> shopDealOrder(@Body RequestBody body);

    @POST("shop/floorSendCost")
    Observable<BaseRespose<String>> floorSendCost(@Body RequestBody body);


    @POST("shop/foodSearch")
    Observable<BaseRespose<List<FoodGoodInfo>>> foodSearch(@Body RequestBody body);


    @POST("Order/reAddCart")
    Observable<BaseRespose<List<FoodGoodInfo>>> reAddCart(@Body RequestBody body);


    @POST("shop/shopEditInfo")
    Observable<BaseRespose<StoreInfoResult>> shopEditInfo(@Body RequestBody body);


    @POST("shop/shopIndex")
    Observable<BaseRespose<StoreInfoResult>> shopIndex(@Body RequestBody body);

    @POST("shop/shopStatus")
    Observable<BaseRespose<JSONObject>> shopStatus(@Body RequestBody body);


    @POST("shop/editShop")
    Observable<BaseRespose<JSONObject>> editShop(@Body StoreInfoParam body);

    /**
     * 二级管理员列表
     *
     * @param parm
     * @return
     */
    @POST("shop/GetShopManageList")
    Observable<BaseRespose<List<SecondAdminManagerResq>>> getSecondAdminList(@Body RequestBody parm);

    /**
     * 删除二级管理员
     *
     * @param parm
     * @return
     */
    @POST("shop/DelShopManageInfo")
    Observable<BaseRespose> deleteSecondAdmin(@Body RequestBody parm);

    /**
     * 更新二级管理员
     *
     * @param parm
     * @return
     */
    @POST("shop/UpShopManageInfo")
    Observable<BaseRespose> updateSecondAdmin(@Body RequestBody parm);

    /**
     * 添加二级管理员
     *
     * @param parm
     * @return
     */
    @POST("shop/AddShopManageInfo")
    Observable<BaseRespose> addSecondAdmin(@Body RequestBody parm);

    /**
     * 获取商户所有二级管理员权限
     *
     * @param parm
     * @return
     */
    @POST("shop/GetShopPowerList")
    Observable<BaseRespose<List<SecondAdminManagerResq.PowerArrayBean>>> getShopLimits();
}
