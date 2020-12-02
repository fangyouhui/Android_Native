package com.pai8.ke.utils;

import android.content.Context;

import com.bigkoo.pickerview.builder.OptionsPickerBuilder;
import com.bigkoo.pickerview.view.OptionsPickerView;
import com.pai8.ke.api.Api;
import com.pai8.ke.base.retrofit.BaseObserver;
import com.pai8.ke.base.retrofit.RxSchedulers;
import com.pai8.ke.entity.resp.ShopListResp;
import com.pai8.ke.entity.resp.BusinessType;

import java.util.ArrayList;
import java.util.List;

/**
 * Picker Utils
 * Created by gh on 2020/11/14.
 */
public class PickerUtils {

    public static void showBusinessType(Context context, int position, BusinessTypeCallback callback) {
        List<String> options1Items = new ArrayList<>();
        Api.getInstance().getBusinessType()
                .doOnSubscribe(disposable -> {
                })
                .compose(RxSchedulers.io_main())
                .subscribe(new BaseObserver<List<BusinessType>>() {
                    @Override
                    protected void onSuccess(List<BusinessType> list) {
                        callback.data(-1, 0, "");
                        for (BusinessType businessType : list) {
                            options1Items.add(businessType.type_name);
                        }
                        OptionsPickerView pvOptions = new OptionsPickerBuilder(context, (options1, option2,
                                                                                         options3, v) -> {
                            callback.data(options1, list.get(options1).id, list.get(options1).type_name);
                        }).build();
                        pvOptions.setNPicker(options1Items, null, null);
                        pvOptions.setSelectOptions(position);
                        pvOptions.setTitleText("选择分类");
                        pvOptions.show();
                    }

                    @Override
                    protected void onError(String msg, int errorCode) {
                        super.onError(msg, errorCode);
                        callback.data(-1, 0, "");
                    }
                });


    }

    public static void showDays(Context context, DaysCallback callback) {
        List<String> a = new ArrayList<>();
        for (int i = 1; i <= 120; i++) {
            a.add(i + "天");
        }
        OptionsPickerView pvOptions = new OptionsPickerBuilder(context, (options1, option2, options3, v) -> {
            callback.data(options1, a.get(options1));
        }).build();
        pvOptions.setNPicker(a, null, null);
        pvOptions.setTitleText("有效时间");
        pvOptions.show();
    }

    public interface BusinessTypeCallback {
        void data(int position, int id, String name);
    }

    public interface DaysCallback {
        void data(int position, String day);
    }
}
