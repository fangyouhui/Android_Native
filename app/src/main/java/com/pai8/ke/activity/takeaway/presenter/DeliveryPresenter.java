package com.pai8.ke.activity.takeaway.presenter;

import com.pai8.ke.activity.takeaway.api.TakeawayApi;
import com.pai8.ke.activity.takeaway.contract.DeliveryContract;
import com.pai8.ke.activity.takeaway.entity.resq.AddressInfo;
import com.pai8.ke.base.BasePresenterImpl;
import com.pai8.ke.base.retrofit.BaseObserver;
import com.pai8.ke.base.retrofit.RxSchedulers;

import java.util.HashMap;
import java.util.List;

public class DeliveryPresenter extends BasePresenterImpl<DeliveryContract.View> {

    public DeliveryPresenter(DeliveryContract.View view) {
        super(view);
    }

    public void getAddress(){
        TakeawayApi.getInstance().addressList()
                .doOnSubscribe(disposable -> {
                })
                .compose(RxSchedulers.io_main())
                .subscribe(new BaseObserver<List<AddressInfo>>() {
                    @Override
                    protected void onSuccess(List<AddressInfo> data){

                        view.getAddressSuccess(data);

                    }

                    @Override
                    protected void onError(String msg, int errorCode) {
                        super.onError(msg, errorCode);
                    }
                });
    }


    public void upAddress(String name,String phone,String address,String number,String lat,String lon){
        HashMap<String, Object> map = new HashMap<>();
        map.put("linkman", name);
        map.put("phone", phone);
        map.put("address", address);
        map.put("house_number", number);
        map.put("latitude", lat);
        map.put("longitude", lon);
        TakeawayApi.getInstance().upAddress(createRequestBody(map))
                .doOnSubscribe(disposable -> {
                })
                .compose(RxSchedulers.io_main())
                .subscribe(new BaseObserver<String>() {
                    @Override
                    protected void onSuccess(String data){
                        view.addAddressSuccess(data);
                    }

                    @Override
                    protected void onError(String msg, int errorCode) {
                        super.onError(msg, errorCode);
                    }
                });
    }

    public void editAddress(String id,String name,String phone,String address,String number,String lat,String lon){
        HashMap<String, Object> map = new HashMap<>();
        map.put("id", id);
        map.put("linkman", name);
        map.put("phone", phone);
        map.put("address", address);
        map.put("house_number", number);
        map.put("latitude", lat);
        map.put("longitude", lon);
        TakeawayApi.getInstance().editAddress(createRequestBody(map))
                .doOnSubscribe(disposable -> {
                })
                .compose(RxSchedulers.io_main())
                .subscribe(new BaseObserver<String>() {
                    @Override
                    protected void onSuccess(String data){
                        view.editAddressSuccess(data);
                    }

                    @Override
                    protected void onError(String msg, int errorCode) {
                        super.onError(msg, errorCode);
                    }
                });
    }


    public void deleteAddress(int id,int position){
        HashMap<String, Object> map = new HashMap<>();
        map.put("id", id);
        TakeawayApi.getInstance().deleteAddress(createRequestBody(map))
                .doOnSubscribe(disposable -> {
                })
                .compose(RxSchedulers.io_main())
                .subscribe(new BaseObserver<String>() {
                    @Override
                    protected void onSuccess(String data){

                        view.deleteAddressSuccess(data,position);
                    }

                    @Override
                    protected void onError(String msg, int errorCode) {
                        super.onError(msg, errorCode);
                    }
                });
    }

}
